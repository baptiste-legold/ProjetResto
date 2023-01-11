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
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.TypePlat;

public class Ressources {

    private TypePlat plat;
    private long debutPreparation; 
    private long finConso;
    
    public Ressources(TypePlat plat, long debutPreparation) {
        this.plat = plat;
        this.debutPreparation = debutPreparation;
    }

    public TypePlat getPlat() {
        return plat;
    }

    public long getDebutPreparation() {
        return debutPreparation;
    }

    public long getFinConso() {
        return finConso;
    }    
}