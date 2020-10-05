package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

//import com.example.myapplication.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.myapplication.ItemCatlog.EXTRA_TITLE;
import static com.example.myapplication.ItemCatlog.EXTRA_SHIPPING_JSON;
import static com.example.myapplication.ItemCatlog.EXTRA_ITEM_ID;
import static com.example.myapplication.ItemCatlog.EXTRA_PRICE;
import static com.example.myapplication.ItemCatlog.EXTRA_SHIP_PRICE;
import static com.example.myapplication.ItemCatlog.EXTRA_VIEW_ITEM_URL;


public class itemDetailTab extends AppCompatActivity {
    //private static final String TAG = "MainActivity";

    private ViewPageAdapter mSectionsPageAdapter;
    public Toolbar toolbar;
    public JSONObject shippingInfoJSON;
    public JSONObject sellerInfoJSON;
    public JSONObject returnInfoJSON;
    public JSONArray imageUrlJSON;
    public JSONObject itemJSON;
    public String title;
    public String shipPrice;
    public String currentPrice;
    public String viewItemUrl;
    private RequestQueue mqueue;
    //public boolean readyState;
    //private ProgressBar Progressbar3;

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.information_variant_unselected,
            R.drawable.seller_tab_icon,
            R.drawable.truck_delivery_unselected
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_detail_tab);
        //Progressbar3 = (ProgressBar) findViewById(R.id.progressBar4);
        //Progressbar3.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        title = intent.getStringExtra(EXTRA_TITLE);
        currentPrice = intent.getStringExtra(EXTRA_PRICE);
        shipPrice = intent.getStringExtra(EXTRA_SHIP_PRICE);
        viewItemUrl = intent.getStringExtra(EXTRA_VIEW_ITEM_URL);
        String getSingleItemId=intent.getStringExtra(EXTRA_ITEM_ID);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        textView.setText(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mqueue = Volley.newRequestQueue(this);
        //Progressbar3.setVisibility(View.VISIBLE);
        jsonParse(getSingleItemId);
        try {
            shippingInfoJSON = new JSONObject(intent.getStringExtra(EXTRA_SHIPPING_JSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mSectionsPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.view_pager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void browserInvoke(View view) {
        Log.d("redirect", viewItemUrl);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(viewItemUrl));
        startActivity(browserIntent);
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#2C46AA"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    private void setupViewPager(ViewPager viewPager) {
        //Progressbar3.setVisibility(View.VISIBLE);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new productTab(imageUrlJSON,title,shipPrice,currentPrice,itemJSON), "PRODUCT");
        adapter.addFragment(new sellerTab(sellerInfoJSON,returnInfoJSON), "SELLER INFO");
        adapter.addFragment(new shippingTab(shippingInfoJSON), "SHIPPING");
        viewPager.setAdapter(adapter);
        //Progressbar3.setVisibility(View.VISIBLE);
    }
    private void jsonParse(String itemID) {
        String mitemID = itemID;
        String getSingleUrl = "https://csci571hw9kkl.wm.r.appspot.com/?urltype=GetSingleItem&itemID="+mitemID;
        Log.d("singleurl",getSingleUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getSingleUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("abcd", "inside request");
                            JSONObject jsonArray = response.getJSONObject("Item");
                            //Log.d("itemJson" ,jsonArray.toString());
                            sellerInfoJSON = jsonArray.getJSONObject("Seller");
                            returnInfoJSON = jsonArray.getJSONObject("ReturnPolicy");
                            imageUrlJSON = jsonArray.getJSONArray("PictureURL");
                            itemJSON = jsonArray;
                            setupViewPager(mViewPager);
                            tabLayout.setupWithViewPager(mViewPager);
                            setupTabIcons();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mqueue.add(request);
        //Progressbar3.setVisibility(View.GONE);

    }

}