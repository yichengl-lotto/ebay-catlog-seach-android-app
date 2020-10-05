package com.example.myapplication;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class productTab extends Fragment {

    JSONArray mimageUrlJSON;
    String mtitle;
    String mshipPrice;
    String mcurrentPrice;
    JSONObject mitemJSON;
    private Context context;
    ProgressBar mprogressBar;
    Boolean mready;

    public productTab(JSONArray imageUrlJSON, String title, String shipPrice, String currentPrice, JSONObject itemJSON) {
        mimageUrlJSON = imageUrlJSON;
        mtitle = title;
        mshipPrice = shipPrice;
        mcurrentPrice = currentPrice;
        mitemJSON = itemJSON;



    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_layout,container,false);
        this.context=getContext();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.product_images_layout);
        //mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar3);


        for(int i= 0; i<mimageUrlJSON.length(); i++) {
            ImageView image = new ImageView(context);

            try {
                String imageurl = mimageUrlJSON.getString(i);
                Picasso.get().load(imageurl).fit().centerInside().into(image);
                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(1000, 1000));
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                //image.setMaxHeight(20);

                // Adds the view to the layout
                layout.addView(image);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //mprogressBar.setVisibility(View.INVISIBLE);

        TextView titleHolder = view.findViewById(R.id.product_title);
        TextView currentPriceHolder = view.findViewById(R.id.product_currentPrice);
        TextView shipPriceHolder = view.findViewById(R.id.product_shipPrice);
        String titleHTML = mtitle;
        String shipPriceHTML;
        titleHolder.setText(titleHTML);
        String currentPriceHTML = "$" + mcurrentPrice;
        currentPriceHolder.setText(currentPriceHTML);
        if (mshipPrice.equals("0.0")) {
            shipPriceHTML ="FREE Shipping";
        }
        else {
            shipPriceHTML = "Ships for $"+mshipPrice;
        }

        shipPriceHolder.setText(shipPriceHTML);
        String productFeature = "";
        TextView productFeatureHolder = view.findViewById(R.id.product_feature);
        TextView productFeatureTitleHolder = view.findViewById(R.id.product_feature_title);
        TextView specificHolder = view.findViewById(R.id.product_specific);
        TextView specificTitleHolder = view.findViewById(R.id.product_specific_title);
        View horizontalLine1 = view.findViewById(R.id.horizontal1);
        View horizontalLine2 = view.findViewById(R.id.horizontal2);
        String productSpecific = "<ul>";
        if(mitemJSON.has("Subtitle")) {
            try {
                productFeature += "<p><b>Subtitle</b>&nbsp&nbsp    "+mitemJSON.getString("Subtitle")+"</p>";

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(mitemJSON.has("ItemSpecifics")) {
            try {
                if(mitemJSON.getJSONObject("ItemSpecifics").has("NameValueList")) {
                    for(int i=0; i<mitemJSON.getJSONObject("ItemSpecifics").getJSONArray("NameValueList").length();i++) {
                        if(mitemJSON.getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(i).getString("Name").equals("Brand")) {
                            productFeature += "<p><b>Brand</b>&nbsp&nbsp    "+mitemJSON.getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(i).getJSONArray("Value").getString(0)+"</p>";


                        }
                        else {
                            productSpecific +="<li>" +mitemJSON.getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(i).getJSONArray("Value").getString(0) +"</li>";
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(productFeature.isEmpty()) {
            productFeatureHolder.setVisibility(View.GONE);
            productFeatureTitleHolder.setVisibility(View.GONE);
            horizontalLine1.setVisibility(View.GONE);

        }
        else {
            productFeatureHolder.setVisibility(View.VISIBLE);
            productFeatureTitleHolder.setVisibility(View.VISIBLE);
            horizontalLine1.setVisibility(View.VISIBLE);
            productFeatureHolder.setText(HtmlCompat.fromHtml(productFeature,0));
        }
        productSpecific += "</ul>";
        if (productSpecific.equals("<ul></ul>")) {
            specificHolder.setVisibility(View.GONE);
            specificTitleHolder.setVisibility(View.GONE);
            horizontalLine2.setVisibility(View.GONE);
        }
        else {
            specificHolder.setVisibility(View.VISIBLE);
            specificTitleHolder.setVisibility(View.VISIBLE);
            horizontalLine2.setVisibility(View.VISIBLE);
            specificHolder.setText(HtmlCompat.fromHtml(productSpecific,0));
        }
       // mprogressBar.setVisibility(View.GONE);
    }

}
