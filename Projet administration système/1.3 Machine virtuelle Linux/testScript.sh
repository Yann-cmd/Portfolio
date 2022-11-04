#!/bin/bash
success=0 #Tests réussis
failed=0 #Tests échoués
errorMSG="[Erreur] " #Prefix pour les messages d'erreur
successMSG="[Succès] " #Prefix pour les messages de succès
defaultUmask="0022" # Umask par défaut sur notre distribution par l'utilisateur principal

checkCommand() {
    if ! type "$1" > /dev/null; then
        echo "$errorMSG La commande  $1 n'existe pas"
        failed=$((failed+1))
    else
        echo "$successMSG La commande $1 a été trouvée"
        success=$((success+1))
    fi
}

showCustomProfile() {
    echo "Le path : $PATH"
}

checkUmask() {
    local umask=`umask`
    if [ $umask -eq $defaultUmask ] ; then
        echo "Le umask n'a pas été modifié"
        failed=$((failed+1))
    else
        echo "Le umask a été modifié, le voici : $umask"
        success=$((success+1))
    fi
}


showAlias() {
    if $(grep ^alias /etc/bash.bashrc | grep $1) ; then 
        echo "$successMSG L'alias $1 a été trouvé"
        success=$((success+1))
    else
        echo "$errorMSG L'alias $1 n'a pas été trouvé"
        failed=$((failed+1))
    fi
}

validateGroup() {
    if grep ^$1 /etc/group ; then
        echo "$successMSG Le groupe $1 a été détecté "
        success=$((success+1))
    else
        echo "$errorMSG le groupe $1 n'existe pas"
        failed=$((failed+1))
    fi
}

validateAccount() {
    if groups $1 | grep $2 ; then
        echo "$successMSG L'utilisateur $1 existe et fait bien parti du groupe $2"
        success=$((success+1))
    else
        echo "$errorMSG L'utilisateur $1 ne fait pas parti du groupe $2"
        failed=$((failed+1))
    fi

}

validateAllAccounts() {
    for el in A B C D E F G H I J K L M N O P Q R S T  ; do
        validateAccount $el Etudiants
    done
}
    

#Check commands
echo "-------------------------------"
echo "Vérification des commandes nano, docker, AsciidocFX, libreoffice, gedit, java, xreader, ganttproject, asciidoctor"
checkCommand AsciidocFX
checkCommand libreoffice
checkCommand gedit
checkCommand ganttproject 
checkCommand java #JRE
checkCommand javac #JDK
checkCommand xreader #Lecteur de PDF
checkCommand docker #Bonus
checkCommand asciidoctor #Bonus
checkCommand convert #Bonus
checkCommand mvn #Bonus

#Check groups
echo "-------------------------------"
echo "Validation des groupes"
validateGroup VM
validateGroup Etudiants

#Check accounts
echo "-------------------------------"
echo "Validation des comptes"
validateAccount testvm VM
validateAccount Etudiant Etudiants
validateAllAccounts

#Check Alias
echo "-------------------------------"
echo "Validation des alias"
showAlias search
showAlias gantt

#Check umask
echo "-------------------------------"
echo "Validation du umask"
checkUmask

#Show Path
echo "-------------------------------"
echo "Affichage du Path"
showCustomProfile

#Show Prompt
echo "Affichage du Prompt"
echo "-------------------------------"
grep PS1= $HOME/.bashrc | tail -1


# Show test results
echo "-------------------------------"
tests=$((success+failed))
echo "$success tests réussis sur $tests"
echo "$failed tests échoués sur $tests"
echo "-------------------------------"
