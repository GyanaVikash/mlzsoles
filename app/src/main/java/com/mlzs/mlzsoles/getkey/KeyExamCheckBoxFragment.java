package com.mlzs.mlzsoles.getkey;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.KeyMultipleChoiceCheckboxAdapter;
import com.mlzs.mlzsoles.fragments.BaseFragment;
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.model.Options;
import com.mlzs.mlzsoles.view.MathJaxWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KeyExamCheckBoxFragment extends BaseFragment {

    View check_box_view;

    MathJaxWebView tvQuestion;
    RecyclerView recyclerView;

    KeyTakeExam takeExam;


    List<Options> optionsList;
    KeyMultipleChoiceCheckboxAdapter adapter;


    String fromWich;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (check_box_view == null) {

            check_box_view = inflater.inflate(R.layout.key_exam_multiple_choice, container, false);


        } else {
            ((ViewGroup) check_box_view.getParent()).removeView(check_box_view);
        }

        initUI();

        return check_box_view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initUI() {

        tvQuestion = (MathJaxWebView) check_box_view.findViewById(R.id.tv_key_choice_question);
        recyclerView = (RecyclerView) check_box_view.findViewById(R.id.rv_key_multiple_choice);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            takeExam = (KeyTakeExam) bundle.getSerializable("question");
            fromWich = bundle.getString("fromWich");

        }


        optionsList = new ArrayList<>();
        tvQuestion.getSettings().setJavaScriptEnabled(true);
        tvQuestion.setText(takeExam.getQuestion());


        try {
            JSONArray jsonArr = new JSONArray(takeExam.getAnswers());
            JSONObject correctObj, userObj;
            ;
            List<String> correctAnsList = new ArrayList<String>();
            List<String> userAnsList = new ArrayList<String>();
            JSONArray userAnsArray = null;
            for (int i = 0; i < jsonArr.length(); i++) {

                Gson gson = new Gson();
                Type type = new TypeToken<Options>() {
                }.getType();
                Options myQuestions = gson.fromJson(jsonArr.get(i).toString(), type);
                optionsList.add(myQuestions);

            }

            JSONArray jsonArray = new JSONArray(takeExam.getCorrect_answers());

            if (takeExam.getUser_submitted() != null) {
                userAnsArray = new JSONArray(takeExam.getUser_submitted());
            }


            if (userAnsArray != null) {

                for (int i = 0; i < userAnsArray.length(); i++) {

                    userObj = userAnsArray.getJSONObject(i);
                    userAnsList.add(userObj.getString("answer"));
                }

            }

            for (int i = 0; i < jsonArray.length(); i++) {
                correctObj = jsonArray.getJSONObject(i);
                correctAnsList.add(correctObj.getString("answer"));
            }

            adapter = new KeyMultipleChoiceCheckboxAdapter(getActivity(), optionsList, correctAnsList, userAnsList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
