package fr.insa.kienlen.resto;

import java.util.Arrays;

public class EspaceStockage
{
    private Ressources[][] ressourcesDispos;
    private int[] stocksActuels;
    private boolean isRetraitLibre;
    private boolean isDepotLibre;
    
    public EspaceStockage(int nbPlats) {
        this.ressourcesDispos = new Ressources[255][nbPlats];    
        this.stocksActuels = new int[nbPlats];
        for(int i = 0; i < nbPlats; i++){
            stocksActuels[i] = 0;
        }
        this.isRetraitLibre = true;
        this.isDepotLibre = true;
    }

    public synchronized Ressources[][] getRessourcesDispos() {
        return ressourcesDispos;
    }

    public synchronized void setRessourcesDispos(Ressources resProduite, int index, int numPlat) {
        this.ressourcesDispos[index][numPlat] = resProduite;
    }

    public synchronized int[] getStocksActuels() {
        return stocksActuels;
    }

    public synchronized void setStocksActuels(int[] stocksActuels) {
        this.stocksActuels = stocksActuels;
    }

    public synchronized int getSpecStock(int valTypePlat){
        return this.stocksActuels[valTypePlat];
    }
    
    public synchronized void setSpecStock(int quantite, int valTypePlat){
        this.stocksActuels[valTypePlat] = quantite;
    }

    /* Gestion de la zone de dépôt de l'espace de stockage */
    public synchronized boolean reserveDepot(){
        if(this.isDepotLibre){
            this.isDepotLibre = false;
            return true;
        }
        else{
            return false;
        }
    }

    public void libereDepot(){
        this.isDepotLibre = true;
    }  
    
    /* Gestion de la zone de retrait de l'espace de stockage */
    public synchronized boolean reserveRetrait(){
        if(this.isRetraitLibre){
            this.isRetraitLibre = false;
            return true;
        }
        else{
            return false;
        }
    }

    public void libereRetrait(){
        this.isRetraitLibre = true;
    }  

    @Override
    public String toString(){
        return "Stocks actuels : " + Arrays.toString(stocksActuels); 
        //stocksActuels[0] + ", " + stocksActuels[1] + ", " + stocksActuels[2] + "]";
    }
}