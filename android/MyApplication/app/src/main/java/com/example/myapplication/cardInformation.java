package com.example.myapplication;

import org.json.JSONObject;

public class cardInformation {
    private String mimageURL;
    private String mitemTitle;
    private String mshipPrice;
    private String mtopRated;
    private String mcondition;
    private String mcurrentPrice;
    private String mitemID;
    private JSONObject mshippingInfo;
    private String mviewItemUrl;
    public cardInformation(String imageUrl, String itemTitle, String shipPrice,String topRated,String condition,String currentPrice, String itemID, JSONObject shippingInfo,String viewItemUrl) {
        mimageURL = imageUrl;
        mitemTitle = itemTitle;
        mshipPrice = shipPrice;
        mtopRated = topRated;
        mcondition = condition;
        mcurrentPrice  = currentPrice;
        mitemID = itemID;
        mshippingInfo = shippingInfo;
        mviewItemUrl = viewItemUrl;
    }
    public String getimageURL() {
        return mimageURL;
    }
    public String getitemTitle() {
        return mitemTitle;
    }
    public String getshiPrice() {
        return mshipPrice;
    }
    public String gettopRated() {
        return mtopRated;
    }
    public String getcondition() {
        return mcondition;
    }
    public String getcurrentPrice() {
        return mcurrentPrice;
    }
    public String getitemID() {
        return mitemID;
    }
    public JSONObject getshippingInfo() {
        return mshippingInfo;
    }
    public String getviewItemUrl() {
        return mviewItemUrl;
    }
}
