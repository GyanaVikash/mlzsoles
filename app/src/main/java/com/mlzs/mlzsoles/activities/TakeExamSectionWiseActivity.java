package com.mlzs.mlzsoles.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.SectionsAdapter;
import com.mlzs.mlzsoles.fragments.ExamAudioFragment;
import com.mlzs.mlzsoles.fragments.ExamBlankFragment;
import com.mlzs.mlzsoles.fragments.ExamCheckBoxFragment;
import com.mlzs.mlzsoles.fragments.ExamMatchFragment;
import com.mlzs.mlzsoles.fragments.ExamParagraphFragment;
import com.mlzs.mlzsoles.fragments.ExamRadioBoxFragment;
import com.mlzs.mlzsoles.fragments.ExamVideoFragmnet;
import com.mlzs.mlzsoles.model.Sections;
import com.mlzs.mlzsoles.model.TakeExam;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TakeExamSectionWiseActivity extends BaseActivity {

    TabLayout questionsTabs;
    ViewPager viewPager;

    TextView tvSubmitTest, tvSubmit, tvTimer, tvCancel;

    String examSlug, quiz_id;

    BottomSheetDialog mBottomSheetDialog;

    RecyclerView rvSections;
    List<Sections> sectionsList;
    SectionsAdapter adapter;

    List<TakeExam> allQuestions;

    LinearLayout llPrevious, llNext;
    int currentPosition, nextTab, totalTabs;

    HashMap<String, String> timeSpent = new HashMap<String, String>();

    HashMap<String, List<String>> ansList = new HashMap<String, List<String>>();

    ArrayList<String> languageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_take_exam_section_wise);

        tvTimer = (TextView) findViewById(R.id.tv_take_exam_timer);
        tvSubmitTest = (TextView) findViewById(R.id.tv_take_exam_submit);

        questionsTabs = (TabLayout) findViewById(R.id.questions_tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager_section);
        rvSections = (RecyclerView) findViewById(R.id.sections_tabs);

        llPrevious = (LinearLayout) findViewById(R.id.ll_take_exam_previous);
        llNext = (LinearLayout) findViewById(R.id.ll_take_exam_next);

        if (getIntent().getStringExtra("start_exam") != null) {
            examSlug = getIntent().getStringExtra("start_exam");
            quiz_id = getIntent().getStringExtra("quiz_id");
        }

        getQuestions();

        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalTabs = allQuestions.size();
                currentPosition = questionsTabs.getSelectedTabPosition();
                nextTab = currentPosition + 1;

                if (totalTabs - currentPosition == 1) {
                    llNext.setVisibility(View.GONE);
                } else {
                    questionsTabs.getTabAt(nextTab).select();
                }

                llPrevious.setVisibility(View.VISIBLE);
            }
        });


        llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = questionsTabs.getSelectedTabPosition();
                nextTab = currentPosition - 1;
                if (nextTab >= 0) {
                    questionsTabs.getTabAt(nextTab).select();
                    if (nextTab == 0) {
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
                } else if (position + 1 == allQuestions.size()) {
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
                } else if (position + 1 == allQuestions.size()) {
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

        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.end_test, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        tvSubmit = (TextView) sheetView.findViewById(R.id.tv_end_test_submit);
        tvCancel = (TextView) sheetView.findViewById(R.id.tv_end_test_cancel);


        tvSubmitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                mBottomSheetDialog.show();
            }
        });

        setTextColorGradient(tvCancel, getResources().getColor(R.color.analysis_bg_primary), getResources().getColor(R.color.analysis_bg));


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mBottomSheetDialog.dismiss();

                getExamResult();

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvSections.setLayoutManager(layoutManager);
    }


    public void getQuestions() {

        sectionsList = new ArrayList<>();
        allQuestions = new ArrayList<>();

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.START_EXAM + examSlug + "?user_id=" + getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String timeHours, timeMinutes, timeSeconds, sectionData, languageName;
                        final JSONArray questionsArray, sectionsArray;
                        JSONObject quizObj, questionsObj;
                        JSONObject quizObject;
                        int hasLanguage;
                        try {

                            quizObject = response.getJSONObject("quiz");
                            timeHours = response.getString("time_hours");
                            timeMinutes = response.getString("time_minutes");
                            timeSeconds = response.getString("time_seconds");
                            int secondsToMs = Integer.parseInt(timeSeconds) * 1000;
                            int minutesToMs = Integer.parseInt(timeMinutes) * 60000;
                            int hoursToMs = Integer.parseInt(timeHours) * 3600000;
                            long total = secondsToMs + minutesToMs + hoursToMs;
                            startTimer(total);

                            sectionData = quizObject.getString("section_data");
                            hasLanguage = quizObject.getInt("has_language");
                            languageList.add("English");
                            if (hasLanguage == 1) {

                                languageName = quizObject.getString("language_name");
                                languageList.add(languageName);

                            }

                            sectionData.replaceAll("\\\\", "");
                            JSONObject jsonObject = new JSONObject(sectionData);
                            Iterator x = jsonObject.keys();
                            sectionsArray = new JSONArray();

                            while (x.hasNext()) {
                                String key = (String) x.next();
                                sectionsArray.put(jsonObject.get(key));
                            }

                            for (int i = 0; i < sectionsArray.length(); i++) {

                                Gson gson = new Gson();
                                Type type = new TypeToken<Sections>() {
                                }.getType();
                                Sections myQuestions = gson.fromJson(sectionsArray.getString(i).toString(), type);
                                sectionsList.add(myQuestions);

                            }

                            if (sectionsList.size() != 0) {

                                adapter = new SectionsAdapter(TakeExamSectionWiseActivity.this, sectionsList, "exam");
                                rvSections.setAdapter(adapter);
                            }

                            questionsObj = response.getJSONObject("right_bar_data");
                            questionsArray = questionsObj.getJSONArray("questions");

                            for (int i = 0; i < questionsArray.length(); i++) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<TakeExam>() {
                                }.getType();
                                TakeExam questions = gson.fromJson(questionsArray.get(i).toString(), type);
                                allQuestions.add(questions);
                            }

                            if (allQuestions.size() != 0) {

                                MyAdapter mTabLayoutAdapter = new MyAdapter(getSupportFragmentManager(), allQuestions, languageList);
                                viewPager.setAdapter(mTabLayoutAdapter);
                                questionsTabs.setupWithViewPager(viewPager);

                                viewPager.setOffscreenPageLimit(allQuestions.size());

                            }
                            for (int i = 0; i < allQuestions.size(); i++) {
                                timeSpent.put(allQuestions.get(i).getId(), "0");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);
    }

    public void changeTabTextColor(String type) {

        currentPosition = questionsTabs.getSelectedTabPosition();

        TabLayout tabs = findViewById(R.id.questions_tabs);

        try {

            TabLayout.Tab tab = tabs.getTabAt(currentPosition);

            Field viewField = TabLayout.Tab.class.getDeclaredField("mView");
            viewField.setAccessible(true);
            Object tabView = viewField.get(tab);

            Field textViewField = tabView.getClass().getDeclaredField("mTextView");
            textViewField.setAccessible(true);
            TextView textView = (TextView) textViewField.get(tabView);

            if (type.equals("add")) {

                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.analysis_bg_primary));
                textView.setTextColor(ContextCompat.getColor(this, R.color.white_color));
                textView.setPadding(20, 15, 20, 15);

            } else {
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.white_color));
                textView.setTextColor(ContextCompat.getColor(this, R.color.grey_color));

            }

        } catch (NoSuchFieldException e) {
            // TODO
        } catch (IllegalAccessException e) {
            // TODO
        }

    }

    public void sectionSelected(List<String> questions) {

        for (int i = 0; i < allQuestions.size(); i++) {

            if (allQuestions.get(i).getId().contains(questions.get(0))) {

                String position = allQuestions.get(i).getQuestion_tags().getSno();


                int tabPosition = Integer.parseInt(position);

                if (tabPosition == 1) {
                    questionsTabs.getTabAt(0).select();
                } else {
                    questionsTabs.getTabAt(tabPosition - 1).select();
                }

            }

        }

    }

    static class MyAdapter extends FragmentStatePagerAdapter {

        List<TakeExam> fragmentAdapterArrayList;
        ArrayList<String> languagesList;

        public MyAdapter(FragmentManager fm, List<TakeExam> takeExamList, ArrayList<String> list) {
            super(fm);
            fragmentAdapterArrayList = takeExamList;

            languagesList = list;
        }

        @Override
        public Fragment getItem(int position) {


            Bundle data;
            TakeExam takeExam = fragmentAdapterArrayList.get(position);

            if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("blanks")) {

                ExamBlankFragment homeFragment = new ExamBlankFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putStringArrayList("languages", languagesList);
                data.putString("fromWich", "SNT");
                homeFragment.setArguments(data);

                return homeFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("radio")) {

                ExamRadioBoxFragment choiceFragment = new ExamRadioBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putStringArrayList("languages", languagesList);
                data.putString("fromWich", "SNT");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("checkbox")) {
                ExamCheckBoxFragment choiceFragment = new ExamCheckBoxFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putStringArrayList("languages", languagesList);
                data.putString("fromWich", "SNT");
                choiceFragment.setArguments(data);

                return choiceFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("para")) {
                ExamParagraphFragment paragraphFragment = new ExamParagraphFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putStringArrayList("languages", languagesList);
                data.putString("fromWich", "SNT");
                paragraphFragment.setArguments(data);
                return paragraphFragment;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("video")) {

                ExamVideoFragmnet videoFragmnet = new ExamVideoFragmnet();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putStringArrayList("languages", languagesList);
                data.putString("fromWich", "SNT");
                videoFragmnet.setArguments(data);

                return videoFragmnet;
            } else if (fragmentAdapterArrayList.get(position).getQuestion_type().equals("audio")) {

                ExamAudioFragment audioFragment = new ExamAudioFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putStringArrayList("languages", languagesList);
                data.putString("fromWich", "SNT");
                audioFragment.setArguments(data);
                return audioFragment;
            } else {
                ExamMatchFragment matchFragment = new ExamMatchFragment();
                data = new Bundle();//Use bundle to pass data
                data.putSerializable("question", takeExam);
                data.putStringArrayList("languages", languagesList);
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

    private void startTimer(long noOfMinutes) {
        CountDownTimer countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                tvTimer.setText(hms);//set text
            }

            public void onFinish() {
                tvTimer.setText("TIME'S UP!!"); //On finish change timer text

                showAlertTimeFinish();
            }
        }.start();

    }

    public void addCheckboxAnswer(String questionID, List<String> selecedValues) {

        ansList.put(questionID, selecedValues);

    }

    public void addRadioAns(String questionID, String selectedOptionValue) {

        List<String> selectedRadioValue = new ArrayList<>();

        selectedRadioValue.add(selectedOptionValue);

        ansList.put(questionID, selectedRadioValue);
    }

    public void addParagraphAns(String questionID, List<String> selectedValue) {

        ansList.put(questionID, selectedValue);
    }


    public void addBlankAns(String questionID, List<String> selecedValues) {

        ansList.put(questionID, selecedValues);
    }

    public void addMatchAns(String questionId, List<String> matchList) {

        ansList.put(questionId, matchList);

    }

    public void getExamResult() {

        final List<String> correctAns = new ArrayList<>();
        final List<String> wrongAns = new ArrayList<>();
        final List<String> notAns = new ArrayList<>();

        JSONObject object = new JSONObject();
        try {

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String ts = gson.toJson(timeSpent);
            String as = gson.toJson(ansList);

            object.put("time_spent", ts);
            object.put("answers", as);
            object.put("quiz_id", quiz_id);
            object.put("student_id", getUserID());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.showProgressDialog(this, "Loading....");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.GET_RESULT + examSlug, object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Utils.dissmisProgress();
                            String title, score, percentage, result, totalMarks;
                            JSONObject object, recordObj,quizObj;
                            String correctArry, wrongArray, notArray;
                            int status,duration,totalQuestions;
                            try {

                                status = response.getInt("status");

                                if (status == 1) {
                                    object = response.getJSONObject("record");
                                    title = object.getString("title");
                                    recordObj = object.getJSONObject("record");
                                    quizObj = object.getJSONObject("quiz");
                                    correctArry = recordObj.getString("correct_answer_questions");
                                    wrongArray = recordObj.getString("wrong_answer_questions");
                                    notArray = recordObj.getString("not_answered_questions");

                                    score = recordObj.getString("marks_obtained");
                                    percentage = recordObj.getString("percentage");
                                    result = recordObj.getString("exam_status");
                                    totalMarks = recordObj.getString("total_marks");

                                    duration = quizObj.getInt("dueration");
                                    totalQuestions = quizObj.getInt("total_questions");

                                    Intent intent = new Intent(TakeExamSectionWiseActivity.this, ResultActivity.class);
                                    intent.putExtra("correct_ans", correctArry);
                                    intent.putExtra("wrong_ans", wrongArray);
                                    intent.putExtra("not_ans", notArray);
                                    intent.putExtra("score", score);
                                    intent.putExtra("percentage", percentage);
                                    intent.putExtra("result", result);
                                    intent.putExtra("title", title);
                                    intent.putExtra("total_marks", totalMarks);
                                    intent.putExtra("dueration",duration);
                                    intent.putExtra("total_questions",totalQuestions);
                                    startActivity(intent);
                                    finish();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, errorListener) {

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(TakeExamSectionWiseActivity.this, getResources().getString(R.string.back_press_disabled), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void showAlertTimeFinish() {

        AlertDialog.Builder builder = new AlertDialog.Builder(TakeExamSectionWiseActivity.this);
        builder.setMessage(getResources().getString(R.string.exceed_time_to_finish_exam)).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        tvSubmit.performClick();
                        tvCancel.setVisibility(View.GONE);

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
