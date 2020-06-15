package com.mlzs.mlzsoles.getkey;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KeyTakeExamActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabs;
    LinearLayout llPrevious, llNext;
    int currentPosition, nextTab, totalTabs;

    String resultSlug,strTitle;

    List<KeyTakeExam> list;

    TextView tvTitle;
    ImageView imgBack;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_take_exam);
        //setStatusBarColor(this,R.color.analysis_bg_primary);

        if (getIntent().getStringExtra("result_slug") != null) {
            resultSlug = getIntent().getStringExtra("result_slug");
            strTitle = getIntent().getStringExtra("sub_name");

        }

        mViewPager = (ViewPager) findViewById(R.id.key_viewpager);
        mTabs = (TabLayout) findViewById(R.id.key_tabs);
        llPrevious = (LinearLayout) findViewById(R.id.ll_take_exam_previous);
        llNext = (LinearLayout) findViewById(R.id.ll_take_exam_next);
        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        tvTitle.setText(strTitle +" "+ getString(R.string.exam_key));
     //   setToolBarBackgroundColor(toolbar,this,R.color.analysis_bg_primary);

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

                totalTabs = list.size();
                currentPosition = mTabs.getSelectedTabPosition();
                nextTab = currentPosition + 1;

                if(totalTabs-currentPosition==1){
                    llNext.setVisibility(View.GONE);
                }else {
                    mTabs.getTabAt(nextTab).select();
                }

                llPrevious.setVisibility(View.VISIBLE);
            }
        });


        llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = mTabs.getSelectedTabPosition();
                nextTab = currentPosition - 1;

                if (nextTab >= 0) {
                    mTabs.getTabAt(nextTab).select();
                    if (nextTab == 0) {
                        llPrevious.setVisibility(View.GONE);
                    }
                }

            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    llPrevious.setVisibility(View.GONE);
                    llNext.setVisibility(View.VISIBLE);
                } else if (position+1 == list.size()) {
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
                } else if (position+1 == list.size()) {
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


        list = new ArrayList<>();

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_EXAM_KEY + resultSlug , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        JSONArray jsonArray;

                        try {
                            jsonArray = response.getJSONArray("questions");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<KeyTakeExam>() {}.getType();
                                KeyTakeExam  myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                list.add(myQuestions);

                            }

                            if (list.size() != 0) {

                                MyAdapter mTabLayoutAdapter = new MyAdapter(getSupportFragmentManager(), list);
                                mViewPager.setAdapter(mTabLayoutAdapter);
                                mTabs.setupWithViewPager(mViewPager);

                                mViewPager.setOffscreenPageLimit(list.size());

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);

    }

    static class MyAdapter extends FragmentStatePagerAdapter {

        List<KeyTakeExam> fragmentAdapterArrayList;


        public MyAdapter(FragmentManager fm, List<KeyTakeExam> takeExamList) {
            super(fm);
            fragmentAdapterArrayList = takeExamList;

        }

        @Override
        public Fragment getItem(int position) {

            Bundle data;
            KeyTakeExam takeExam = fragmentAdapterArrayList.get(position);

            if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("blanks")) {

                KeyExamBlankFragment homeFragment = new KeyExamBlankFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "NSNT");
                homeFragment.setArguments(data);

                return homeFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("radio")) {

                KeyExamRadioBoxFragment choiceFragment = new KeyExamRadioBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "NSNT");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("checkbox")) {
                KeyExamCheckBoxFragment choiceFragment = new KeyExamCheckBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "NSNT");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("para")) {
                KeyExamParagraphFragment paragraphFragment = new KeyExamParagraphFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "NSNT");
                paragraphFragment.setArguments(data);
                return paragraphFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("video")) {

                KeyExamVideoFragmnet videoFragmnet = new KeyExamVideoFragmnet();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "NSNT");
                videoFragmnet.setArguments(data);

                return videoFragmnet;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("audio")) {

                KeyExamAudioFragment audioFragment = new KeyExamAudioFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "NSNT");
                audioFragment.setArguments(data);
                return audioFragment;
            } else {
                KeyExamMatchFragment matchFragment = new KeyExamMatchFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putString("fromWich", "NSNT");
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
