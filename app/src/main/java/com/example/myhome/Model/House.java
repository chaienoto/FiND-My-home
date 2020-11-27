package com.example.myhome.Model;

import android.net.Uri;

public class House {
    public String ID, address, price, detail;
    public Uri img;
    public String path;

    public House(String ID, String address, String price, String detail, Uri img, String path) {
        this.ID = ID;
        this.address = address;
        this.price = price;
        this.detail = detail;
        this.img = img;
        this.path = path;
    }


}
