package com.mlzs.mlzsoles.fragments;


import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mlzs.mlzsoles.activities.TakeExamActivity;
import com.mlzs.mlzsoles.activities.TakeExamSectionWiseActivity;
import com.mlzs.mlzsoles.activities.TakeExamSectionWiseTimeActivity;
import com.mlzs.mlzsoles.adapters.BlankAdapter;
import com.mlzs.mlzsoles.adapters.SpinnerLanguageAdapter;
import com.mlzs.mlzsoles.model.TakeExam;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.mlzs.mlzsoles.view.MathJaxWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExamBlankFragment extends BaseFragment {

    View home_fragment_view;
    TextView tvMarkForReview;
    ImageView markForReview,bookMark,imgHint;
    TakeExam takeExam ;
    LinearLayout llMarkForReview;
    
    int markReviewCount=1;

    boolean setCheckedMarkForReview;
    String fromWich;

    Spinner sprLanguage ;
    List<String> languagesList;
    int languageSelected=0;

    MathJaxWebView mathJaxWebView;

    RecyclerView recyclerView ;

    BlankAdapter adapter ;

    Map<Integer, String> mapList = new HashMap<Integer, String>();

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        home_fragment_view = inflater.inflate(R.layout.take_exam_fill_blank,container,false);

        initUI();

        return home_fragment_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initUI(){

        mathJaxWebView = (MathJaxWebView) home_fragment_view.findViewById(R.id.webView_take_exam_blank_question);
        llMarkForReview = (LinearLayout)home_fragment_view.findViewById(R.id.ll_take_exam_mark_for_review);
        markForReview = (ImageView)home_fragment_view.findViewById(R.id.img_take_exam_mark_for_review);
        tvMarkForReview = (TextView)home_fragment_view.findViewById(R.id.tv_take_exam_mark_for_review) ;
        bookMark = (ImageView)home_fragment_view.findViewById(R.id.img_take_exam_bookmark);
        sprLanguage = (Spinner)home_fragment_view.findViewById(R.id.spr_select_language);
        imgHint = (ImageView)home_fragment_view.findViewById(R.id.img_take_exam_hint);
        recyclerView = (RecyclerView)home_fragment_view.findViewById(R.id.rv_take_exam_blank);


        Bundle bundle = this.getArguments();

        if(bundle != null) {

            takeExam =(TakeExam) bundle.getSerializable("question");
            fromWich = bundle.getString("fromWich");
            languagesList = bundle.getStringArrayList("languages");

        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if(languagesList.size()>1){

            sprLanguage.setVisibility(View.VISIBLE);

            SpinnerLanguageAdapter adapter = new SpinnerLanguageAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,languagesList);
            sprLanguage.setAdapter(adapter);

        }

        sprLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                languageSelected = position ;

                if(position == 1){

                    if(takeExam.getQuestion_l2()!=null&&!takeExam.getQuestion_l2().equals("")){
                        mathJaxWebView.setText(takeExam.getQuestion_l2());
                    }
                }else {
                    mathJaxWebView.setText(takeExam.getQuestion());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mathJaxWebView.getSettings().setJavaScriptEnabled(true);
        mathJaxWebView.setText(takeExam.getQuestion());


        if(takeExam.getQuestion_tags().getIs_bookmarked()==1){
            bookMark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.exams_bg));
        }else {
            bookMark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.grey_color));
        }


        if(setCheckedMarkForReview){
            markForReview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.analysis_bg_primary));
            tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(),R.color.analysis_bg_primary));
        }else {
            markForReview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.grey_color));
            tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(),R.color.grey_color));
        }
        
        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBookMark();

            }
        });

        llMarkForReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markReviewCount++;

                if(markReviewCount%2==0){
                    setCheckedMarkForReview=true;
                    markForReview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.analysis_bg_primary));
                    tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(),R.color.analysis_bg_primary));

                    if(fromWich.equals("NSNT")){
                        ((TakeExamActivity)getActivity()).changeTabTextColor("add");
                    }else if(fromWich.equals("SNT")){
                        ((TakeExamSectionWiseActivity)getActivity()).changeTabTextColor("add");
                    }
                    else if(fromWich.equals("ST")){
                        ((TakeExamSectionWiseTimeActivity)getActivity()).changeTabTextColor("add");
                    }

                    Toast.makeText(getActivity(), "Marked for review ", Toast.LENGTH_SHORT).show();
                }else {
                    setCheckedMarkForReview=false;
                    markForReview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.grey_color));
                    tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(),R.color.grey_color));
                    if(fromWich.equals("NSNT")){
                        ((TakeExamActivity)getActivity()).changeTabTextColor("remove");
                    }else if(fromWich.equals("SNT")){
                        ((TakeExamSectionWiseActivity)getActivity()).changeTabTextColor("remove");
                    }
                    else if(fromWich.equals("ST")){
                        ((TakeExamSectionWiseTimeActivity)getActivity()).changeTabTextColor("remove");
                    }


                }
            }
        });


        adapter = new BlankAdapter(getActivity(),Integer.parseInt(takeExam.getTotal_correct_answers()),this);
        recyclerView.setAdapter(adapter);


        if(takeExam.getHint()!=null && !takeExam.getHint().equals("")){

            imgHint.setVisibility(View.VISIBLE);

        }

        imgHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHint(takeExam.getHint());
            }
        });


    }



    public void addBlankValues(String values,int pos) {

        HashMap hash = new HashMap();

        if(mapList.containsKey(pos)){

            mapList.remove(pos);

            hash.put(pos,values);

        }else {

            hash.put(pos,values);

        }

        mapList.putAll(hash);

        // Converting HashMap keys into ArrayList
        List<String> valueList = new ArrayList<String>(mapList.values());

        if(fromWich.equals("NSNT")){
            ((TakeExamActivity) getActivity()).addBlankAns(takeExam.getId(),valueList);
        }
        else if(fromWich.equals("SNT")){
            ((TakeExamSectionWiseActivity) getActivity()).addBlankAns(takeExam.getId(),valueList);
        }
        else if(fromWich.equals("ST")){
            ((TakeExamSectionWiseTimeActivity) getActivity()).addBlankAns(takeExam.getId(),valueList);
        }

    }

    public void addBookMark(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",((BaseActivity)getActivity()).getUserID());
            jsonObject.put("item_id",takeExam.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.showProgressDialog(getActivity(),"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.BOOK_MARK_SAVE_REMOVE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        int status;

                        try {
                            status = response.getInt("status");

                            if(status==1){
                             takeExam.getQuestion_tags().setIs_bookmarked(1);
                             bookMark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.exams_bg));
                             Toast.makeText(getActivity(), "added to book mark list", Toast.LENGTH_SHORT).show();
                            }else {
                                takeExam.getQuestion_tags().setIs_bookmarked(0);
                                bookMark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.grey_color));
                                Toast.makeText(getActivity(), "removed from book mark list", Toast.LENGTH_SHORT).show();
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
