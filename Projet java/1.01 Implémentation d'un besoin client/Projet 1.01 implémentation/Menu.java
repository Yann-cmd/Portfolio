
import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.text.*;
import java.util.Random;

public class Menu
{

    //texte du menu
    public static String [][] promo = null;
    public static int nbItemMenu = 3;
    public static String texteMenu = "\n/**********************************************/\n"+
        "\t\t\tSelectionner\n\t\t1 - un etudiant\n"+"\t\t2 - affiche la promo\n\t\t0 - Quitter\n"+"/**********************************************/\n\n";

    /**  permet de retourner une valeur entiere saisie au clavier comprise entre pfBorneInf et pfBorneSup
     *@param pfBorneSup In la borne sup
     *@param pfBorneInf In la borne inf
     *@param pfMessage In message à afficher
     *@return valeur entiere comprise entre pfBorneInf et pfBorneSup
     **/
    public static int saisieIntC (int pfBorneInf,  int pfBorneSup, String pfMessage){
        int valeur;
        Scanner clavier = new Scanner(System.in) ;
        System.out.print(pfMessage); 

        valeur=clavier.nextInt();
        while (valeur<pfBorneInf || valeur>pfBorneSup){
            System.out.println(pfMessage);
            System.out.print("Erreur ! Donnez une valeur comprise " + pfBorneInf +" et "+pfBorneSup+ "?");
            valeur=clavier.nextInt();
        }
        return valeur;
    }

    /** Sélection aléatoire d'un élève. Celui ci ne peut être 
     * 
     * @param IN  pfDetu :correspond à l'indice du dernier etudiant appele
     * @param IN/OUT pfnbAppels : tableau de compteur de nombre d'appel
     * @return nouveau : indice de l'étudiant sélectionné 
     */
    public static int choixAleatoire( int pfDetu,int[] pfnbAppels){
        //Initialisation du nouvel indice de l'étudiant
        int nouveau=tirage();
        
        //Initialisation d'une boucle conditionnelle qui donne à "nouveau" un nouvel indice et qui s'arrête selon deux cas
        //Celui où l'indice de l'étudiant sélectionné précédemment est équivalent au nouveau
        //Où celui où le nombre d'appels de "nouveau" est à 1

        while(pfDetu==nouveau || pfnbAppels[nouveau]==1){
            nouveau=tirage();
        }
        //Incrémentation du nombre d'appels de "nouveau" pour éviter son retour immédiat
        pfnbAppels[nouveau]++;

        return nouveau;

    }

    public static int tirage(){
        return (int) (Math.random()*promo.length);
    }

    public static void afficherEtudiant(int IdEtu) {
        System.out.println(promo[IdEtu][0]+ " " +promo[IdEtu][1]);
    }

    public static void afficherPromo(){
        System.out.println("Liste de la promotion :\n");
        for (int i=0;i<ListeEtudiants.nbEtudiant(promo);i++){
            System.out.println("etu : "+(i+1) +"\t"+promo [i][ 0]+"\t" +promo [i][1]);
        }
    }

    /**  affiche le menu Général et exécute les choix...
    **/
    public static void testMenu(){
        Scanner clavier=new Scanner(System.in);
        int choixUtilisateur ;

        int Tab[] = new int [ListeEtudiants.nbEtudiant(promo)];
        //tableau contentnant 0 si l'étudiant n'est pas appelé ou 1 sinon
        int IdEtu = -1; //Indice de l'étudiant
        
        //DernierAppels correspond à l'indice du dernier étudiant appelé
        int DernierAppele=-1;
        
        //La variable nbAppels correspond au nombres d'appels efectué dans le programme
        int nbAppels=0;
        
        
        //Initialisation de toutes les cases de tab à 0
        for(int k=0;k<promo.length;k++){
            Tab[k]=0;
        }

        boolean stop=false;
        while(!stop){
            System.out.println(texteMenu);
            choixUtilisateur = saisieIntC ( 0, nbItemMenu, "Choisir ");
            try{
                switch(choixUtilisateur){
                    case 2 :
                        afficherPromo();
                        break ;
                    case 1 :
                        
                        //Nous donnons un indice aléatoire à Idetu en appelant la fonction choixAleatoire.
                        IdEtu = choixAleatoire(DernierAppele,Tab);
                        //L'indice de Idetu deviens par la suite l'indice de DernierAppele qui correspond au dernier étudiant appelé
                        DernierAppele=IdEtu;
                        nbAppels++;
                        afficherEtudiant(IdEtu);
                        //Lorsque le nombre d'appels corresponds au nombre d'étudiants présents dans la promo
                        //nous faisons une boucle afin de pouvoir remettre tous les compteurs de nombre d'appels dans Tab à 0
                        //ainsi que remettre la variable nbAppels, elle-même, à 0.
                        if(nbAppels==ListeEtudiants.nbEtudiant(promo)){
                            for(int k=0;k<promo.length;k++){
                                Tab[k]=0;
                            }
                            nbAppels=0;
                        }
                        break;

                    case 0 :
                        System.out.println ("Bonne journée\n");
                        stop=true;
                        break ;
                    default :
                        System.out.println("Veuillez écrire un chiffre :");
                        break;
                }
            }catch (Exception e) {
                //Pour éviter d'écrire avec une lettre, on donne la valeur -1 
                //à choixUtilisateur pour pouvoir traiter l'erreur et continuer :
                System.out.println("ERREUR !");
                choixUtilisateur = -1;
            }
        }
    }

    public static void main(String arguments[]) {
        try {
            promo = ListeEtudiants.getListe("listeetudiants.csv", ","); //appel du sous programme précédé du nom de la classe où elle est définie
            System.out.println("Il y a : " + ListeEtudiants.nbEtudiant(promo) + " etudiants"); 
            testMenu();

        }
        catch (Exception e) {  
            System.out.println("Erreur : "+e.getMessage());

        } 

    } // fin main

}
