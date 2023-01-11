/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.kienlen.resto;

// import des différentes classes 
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.CommandeClient;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.GestionnaireTemps;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.SimulateurGlobal;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.ParametresSimulation;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.TypePlat;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author camil
 */
public class Employes extends Thread{
    // attribus d'un employé: chaque employé a un nom
    private int identifiant;
    private String prenom;
    private String nom;    
    private boolean occupe; 
    
    // constructeur: donne les caractéristiques de l'objet 
    public Employes (int identifiant, String prenom, String nom) {
        this.identifiant = identifiant;
        this.prenom = prenom;
        this.nom = nom;
        this.occupe = false;
    }

    // fonction d'affichage de l'objet Employé
    @Override
    public String toString() {
        return "Employé n°" + this.identifiant + " : " + this.nom + " " + this.prenom;
    }

    // réécriture de la fonction run qui spécifie ce que fait le thread lorsqu'il est lancé:
    // ici cette fonction spécifie les différentes tâches que peut réaliser l'employé
    @Override
    public void run() {
        // tant que le restaurant n'est pas fermé, on teste les actions
        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();

        
        int nbrTypes = paras.getResto().getCarte().getLesPlats().length;
        int nombreElements;
        int curStock;
        int maxStock; 
        
        while(true) {  
            
            // prends un commande 
            int caisseReservee = resto.getLeComptoir().reserveCaisse(); 
            if (caisseReservee != -1) { // si une caisse est libre
                Optional<CommandeClient> commande = this.simu.getFileAttente().prendCommande(); // on recupère le fait qu'il y ait ou pas une commande  
                if (commande.isPresent()) { // si ya un client
                    CommandeClient vraieCommande = commande.get(); // récupère la vrai commande du client.

                    for (int i=0; i< nbrTypes; i++){// pour les 3 types de plats
                        nombreElements = vraieCommande.getCommande()[i];// on test la première case du tableau (nb burger)
                        // on récupère le nombre de burger dans le stock
                        // tant que il en veut plus que le stock
                        curStock = resto.getLeStock().getStock(i);
                        while (curStock < nombreElements){ // si ya pas assez 
                           this.simu.getGestionnaireTemps().sleepDureeResto(60000); // on attends 
                           curStock = resto.getLeStock().getStock(i);
                            if (resto.getLeStock().reserveStock()!= -1){ 
                                resto.getLeStock().reserveStock(); // on reserve le stock
                            }
                        }
                        resto.getLeStock().setStock(curStock-nombreElements,i);
                        resto.getLeStock().libereStock();
                    }
                }
            }
            // fin de commande 
            resto.getLeComptoir().libereCaisse(caisseReservee);// on libère la caisse 
            
            // on produit 
            for (int i=0; i< nbrTypes; i++){
                curStock = resto.getLeStock().getStock(i);
                maxStock= paras.getResto().getStockage().getCapacites()[i];
                if (curStock < maxStock){
                    if (resto.getLeStock().reserveStock()!= -1){ 
                        resto.getLeStock().reserveStock(); 
                        resto.getLeStock().setStocksActuels(curStock+1, i);
                        resto.getLeStock().libereStock();   
                    }
                }
            }
            // fin d eproduction 
                
                // produit 
             
//            else if (produit==true){
            
//            else if (attendre==true){
                
            
//            else {
                
        }       
    }

    public void produire(TypePlat plat, int quantite){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();
        Ressources[] resProduites = new Ressources[quantite];
        boolean lockDepot = false;
        int numPlat, indexResProduites, stocksActuels;


        for(int i = 0; i < quantite; i++){
            resProduites[i] =  new Ressources(plat, gestTemps.currentTimeResto());
        }
        Utils.sleepNoInterrupt(plat.getDureesPreparation()[quantite - 1 ]);             //on attend le temps de produire toutes les ressources

        lockDepot = resto.getLeStock().reserveDepot();
        while(!lockDepot){
            lockDepot = resto.getLeStock().reserveDepot();
        } 
        Utils.sleepNoInterrupt(resto.getInfoResto().getDureeChargement());              //A vérifier  

        if(plat.getNom().equalsIgnoreCase("burger")){
            numPlat = 1;
        } else if(plat.getNom().equalsIgnoreCase("frites")){
            numPlat = 2;
        } else if(plat.getNom().equalsIgnoreCase("salade")){
            numPlat = 3;
        }
        
        indexResProduites = 0;
        stocksActuels = resto.getLeStock().getStocksActuels()[numPlat];
        for(int i = stocksActuels; i < stocksActuels + quantite; i++){
            resto.getLeStock().setRessourcesDispos(resProduites[indexResProduites], i, numPlat);
            indexResProduites += 1;
        }
        resto.getLeStock().setSpecStock(stocksActuels + quantite, numPlat);
        resto.getLeStock().libereDepot();
    }

    public void servir(){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();  
        Gestion gestion;      
        int caisseReservee = resto.getLeComptoir().reserveCaisse(); 
        int nbProduits, stocksActuels;
        TypePlat plat;
        Optional<CommandeClient> commande;
        CommandeClient vraieCommande;

        if (caisseReservee != -1) { // si une caisse est libre
            commande = this.simu.getFileAttente().prendCommande(); // on recupère le fait qu'il y ait ou pas une commande  
            if (commande.isPresent()) { // si ya un client
                vraieCommande = commande.get(); // récupère la vrai commande du client.
                Utils.sleepNoInterrupt(resto.getInfoResto().getDureeCommande());
            }
        }

        while(resto.getLeStock().reserveRetrait()){
            //on attend de pouvoir accéder à la zone de retrait de l'espace de stockage
        }

        for(int i = 0; i < 3; i++){
            switch(i){
                case 1:
                    plat = TypePlat.burger();
                    break;
                case 2:
                    plat = TypePlat.frites();
                    break;
                case 3:
                    plat = TypePlat.salade();
            }
            nbProduits = vraieCommande.getCommande()[i];
            if(nbProduits != 0){
                jeter(i);                                                        //Fonction à implémenter
            }
            stocksActuels = resto.getLeStock().getStocksActuels()[i];
            while(stocksActuels < nbProduits){
                stocksActuels = resto.getLeStock().getStocksActuels()[i];
            }
            resto.getLeStock().setSpecStock(stocksActuels - nbProduits, i);
            Utils.sleepNoInterrupt(resto.getInfoResto().getDureeDechargement());
            gestion.setVentes(gestion.getVentes()[i] + nbProduits, i);
        }
        resto.getLeStock().libereRetrait();
    }

    public void jeter(int numPlat){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();  
        boolean perime = false;
        int indexRes = -1;
        Ressources resTemp;

        do{
            indexRes++;
            if(indexRes < resto.getInfoResto().getStockage().getCapacites()[numPlat]){
                resTemp = resto.getLeStock().getRessourcesDispos()[indexRes][numPlat];
                if(resTemp.getFinConso() < gestTemps.currentTimeResto()){
                    perime = true;
                    Utils.sleepNoInterrupt(resto.getInfoResto().getDureeDechargement());                                
                }
                else{
                    perime = false;
                }
            }
        } while(perime);
        for(int i = 0; i < indexRes; i++){                                                      //A revoir
            resTemp = resto.getLeStock().getRessourcesDispos()[indexRes + 1][numPlat];
            resto.getLeStock().setRessourcesDispos(resTemp, indexRes, numPlat);
        }
    }    
}