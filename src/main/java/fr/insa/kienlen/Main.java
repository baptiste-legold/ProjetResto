package fr.insa.kienlen;

import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.ArbitreLogSout;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.fourni.SimulateurGlobal;
import fr.insa.beuvron.cours.multiTache.projets.restoV2.parametres.ParametresSimulation;
import fr.insa.kienlen.resto.MyResto;
import java.util.Random;

public class Main {

    public static void gogogo() {
        ParametresSimulation paras = ParametresSimulation.parasMiniTest();
        MyResto resto = new MyResto();
        SimulateurGlobal simu = new SimulateurGlobal(paras,
                resto,
                new ArbitreLogSout(-1),
                2000,  // taux d'accélération
                new Random(123456));
        simu.start();        
    }

    public static void main(String[] args) {
        gogogo();
    }    
}
