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
import com.mlzs.mlzsoles.adapters.SectionsAdapter;
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.model.Sections;
import com.mlzs.mlzsoles.utils.BaseActivity;
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

public class KeyTakeExamSectionWiseActivity extends BaseActivity {

    TabLayout questionsTabs;
    ViewPager viewPager ;

    RecyclerView rvSections;
    List<Sections> sectionsList;
    SectionsAdapter adapter ;

    List<KeyTakeExam> allQuestions;

    LinearLayout llPrevious,llNext;
    int currentPosition,nextTab,totalTabs;

    String resultSlug,strTitle;

    TextView tvTitle;
    ImageView imgBack;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.key_take_exam_section_wise);
        //setStatusBarColor(this,R.color.analysis_bg_primary);

        if (getIntent().getStringExtra("result_slug") != null) {
            resultSlug = getIntent().getStringExtra("result_slug");
            strTitle = getIntent().getStringExtra("sub_name");



        }


        questionsTabs = (TabLayout)findViewById(R.id.key_questions_tabs);
        viewPager = (ViewPager)findViewById(R.id.key_viewpager_section);
        rvSections = (RecyclerView)findViewById(R.id.key_sections_tabs);
        llPrevious = (LinearLayout)findViewById(R.id.ll_take_exam_previous);
        llNext = (LinearLayout)findViewById(R.id.ll_take_exam_next);

        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tvTitle.setText(strTitle + " "+ getString(R.string.exam_key));
       // setToolBarBackgroundColor(toolbar,this,R.color.analysis_bg_primary);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getExamsKey();

        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalTabs = allQuestions.size();
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvSections.setLayoutManager(layoutManager);
    }


    public void getExamsKey(){

        allQuestions = new ArrayList<>();
        sectionsList = new ArrayList<>();

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
                        KeyTakeExam myQuestions = null;
                        String sectionData;
                        try {
                            quizObject = response.getJSONObject("quiz");
                            questionsObj = response.getJSONObject("right_bar_data");
                            jsonArray = questionsObj.getJSONArray("questions");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<KeyTakeExam>() {}.getType();
                                myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                allQuestions.add(myQuestions);

                            }

                            if (allQuestions.size() != 0) {

                                MyAdapter mTabLayoutAdapter = new MyAdapter(getSupportFragmentManager(), allQuestions);
                                viewPager.setAdapter(mTabLayoutAdapter);
                                questionsTabs.setupWithViewPager(viewPager);

                                viewPager.setOffscreenPageLimit(allQuestions.size());

                            }
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
                                Sections sections = gson.fromJson(sectionsArray.getString(i).toString(),type);
                                sectionsList.add(sections);

                            }

                            if(sectionsList.size()!=0){

                                adapter = new SectionsAdapter(KeyTakeExamSectionWiseActivity.this,sectionsList,"key");
                                rvSections.setAdapter(adapter);
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


        for (int i=0;i<allQuestions.size();i++){

            if (allQuestions.get(i).getId().contains(questions.get(0))) {

                String position = allQuestions.get(i).getQuestion_tags().getSno();


                int tabPosition = Integer.parseInt(position);

                if(tabPosition==1){
                    questionsTabs.getTabAt(0).select();
                }else {
                    questionsTabs.getTabAt(tabPosition-1).select();
                }

            }

        }

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
                data.putString("fromWich", "SNT");
                homeFragment.setArguments(data);

                return homeFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("radio")) {

                KeyExamRadioBoxFragment choiceFragment = new KeyExamRadioBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "SNT");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("checkbox")) {
                KeyExamCheckBoxFragment choiceFragment = new KeyExamCheckBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "SNT");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("para")) {
                KeyExamParagraphFragment paragraphFragment = new KeyExamParagraphFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "SNT");
                paragraphFragment.setArguments(data);
                return paragraphFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("video")) {

                KeyExamVideoFragmnet videoFragmnet = new KeyExamVideoFragmnet();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "SNT");
                videoFragmnet.setArguments(data);

                return videoFragmnet;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("audio")) {

                KeyExamAudioFragment audioFragment = new KeyExamAudioFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "SNT");
                audioFragment.setArguments(data);
                return audioFragment;
            } else {
                KeyExamMatchFragment matchFragment = new KeyExamMatchFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "SNT");
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
