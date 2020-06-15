package com.mlzs.mlzsoles.getkey;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.KeyMultipleChoiceRadioAdapter;
import com.mlzs.mlzsoles.fragments.BaseFragment;
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.model.Options;
import com.mlzs.mlzsoles.view.MathJaxWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class KeyExamRadioBoxFragment extends BaseFragment {

    View home_fragment_view;


    RecyclerView recyclerView;
    KeyMultipleChoiceRadioAdapter adapter;
    KeyTakeExam takeExam ;
    List<Options> optionsList;
    String fromWich;
    MathJaxWebView tvQuestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (home_fragment_view == null) {

            home_fragment_view = inflater.inflate(R.layout.key_exam_multiple_choice,container,false);

        } else {
            ((ViewGroup) home_fragment_view.getParent()).removeView(home_fragment_view);
        }

       initUI();

        return home_fragment_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void initUI(){


        tvQuestion = (MathJaxWebView)home_fragment_view.findViewById(R.id.tv_key_choice_question) ;
        recyclerView = (RecyclerView)home_fragment_view.findViewById(R.id.rv_key_multiple_choice);


        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        Bundle bundle = this.getArguments();

        if(bundle != null) {

            takeExam =(KeyTakeExam) bundle.getSerializable("question");

            fromWich = bundle.getString("fromWich");

        }


        tvQuestion.getSettings().setJavaScriptEnabled(true);
        tvQuestion.setText(takeExam.getQuestion());

        optionsList = new ArrayList<>();

        try {
            JSONArray jsonArr = new JSONArray(takeExam.getAnswers());
            for (int i=0;i<jsonArr.length();i++){
                Gson gson = new Gson();
                Type type = new TypeToken<Options>() {}.getType();
                Options myQuestions = gson.fromJson(jsonArr.get(i).toString(), type);
                optionsList.add(myQuestions);
            }

            adapter = new KeyMultipleChoiceRadioAdapter(getActivity(),optionsList,takeExam.getId(),takeExam.getCorrect_answers(),takeExam.getUser_submitted());
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
