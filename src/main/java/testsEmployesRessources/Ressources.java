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
package testsEmployesRessources;

/**
 *
 * @author camil
 */
public class Ressources {
    private String nom;
    private int dispo;
    private static long tempsProduction;
    private static long tempsConsommation;
    private static long FinDeProcuction; 
    public static int MAXRessource = 10;
    
public Ressources(int val, String nom, long tempsProduction,long tempsConsommation) {
        this.dispo = val;
        this.nom=nom; 
        this.tempsProduction=tempsProduction; 
        this.tempsConsommation=tempsConsommation; 
        this.FinDeProcuction=FinDeProcuction; 
    }
}


public synchronized void produit() {
        boolean ok = false;
        while (!ok) {

            synchronized (this) {
                if (this.dispo < MAXR) {
                    this.dispo = this.dispo + 1;
                    ok = true;
                    this.notifyAll();
                } 
                else {
                    this.wait();
                }
            }
        }
    }

    public synchronized void consomme() {
        boolean ok = false;
        while (!ok) {

            synchronized (this) {
                if (this.dispo > 0) {
                    this.dispo = this.dispo - 1;
                    ok = true;
                    this.notifyAll();
                } 
                else {
                    this.wait();
                    }
                }
            }
        }
    }