/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import java.io.Serializable;
import traders.Trader;

/**
 *
 * @author aruna
 */
public class Item implements Serializable{
    String itemname;
    int price;
    String Owner;
    Trader tradersinterface;
    public Item(String itemname,int price,String owner,Trader tradersinterface)
    {
        this.itemname=itemname;
        this.Owner=owner;
        this.price=price;
        this.tradersinterface=tradersinterface;
    }

    public String getItemname() {
        return itemname;
    }

    public int getPrice() {
        return price;
    }

    public String getOwner() {
        return Owner;
    }

    public Trader getTradersinterface() {
        return tradersinterface;
    }

    
    
}
