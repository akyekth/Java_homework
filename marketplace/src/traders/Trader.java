/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traders;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import marketplace.Item;

/**
 *
 * @author aruna
 */
public interface Trader extends Remote {
    public void updateMarket(ArrayList<Item> itemList) throws RemoteException;
    public void notifyWishItem(Item item) throws RemoteException;
    public void updateWishItem(Item item) throws RemoteException;
    public String getName() throws RemoteException;
    
    public void notifyShortageMoney() throws RemoteException;
    public void notifySold(String name, float price) throws RemoteException;
    
    public void updateBalance(float balance) throws RemoteException;
}
