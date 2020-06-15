package com.mlzs.mlzsoles.activities;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.mlzs.mlzsoles.adapters.SettingsExamSeriesAdapter;
import com.mlzs.mlzsoles.adapters.SettingsLmsSeriesAdapter;
import com.mlzs.mlzsoles.model.Settings;
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

public class SettingsActivity extends BaseActivity {

    TextView tvTitle;

    ImageView imgBack;

    Toolbar toolbar ;

    List<Settings> listExams;
    List<Settings>listLMS;

    List<String> selectedExamsList;
    List<String>selectedLMSList;

    RecyclerView rvExamSeries,rvLmsSeries;

    SettingsExamSeriesAdapter examSeriesAdapter;
    SettingsLmsSeriesAdapter lmsSeriesAdapter;



    Button btnUpdateSettings;
    ArrayList<String> updateLmsList = new ArrayList<String>();
    ArrayList<String> updateExamsList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        rvExamSeries = (RecyclerView)findViewById(R.id.rv_settings_exam_series);
        rvLmsSeries = (RecyclerView)findViewById(R.id.rv_settings_lms_series);
        btnUpdateSettings = (Button)findViewById(R.id.btn_settings_update);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvTitle.setText(getString(R.string.str_nav_settings));


        if(appState.getNetworkCheck()){
            getSettingsData();
        }else {
            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvExamSeries.setLayoutManager(layoutManager);
        rvExamSeries.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        rvLmsSeries.setLayoutManager(layoutManager1);
        rvLmsSeries.setNestedScrollingEnabled(false);

        btnUpdateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("quiz_categories",updateExamsList);
                    jsonObject.put("lms_categories",updateLmsList);

                    updateSettings(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void updsteLMSSettings(String id,String type){

        if(type.equals("add")){
            updateLmsList.add(id);
        }else  {
            updateLmsList.remove(id);
        }

    }

    public void updateExamSettings(String id,String type){

        if(type.equals("add")){

            updateExamsList.add(id);
        }
        else {
            updateExamsList.remove(id);
        }
    }

    public void getSettingsData(){

        listExams = new ArrayList<>();
        listLMS = new ArrayList<>();

        selectedExamsList = new ArrayList<>();
        selectedLMSList = new ArrayList<>();

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_SETTINGS+getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        final JSONArray jsonArrayExams,jsonArrayLMS,selectedExamsArray,selectedLMSArray;
                        JSONObject selecedObject ;
                        try {
                            jsonArrayExams    = response.getJSONArray("quiz_categories");
                            jsonArrayLMS = response.getJSONArray("lms_category");

                            selecedObject = response.getJSONObject("selected_options");

                            selectedExamsArray = selecedObject.getJSONArray("quiz_categories");
                            selectedLMSArray = selecedObject.getJSONArray("lms_categories");

                            btnUpdateSettings.setVisibility(View.VISIBLE);

                            for (int i=0;i<jsonArrayExams.length();i++){
                                Gson gson = new Gson();
                                Type type = new TypeToken<Settings>() {}.getType();
                                Settings myQuestions = gson.fromJson(jsonArrayExams.get(i).toString(), type);
                                listExams.add(myQuestions);

                            }

                            for (int i=0;i<jsonArrayLMS.length();i++){
                                Gson gson = new Gson();
                                Type type = new TypeToken<Settings>() {}.getType();
                                Settings myQuestions = gson.fromJson(jsonArrayLMS.get(i).toString(), type);
                                listLMS.add(myQuestions);

                            }

                            for(int i=0;i<selectedExamsArray.length();i++){

                                selectedExamsList.add(selectedExamsArray.get(i).toString());
                            }

                            for(int i=0;i<selectedLMSArray.length();i++){

                                selectedLMSList.add(selectedLMSArray.get(i).toString());
                            }

                            if(listExams.size()!=0){
                                examSeriesAdapter = new SettingsExamSeriesAdapter(SettingsActivity.this,listExams,selectedExamsList);
                                rvExamSeries.setAdapter(examSeriesAdapter);
                            }

                            if(listLMS.size()!=0){

                                lmsSeriesAdapter = new SettingsLmsSeriesAdapter(SettingsActivity.this,listLMS,selectedLMSList);
                                rvLmsSeries.setAdapter(lmsSeriesAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);

    }


    public void updateSettings(JSONObject jsonObject){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.UPDATE_SETTINGS+getUserID(), jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.dissmisProgress();
                        String mssg;
                        try {
                            mssg = response.getString("message");
                            Toast.makeText(SettingsActivity.this, mssg, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {

        };

        queue.add(jsonObjReq);

    }

}
