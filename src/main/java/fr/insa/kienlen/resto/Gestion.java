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

import java.util.Arrays;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.TypePlat;

/**
 *
 * @author blegold01
 */
public class Gestion {
    
    private int[] ventes;
    private int[] produits;
    private int[] commandesEnCours;

    public Gestion(int nbPlats) {
        int[] temp = new int[nbPlats];
        for(int i = 0; i < nbPlats; i++){
            temp[i] = 0;            
        }
        ventes = temp;
        produits = temp;
        commandesEnCours = temp;
    }

    public synchronized int[] getCommandesEnCours(){
        return this.commandesEnCours;
    }

    public synchronized void ajouteCommande(int[] commande) {
        for(int i=0; i < commande.length; i++){
            commandesEnCours[i] += commande[i];
        }
    }

    public synchronized void ajoutePlat(int numPlat, int quantite){
        commandesEnCours[numPlat] += quantite; 
    }    


    public synchronized void retirePlat(int numPlat, int quantite){
        commandesEnCours[numPlat] -= quantite; 
    }    

    public int[] getVentes() {
        return ventes;
    }

    public synchronized void setVentes(int quantiteAjoutee, int numPlat) {
        this.ventes[numPlat] += quantiteAjoutee;
    }

    public int[] getProduits() {
        return produits;
    }

    public synchronized void setProduits(int quantiteAjoutee, int numPlat) {
        this.produits[numPlat] += quantiteAjoutee;
    }

    @Override
    public String toString(){
        return "Commandes en cours : " + Arrays.toString(commandesEnCours);
    }

    public int calculBenefice(){
        int benefice = 0;
        TypePlat plat = new TypePlat();
        for(int i = 0; i < 3; i++){
            switch(i){
                case 0:
                    plat = TypePlat.burger();
                    break;
                case 1:
                    plat = TypePlat.frites();
                    break;
                case 2:
                    plat = TypePlat.salade();
                    break;
            }
            benefice = benefice + this.ventes[i] * plat.getPrixVente() - this.produits[i] * plat.getCoutPreparation();
        }
        return benefice;
    }
}