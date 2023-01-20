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

import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.SimuResto;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.SimulateurGlobal;


/**
 *
 * @author camil
 */
public class MyResto extends SimuResto {
    
    private Comptoir leComptoir;
    private EspaceStockage leStock;
    private Gestion laGestion;

    public EspaceStockage getLeStock() {
        return leStock;
    }

    public Gestion getLaGestion(){
        return laGestion;
    }

    /**
     * @return the leComptoir
     */
    public Comptoir getLeComptoir() {
        return leComptoir;
    }

    public void setLeStock(EspaceStockage stock){
        this.leStock = stock;
    }

    @Override
    public void start() {
        SimulateurGlobal simu = this.getSimu();        
        int nbCaisses = simu.getParasSimu().getResto().getNbrCaisse();
        int nbEmployes = simu.getParasSimu().getResto().getNbrEmployes();
        int nbPlats = simu.getParasSimu().getResto().getCarte().getLesPlats().length;
        Employes[] lesEmployes = new Employes[nbEmployes];        
        Caisse[] lesCaisses = new Caisse[nbCaisses];
        for(int i=0; i < nbCaisses; i++){
            lesCaisses[i] = new Caisse(i);
        }
        this.leComptoir = new Comptoir(lesCaisses);
        this.leStock = new EspaceStockage(nbPlats);
        this.laGestion = new Gestion(nbPlats);

        System.out.println("Current time resto : " + this.getSimu().getGestionnaireTemps().currentTimeResto());
        System.out.println("Durée ouverture : " + this.getSimu().getParasSimu().getDureeOuverture());

        for(int i = 0; i < nbEmployes; i++){
            lesEmployes[i] = new Employes(i, "toto", "titi", simu);
            lesEmployes[i].start();
        }

        for(int i = 0; i < nbEmployes; i++){
            try {
                lesEmployes[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        int benefice = this.laGestion.calculBenefice();     
        System.out.println("\nProduction de : " + this.laGestion.getProduits()[0] + " burgers, " + this.laGestion.getProduits()[1] + " frites et " + this.laGestion.getProduits()[2] + " salades.");
        System.out.println("Vente de : " + this.laGestion.getVentes()[0] + " burgers, " + this.laGestion.getVentes()[1] + " frites et " + this.laGestion.getVentes()[2] + " salades.");
        System.out.println("Bénéfices : " + benefice);        
    }    
}
