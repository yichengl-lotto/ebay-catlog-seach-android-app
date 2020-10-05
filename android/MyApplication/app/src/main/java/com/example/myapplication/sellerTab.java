package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class sellerTab extends Fragment {
    JSONObject msellerInfo;
    JSONObject mreturnInfo;
    TextView sellerInfoArea;
    TextView returnInfoArea;

    public sellerTab(JSONObject sellerInfo,JSONObject returnInfo) {
        msellerInfo = sellerInfo;
        mreturnInfo = returnInfo;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_layout,container,false);




        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sellerInfoArea = (TextView) view.findViewById(R.id.sellerList);
        String htmlResult = " <ul>";
        Iterator<String> sellerInfoKeys = msellerInfo.keys();
        while(sellerInfoKeys.hasNext()) {
            String key = sellerInfoKeys.next();
            if(!key.equals("shippingServiceCost")) {
                try {
                    String value = msellerInfo.getString(key);
                    if (value.equals("false")) {
                        value = "No";
                    }
                    if (value.equals("true")) {
                        value = "Yes";
                    }
                    String[] key_l = key.split("(?=\\p{Upper})");
                    String key_adjust = key_l[0].substring(0,1).toUpperCase()+key_l[0].substring(1);
                    Log.d("key initial", key_adjust);
                    for (int i = 1; i<key_l.length;i++) {
                        key_adjust+= " " + key_l[i];
                        Log.d("key", key_l[i]);
                    }
                    htmlResult += "<li><b>"+key_adjust+"</b>: "+value + "</li>";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                continue;
            }


        }
        htmlResult += "</ul>";
        sellerInfoArea.setText(HtmlCompat.fromHtml(htmlResult,0));

        returnInfoArea = (TextView) view.findViewById(R.id.returnList);
        String htmlResult1 = " <ul>";
        Iterator<String> sellerInfoKeys1 = mreturnInfo.keys();
        while(sellerInfoKeys1.hasNext()) {
            String key = sellerInfoKeys1.next();
            if(!key.equals("shippingServiceCost")) {
                try {
                    String value = mreturnInfo.getString(key);
                    if (value.equals("false")) {
                        value = "No";
                    }
                    if (value.equals("true")) {
                        value = "Yes";
                    }
                    String[] key_l = key.split("(?=\\p{Upper})");
                    String key_adjust = key_l[0].substring(0,1).toUpperCase()+key_l[0].substring(1);
                    Log.d("key initial", key_adjust);
                    for (int i = 1; i<key_l.length;i++) {
                        key_adjust+= " " + key_l[i];
                        Log.d("key", key_l[i]);
                    }
                    htmlResult1 += "<li><b>"+key_adjust+"</b>: "+value + "</li>";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                continue;
            }


        }
        htmlResult1 += "</ul>";
        returnInfoArea.setText(HtmlCompat.fromHtml(htmlResult1,0));


    }
}
