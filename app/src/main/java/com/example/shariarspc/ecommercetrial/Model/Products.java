package com.example.shariarspc.ecommercetrial.Model;

public class Products {
    private String  pdescription,pname,pprice,pimage,pcatagory,ptime,pdate,pid,productState,sellerAddress,sellerEmail,sellerName,sellerPhone,sid;

    public Products() {
    }

    public Products(String pdescription, String pname, String pprice, String pimage, String pcatagory, String ptime, String pdate, String pid, String productState, String sellerAddress, String sellerEmail, String sellerName, String sellerPhone, String sid) {
        this.pdescription = pdescription;
        this.pname = pname;
        this.pprice = pprice;
        this.pimage = pimage;
        this.pcatagory = pcatagory;
        this.ptime = ptime;
        this.pdate = pdate;
        this.pid = pid;
        this.productState = productState;
        this.sellerAddress = sellerAddress;
        this.sellerEmail = sellerEmail;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sid = sid;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
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

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getPcatagory() {
        return pcatagory;
    }

    public void setPcatagory(String pcatagory) {
        this.pcatagory = pcatagory;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
