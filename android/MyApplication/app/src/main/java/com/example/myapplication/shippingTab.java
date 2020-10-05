package com.example.myapplication;

//import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.text.HtmlCompat;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;

public class shippingTab extends Fragment {
    JSONObject mshippingInfo;
    TextView shipInfoArea;

    public shippingTab(JSONObject shippingInfo) {
        mshippingInfo = shippingInfo;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shipping_layout,container,false);




        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shipInfoArea = (TextView) view.findViewById(R.id.shippingList);
        String htmlResult = "<ul>";
        Iterator<String> shippingInfoKeys = mshippingInfo.keys();
        while(shippingInfoKeys.hasNext()) {
            String key = shippingInfoKeys.next();
            if(!key.equals("shippingServiceCost")) {
                try {
                    String value = mshippingInfo.getJSONArray(key).getString(0);
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
        shipInfoArea.setText(HtmlCompat.fromHtml(htmlResult,0));
    }
}
