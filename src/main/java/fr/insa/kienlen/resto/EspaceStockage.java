package fr.insa.kienlen.resto;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EspaceStockage
{
    private int[] stocksActuels;
    private Lock lock = new ReentrantLock();

    public EspaceStockage(int[] stocksActuels) {
        this.stocksActuels = stocksActuels;
    }

    public int[] getStocksActuels() {
        return stocksActuels;
    }

    public void setStocksActuels(int[] stocksActuels) {
        this.stocksActuels = stocksActuels;
    }

    public synchronized int reserveStock() {
        lock.lock();
        try {
            this.wait();
            return 1;
        } catch (InterruptedException e) {
            System.out.println("Espace de stockage non disponible");
            return -1;
        }
    }
    
    public int getStock(int valTypePlat){
        return this.stocksActuels[valTypePlat];
    }
    
    public void setStock(int quantite, int valTypePlat){
        this.stocksActuels[valTypePlat] = quantite;
    }
    
    public synchronized void libereStock() {
        lock.unlock();
    }
}