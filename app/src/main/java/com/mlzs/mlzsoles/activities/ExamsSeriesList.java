package com.mlzs.mlzsoles.activities;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.mlzs.mlzsoles.adapters.ExamSeriesListAdapter;
import com.mlzs.mlzsoles.model.ExamSeriesList;
import com.mlzs.mlzsoles.model.ExamsSeries;
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

public class ExamsSeriesList extends BaseActivity {

    RecyclerView recyclerView;
    TextView tvTitle,noSeries;
    ImageView imgBack;
    Toolbar toolbar;
    ExamsSeries examsSeries ;

    List<ExamSeriesList> examsSeriesLists;
    ExamSeriesListAdapter adapter;

    SearchView searchView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_series_list);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);

        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.rv_exams_series_list);
        noSeries = (TextView)findViewById(R.id.tv_no_exams_series_list);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle.setText(getString(R.string.exam_series));

        if(getIntent().getSerializableExtra("exam_series_list")!=null){
            examsSeries = (ExamsSeries)getIntent().getSerializableExtra("exam_series_list");
        }

        tvTitle.setText(examsSeries.getTitle());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(appState.getNetworkCheck()){
            getExamSeriesListData();
        }else {
            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }



    }

    public void getExamSeriesListData(){

        examsSeriesLists = new ArrayList<>();
        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_EXAM_SERIES_LIST+examsSeries.getSlug()+"?user_id="+getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg;
                        int status;
                        final JSONArray jsonArray ;
                        try {

                            status = response.getInt("status");
                            if(status==1){
                                jsonArray    = response.getJSONArray("items_list");

                                noSeries.setVisibility(View.GONE);
                                for (int i=0;i<jsonArray.length();i++){
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<ExamSeriesList>() {}.getType();
                                    ExamSeriesList myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    examsSeriesLists.add(myQuestions);
                                }

                                if(examsSeriesLists.size()!=0){
                                    adapter = new ExamSeriesListAdapter(ExamsSeriesList.this,examsSeriesLists);
                                    recyclerView.setAdapter(adapter);
                                }else {
                                    noSeries.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }

                            }
                            else {
                                mssg = response.getString("message");
                                noSeries.setVisibility(View.VISIBLE);
                                noSeries.setText(mssg);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

}
