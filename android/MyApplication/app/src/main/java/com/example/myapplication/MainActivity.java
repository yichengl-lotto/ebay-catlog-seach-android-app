package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    EditText keyword;
    EditText minPrice;
    EditText maxPrice;
    CheckBox conditionNew;
    CheckBox conditionUsed;
    CheckBox conditionUnspecified;
    Spinner spinner;
    Button search;
    Button clear;
    TextView warn1;
    TextView warn2;

    public static final String EXTRA_KEYWORD="p_keyword";
    public static final String EXTRA_MINPRICE="p_minprice";
    public static final String EXTRA_MAXPRICE="p_maxprice";
    public static final String EXTRA_CONDITION_NEW="p_condition_new";
    public static final String EXTRA_CONDITION_USED="p_condition_used";
    public static final String EXTRA_CONDITION_UNSPECIFIED="p_condition_unspecified";
    public static final String EXTRA_SORTBY = "p_sortby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyword = findViewById(R.id.keyword);
        minPrice = findViewById(R.id.minPrice);
        maxPrice=findViewById(R.id.maxPrice);
        conditionNew = findViewById(R.id.conditionNew);
        conditionUsed = findViewById(R.id.conditionUsed);
        conditionUnspecified = findViewById(R.id.conditionUnspecified);
        spinner= findViewById(R.id.sorBy);
        search = findViewById(R.id.searchButton);
        clear =findViewById(R.id.clearButton);
        warn1 = findViewById(R.id.keywordWarning);
        warn2 = findViewById(R.id.priceWarning);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this ,R.array.sorbySpinner,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkDataWrong();
                if(check) {
                    String p_condition_new_string;
                    String p_condition_used_string;
                    String p_condition_unspecified_string;
                    String p_sortby_string;
                    String p_keyword=keyword.getText().toString();
                    String p_minPrice=minPrice.getText().toString();
                    Log.d("minPrice", p_minPrice);
                    String p_maxPrice=maxPrice.getText().toString();
                    boolean p_conditionNew=conditionNew.isChecked();
                    boolean p_conditionUsed=conditionUsed.isChecked();
                    boolean p_conditionUnspecified=conditionUnspecified.isChecked();
                    int p_spinner = spinner.getSelectedItemPosition();
                    if(p_conditionNew) {
                        p_condition_new_string = "New";
                    }
                    else {
                        p_condition_new_string="No";
                    }
                    if(p_conditionUsed) {
                        p_condition_used_string = "Used";
                    }
                    else {
                        p_condition_used_string ="No";
                    }
                    if(p_conditionUnspecified) {
                        p_condition_unspecified_string = "Unspecified";
                    }
                    else {
                        p_condition_unspecified_string = "No";
                    }

                    if(p_spinner==0) {
                        p_sortby_string = "BestMatch";
                    }
                    else if(p_spinner == 1) {
                        p_sortby_string = "CurrentPriceHighest";
                    }
                    else if(p_spinner == 2) {
                        p_sortby_string = "PricePlusShippingHighest";
                    }
                    else  {
                        p_sortby_string = "PricePlusShippingLowest";
                    }

                    Intent i = new Intent(MainActivity.this, ItemCatlog.class);
                    i.putExtra(EXTRA_KEYWORD,p_keyword);
                    i.putExtra(EXTRA_MINPRICE,p_minPrice);
                    i.putExtra(EXTRA_MAXPRICE,p_maxPrice);
                    i.putExtra(EXTRA_CONDITION_NEW,p_condition_new_string);
                    i.putExtra(EXTRA_CONDITION_USED,p_condition_used_string);
                    i.putExtra(EXTRA_CONDITION_UNSPECIFIED,p_condition_unspecified_string);
                    i.putExtra(EXTRA_SORTBY,p_sortby_string);

                    startActivity(i);

                }
                else {
                    return;
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword.setText("");
                minPrice.setText("");
                maxPrice.setText("");
                if(conditionNew.isChecked()) {
                    conditionNew.setChecked(false);
                }
                if(conditionUsed.isChecked()) {
                    conditionUsed.setChecked(false);
                }
                if(conditionUnspecified.isChecked()){
                    conditionUnspecified.setChecked(false);
                }
                spinner.setSelection(0);
                warn1.setVisibility(View.GONE);
                warn2.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
        String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    boolean isEmpty (EditText text) {
        CharSequence keywords =text.getText().toString();
        return TextUtils.isEmpty((keywords));
    }
    boolean priceWrong(EditText minprice, EditText maxprice) {
        //String min = .getText()
        if(isEmpty(minprice) || isEmpty(maxprice)) {
            return false;
        }
        else {
            int min = Integer.parseInt(minprice.getText().toString());
            int max = Integer.parseInt(maxprice.getText().toString());

            return (max < min);
        }

    }
    boolean checkDataWrong() {
        Log.d("price " , String.valueOf(priceWrong(minPrice,maxPrice)));
        if(isEmpty(keyword) ||priceWrong(minPrice,maxPrice) ) {
            Toast.makeText(this, "please fix all fields with errors",Toast.LENGTH_SHORT).show();
            if(isEmpty(keyword)) {
                warn1.setVisibility(View.VISIBLE);
            }
            else {
                warn1.setVisibility(View.GONE);
            }
            if(priceWrong(minPrice,maxPrice)) {
                warn2.setVisibility(View.VISIBLE);
            }
            else {
                warn2.setVisibility(View.GONE);
            }
            return false;
        }
        else {
            warn1.setVisibility(View.GONE);
            warn2.setVisibility(View.GONE);
            return true;

        }


    }
}