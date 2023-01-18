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
    
    public static void main(String[] args) {
        /*Caisse[] ca = new Caisse[3];
        for(int i=0; i<3; i++){
            ca[i] = new Caisse(i);
        }
        Comptoir co = new Comptoir(ca);
        int demande1 = co.reserveCaisse();
        System.out.println(demande1);
        int demande2 = co.reserveCaisse();
        System.out.println(demande2);
        co.libereCaisse(ca[0]);
        int demande3 = co.reserveCaisse();
        System.out.println(demande3);
        int demande4 = co.reserveCaisse();
        System.out.println(demande4);
        co.libereCaisse(ca[1]);
        int demande5 = co.reserveCaisse();
        System.out.println(demande5);
        int demande6 = co.reserveCaisse();
        System.out.println(demande6);*/
    }
}