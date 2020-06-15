package com.mlzs.mlzsoles.getkey;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.SectionsWithTimerAdapter;
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.model.Sections;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.RecyclerItemClickListener;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeyTakeExamSectionWiseTimeActivity extends BaseActivity {

    TabLayout questionsTabs;
    ViewPager viewPager ;


    RecyclerView rvSections;
    List<Sections> sectionsList;
    SectionsWithTimerAdapter adapter ;

    List<KeyTakeExam> allQuestions;
    List<KeyTakeExam> sectionQuestions;

    LinearLayout llPrevious,llNext;
    int currentPosition,nextTab,totalTabs;

    String resultSlug,strTitle;

    TextView tvTitle;
    ImageView imgBack;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.key_section_wise_time);
        //setStatusBarColor(this,R.color.analysis_bg_primary);

        if (getIntent().getStringExtra("result_slug") != null) {
            resultSlug = getIntent().getStringExtra("result_slug");
            strTitle = getIntent().getStringExtra("sub_name");

        }
        questionsTabs = (TabLayout)findViewById(R.id.key_questions_tabs_time);
        viewPager = (ViewPager)findViewById(R.id.key_viewpager_section_time);
        rvSections = (RecyclerView)findViewById(R.id.key_sections_tabs_time);
        llPrevious = (LinearLayout)findViewById(R.id.ll_take_exam_previous);
        llNext = (LinearLayout)findViewById(R.id.ll_take_exam_next);
        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        tvTitle.setText(strTitle + " "+ getString(R.string.exam_key));
     //   setToolBarBackgroundColor(toolbar,this,R.color.analysis_bg_primary);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvSections.setLayoutManager(layoutManager);

        getExamsKey();

        rvSections.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        adapter.startSection(position);

                    }
                })
        );

        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalTabs = sectionQuestions.size();
                currentPosition = questionsTabs.getSelectedTabPosition();
                nextTab = currentPosition+1;


                if(totalTabs-currentPosition==1){
                    llNext.setVisibility(View.GONE);
                }else {
                    questionsTabs.getTabAt(nextTab).select();
                }


                llPrevious.setVisibility(View.VISIBLE);
            }
        });


        llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = questionsTabs.getSelectedTabPosition();
                nextTab = currentPosition-1;

                if(nextTab>=0){
                    questionsTabs.getTabAt(nextTab).select();
                    if(nextTab==0){
                        llPrevious.setVisibility(View.GONE);
                    }
                }

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    llPrevious.setVisibility(View.GONE);
                    llNext.setVisibility(View.VISIBLE);
                } else if (position+1 == allQuestions.size()) {
                    llNext.setVisibility(View.GONE);
                    llPrevious.setVisibility(View.VISIBLE);
                } else {
                    llPrevious.setVisibility(View.VISIBLE);
                    llNext.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    llPrevious.setVisibility(View.GONE);
                    llNext.setVisibility(View.VISIBLE);
                } else if (position+1 == allQuestions.size()) {
                    llNext.setVisibility(View.GONE);
                    llPrevious.setVisibility(View.VISIBLE);
                } else {
                    llPrevious.setVisibility(View.VISIBLE);
                    llNext.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void getExamsKey(){

        sectionsList = new ArrayList<>();
        allQuestions = new ArrayList<>();
        sectionQuestions = new ArrayList<>();

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_EXAM_KEY + resultSlug , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        JSONArray jsonArray,sectionsArray;
                        JSONObject quizObject,questionsObj;
                        String sectionData;

                        try {
                            quizObject = response.getJSONObject("quiz");
                            questionsObj = response.getJSONObject("right_bar_data");
                            jsonArray = questionsObj.getJSONArray("questions");


                            sectionData = quizObject.getString("section_data");

                            sectionData.replaceAll("\\\\", "");

                            JSONObject jsonObject = new JSONObject(sectionData);
                            Iterator x = jsonObject.keys();
                            sectionsArray = new JSONArray();

                            while (x.hasNext()){
                                String key = (String) x.next();
                                sectionsArray.put(jsonObject.get(key));
                            }

                            for(int i=0;i<sectionsArray.length();i++){

                                Gson gson = new Gson();
                                Type type = new TypeToken<Sections>(){}.getType();
                                Sections myQuestions = gson.fromJson(sectionsArray.getString(i).toString(),type);
                                sectionsList.add(myQuestions);

                            }

                            if(sectionsList.size()!=0){

                                adapter = new SectionsWithTimerAdapter(KeyTakeExamSectionWiseTimeActivity.this,sectionsList,"key");
                                rvSections.setAdapter(adapter);
                            }


                            for (int i=0;i<jsonArray.length();i++){
                                Gson gson = new Gson();
                                Type type = new TypeToken<KeyTakeExam>() {}.getType();
                                KeyTakeExam  questions = gson.fromJson(jsonArray.get(i).toString(), type);
                                allQuestions.add(questions);

                            }

                            if(sectionsList.size()!=0){

                                List<String> questions = sectionsList.get(0).getQuestions();

                                for(int i=0;i<allQuestions.size();i++){
                                    for (int j=0;j<questions.size();j++){

                                        if(allQuestions.get(i).getId().equals(questions.get(j))){

                                            sectionQuestions.add(allQuestions.get(i));

                                        }
                                    }
                                }
                            }

                            if(sectionQuestions.size()!=0){

                                MyAdapter mTabLayoutAdapter = new MyAdapter(getSupportFragmentManager(), sectionQuestions);
                                viewPager.setAdapter(mTabLayoutAdapter);
                                questionsTabs.setupWithViewPager(viewPager);

                                viewPager.setOffscreenPageLimit(sectionQuestions.size());

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);

    }

    public void sectionSelected(List<String> questions){

        sectionQuestions = new ArrayList<>();

        for(int i=0;i<allQuestions.size();i++){

            for (int j=0;j<questions.size();j++){

                if(allQuestions.get(i).getId().equals(questions.get(j))){

                    sectionQuestions.add(allQuestions.get(i));
                }
            }
        }

        MyAdapter mTabLayoutAdapter = new MyAdapter(getSupportFragmentManager(), sectionQuestions);
        viewPager.setAdapter(mTabLayoutAdapter);
        questionsTabs.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(sectionQuestions.size());

    }

    static class MyAdapter extends FragmentStatePagerAdapter {

        List<KeyTakeExam> fragmentAdapterArrayList;


        public MyAdapter(FragmentManager fm, List<KeyTakeExam> takeExamList) {
            super(fm);
            fragmentAdapterArrayList = takeExamList;
        }

        @Override
        public Fragment getItem(int position) {


            Bundle data ;
            KeyTakeExam takeExam = fragmentAdapterArrayList.get(position);

            if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("blanks")) {

                KeyExamBlankFragment homeFragment = new KeyExamBlankFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "ST");
                homeFragment.setArguments(data);

                return homeFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("radio")) {

                KeyExamRadioBoxFragment choiceFragment = new KeyExamRadioBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "ST");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("checkbox")) {
                KeyExamCheckBoxFragment choiceFragment = new KeyExamCheckBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "ST");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("para")) {
                KeyExamParagraphFragment paragraphFragment = new KeyExamParagraphFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "ST");
                paragraphFragment.setArguments(data);
                return paragraphFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("video")) {

                KeyExamVideoFragmnet videoFragmnet = new KeyExamVideoFragmnet();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "ST");
                videoFragmnet.setArguments(data);

                return videoFragmnet;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("audio")) {

                KeyExamAudioFragment audioFragment = new KeyExamAudioFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "ST");
                audioFragment.setArguments(data);
                return audioFragment;
            } else {
                KeyExamMatchFragment matchFragment = new KeyExamMatchFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "ST");
                matchFragment.setArguments(data);
                return matchFragment;
            }


        }

        @Override
        public int getCount() {
            return fragmentAdapterArrayList.size();
        }

        @Override
        public int getItemPosition(Object object) {

            return MyAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return fragmentAdapterArrayList.get(position).getQuestion_tags().getSno();
        }
    }



}
