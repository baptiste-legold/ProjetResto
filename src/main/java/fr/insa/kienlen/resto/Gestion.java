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

import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.CommandeClient;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.Restaurant;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.TypePlat;

/**
 *
 * @author blegold01
 */
public class Gestion {
    
    private int[] ventes = new int[3];
    private int[] produits = new int[3];
    private int[] commandesEnCours = new int[3];

    public Gestion() {
        for(int i = 0; i < 3; i++){
            ventes[i] = 0;
            produits[i] = 0;
            commandesEnCours[i] = 0;
        }
    }

    public synchronized void ajouteCommande(CommandeClient commande) {
        int[] ajout = commande.getCommande();
        for(int i=0; i < 3; i++){
            commandesEnCours[i] += ajout[i];
        }
    }

    public synchronized void ajoutePlat(int numPlat, int quantite){
        commandesEnCours[numPlat] += quantite; 
    }    


    public synchronized void retirePlat(int numPlat, int quantite){
        commandesEnCours[numPlat] -= quantite; 
    }    

    public int calculBenefice(){
        int benefice = 0;
        TypePlat plat = new TypePlat();
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
            benefice += this.ventes[i] * plat.getPrixVente() - this.produits[i] * plat.getCoutPreparation();
        }
        return benefice;
    }

    public int[] getVentes() {
        return ventes;
    }

    public void setVentes(int quantiteAjoutee, int numPlat) {
        this.ventes[numPlat] += quantiteAjoutee;
    }

    public int[] getProduits() {
        return produits;
    }

    public void setProduits(int quantiteAjoutee, int numPlat) {
        this.produits[numPlat] += quantiteAjoutee;
    }

    public int[] getCommandesEnCours(){
        return this.commandesEnCours;
    }

    public static void main(String[] args) {
        
    }
}