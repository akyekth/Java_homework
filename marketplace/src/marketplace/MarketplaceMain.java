/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import bank.Bank;
import bank.Server;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author aruna
 */
@SuppressWarnings("serial")
public class MarketplaceMain {
    private static final String USAGE = "java Marketrmi.Server <market_rmi_url>";
    private static final String MARKET = "MarketPlace";
     private static final String Host="localhost";
     private String marketname;
     Bank bankobj;

    public MarketplaceMain()throws RemoteException,MalformedURLException, NotBoundException
    {
         super();
       try{
              bankobj=(Bank)Naming.lookup("rmi://localhost/1099/Nordea");
              System.out.println(bankobj + " is ready.");
              Marketplace marketobj=new MarketplaceImpl(MARKET,bankobj);
              try {
                LocateRegistry.getRegistry(1098).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1098);
            }
              String rminame= "rmi://localhost/1098/MarketPlace";
               Naming.rebind(rminame, marketobj);
               
            
               
          }catch(Exception e){
              e.printStackTrace();
          }
    }
    
    public static void main(String[] args) throws NotBoundException {
        try {
            new MarketplaceMain();
            System.out.println("Market is ready!");
        } catch (RemoteException re) {
            System.out.println(re);
            System.exit(1);
        } catch (MalformedURLException me) {
            System.out.println(me);
            System.exit(1);
        }
    }
    
}
