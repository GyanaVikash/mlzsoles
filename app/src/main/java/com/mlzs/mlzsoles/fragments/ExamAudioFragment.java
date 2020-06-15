package com.mlzs.mlzsoles.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.mlzs.mlzsoles.adapters.AudioAdapter;
import com.mlzs.mlzsoles.adapters.SpinnerLanguageAdapter;
import com.mlzs.mlzsoles.model.Paragraph;
import com.mlzs.mlzsoles.model.TakeExam;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.mlzs.mlzsoles.view.MathJaxWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamAudioFragment extends BaseFragment implements View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    View audio_view;

    TextView tvMarkForReview;
    ImageView markForReview, bookMark, imgHint;
    LinearLayout llMarkForReview;
    int markReviewCount = 1;
    boolean setCheckedMarkForReview;
    ImageButton imgPlay;
    SeekBar seekBar;
    MathJaxWebView mathJaxWebView;

    TakeExam takeExam;
    RecyclerView recyclerView;
    String fromWich;

    List<Paragraph> parentList;
    AudioAdapter adapter;

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    boolean isPlayed = false;

    Map<Integer, String> mapList = new HashMap<Integer, String>();

    Spinner sprLanguage ;
    List<String> languagesList;
    int languageSelected=0;
    String uriPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        audio_view = inflater.inflate(R.layout.take_exam_audio, container, false);

        initUI();
        return audio_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initUI() {

        mathJaxWebView = (MathJaxWebView) audio_view.findViewById(R.id.webView_take_exam_audio_question);
        llMarkForReview = (LinearLayout) audio_view.findViewById(R.id.ll_take_exam_mark_for_review);
        markForReview = (ImageView) audio_view.findViewById(R.id.img_take_exam_mark_for_review);
        tvMarkForReview = (TextView) audio_view.findViewById(R.id.tv_take_exam_mark_for_review);
        bookMark = (ImageView) audio_view.findViewById(R.id.img_take_exam_bookmark);
        recyclerView = (RecyclerView) audio_view.findViewById(R.id.rv_take_exam_audio);
        imgPlay = (ImageButton) audio_view.findViewById(R.id.img_audio_play_pause);
        seekBar = (SeekBar) audio_view.findViewById(R.id.seekbar_audio);
        sprLanguage = (Spinner) audio_view.findViewById(R.id.spr_select_language);
        imgHint = (ImageView) audio_view.findViewById(R.id.img_take_exam_hint);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            takeExam = (TakeExam) bundle.getSerializable("question");
            fromWich = bundle.getString("fromWich");
            languagesList = bundle.getStringArrayList("languages");
        }


        if (languagesList.size() > 1) {

            sprLanguage.setVisibility(View.VISIBLE);

            SpinnerLanguageAdapter adapter = new SpinnerLanguageAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, languagesList);
            sprLanguage.setAdapter(adapter);

        }

        parentList = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(takeExam.getAnswers());
            for (int i = 0; i < jsonArray.length(); i++) {
                Gson gson = new Gson();
                Type type = new TypeToken<Paragraph>() {
                }.getType();
                Paragraph questions = gson.fromJson(jsonArray.get(i).toString(), type);
                parentList.add(questions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new AudioAdapter(getActivity(), parentList, this, 0);
        recyclerView.setAdapter(adapter);

        mathJaxWebView.getSettings().setJavaScriptEnabled(true);
        mathJaxWebView.setText(takeExam.getQuestion());

        sprLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageSelected = position;

                if (position == 1) {
                    if (takeExam.getQuestion_l2() != null && !takeExam.getQuestion_l2().equals("")) {
                        mathJaxWebView.setText(takeExam.getQuestion_l2());
                    }
                    adapter = new AudioAdapter(getActivity(), parentList, ExamAudioFragment.this, position);
                    recyclerView.setAdapter(adapter);

                } else {

                    mathJaxWebView.setText(takeExam.getQuestion());
                    adapter = new AudioAdapter(getActivity(), parentList, ExamAudioFragment.this, position);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (takeExam.getQuestion_tags() != null) {
            if (takeExam.getQuestion_tags().getIs_bookmarked() == 1) {
                bookMark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.exams_bg));
            } else {
                bookMark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_color));
            }
        }


        if (setCheckedMarkForReview) {
            markForReview.setColorFilter(ContextCompat.getColor(getActivity(), R.color.analysis_bg_primary));
            tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.analysis_bg_primary));

        } else {
            markForReview.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_color));
            tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey_color));

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

                if (markReviewCount % 2 == 0) {
                    setCheckedMarkForReview = true;
                    markForReview.setColorFilter(ContextCompat.getColor(getActivity(), R.color.analysis_bg_primary));
                    tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.analysis_bg_primary));

                    if (fromWich.equals("NSNT")) {
                        ((TakeExamActivity) getActivity()).changeTabTextColor("add");
                    } else if (fromWich.equals("SNT")) {
                        ((TakeExamSectionWiseActivity) getActivity()).changeTabTextColor("add");
                    } else if (fromWich.equals("ST")) {
                        ((TakeExamSectionWiseTimeActivity) getActivity()).changeTabTextColor("add");
                    }

                    Toast.makeText(getActivity(), "Marked for review", Toast.LENGTH_SHORT).show();

                } else {
                    setCheckedMarkForReview = false;
                    markForReview.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_color));
                    tvMarkForReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey_color));

                    if (fromWich.equals("NSNT")) {
                        ((TakeExamActivity) getActivity()).changeTabTextColor("remove");
                    } else if (fromWich.equals("SNT")) {
                        ((TakeExamSectionWiseActivity) getActivity()).changeTabTextColor("remove");
                    } else if (fromWich.equals("ST")) {
                        ((TakeExamSectionWiseTimeActivity) getActivity()).changeTabTextColor("remove");
                    }

                    // Toast.makeText(getActivity(), "Marked for review is removed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (takeExam.getHint() != null && !takeExam.getHint().equals("")) {

            imgHint.setVisibility(View.VISIBLE);

        }

        imgHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHint(takeExam.getHint());
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        seekBar.setOnTouchListener(this);

        if (takeExam.getQuestion_file() != null) {

            uriPath = Utils.EXAM_TYPE_BASE_URL + takeExam.getQuestion_file(); //update package name

        }

        try {

            mediaPlayer.setDataSource(uriPath);
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mediaPlayer.isPlaying()) {
                    imgPlay.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    isPlayed = true;
                } else if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.play);
                }

                primarySeekBarProgressUpdater();
            }
        });


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (!isVisibleToUser) {
            if (isPlayed) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.play);
                }
            }

        }
    }

    public void addBookMark() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", ((BaseActivity) getActivity()).getUserID());
            jsonObject.put("item_id", takeExam.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.showProgressDialog(getActivity(), "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.BOOK_MARK_SAVE_REMOVE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg, amountPay, discount;
                        int status;

                        try {
                            mssg = response.getString("message");
                            status = response.getInt("status");
                            if (status == 1) {
                                takeExam.getQuestion_tags().setIs_bookmarked(1);
                                bookMark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.exams_bg));
                                Toast.makeText(getActivity(), "added to book mark list", Toast.LENGTH_SHORT).show();
                            } else {
                                takeExam.getQuestion_tags().setIs_bookmarked(0);
                                bookMark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_color));
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


    public void addParagraphAns(String values,String type,int pos){



        HashMap hash = new HashMap();

        if(type.equals("add")){

            hash.put(pos,values);


        }else {

            mapList.remove(pos);

        }
		   mapList.putAll(hash);
        // Converting HashMap keys into ArrayList
        List<String> valueList = new ArrayList<String>(mapList.values());

        if (fromWich.equals("NSNT")) {
          ((TakeExamActivity) getActivity()).addParagraphAns(takeExam.getId(), valueList);
        } else if (fromWich.equals("SNT")) {
            ((TakeExamSectionWiseActivity) getActivity()).addParagraphAns(takeExam.getId(), valueList);
        } else if (fromWich.equals("ST")) {
            ((TakeExamSectionWiseTimeActivity) getActivity()).addParagraphAns(takeExam.getId(), valueList);
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.seekbar_audio) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
/** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
    }


    private void primarySeekBarProgressUpdater() {
        seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }


}
