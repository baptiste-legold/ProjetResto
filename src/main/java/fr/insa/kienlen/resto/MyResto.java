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
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.Restaurant;

/**
 *
 * @author camil
 */
public class MyResto extends SimuResto {
    
    private Comptoir leComptoir;
    private EspaceStockage leStock;
    private Ressources[] lesRessources; 

    public Ressources[] getLesRessources() {
        return lesRessources;
    }

    public EspaceStockage getLeStock() {
        return leStock;
    }

    /**
     * @return the leComptoir
     */
    public Comptoir getLeComptoir() {
        return leComptoir;
    }

    @Override
    public void start() {
        SimulateurGlobal simu = this.getSimu();
        Restaurant resto = new Restaurant
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }    
}
