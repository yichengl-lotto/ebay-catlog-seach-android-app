package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.text.HtmlCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.example.myapplication.MainActivity.EXTRA_KEYWORD;
import static com.example.myapplication.MainActivity.EXTRA_MINPRICE;
import static com.example.myapplication.MainActivity.EXTRA_MAXPRICE;
import static com.example.myapplication.MainActivity.EXTRA_CONDITION_NEW;
import static com.example.myapplication.MainActivity.EXTRA_CONDITION_USED;
import static com.example.myapplication.MainActivity.EXTRA_CONDITION_UNSPECIFIED;
import static com.example.myapplication.MainActivity.EXTRA_SORTBY;

public class ItemCatlog extends AppCompatActivity implements  cardAdaptor.onCardClickL{
    private RecyclerView mRecyclerView;
    private cardAdaptor mcardAdapter;
    private ArrayList<cardInformation> mcardList;
    private RequestQueue queue ;
    private TextView test0;
    private ProgressBar loadProgress;
    private TextView noResult;
    private TextView first_line;
    String keyword;
    String keyword1;
    String minPrice;
    String maxPrice;
    String conditionNew;
    String conditionUsed;
    String conditionUnspecified;
    String sortby;
    ScrollView scrollView;
    //JSONObject shippingInfo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_PRICE = "currentPrice";
    public static final String EXTRA_SHIPPING_JSON = "shippingInfoJson";
    public static final String EXTRA_ITEM_ID= "itemID";
    public static final String EXTRA_SHIP_PRICE="shipPrice";
    public static final String EXTRA_VIEW_ITEM_URL = "viewItemURL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //Toolbar toolbar ;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_catlog);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });*/

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);

        Intent intent = getIntent();
         keyword1 = intent.getStringExtra(EXTRA_KEYWORD);
         String[] splited_keyword = keyword1.split("\\s+");
         keyword = splited_keyword[0];
         if(splited_keyword.length>1) {
             for (int i=1; i < splited_keyword.length; i++) {
                 keyword += "%20" + splited_keyword[i];
             }
         }
         Log.d("keyword_organized", keyword);
        loadProgress = findViewById(R.id.progressBar);
        loadProgress.setVisibility(View.VISIBLE);
         minPrice = intent.getStringExtra(EXTRA_MINPRICE);
         Log.d("minPrice in ItemCatlog: ",minPrice);
         maxPrice = intent.getStringExtra(EXTRA_MAXPRICE);
         conditionNew = intent.getStringExtra(EXTRA_CONDITION_NEW);
         conditionUnspecified = intent.getStringExtra(EXTRA_CONDITION_UNSPECIFIED);
         conditionUsed = intent.getStringExtra(EXTRA_CONDITION_USED);
         sortby = intent.getStringExtra(EXTRA_SORTBY);
        //TextView test0 = findViewById(R.id.testTitle);
        //test0.setText(title);

        mRecyclerView = findViewById(R.id.recycleCard);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mcardList = new ArrayList<>();
        first_line=findViewById(R.id.resultNumber);
        queue = Volley.newRequestQueue(this);
        test0 = findViewById(R.id.resultNumber);

        jsonParse();

        /*
        scrollView = (ConstraintLayout) findViewById(R.id.frame1);
        scrollView.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listView.getChildAt(0) != null) {
                    swipeRefreshLayout.setEnabled(listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0);
                }
            }
        });*/
        /*
        mSwipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getScrollY() == 0) {
                            mSwipeRefreshLayout.setEnabled(true);
                            mSwipeRefreshLayout.setRefreshing(true);
                            jsonParse();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        else {
                            mSwipeRefreshLayout.setEnabled(false);
                        }

                    }
                });*/
        //scrollView = (ScrollView) findViewById(R.id.scrollView2);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();


                    mSwipeRefreshLayout.setRefreshing(true);
                    jsonParse();
                mSwipeRefreshLayout.setRefreshing(false);


            }
        });







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
    private String urlParse() {
        String url1= "https://csci571hw9kkl.wm.r.appspot.com/?urltype=findItemsAdvanced";
        url1 += "&keyword="+keyword+"&sortby="+sortby+"&condition_new="+conditionNew+"&condition_used="+conditionUsed+"&condition_unspecified="+conditionUnspecified;
        if(!TextUtils.isEmpty(minPrice)) {
            url1+="&minprice="+minPrice;
        }
        if(!TextUtils.isEmpty(maxPrice)) {
            url1 += "&maxprice=" +maxPrice;
        }
        Log.d("url_new: ",url1);

        return url1;
    }

    private void jsonParse() {
        String url = urlParse();
        //Log.d("url_new: ",url);
        //String url = "https://csci571-hello.wl.r.appspot.com/?keyword=mask&sortby=PricePlusShippingHighest&minprice=30&maxprice=50&condition_new=1&condition_used=0&condition_verygood=0&condition_good=0&condition_acceptable=0&expship=true&returnOnly=false&freeshipOnly=false";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("abc","inside onResponse");
                            JSONArray jsonArray = response.getJSONArray("findItemsAdvancedResponse");

                            String resultNum = response.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getString("@count");
                            Log.d("resultNum", resultNum);
                            if (resultNum.equals("0")) {
                                Log.d("check if no result", resultNum);
                                noResult = findViewById(R.id.noResult);
                                noResult.setVisibility(View.VISIBLE);
                                if(loadProgress.getVisibility()==View.VISIBLE) {
                                    loadProgress.setVisibility(View.GONE);
                                }
                                Toast.makeText(ItemCatlog.this, "No Records",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                noResult = findViewById(R.id.noResult);
                                noResult.setVisibility(View.INVISIBLE);
                                String html_resultNum = "<b>Showing <span style='color:blue'>"+resultNum+"</span> results for <span style='color:blue'>" +keyword1+"</span></b>";
                                first_line.setText(HtmlCompat.fromHtml(html_resultNum,0));

                                JSONArray item = response.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
                                for (int i=0; i<item.length();i++) {
                                    String title = "";
                                    String imageURL = "";
                                    String shipPrice= "";
                                    String topRated= "";
                                    String condition="";
                                    String currentPrice="";
                                    String itemID="";
                                    String viewItemUrl = "";
                                    JSONObject shippingInfo;
                                    if(item.getJSONObject(i).has("title")) {
                                        Log.d("title checked","true");
                                         title = item.getJSONObject(i).getJSONArray("title").getString(0);
                                    }
                                    else {
                                        continue;
                                    }
                                    if(item.getJSONObject(i).has("viewItemURL")) {
                                        viewItemUrl = item.getJSONObject(i).getJSONArray("viewItemURL").getString(0);
                                    }
                                    else {
                                        continue;
                                    }
                                    if(item.getJSONObject(i).has("galleryURL")) {

                                            imageURL = item.getJSONObject(i).getJSONArray("galleryURL").getString(0);

                                    }


                                    if(item.getJSONObject(i).has("shippingInfo")) {
                                        Log.d("shipping info", "true");
                                        shippingInfo = item.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0);
                                        if(item.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")){
                                            Log.d("shipping Service Cost", "true");
                                            if(item.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).has("__value__")) {
                                                Log.d("__value__", "true");
                                                shipPrice = item.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                                            }
                                            else {
                                                continue;
                                            }
                                        }
                                        else {
                                            continue;
                                        }
                                    }
                                    else {
                                        continue;
                                    }
                                    if (item.getJSONObject(i).has("topRatedListing")) {
                                        Log.d("top rated listing", "true");
                                        topRated = item.getJSONObject(i).getJSONArray("topRatedListing").getString(0);
                                    }
                                    if(item.getJSONObject(i).has("condition")) {

                                        if(item.getJSONObject(i).getJSONArray("condition").getJSONObject(0).has("conditionDisplayName")) {
                                            condition = item.getJSONObject(i).getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
                                        }
                                    }
                                    if(item.getJSONObject(i).has("sellingStatus")) {

                                        if(item.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice")) {
                                            if(item.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).has("__value__")) {
                                                currentPrice = item.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__");
                                            }
                                            else {
                                                Log.d("selling _value_", "false");
                                                continue;
                                            }
                                        }
                                        else {
                                            Log.d("current price","false");
                                            continue;
                                        }
                                    }
                                    else {
                                        Log.d("selling status", "false");
                                        continue;
                                    }
                                    if(item.getJSONObject(i).has("itemId")) {

                                        itemID = item.getJSONObject(i).getJSONArray("itemId").getString(0);
                                    }
                                    else {
                                        Log.d("itemId","false");
                                        continue;
                                    }


                                    //Log.d("title: ",title);
                                    //Log.d("imageURL: ",imageURL);
                                    // Log.d("shipPrice: ",shipPrice);
                                    // Log.d("topRated: ",topRated);
                                    // Log.d("condition: ", condition);
                                    // Log.d("currentPrice: ", currentPrice);

                                    mcardList.add(new cardInformation(imageURL, title,shipPrice,topRated,condition,currentPrice,itemID,shippingInfo,viewItemUrl));
                                }
                                mcardAdapter = new cardAdaptor(ItemCatlog.this, mcardList);
                                mRecyclerView.setAdapter(mcardAdapter);
                                mcardAdapter.setCardListener(ItemCatlog.this);

                                //String title0 = item.getJSONObject(0).getJSONArray("title").getString(0);
                                //Log.d("title0: ",title0);
                                //test0.setText(title0);
                                if(loadProgress.getVisibility()==View.VISIBLE) {
                                    loadProgress.setVisibility(View.GONE);
                                }
                            }

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
        queue.add(request);


    }


    @Override
    public void onCardClick(int position) {
        Intent details = new Intent(this,itemDetailTab.class);
        cardInformation clickedCard = mcardList.get(position);
        details.putExtra(EXTRA_TITLE,clickedCard.getitemTitle());
        details.putExtra(EXTRA_SHIPPING_JSON,clickedCard.getshippingInfo().toString());
        details.putExtra(EXTRA_ITEM_ID,clickedCard.getitemID());
        details.putExtra(EXTRA_SHIP_PRICE,clickedCard.getshiPrice());
        details.putExtra(EXTRA_PRICE,clickedCard.getcurrentPrice());
        details.putExtra(EXTRA_VIEW_ITEM_URL, clickedCard.getviewItemUrl());

        startActivity(details);
    }


}