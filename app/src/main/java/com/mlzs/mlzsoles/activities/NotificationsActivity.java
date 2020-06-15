package com.mlzs.mlzsoles.activities;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.NotificationsAdapter;
import com.mlzs.mlzsoles.model.Notifications;
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

public class NotificationsActivity extends BaseActivity {


    RecyclerView recyclerView;
    TextView noNotifications,tvTitle;
    ImageView imgBack;
    Toolbar toolbar;

    List<Notifications> notificationsList;
    NotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = (RecyclerView)findViewById(R.id.rv_notifications);
        noNotifications = (TextView)findViewById(R.id.tv_no_notifications);
        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle.setText(getString(R.string.notifications));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        getNotifications();

    }

    public void getNotifications(){

        notificationsList = new ArrayList<>();
        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.NOTIFICATIONS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        int status;
                        JSONArray jsonArray;
                        try {
                            status = response.getInt("status");
                            if(status==1){

                                jsonArray = response.getJSONArray("records");

                                for (int i=0;i<jsonArray.length();i++){
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<Notifications>() {}.getType();
                                    Notifications myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    notificationsList.add(myQuestions);
                                }

                                if(notificationsList.size()>0){
                                    noNotifications.setVisibility(View.GONE);
                                    adapter = new NotificationsAdapter(NotificationsActivity.this,notificationsList);
                                    recyclerView.setAdapter(adapter);
                                }else {
                                    noNotifications.setVisibility(View.VISIBLE);
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
