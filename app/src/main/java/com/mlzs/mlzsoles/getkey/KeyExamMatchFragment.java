package com.mlzs.mlzsoles.getkey;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.KeyMatchRightAdapter;
import com.mlzs.mlzsoles.adapters.MatchLeftAdapter;
import com.mlzs.mlzsoles.fragments.BaseFragment;
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.view.MathJaxWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class KeyExamMatchFragment extends BaseFragment {

    View home_fragment_view;

    TextView tvMarkForReview;

    KeyTakeExam takeExam;

    RecyclerView rvLeft, rvRight;

    List<String> leftList;
    List<String> rightList;

    MatchLeftAdapter leftAdapter;
    KeyMatchRightAdapter rightAdapter;

    ImageView markForReview, bookMark;
    LinearLayout llMarkForReview;
    String fromWich;

    JSONObject objectLeft, objectRight;
    JSONArray arrayLeft, arrayRight;

    MathJaxWebView tvQuestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        home_fragment_view = inflater.inflate(R.layout.key_exam_match, container, false);

        initUI();


        return home_fragment_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initUI() {

        tvQuestion = (MathJaxWebView) home_fragment_view.findViewById(R.id.tv_key_match_question);
        rvLeft = (RecyclerView) home_fragment_view.findViewById(R.id.rv_key_match_left);
        rvRight = (RecyclerView) home_fragment_view.findViewById(R.id.rv_key_match_right);
        llMarkForReview = (LinearLayout) home_fragment_view.findViewById(R.id.ll_take_exam_mark_for_review);
        markForReview = (ImageView) home_fragment_view.findViewById(R.id.img_take_exam_mark_for_review);
        tvMarkForReview = (TextView) home_fragment_view.findViewById(R.id.tv_take_exam_mark_for_review);
        bookMark = (ImageView) home_fragment_view.findViewById(R.id.img_take_exam_bookmark);

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            takeExam = (KeyTakeExam) bundle.getSerializable("question");
            fromWich = bundle.getString("fromWich");


        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rvLeft.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        rvRight.setLayoutManager(layoutManager1);


        tvQuestion.getSettings().setJavaScriptEnabled(true);
        tvQuestion.setText(takeExam.getQuestion());


        leftList = new ArrayList<>();
        rightList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(takeExam.getAnswers());
            JSONObject correctObj, userObj;
            ;
            List<String> correctAnsList = new ArrayList<String>();
            List<String> userAnsList = new ArrayList<String>();
            JSONArray userAnsArray = null;
            objectLeft = jsonObject.getJSONObject("left");
            objectRight = jsonObject.getJSONObject("right");

            arrayLeft = objectLeft.getJSONArray("options");
            arrayRight = objectRight.getJSONArray("options");

            for (int i = 0; i < arrayLeft.length(); i++) {

                leftList.add(arrayLeft.get(i).toString());

            }

            for (int i = 0; i < arrayRight.length(); i++) {

                rightList.add(arrayRight.get(i).toString());

            }

            leftAdapter = new MatchLeftAdapter(getActivity(), leftList);
            rvLeft.setAdapter(leftAdapter);

            JSONArray correctArray = new JSONArray(takeExam.getCorrect_answers());

            if (takeExam.getUser_submitted() != null) {
                userAnsArray = new JSONArray(takeExam.getUser_submitted());
            }

            if (userAnsArray != null) {

                for (int i = 0; i < userAnsArray.length(); i++) {

                    userObj = userAnsArray.getJSONObject(i);
                    userAnsList.add(userObj.getString("answer"));
                }

            }

            for (int i = 0; i < correctArray.length(); i++) {
                correctObj = correctArray.getJSONObject(i);
                correctAnsList.add(correctObj.getString("answer"));
            }

            rightAdapter = new KeyMatchRightAdapter(getActivity(), rightList, correctAnsList, userAnsList);
            rvRight.setAdapter(rightAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
