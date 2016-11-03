/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traders;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import marketplace.Item;

/**
 *
 * @author aruna
 */
@SuppressWarnings("serial")
public class TradersImpl extends UnicastRemoteObject implements Trader {
    String name;
    Trader traders;
    public TradersImpl(String name)throws RemoteException
    {
       super();
       this.name=name;
          
    }
    @Override
    public String getName() throws RemoteException
    {
        return this.name;
    }
    @Override
    public void updateMarket(ArrayList<Item> itemList) throws RemoteException {
        for(int i=0;i<itemList.size();i++)
        {
            String str=itemList.get(i).getItemname()+" , "+itemList.get(i).getPrice()+" , "+itemList.get(i).getOwner();
            System.out.println(str);
        }
     
    }

    /**
     *
     * @param item
     * @throws RemoteException
     */
    @Override
    public void notifyWishItem(Item item) throws RemoteException {
        String str="item meets your requirement   ";
        str+=item.getItemname()+","+item.getPrice()+","+item.getOwner();
        System.out.println(str);
        
    }

    @Override
    public void updateWishItem(Item item) throws RemoteException {
        
        
    }

    @Override
    public void notifyShortageMoney() throws RemoteException {
        String str="your balance is not enough...";
        System.out.println(str);
                
    }

    @Override
    public void notifySold(String name, float price) throws RemoteException {
        String str="The item has been sold...";
        str += name + "," + price;
         System.out.println(str);
        
    }

    @Override
    public void updateBalance(float balance) throws RemoteException {
        String str="updated new balance...";
        str += balance;
        System.out.println(str);
    }
    
}
