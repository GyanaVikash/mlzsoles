package com.mlzs.mlzsoles.activities;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.SubscriptionsAdapter;
import com.mlzs.mlzsoles.model.Subscriptions;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionActivity extends BaseActivity {

    TextView tvTitle, noSubscriptions;
    ImageView imgBack;
    Toolbar toolbar;
    RecyclerView recyclerView;

    SubscriptionsAdapter adapter;
    List<Subscriptions> subscriptionsList;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        imgBack = (ImageView) findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tvTitle.setText(getString(R.string.subscription_list));

        noSubscriptions = (TextView) findViewById(R.id.tv_no_subscriptions);
        recyclerView = (RecyclerView) findViewById(R.id.rv_subscriptions);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        userID = getUserID();

        if(appState.getNetworkCheck()){
            getMySubscriptions();
        }else {

            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }


    }

    public void getMySubscriptions() {


        subscriptionsList = new ArrayList<>();

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.SUBSCRIPTIONS + userID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg;
                        int status;
                        JSONArray jsonArray;
                        try {
                            status = response.getInt("status");
                            if (status == 1) {

                                jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<Subscriptions>() {
                                    }.getType();
                                    Subscriptions myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    subscriptionsList.add(myQuestions);
                                }

                                if (subscriptionsList.size() > 0) {
                                    noSubscriptions.setVisibility(View.GONE);
                                    adapter = new SubscriptionsAdapter(SubscriptionActivity.this, subscriptionsList);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    noSubscriptions.setVisibility(View.VISIBLE);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);

    }


}
