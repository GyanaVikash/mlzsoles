package com.mlzs.mlzsoles.getkey;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.KeyParagrapAdapter;
import com.mlzs.mlzsoles.fragments.BaseFragment;
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.model.Paragraph;
import com.mlzs.mlzsoles.view.MathJaxWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KeyExamParagraphFragment extends BaseFragment {

    View paragraph_view;

    MathJaxWebView tvQuestion;

    KeyTakeExam takeExam;
    RecyclerView recyclerView;
    String fromWich;

    List<Paragraph> parentList;
    KeyParagrapAdapter adapter;
    List<String> correctAnsList = new ArrayList<String>();
    List<String> userAnsList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        paragraph_view = inflater.inflate(R.layout.key_exam_paragraph, container, false);


        initUI();

        return paragraph_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void initUI() {

        tvQuestion = (MathJaxWebView) paragraph_view.findViewById(R.id.tv_key_paragraph_question);

        recyclerView = (RecyclerView) paragraph_view.findViewById(R.id.rv_key_paraghraph);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            takeExam = (KeyTakeExam) bundle.getSerializable("question");
            fromWich = bundle.getString("fromWich");


        }

        tvQuestion.getSettings().setJavaScriptEnabled(true);
        tvQuestion.setText(takeExam.getQuestion());

        parentList = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(takeExam.getAnswers());
            JSONArray userArray = null, correctAnsArray;
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                Gson gson = new Gson();
                Type type = new TypeToken<Paragraph>() {
                }.getType();
                Paragraph questions = gson.fromJson(jsonArray.get(i).toString(), type);
                parentList.add(questions);
            }


            if (takeExam.getUser_submitted() != null) {
                userArray = new JSONArray(takeExam.getUser_submitted());
            }
            correctAnsArray = new JSONArray(takeExam.getCorrect_answers());

            if (correctAnsArray != null) {
                for (int i = 0; i < correctAnsArray.length(); i++) {
                    jsonObject = correctAnsArray.getJSONObject(i);

                    correctAnsList.add(jsonObject.getString("answer"));

                }

            }

            if (userArray != null) {
                for (int i = 0; i < userArray.length(); i++) {
                    jsonObject = userArray.getJSONObject(i);
                    userAnsList.add(jsonObject.getString("answer"));
                }
            }
            adapter = new KeyParagrapAdapter(getActivity(), parentList, correctAnsList, userAnsList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
