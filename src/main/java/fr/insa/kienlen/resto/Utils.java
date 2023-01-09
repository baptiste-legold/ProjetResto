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

/**
 *
 * @author camil
 */
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francois
 */
public class Utils {

    public static int DEBUGLEVEL = -1;

    public static boolean DEBUGAFFICHETHREAD = true;

    public static void debug(int level, String message) {
        if (level <= DEBUGLEVEL) {
            String s = "DEBUG (" + level + ") : ";
            if (DEBUGAFFICHETHREAD) {
                s = s + "[" + Thread.currentThread().getName() + "] : ";
            }
            s = s + message;
            System.out.println(s);
        }
    }

    public static void sleepNoInterrupt(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new Error("unexpected interrupt", ex);
        }
    }

    public static void joinNoInterrupt(Thread t) {
        try {
            t.join();
        } catch (InterruptedException ex) {
            throw new Error("unexpected interrupt", ex);
        }
    }

}
