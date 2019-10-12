package com.refl3xn.prototype;


public class Item {
    String item, itemCost, pickupAddress, deliveryAddress, deliveryCost;
    Double dLat, dLng, pLat, pLng;
    String uid, duid;



    public Item(){}

    public Item(String item, String itemCost, String pickupAddress, String deliveryAddress, String deliveryCost, Double dLat, Double dLng, Double pLat, Double pLng, String uid, String duid) {
        this.item = item;
        this.itemCost = itemCost;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.deliveryCost = deliveryCost;
        this.dLat = dLat;
        this.dLng = dLng;
        this.pLat = pLat;
        this.pLng = pLng;
        this.uid = uid;
        this.duid = duid;
    }

    public String getDuid() {
        return duid;
    }

    public void setDuid(String duid) {
        this.duid = duid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Double getdLat() {
        return dLat;
    }

    public void setdLat(Double dLat) {
        this.dLat = dLat;
    }

    public Double getdLng() {
        return dLng;
    }

    public void setdLng(Double dLng) {
        this.dLng = dLng;
    }

    public Double getpLat() {
        return pLat;
    }

    public void setpLat(Double pLat) {
        this.pLat = pLat;
    }

    public Double getpLng() {
        return pLng;
    }

    public void setpLng(Double pLng) {
        this.pLng = pLng;
    }
}
