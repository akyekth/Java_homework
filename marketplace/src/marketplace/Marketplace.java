/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;
import traders.Trader;

/**
 *
 * @author aruna
 */
public interface Marketplace extends Remote {
    //public Trader getTrader(String client)throws RemoteException;
    void registerClient(Trader obj) throws RemoteException;
    void unregisterClient(Trader obj) throws RemoteException;
    void  addItem(Item item) throws RemoteException;
    void addWish(Trader wisher, Item item) throws RemoteException;
    public void  buyItem(String buyer, Trader buyerInterface, String name, float price, String seller) throws RemoteException;
          
    
}
