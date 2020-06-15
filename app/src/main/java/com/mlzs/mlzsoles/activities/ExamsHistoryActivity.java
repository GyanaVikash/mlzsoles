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
import com.mlzs.mlzsoles.adapters.ExamsHistoryAdapter;
import com.mlzs.mlzsoles.model.ExamHistory;
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

public class ExamsHistoryActivity extends BaseActivity {

    TextView tvTitle,noExams;
    ImageView imgBack;
    Toolbar toolbar ;

    RecyclerView recyclerView ;

    List<ExamHistory> examsHistoryList;
    ExamsHistoryAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_history);

        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.rv_exams_history);
        noExams = (TextView)findViewById(R.id.tv_no_exam_history);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvTitle.setText(getString(R.string.exams_history));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getExamsList();
    }

    public void getExamsList(){

        examsHistoryList = new ArrayList<>();
        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_EXAMS_HISTORY+getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        final JSONArray jsonArrayExams;

                        try {
                            jsonArrayExams    = response.getJSONArray("records");

                            for (int i=0;i<jsonArrayExams.length();i++){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ExamHistory>() {}.getType();
                                ExamHistory myQuestions = gson.fromJson(jsonArrayExams.get(i).toString(), type);
                                examsHistoryList.add(myQuestions);
                            }

                            if(examsHistoryList.size()>0){
                                noExams.setVisibility(View.GONE);
                                adapter = new ExamsHistoryAdapter(ExamsHistoryActivity.this,examsHistoryList);
                                recyclerView.setAdapter(adapter);

                            }else {
                                noExams.setVisibility(View.VISIBLE);
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
