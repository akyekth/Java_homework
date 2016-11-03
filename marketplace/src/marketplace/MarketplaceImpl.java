/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import bank.Account;
import bank.Bank;
import bank.RejectedException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import traders.Trader;
import traders.TradersImpl;

/**
 *
 * @author aruna
 */
@SuppressWarnings("serial")
public class MarketplaceImpl extends UnicastRemoteObject implements Marketplace {
    Item item;
    private String marketname;
    private ArrayList<Trader> tradersclient = new ArrayList<>();
    //private Map<String,Trader> trader=new HashMap();
    private ArrayList<Item> wishlist=new ArrayList();
    private ArrayList<Item> itemlist=new ArrayList();
    Bank bankobj;
    Account account;
    
    public MarketplaceImpl(String marketname,Bank bankobj)throws RemoteException
    {
        super();
        this.marketname=marketname;
        this.bankobj=bankobj;
    }

    
    //@Override
   /*public Trader getTrader(String client)throws RemoteException
   {  
       for(int i=0;i<tradersclient.size();i++)
       {
       return tradersclient.get(i).equals();
       }       
       
   }*/
    
    @Override
    public synchronized void addItem(Item item) throws RemoteException {
        itemlist.add(item);
        System.out.println(item.getItemname()+"item is added");
    }
    @Override
    public void registerClient(Trader client) throws RemoteException {
        
        if(tradersclient.contains(client))
        {
            System.out.println("client alredy registered...");
        }
        tradersclient.add(client);
        
          Account acc;
        try {
            System.out.println(client);
            String name = client.getName();
            System.out.println(name);
            acc = bankobj.newAccount(name);
            acc.deposit(1000); 

        } catch (RejectedException ex) {
            Logger.getLogger(MarketplaceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    @Override
    public void unregisterClient(Trader client) throws RemoteException {
        if(!tradersclient.contains(client))
          {
              throw new RemoteException("client not registered in marketplace");
          }
        tradersclient.remove(client);
        removeItemlist(itemlist,client);
        removeItemlist(wishlist,client);
        //update itemlist in market after deleting registered client
        updateItemlist();
    }

    @Override
    public void addWish(Trader wisher, Item item) throws RemoteException {
         if(!checkmarket(wisher, item))
        {
            wishlist.add(item);
            System.out.println("item is added to wishlist");
        }
    }

    @Override
    public void buyItem(String buyer, Trader buyerInterface, String name, float price, String seller) throws RemoteException {

        Account buyerAcc = bankobj.getAccount(buyer);
        Account sellerAcc = bankobj.getAccount(seller);
        
        try
        {
            buyerAcc.withdraw(price);
            sellerAcc.deposit(price);
            System.out.println("buyer balance"+"   "+ buyer+"  "+buyerAcc.getBalance());
            System.out.println("seller balance"+ "  "+ seller +" "+sellerAcc.getBalance());
            for(int i=0; i<itemlist.size(); i++)
            {
                if(itemlist.get(i).getItemname().equals(name) && itemlist.get(i).getPrice() == price && itemlist.get(i).getOwner().equals(seller))
                {
                    Trader sellerInterface = itemlist.get(i).getTradersinterface();
                    sellerInterface.notifySold(name, price);
                    buyerInterface.updateBalance(buyerAcc.getBalance());
                    sellerInterface.updateBalance(sellerAcc.getBalance());
                    itemlist.remove(i);
                    --i;
                    break;
                }
            }
            updateItemlist();
           
            
        } catch (RejectedException ex)
        {
            buyerInterface.notifyShortageMoney();
        }
    }

    
 
    public boolean checkmarket(Trader wisher,Item item) throws RemoteException
    {  
       // this.item=item;
        boolean bool=false;
      
        System.out.println("number of items in the database::"+itemlist.size());
        for(int i=0 ;i<itemlist.size();i++)
        {
          if(item.getItemname().equals(itemlist.get(i).getItemname()))
          {
              if(item.getPrice()>=itemlist.get(i).getPrice())
              {
                  wisher.notifyWishItem(itemlist.get(i));
                  wisher.updateWishItem(item);
                
                  bool=true;
              }
          }
          
        }
        return bool;
    }
    public synchronized void updateItemlist()
    {
     for (Trader client : tradersclient) {
            try
            {
                client.updateMarket(itemlist);
            } catch (RemoteException ex)
            {
                Logger.getLogger(MarketplaceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }         
    }
    public void removeItemlist(ArrayList<Item> iemlist,Trader client)
    {
        for(int i=0;i<itemlist.size();i++)
        {
            if(itemlist.get(i).getTradersinterface().equals(client))
            {
                itemlist.remove(client);
            }
        }
    }

    
}
