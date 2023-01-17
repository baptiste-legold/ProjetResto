package fr.insa.kienlen.resto;

public class EspaceStockage
{
    private Ressources[][] ressourcesDispos;
    private int[] stocksActuels;
    private boolean isRetraitLibre;
    private boolean isDepotLibre;
    
    public EspaceStockage() {
        this.ressourcesDispos = new Ressources[255][3];    
        this.stocksActuels = new int[]{0};
        this.isRetraitLibre = true;
        this.isDepotLibre = true;
    }

    public Ressources[][] getRessourcesDispos() {
        return ressourcesDispos;
    }

    public void setRessourcesDispos(Ressources resProduite, int index, int numPlat) {
        this.ressourcesDispos[index][numPlat] = resProduite;
    }

    public int[] getStocksActuels() {
        return stocksActuels;
    }

    public void setStocksActuels(int[] stocksActuels) {
        this.stocksActuels = stocksActuels;
    }

    public int getSpecStock(int valTypePlat){
        return this.stocksActuels[valTypePlat];
    }
    
    public void setSpecStock(int quantite, int valTypePlat){
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
}