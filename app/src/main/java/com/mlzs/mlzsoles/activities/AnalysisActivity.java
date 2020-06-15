package com.mlzs.mlzsoles.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.AnalysisByExamAdapter;
import com.mlzs.mlzsoles.adapters.SpinnerAnalysisSubjectsAdapter;
import com.mlzs.mlzsoles.model.AnalysisByExam;
import com.mlzs.mlzsoles.model.AnalysisBySubject;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnalysisActivity extends BaseActivity {


    PieChart pieChart;
    Toolbar toolbar;
    ImageView imgBack;
    TextView tvTitle,tvViewChart,tvNoAnalysis;
    Spinner sprSubjects;
    LinearLayout llSubjects;


    List<AnalysisByExam> analysisByExamList;

    List<AnalysisBySubject> analysisBySubjectList;

    ArrayList<PieEntry> yValues;

    RecyclerView recyclerView;
    AnalysisByExamAdapter adapter;

    LinearLayout llTitles;

    int count = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        pieChart = (PieChart) findViewById(R.id.piechart_1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgBack = (ImageView) findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        sprSubjects = (Spinner) findViewById(R.id.spr_analysis_subjects);
        llSubjects = (LinearLayout) findViewById(R.id.ll_subjects);
        recyclerView = (RecyclerView)findViewById(R.id.rv_analysis_by_exam);
        llTitles = (LinearLayout)findViewById(R.id.ll_analysis_titles);

        tvViewChart = findViewById(R.id.tv_analysis_view_chart);
        tvNoAnalysis = findViewById(R.id.tv_no_analysis);
        setSupportActionBar(toolbar);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle.setText(getString(R.string.analysis_by_exam));
        getAnalysisByExam();

        sprSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setSubjectPieChart(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        tvViewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;

                if(count%2==0){
                    tvViewChart.setText(getResources().getString(R.string.hide_chart));
                    pieChart.setVisibility(View.VISIBLE);
                }else {
                    tvViewChart.setText(getResources().getString(R.string.view_chart));
                    pieChart.setVisibility(View.GONE);
                }
            }
        });

    }

    public void getAnalysisBySubject() {

        analysisBySubjectList = new ArrayList<>();

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.ANALYSIS_BY_SUBJECT + getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        JSONArray jsonArray;
                        int status;
                        String mssg;

                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");
                            jsonArray = response.getJSONArray("subjects");

                            if (status == 1) {
                                pieChart.setVisibility(View.VISIBLE);
                                tvNoAnalysis.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<AnalysisBySubject>() {}.getType();
                                    AnalysisBySubject myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    analysisBySubjectList.add(myQuestions);
                                }

                                if (analysisBySubjectList.size() != 0) {
                                    SpinnerAnalysisSubjectsAdapter countryAdapter = new SpinnerAnalysisSubjectsAdapter(AnalysisActivity.this, android.R.layout.simple_spinner_dropdown_item, analysisBySubjectList);
                                    sprSubjects.setAdapter(countryAdapter);
                                }


                            } else {
                                Toast.makeText(AnalysisActivity.this, mssg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);

    }

    public void getAnalysisByExam() {

        analysisByExamList = new ArrayList<>();

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.ANALYSIS_BY_EXAM + getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        JSONArray jsonArray;
                        int status;
                        String mssg;

                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");
                            jsonArray = response.getJSONArray("records");

                            if (status == 1) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<AnalysisByExam>() {}.getType();
                                    AnalysisByExam myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    analysisByExamList.add(myQuestions);


                                }

                                if (analysisByExamList.size() != 0) {
                                    setExamPieChart();
                                    tvNoAnalysis.setVisibility(View.GONE);
                                    llTitles.setVisibility(View.VISIBLE);
                                    adapter = new AnalysisByExamAdapter(AnalysisActivity.this,analysisByExamList);
                                    recyclerView.setAdapter(adapter);
                                }else {
                                    llTitles.setVisibility(View.GONE);
                                    tvNoAnalysis.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Toast.makeText(AnalysisActivity.this, mssg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);

    }


    public void setExamPieChart() {

        AnalysisByExam analysisByExam = null;
        PieDataSet dataSet = null;
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setDrawHoleEnabled(true);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        yValues = new ArrayList<>();

        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < analysisByExamList.size(); i++) {

            analysisByExam = analysisByExamList.get(i);
            yValues.add(new PieEntry(Float.parseFloat(analysisByExam.getAttempts()), analysisByExam.getTitle()));
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colors.add(color);
        }
        dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);

        pieData.setValueFormatter(new MyValueFormatter());
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setData(pieData);

    }


    public void setSubjectPieChart(int position) {

        PieDataSet dataSet = null;

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);


        yValues = new ArrayList<>();

        if(!analysisBySubjectList.get(position).getCorrect_answers().equals("0")){
            yValues.add(new PieEntry(Float.parseFloat(analysisBySubjectList.get(position).getCorrect_answers()), getString(R.string.correct)));
        }
        if(!analysisBySubjectList.get(position).getWrong_answers().equals("0")){
            yValues.add(new PieEntry(Float.parseFloat(analysisBySubjectList.get(position).getWrong_answers()), getString(R.string.wrong)));
        }
        if(!analysisBySubjectList.get(position).getNot_answered().equals("0")){
            yValues.add(new PieEntry(Float.parseFloat(analysisBySubjectList.get(position).getNot_answered()), getString(R.string.not_answered)));
        }

        dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueFormatter(new MyValueFormatter());
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_analysis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.by_exam:
                tvTitle.setText(getString(R.string.analysis_by_exam));
                llSubjects.setVisibility(View.GONE);
                getAnalysisByExam();
                recyclerView.setVisibility(View.VISIBLE);
                llTitles.setVisibility(View.VISIBLE);
                tvViewChart.setVisibility(View.VISIBLE);

                if(count%2==0){

                    pieChart.setVisibility(View.VISIBLE);
                }else {

                    pieChart.setVisibility(View.GONE);
                }

                return true;

            case R.id.by_subject:

                tvTitle.setText(getString(R.string.analysis_by_subject));
                llSubjects.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                llTitles.setVisibility(View.GONE);
                tvViewChart.setVisibility(View.GONE);
                getAnalysisBySubject();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " "; // e.g. append a dollar-sign
        }
    }
}
