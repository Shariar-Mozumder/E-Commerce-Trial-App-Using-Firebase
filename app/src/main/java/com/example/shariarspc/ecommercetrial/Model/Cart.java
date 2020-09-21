package com.example.shariarspc.ecommercetrial.Model;

public class Cart {

    private String pid,pname,pprice,quantity,discount;

    public Cart() {
    }

    public Cart(String pid, String pname, String pprice, String quantity, String discount) {
        this.pid = pid;
        this.pname = pname;
        this.pprice = pprice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
