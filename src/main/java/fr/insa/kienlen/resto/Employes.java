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
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.GestionnaireFileAttente;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.GestionnaireTemps;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.SimulateurGlobal;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.Utils;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.ParametresSimulation;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.TypePlat;

import java.util.Optional;

/**
 *
 * @author camil
 */
public class Employes extends Thread{
    // attribus d'un employé: chaque employé a un nom
    private int id;
    private String prenom;
    private String nom;    
    private SimulateurGlobal simu; 
    
    // constructeur: donne les caractéristiques de l'objet 
    public Employes (int identifiant, String prenom, String nom, SimulateurGlobal simu) {
        this.id = identifiant;
        this.prenom = prenom;
        this.nom = nom;
        this.simu = simu;
    }

    // fonction d'affichage de l'objet Employé
    @Override
    public String toString() {
        return "Employé n°" + this.id + " : " + this.nom + " " + this.prenom;
    }

    // réécriture de la fonction run qui spécifie ce que fait le thread lorsqu'il est lancé:
    // ici cette fonction spécifie les différentes tâches que peut réaliser l'employé
    @Override
    public void run() {
        // tant que le restaurant n'est pas fermé, on teste les actions.
        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        //int tailleCarte = resto.getSimu().getParasSimu().getResto().getCarte().getLesPlats().length;
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();
        int numCaisse;
        
        if(this.id == resto.getSimu().getParasSimu().getResto().getNbrEmployes()){
            System.out.println("Current time resto : " + gestTemps.currentTimeResto());
            System.out.println("Durée ouverture : " + paras.getDureeOuverture());

            while(gestTemps.currentTimeResto() <  paras.getDureeOuverture()){

            }

            
            int benefice = resto.getLaGestion().calculBenefice();     
            System.out.println("\nProduction de : " + resto.getLaGestion().getProduits()[0] + " burgers, " + resto.getLaGestion().getProduits()[1] + " frites et " + resto.getLaGestion().getProduits()[2] + " salades.");
            System.out.println("Vente de : " + resto.getLaGestion().getVentes()[0] + " burgers, " + resto.getLaGestion().getVentes()[1] + " frites et " + resto.getLaGestion().getVentes()[2] + " salades.");
            System.out.println("Bénéfices : " + benefice);
        } 
        else{
            while(gestTemps.currentTimeResto() <  paras.getDureeOuverture()){
                //System.out.println("Recherche d'une caisse...");         
                numCaisse = resto.getLeComptoir().reserveCaisse();
                if(numCaisse != -1){
                    System.out.println("Caisse " + numCaisse +" affectée à l'employé n°" + this.id);
                    servir(numCaisse);
                }
                else{
                    //System.out.println("Employé " + id + " affecté à la production.");
                    int numPlat = -1;
                    int nbPlacesRestantes = 0;
                    int quantiteAPrduire = 0;
                    int capacites, stocksActuels, commande;
                    //System.out.println(resto.getLaGestion().toString());
                    while (quantiteAPrduire == 0){                    
                        numPlat++;
                        if(numPlat == 3){
                            break;
                        }
                        //System.out.println(TypePlat.getTypePlat(numPlat));
                        //System.out.println(paras.getResto().getStockage().getCapacites().length);
                        capacites = paras.getResto().getStockage().getCapacites()[numPlat]; 
                        //System.out.println("Capacite de stockage : " + capacites);
                        stocksActuels = resto.getLeStock().getSpecStock(numPlat);
                        //System.out.println("Stocks actuels : " + stocksActuels);
                        nbPlacesRestantes = capacites - stocksActuels;
                        //System.out.println("Places restantes : " + nbPlacesRestantes);

                        commande = resto.getLaGestion().getCommandesEnCours()[numPlat];
                        //System.out.println("Nombre de plats à produire (" + numPlat + "): " + commande);
                        if(nbPlacesRestantes !=0 && commande != 0){
                            if(commande > nbPlacesRestantes){
                                quantiteAPrduire = nbPlacesRestantes;
                            }
                            else{
                                quantiteAPrduire = commande;
                            }
                            //System.out.println("Quantité à produire (" + numPlat + "): " + quantiteAPrduire);
                            break;
                        }
                        else{
                            quantiteAPrduire = 0;
                        }
                    }
                    if(quantiteAPrduire != 0){
                        System.out.println("Production de " + quantiteAPrduire + " " + TypePlat.getTypePlat(numPlat).getNom() + " par l'employé " + id);
                        resto.getLaGestion().retirePlat(numPlat, quantiteAPrduire);
                        produire(numPlat, quantiteAPrduire);
                    }
                }
            }
        }
    }        

    public void produire(int numPlat, int quantite){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();
        Ressources[] resProduites = new Ressources[quantite];
        TypePlat plat = TypePlat.getTypePlat(numPlat);
        int stocksActuels;

        //System.out.println("Stocks actuels");
        //resto.getLeStock().toString();

        System.out.println("Employé " + id + " produit :");
        long debutPrepa = gestTemps.currentTimeResto();
        for(int i = 0; i < quantite; i++){
            resProduites[i] =  new Ressources(plat, debutPrepa, quantite);
            System.out.println(resProduites[i].toString());
        }
        System.out.println("Employé " + id + " attend le temps de production de " + quantite + " " + plat.getNom());
        Utils.sleepNoInterrupt(gestTemps.dureeRestoVersDureeOrdi(plat.getDureesPreparation()[quantite - 1]) );             //on attend le temps de produire toutes les ressources
        System.out.println("Employé " + id + " a fini la production de " + quantite + " " + plat.getNom());
        resto.getLaGestion().setProduits(quantite, numPlat);
        //System.out.println(resto.getLaGestion().getCommandesEnCours()[numPlat]);
        //resto.getLaGestion().retirePlat(numPlat, quantite);
        System.out.println(resto.getLaGestion().toString());

        System.out.println("Employé " + id + " demande l'accès à l'espace de dépot");
        while(!resto.getLeStock().reserveDepot()){
            //on attend de pouvoir accéder à la zone de retrait de l'espace de stockage
            //System.out.println("Espace de dépot occupé...");
            Utils.sleepNoInterrupt(gestTemps.dureeRestoVersDureeOrdi(30000));
        }
        /*lockDepot = resto.getLeStock().reserveDepot();
        long debutAttente = gestTemps.currentTimeResto();
        long timeOut = 10;
        while(!lockDepot){
            lockDepot = resto.getLeStock().reserveDepot();
            if(gestTemps.currentTimeResto() - debutAttente > Utils.minToMs(timeOut) * simu.getGestionnaireTemps().getMultiplicateur()){
                System.out.println("Temps dépassé, lock de dépot non obtenu");
                break;
            }
        }*/ 
        //if(lockDepot){
        System.out.println("Employé " + id + " a obtenu l'accès à l'espace de dépot");
        //System.out.println(resto.getLeStock().toString());
        System.out.println("Début chargement de l'employé " + id);
        Utils.sleepNoInterrupt(gestTemps.dureeRestoVersDureeOrdi(resto.getSimu().getParasSimu().getResto().getDureeChargement()));             
        System.out.println("Fin du temps de chargement de l'employé " + id);           
        stocksActuels = resto.getLeStock().getStocksActuels()[numPlat];
        for(int i = 0; i < quantite; i++){
            resto.getLeStock().setRessourcesDispos(resProduites[i], stocksActuels + i, numPlat);
        }        
        resto.getLeStock().setSpecStock(stocksActuels + quantite, numPlat);
        System.out.println(resto.getLeStock().toString());
        resto.getLeStock().libereDepot();
        System.out.println("Employé " + id + " libère l'espace de dépot");
        //}
    }

    public void servir(int caisseReservee){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        ParametresSimulation paras = this.simu.getParasSimu();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();  
        GestionnaireFileAttente file = simu.getFileAttente();   
        int nbProduits, stockActuel;
        //Optional<CommandeClient> commande;
        int[] vraieCommande = new int[paras.getResto().getCarte().getLesPlats().length];
        System.out.println(resto.getLeStock().toString());

        while (!simu.getFileAttente().fileClosed()) {
            System.out.println("File d'attente ouverte !");
            Optional<CommandeClient> nextCommande = Optional.empty();
            while (!simu.getFileAttente().fileClosed() && (nextCommande = file.prendCommande()).isEmpty()) {
                //System.out.println("Employé " + id + " attend un client...");
                file.attendsUnClient(1000);
            }
            //commande = this.simu.getFileAttente().prendCommande(); // on recupère le fait qu'il y ait ou pas une commande
            //System.out.println("Commande client : " + commande);  
            if (!nextCommande.isEmpty()) { // si ya un client
                vraieCommande = nextCommande.get().getCommande(); // récupère la vrai commande du client.
                System.out.println("Commande prise par l'employé n°" + id + " : ");
                System.out.println(nextCommande.get().toString()); 
                System.out.println(resto.getLaGestion().toString());               
                System.out.println("Employé " + id + " attend la durée de prise de commande...");
                Utils.sleepNoInterrupt(gestTemps.dureeRestoVersDureeOrdi(paras.getResto().getDureeCommande()));
                System.out.println("Fin de l'attente");
                resto.getLaGestion().ajouteCommande(vraieCommande);
                System.out.println(resto.getLaGestion().toString());
            }
            System.out.println("Employé " + id + " demande l'accès à l'espace de retrait.");
            while(!resto.getLeStock().reserveRetrait()){
                //on attend de pouvoir accéder à la zone de retrait de l'espace de stockage
                //System.out.println("Espace de retrait occupé...");
                Utils.sleepNoInterrupt(gestTemps.dureeRestoVersDureeOrdi(30000));     //on réessaye toutes les 30s en durée resto
            }
            System.out.println("Employé " + id + " a obtenu l'accès à l'espace de retrait.");

            System.out.println(resto.getLeStock().toString());
            System.out.println("Début du processus de déchargement par l'employé " + id);
            for(int numPlat = 0; numPlat < 3; numPlat++){
                nbProduits = vraieCommande[numPlat];
                if(nbProduits != 0){                  
                    if(resto.getLeStock().getStocksActuels()[numPlat] != 0){
                        //System.out.println("Employé " + id + " commence à jeter le plat " + numPlat);
                        jeter(numPlat);
                        //System.out.println("Employé " + id + " a terminé de jeter le plat " + numPlat);                                         
                    }                   
                    while(resto.getLeStock().getStocksActuels()[numPlat] < nbProduits){
                        //System.out.println("Employé " + id + " attend un ou plusieurs plats " + numPlat); 
                        Utils.sleepNoInterrupt(gestTemps.dureeRestoVersDureeOrdi(30000));         // on réessaye toutes les 30s en durée resto avant d'avoir 
                    }                                                                                        // toutes les ressources requises
                    System.out.println("Employé " + id + " dipose de tous les plats " + numPlat);                                                                                  
                    stockActuel = resto.getLeStock().getStocksActuels()[numPlat];
                    resto.getLeStock().setSpecStock(stockActuel - nbProduits, numPlat);
                    System.out.println("Employé " + id + " attend la durée de déchargement...");
                    Utils.sleepNoInterrupt(simu.getGestionnaireTemps().dureeRestoVersDureeOrdi(resto.getSimu().getParasSimu().getResto().getDureeDechargement()));
                    System.out.println("Fin de l'attente");
                    resto.getLaGestion().setVentes(nbProduits, numPlat);
                }
            }
            System.out.println("Fin déchargement de l'employé " + id);
            System.out.println(resto.getLeStock().toString());            
            System.out.println("Employé " + id + " libère l'espace de retrait.");
            resto.getLeStock().libereRetrait();
        }
        System.out.println("Employé " + id + " libère la caisse " + caisseReservee + ".");
        resto.getLeComptoir().libereCaisse(caisseReservee);
    }

    public void jeter(int numPlat){

        MyResto resto = (MyResto) this.simu.getGestionnaireResto();
        GestionnaireTemps gestTemps = this.simu.getGestionnaireTemps();
        Ressources resTemp;  
        boolean perime = false;
        int indexRes = -1;        

        do{
            indexRes++;
            if(indexRes < resto.getLeStock().getStocksActuels()[numPlat]){
                resTemp = resto.getLeStock().getRessourcesDispos()[indexRes][numPlat];
                if(resTemp.getFinConso() < gestTemps.currentTimeResto()){
                    resto.getLeStock().setSpecStock(resto.getLeStock().getSpecStock(numPlat) - 1, numPlat);
                    perime = true;                               
                }
                else{
                    perime = false;
                }
            }
        } while(perime);
        System.out.println("Nombre de ressources " + numPlat + " à jeter : " + indexRes);
        System.out.println("Stocks avant de jeter :");
        System.out.println(resto.getLeStock().toString());
        for(int i = 0; i < indexRes; i++){                                                      
            resTemp = resto.getLeStock().getRessourcesDispos()[indexRes + i + 1][numPlat];
            resto.getLeStock().setRessourcesDispos(resTemp, i, numPlat);            
        }
        System.out.println("Stocks après avoir jeté :");
        System.out.println(resto.getLeStock().toString());
        resto.getLaGestion().ajoutePlat(numPlat, indexRes);
        System.out.println(resto.getLaGestion().toString());
    } 
}