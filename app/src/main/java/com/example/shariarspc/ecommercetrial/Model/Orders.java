package com.example.shariarspc.ecommercetrial.Model;

public class Orders {
    private String address,date,district,name,phone,state,time,totalamount;

    public Orders() {
    }

    public Orders(String address, String date, String district, String name, String phone, String state, String time, String totalamount) {
        this.address = address;
        this.date = date;
        this.district = district;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.totalamount = totalamount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }
}
