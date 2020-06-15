package com.mlzs.mlzsoles.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.ExamSeriesAdapter;
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

public class ExamsSeriesActivity extends BaseActivity {

    RecyclerView rvExamSeries;
    TextView tvTitle,noSeries;

    ImageView imgBack;
    Toolbar toolbar;

    ExamSeriesAdapter adapter;
    List<ExamsSeries> examsSeriesList;

    Button updateSettings ;
    SearchView searchView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_series);

        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        rvExamSeries = (RecyclerView)findViewById(R.id.rv_exams_series);
        noSeries = (TextView)findViewById(R.id.tv_no_exams_series);

        updateSettings = findViewById(R.id.btn_exam_series_update_settings);

        tvTitle.setText(getString(R.string.exam_series));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvExamSeries.setLayoutManager(layoutManager);


        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExamsSeriesActivity.this,SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(appState.getNetworkCheck()){
            getExamSeriesData();
        }else {
            Toast.makeText(this, getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void getExamSeriesData(){

        examsSeriesList = new ArrayList<>();

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.EXAMS_SERIES+getUserID(), null,
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
                                jsonArray    = response.getJSONArray("records");
                                updateSettings.setVisibility(View.GONE);
                                noSeries.setVisibility(View.GONE);
                                for (int i=0;i<jsonArray.length();i++){
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<ExamsSeries>() {}.getType();
                                    ExamsSeries myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    examsSeriesList.add(myQuestions);
                                }

                                if(examsSeriesList.size()!=0){
                                    adapter = new ExamSeriesAdapter(ExamsSeriesActivity.this,examsSeriesList);
                                    rvExamSeries.setAdapter(adapter);
                                }else {
                                    noSeries.setVisibility(View.VISIBLE);
                                }

                            }
                            else {
                                mssg = response.getString("message");
                                noSeries.setVisibility(View.VISIBLE);
                                noSeries.setText(mssg);
                                updateSettings.setVisibility(View.VISIBLE);
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
                if(examsSeriesList.size()!=0){
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if(examsSeriesList.size()!=0){
                    adapter.getFilter().filter(query);
                }

                return false;
            }
        });
        return true;
    }
}
