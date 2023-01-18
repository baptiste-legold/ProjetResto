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
        int nbCaisses = this.getSimu().getParasSimu().getResto().getNbrCaisse();
        int nbEmployes = this.getSimu().getParasSimu().getResto().getNbrEmployes();
        Caisse[] lesCaisses = new Caisse[nbCaisses];
        for(int i=0; i < nbCaisses; i++){
            lesCaisses[i] = new Caisse(i);
        }
        this.leComptoir = new Comptoir(lesCaisses);
        this.leStock = new EspaceStockage();
        int[] commandesInitiales = new int[]{2,0,0};
        this.laGestion = new Gestion(commandesInitiales);
        for(int i = 0; i <= nbEmployes; i++){
            Employes emp = new Employes(i, "toto", "titi", simu);
            emp.start();
        }  
    }    
}
