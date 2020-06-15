package com.mlzs.mlzsoles.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class ResultActivity extends BaseActivity {


    Toolbar toolbar;
    ImageView imgBack;
    TextView tvTitle, tvScore, tvPercentage, tvResult, tvCorrect, tvWrong, tvNotAns, tvDuration, tvTotalQuestions;

    String score, percentage, result, title, wrong_ans, correct_ans, not_ans, totalMarks;

    JSONArray correctArray, wrongArray, notArray;

    String correctAns, wrongAns, notAns;

    ProgressBar progressBar;

    int intPercentage, duration, totalQuestions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgBack = (ImageView) findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        tvScore = (TextView) findViewById(R.id.tv_result_score);
        tvPercentage = (TextView) findViewById(R.id.tv_result_percentage);
        tvResult = (TextView) findViewById(R.id.tv_result);

        progressBar = findViewById(R.id.pb_result);
        tvCorrect = findViewById(R.id.tv_result_correct);
        tvWrong = findViewById(R.id.tv_result_wrong);
        tvNotAns = findViewById(R.id.tv_result_notans);
        tvDuration = findViewById(R.id.tv_result_duration);

        tvTotalQuestions = findViewById(R.id.tv_result_total_questions);


        if (getIntent().getStringExtra("correct_ans") != null) {

            title = getIntent().getStringExtra("title");
            correct_ans = getIntent().getStringExtra("correct_ans");
            wrong_ans = getIntent().getStringExtra("wrong_ans");
            not_ans = getIntent().getStringExtra("not_ans");
            score = getIntent().getStringExtra("score");
            percentage = getIntent().getStringExtra("percentage");
            result = getIntent().getStringExtra("result");
            totalMarks = getIntent().getStringExtra("total_marks");
            duration = getIntent().getIntExtra("dueration", duration);

            totalQuestions = getIntent().getIntExtra("total_questions", totalQuestions);



            try {
                correctArray = new JSONArray(correct_ans);
                wrongArray = new JSONArray(wrong_ans);
                notArray = new JSONArray(not_ans);

                correctAns = String.valueOf(correctArray.length());
                wrongAns = String.valueOf(wrongArray.length());
                notAns = String.valueOf(notArray.length());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvTitle.setText(getString(R.string.result_for) + " " + title);

            tvScore.setText(score + " / " + totalMarks);
            tvResult.setText(result);
            tvCorrect.setText(correctAns);
            tvWrong.setText(wrongAns);
            tvNotAns.setText(notAns);

            tvDuration.setText(duration + " " + getResources().getString(R.string.minutes));

            tvTotalQuestions.setText(getResources().getString(R.string.total_questios) + " : " + totalQuestions);


            intPercentage = new Double(percentage).intValue();

            if(intPercentage>=0){
                tvPercentage.setText(intPercentage + " %");

                progressBar.setProgress(intPercentage);
            }else {

                tvPercentage.setText("0 %");

                progressBar.setProgress(0);
            }

        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
