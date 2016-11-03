/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traders;

import bank.RejectedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.StringTokenizer;
import bank.Bank;
import marketplace.*;
import bank.Account;

/**
 *
 * @author aruna
 */
public class TradersMain {
    private static final String USAGE = "java marketrmi.Client <bank_url>";
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String DEFAULT_MARKET_NAME="MarketPlace";
    String clientname;
    private String  bankname,marketname;
    Bank bankobj;
    Marketplace marketobj;
    Account account;
    Item item;
    private static TradersImpl timpl;
    static enum CommandName {
       registerclient,unregisterclient,buyitem,additem,addwish, quit, help, list,addsold;
    };
    public TradersMain(String bankname,String marketname)
    {
     
     this.bankname=bankname;
     this.marketname=marketname;
     try {
            try {
                LocateRegistry.getRegistry(1099).list();
                LocateRegistry.getRegistry(1098).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
                LocateRegistry.createRegistry(1098);
            }
            
            bankobj = (Bank) Naming.lookup("rmi://localhost/1099/"+bankname);
            marketobj =(Marketplace)Naming.lookup("rmi://localhost/1098/"+marketname);
            
        } catch (Exception e) {
            System.out.println("The runtime failed: " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to bank: " + bankname);
        System.out.println("Connected to Market"+marketname);
     
    }
    public TradersMain()
    {
     
     this(DEFAULT_BANK_NAME,DEFAULT_MARKET_NAME);
    }
    public void run()
    {
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print(clientname + "@" + bankname + ">");
            try {
                String userInput = consoleIn.readLine();
                execute(parse(userInput));
            } catch (RejectedException re) {
                System.out.println(re);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    }     
   void execute(Command command) throws RemoteException, RejectedException {
        if (command == null) {
            return;
        }
        timpl=new TradersImpl(command.getUserName());
        String userName = command.getUserName();
        this.clientname=userName;
        String itemName=command.getItemname();
        int prise=command.getAmount();
        String seller=command.getSellername();
        
       // Trader =marketobj.gettrader
        /**/
        switch (command.getCommandName())
        {
            case list:
                    try {
                            for (String accountHolder : bankobj.listAccounts()) {
                                System.out.println(accountHolder);
                            }
                           } catch (Exception e) {
                            e.printStackTrace();
                            return;
                       }
                    return;
            case quit:
                        System.exit(0);
                
            case help:
                        for (CommandName commandName : CommandName.values()) {
                            System.out.println(commandName);
                        }
                        return;
                        
         }
        if (userName == null) { 
            userName = clientname;
           }
        if (userName == null) {
            System.out.println("name is not specified");
            return;
        } 
        switch (command.getCommandName()) {
                       
            case registerclient:
                     System.out.println(timpl.getName());
                      marketobj.registerClient(timpl);
                     
                      return;
                    
            case unregisterclient:
                      marketobj.unregisterClient(timpl);
                       return;
            case buyitem:
                    String ownerrname=command.getSellername();
                    String buyername=command.userName;
                    timpl.name =buyername;
                    int prise1=command.getAmount();
                    String itemname=command.getItemname();
                     marketobj.buyItem(buyername, timpl,itemname, prise1, ownerrname);
                   return;
            case additem: 
                     timpl.name=command.userName;
                     Item item1=new Item(itemName,prise,timpl.name,timpl);
                       marketobj.addItem(item1);
                       return;
            case addwish:
                timpl.name=command.userName;
                     Item item2=new Item(itemName,prise,timpl.name,timpl);
                       marketobj.addWish(timpl, item2);
                     return;
                           
                       
                     
                
            default:
                System.out.println("Illegal command");
        }
   }
    public Command parse(String userInput)
    {
         if (userInput == null) {
            return null;
        }

        StringTokenizer tokenizer = new StringTokenizer(userInput);
        if (tokenizer.countTokens() == 0) {
            
            return null;
        }
         System.out.println(tokenizer.countTokens());
         System.out.println(userInput);
        CommandName commandName = null;
        String userName=null;
        String itemname = null;
         int amount = 0;
        int userInputTokenNo = 1;
        String sellername=null;

        while (tokenizer.hasMoreTokens()) {
            switch (userInputTokenNo) {
                case 1:
                    try {
                        String commandNameString = tokenizer.nextToken();
                        commandName = CommandName.valueOf(CommandName.class, commandNameString);
                    } catch (IllegalArgumentException commandDoesNotExist) {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;
                case 2:
                    userName = tokenizer.nextToken();
                    break;
                case 3:
                      itemname=tokenizer.nextToken();
                    break;
                case 4:
                    
                        amount = Integer.parseInt(tokenizer.nextToken());
                    
                    break;
                case 5:
                       sellername=tokenizer.nextToken();
                       System.out.println(sellername);
                        break;
                default:
                    System.out.println("Illegal command");
                    break;
            }
            userInputTokenNo++;
        }
        return new Command(commandName,userName,itemname,amount,sellername);
    }
    public class Command
    {
        private String userName;
        private int amount;
        private CommandName commandName;
        private String itemname,sellername;
        private String getUserName() {
            return userName;
        }

        private int getAmount() {
            return amount;
        }
        private String getItemname()
        {
          return itemname;  
        }

        private CommandName getCommandName() {
            return commandName;
        }  
        private String getSellername()
        {
            return sellername;
        }
    
        public Command(CommandName commandname,String userName,String itemname,int amount,String sellername)
        {          
            this.amount=amount;
            this.commandName=commandname;
            this.itemname=itemname;
            this.userName=userName;
            this.sellername=sellername;
        }
    }
    public static void main(String[] args)
    {
       if ((args.length > 1) || (args.length > 0 && args[0].equals("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bankName,marketname;
        if (args.length > 2) {
            bankName = args[0];
            marketname =args[2];
            new TradersMain(bankName,marketname).run();
        } else {
            new TradersMain().run();
        }
    }
    
}
