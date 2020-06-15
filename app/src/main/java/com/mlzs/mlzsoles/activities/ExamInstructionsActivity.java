package com.mlzs.mlzsoles.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ExamInstructionsActivity extends BaseActivity {

    Toolbar toolbar ;
    ImageView imgBack;
    TextView tvTitle,instructionsTitle,tvInstuctions;
    Button btnStartExam;

    String examSlug,quiz_id,examType;
    CheckBox checkBox ;

    boolean checkboxSelected ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_instructions);

        toolbar = (Toolbar)findViewById(R.id.toolbar);


        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle= (TextView)findViewById(R.id.tv_toolbar_title);
        tvInstuctions = (TextView)findViewById(R.id.tv_instructions_text);
        instructionsTitle = (TextView)findViewById(R.id.tv_instructions_title);

        btnStartExam = (Button)findViewById(R.id.btn_start_exam);
        checkBox = (CheckBox)findViewById(R.id.checkboxE_exam_instructions);

        tvTitle.setText(getString(R.string.exam_instructions));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if(getIntent().getStringExtra("start_exam")!=null){
            examSlug = getIntent().getStringExtra("start_exam");
            quiz_id = getIntent().getStringExtra("quiz_id");
            examType = getIntent().getStringExtra("exam_type");
        }

        getExamInstuctions();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    checkboxSelected = true;
                    btnStartExam.setClickable(true);
                    btnStartExam.setEnabled(true);
                    btnStartExam.setBackgroundResource(R.drawable.analysis_bg);

                }else {
                    checkboxSelected =false;
                    btnStartExam.setClickable(true);
                    btnStartExam.setEnabled(true);
                    btnStartExam.setBackgroundColor(ContextCompat.getColor(ExamInstructionsActivity.this,R.color.grey_300));

                }
            }
        });


        btnStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkboxSelected){
                    if(examType.equals("NSNT")){
                        Intent intent = new Intent(ExamInstructionsActivity.this,TakeExamActivity.class);
                        intent.putExtra("start_exam",examSlug);
                        intent.putExtra("quiz_id",quiz_id);
                        startActivity(intent);
                    }
                    else if(examType.equals("SNT")){
                        Intent intent = new Intent(ExamInstructionsActivity.this,TakeExamSectionWiseActivity.class);
                        intent.putExtra("start_exam",examSlug);
                        intent.putExtra("quiz_id",quiz_id);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ExamInstructionsActivity.this,TakeExamSectionWiseTimeActivity.class);
                        intent.putExtra("start_exam",examSlug);
                        intent.putExtra("quiz_id",quiz_id);
                        startActivity(intent);

                    }
                }else {

                    Toast.makeText(ExamInstructionsActivity.this, getString(R.string.plz_select_checkbox), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void getExamInstuctions(){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.EXAM_INSTUCTIONS+examSlug, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg,data,title;
                        int status;

                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");
                            if(status==1){
                                data=response.getString("instruction_data");
                                title=response.getString("instruction_title");

                                instructionsTitle.setText(Html.fromHtml(title));
                                tvInstuctions.setText(Html.fromHtml(data));
                            }else {
                                Toast.makeText(ExamInstructionsActivity.this, mssg, Toast.LENGTH_SHORT).show();
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
