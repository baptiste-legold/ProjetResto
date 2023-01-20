package fr.insa.kienlen.resto;

public class Comptoir {

    private Caisse[] lesCaisses;
    private boolean[] caissesDispos;

    public Comptoir(Caisse[] caisses) {
        this.lesCaisses = caisses;
        boolean[] tab = new boolean[caisses.length];
        for(int i=0; i<tab.length; i++){
            tab[i]=true;
        }
        this.caissesDispos = tab;
    }

    public Caisse[] getLesCaisses() {
        return this.lesCaisses;
    }

    public boolean[] getCaissesDispos(){
        return this.caissesDispos;
    }

    public synchronized int reserveCaisse(){
        int i = 0;
        while(!caissesDispos[i]){
            i++;
            if(i == lesCaisses.length){
                break;
            }
        }
        if(i < lesCaisses.length){
            caissesDispos[i] = false;
            return lesCaisses[i].getId();
        }
        else{
            return -1;
        }
    }

    public void libereCaisse(int caisseALiberer){
        this.caissesDispos[caisseALiberer] = true;
    }    
}