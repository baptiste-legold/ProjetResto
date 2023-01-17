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

import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.ArbitreLogSout;
// import des différentes classes 
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.CommandeClient;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.GestionnaireFileAttente;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.GestionnaireTemps;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.SimulateurGlobal;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.Utils;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.ParametresSimulation;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.TypePlat;

import java.util.Optional;
import java.util.Random;
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
    private SimulateurGlobal simu; 
    private Gestion gestion;
    
    // constructeur: donne les caractéristiques de l'objet 
    public Employes (int identifiant, String prenom, String nom, SimulateurGlobal simu, Gestion gestion) {
        this.identifiant = identifiant;
        this.prenom = prenom;
        this.nom = nom;
        this.simu = simu;
        this.gestion = gestion;
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
        // tant que le restaurant n'est pas fermé, on teste les actions.
        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        //int tailleCarte = resto.getSimu().getParasSimu().getResto().getCarte().getLesPlats().length;
        resto.setLeStock(new EspaceStockage());
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();
        long multiplicateur = this.simu.getGestionnaireTemps().getMultiplicateur();
        int compteurCaisses = 0;

        while(gestTemps.currentTimeResto() - gestTemps.getTempsOrdiDebut() * multiplicateur <  paras.getDureeOuverture()){
            /*for(int i = 0; i < this.simu.getParasSimu().getResto().getNbrCaisse(); i++){
                if(resto.getLeComptoir().getCaissesDispos()[i]){
                    compteurCaisses++;
                }
            }
            if(compteurCaisses >= 1){
                servir();
            } else{*/
                int numPlat = -1;
                int nbPlacesRestantes = 0;
                int quantiteAPrduire = 0;
                int capacites, stocksActuels, commande;
                while (quantiteAPrduire == 0){                    
                    numPlat++;
                    System.out.println("Num plat : " + numPlat);
                    if(numPlat == 3){
                        System.out.println("Je sors de la boucle");
                        break;
                    }
                    System.out.println(TypePlat.getTypePlat(numPlat) + " :");
                    System.out.println(paras.getResto().getStockage().getCapacites().length);
                    capacites = this.simu.getParasSimu().getResto().getStockage().getCapacites()[0]; 
                    System.out.println("Capacite de stockage : " + capacites);
                    stocksActuels = resto.getLeStock().getSpecStock(numPlat);
                    System.out.println("Stocks actuels : " + stocksActuels);
                    nbPlacesRestantes = capacites - stocksActuels;
                    System.out.println("Places restantes : " + nbPlacesRestantes);

                    commande = gestion.getCommandesEnCours()[numPlat];
                    System.out.println("Commande en cours : " + commande);
                    if(nbPlacesRestantes !=0 && commande != 0){
                        if(commande > nbPlacesRestantes){
                            quantiteAPrduire = nbPlacesRestantes;
                        }
                        else{
                            quantiteAPrduire = commande;
                        }
                        System.out.println("Quantité à produire : " + quantiteAPrduire);
                        break;
                    }
                    else{
                        quantiteAPrduire = 0;
                    }
                }
                if(quantiteAPrduire != 0){
                    System.out.println("Production de " + quantiteAPrduire + " " + TypePlat.getTypePlat(numPlat));
                    produire(TypePlat.getTypePlat(numPlat), quantiteAPrduire);
                }
            //}
        }  
        int benefice = gestion.calculBenefice();     
        System.out.println("\nProduction de : " + gestion.getProduits()[0] + " burgers," + gestion.getProduits()[1] + " frites et " + gestion.getProduits()[2] + " salades.");
        System.out.println("Vente de : " + gestion.getVentes()[0] + " burgers," + gestion.getVentes()[1] + " frites et " + gestion.getVentes()[2] + " salades.");
        System.out.println("Bénéfices : " + benefice);     
    }
        

    public void produire(TypePlat plat, int quantite){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();
        Ressources[] resProduites = new Ressources[quantite];
        boolean lockDepot = false;
        int numPlat = 0;
        int indexResProduites, stocksActuels;

        //System.out.println("Stocks actuels");
        //resto.getLeStock().toString();

        System.out.println("Production de :");
        for(int i = 0; i < quantite; i++){
            resProduites[i] =  new Ressources(plat, gestTemps.currentTimeResto(), quantite);
            System.out.println(resProduites[i].toString());
        }
        System.out.println("Attente du temps de production de " + quantite + " " + plat.getNom());
        Utils.sleepNoInterrupt(plat.getDureesPreparation()[quantite - 1]);             //on attend le temps de produire toutes les ressources
        System.out.println(plat.getDureesPreparation()[quantite - 1] + " écoulés");
        gestion.setProduits(quantite, numPlat);
        gestion.retirePlat(numPlat, quantite);

        System.out.println("Lock de dépot en cours d'obtention");
        lockDepot = resto.getLeStock().reserveDepot();
        long debutAttente = gestTemps.currentTimeResto();
        long timeOut = 10;
        while(!lockDepot){
            lockDepot = resto.getLeStock().reserveDepot();
            if(gestTemps.currentTimeResto() - debutAttente > Utils.minToMs(timeOut) * simu.getGestionnaireTemps().getMultiplicateur()){
                System.out.println("Temps dépassé, lock de dépot non obtenu");
                break;
            }
        } 
        if(lockDepot){
            System.out.println("Lock de dépot obtenu, début du temp de chargement");
            Utils.sleepNoInterrupt(resto.getSimu().getParasSimu().getResto().getDureeChargement());              //A vérifier  
            System.out.println("Fin du temps de chargement");
            gestion.setProduits(quantite, numPlat);       
            
            indexResProduites = 0;
            stocksActuels = resto.getLeStock().getStocksActuels()[numPlat];
            for(int i = stocksActuels; i < stocksActuels + quantite; i++){
                resto.getLeStock().setRessourcesDispos(resProduites[indexResProduites], i, numPlat);
                indexResProduites += 1;
            }
            System.out.println("Nouveau stock");
            resto.getLeStock().toString();
            resto.getLeStock().setSpecStock(stocksActuels + quantite, numPlat);
            resto.getLeStock().libereDepot();
            System.out.println("Lock de dépôt libéré");
        }
    }

    /*public void servir(){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();  
        GestionnaireFileAttente file = simu.getFileAttente();    
        int caisseReservee = resto.getLeComptoir().reserveCaisse(); 
        int nbProduits, stocksActuels;
        TypePlat plat;
        Optional<CommandeClient> commande;
        CommandeClient vraieCommande;
        int numCaisse;

        while(caisseReservee == -1){
            caisseReservee = resto.getLeComptoir().reserveCaisse();
        }

        while (!simu.getFileAttente().fileClosed()) {
            Optional<CommandeClient> vraieCommande = Optional.empty();
            while (!simu.getFileAttente().fileClosed() && (vraieCommande = file.prendCommande()).isEmpty()) {
                file.attendsUnClient(1000);
            }
            commande = this.simu.getFileAttente().prendCommande(); // on recupère le fait qu'il y ait ou pas une commande  
            if (commande.isPresent()) { // si ya un client
                vraieCommande = commande.get(); // récupère la vrai commande du client.
                gestion.ajouteCommande(vraieCommande);
                Utils.sleepNoInterrupt(resto.getSimu().getParasSimu().getResto().getDureeCommande());
            }

            while(resto.getLeStock().reserveRetrait()){
                //on attend de pouvoir accéder à la zone de retrait de l'espace de stockage
            }
        }

        for(int i = 0; i < 3; i++){
            plat = TypePlat.getNumPlat(i);
            nbProduits = vraieCommande.getCommande()[i];
            if(nbProduits != 0){
                jeter(i);                                                        //Fonction à implémenter
            }
            stocksActuels = resto.getLeStock().getStocksActuels()[i];
            while(stocksActuels < nbProduits){
                stocksActuels = resto.getLeStock().getStocksActuels()[i];
            }
            resto.getLeStock().setSpecStock(stocksActuels - nbProduits, i);
            Utils.sleepNoInterrupt(resto.getSimu().getParasSimu().getResto().getDureeDechargement());
            gestion.setVentes(nbProduits, i);
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
            if(indexRes < resto.getSimu().getParasSimu().getResto().getStockage().getCapacites()[numPlat]){
                resTemp = resto.getLeStock().getRessourcesDispos()[indexRes][numPlat];
                if(resTemp.getFinConso() < gestTemps.currentTimeResto()){
                    perime = true;                               
                }
                else{
                    perime = false;
                }
            }
        } while(perime);
        Utils.sleepNoInterrupt(resto.getSimu().getParasSimu().getResto().getDureeDechargement()); 
        for(int i = 0; i < indexRes; i++){                                                      //A revoir
            resTemp = resto.getLeStock().getRessourcesDispos()[indexRes + 1][numPlat];
            resto.getLeStock().setRessourcesDispos(resTemp, indexRes, numPlat);            
        }
        gestion.ajoutePlat(numPlat, indexRes);
    } */  
    
    public static void main(String[] args) {
        TypePlat plat = TypePlat.burger();
        Gestion gestion = new Gestion();
        ParametresSimulation paras = ParametresSimulation.parasMiniTest();
        MyResto resto = new MyResto();
        SimulateurGlobal simu = new SimulateurGlobal(paras,
                resto,
                new ArbitreLogSout(-1),
                200,  // taux d'accélération
                new Random(123456));
        simu.start();
        Employes emp = new Employes(1, "toto", "titi", simu, gestion);
        gestion.ajoutePlat(0, 2);
        emp.produire(plat, 2);
    }
}