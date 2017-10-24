package com.example.testmutifileupload.bean;

/**
 * Created by Administrator on 2017/10/24.
 */

public class Goods {
    private String goodsId;
    private String goodsName;
    private String goodsDes;
    private String goodsImgUri;
    private double goodsPrice;
    private String goodsImgUrl;

    public Goods(){}

    public Goods(String goodsName, String goodsDes, String goodsImgUri, double goodsPrice) {
        this.goodsName = goodsName;
        this.goodsDes = goodsDes;
        this.goodsImgUri = goodsImgUri;
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDes() {
        return goodsDes;
    }

    public void setGoodsDes(String goodsDes) {
        this.goodsDes = goodsDes;
    }

    public String getGoodsImgUri() {
        return goodsImgUri;
    }

    public void setGoodsImgUri(String goodsImgUri) {
        this.goodsImgUri = goodsImgUri;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsDes='" + goodsDes + '\'' +
                ", goodsImgUri='" + goodsImgUri + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsImgUrl='" + goodsImgUrl + '\'' +
                '}';
    }
}
