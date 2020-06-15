package com.mlzs.mlzsoles.getkey;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.mlzs.mlzsoles.R;

import com.mlzs.mlzsoles.adapters.KeyAudioAdapter;
import com.mlzs.mlzsoles.fragments.BaseFragment;
import com.mlzs.mlzsoles.model.KeyTakeExam;
import com.mlzs.mlzsoles.model.Paragraph;
import com.mlzs.mlzsoles.utils.Utils;
import com.mlzs.mlzsoles.view.MathJaxWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KeyExamAudioFragment extends BaseFragment implements View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    View audio_view;

    MathJaxWebView tvQuestion;

    ImageButton imgPlay;
    SeekBar seekBar;

    KeyTakeExam takeExam;
    RecyclerView recyclerView;
    String fromWich;

    List<Paragraph> parentList;
    KeyAudioAdapter adapter;

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    boolean isPlayed = false;
    String uriPath ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        audio_view = inflater.inflate(R.layout.key_exam_audio, container, false);

        initUI();
        return audio_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initUI() {

        tvQuestion = (MathJaxWebView) audio_view.findViewById(R.id.tv_key_audio_question);

        recyclerView = (RecyclerView) audio_view.findViewById(R.id.rv_key_audio);
        seekBar = (SeekBar) audio_view.findViewById(R.id.seekbar_audio_key);
        imgPlay = (ImageButton) audio_view.findViewById(R.id.img_key_audio_play_pause);

        mediaPlayer = new MediaPlayer();

        isPlayed = false;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            takeExam = (KeyTakeExam) bundle.getSerializable("question");
            fromWich = bundle.getString("fromWich");

        }

        parentList = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(takeExam.getAnswers());
            JSONArray userArray = null, correctAnsArray;
            List<String> correctAnsList = new ArrayList<String>();
            List<String> userAnsList = new ArrayList<String>();
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

            for (int i = 0; i < correctAnsArray.length(); i++) {
                jsonObject = correctAnsArray.getJSONObject(i);

                correctAnsList.add(jsonObject.getString("answer"));

            }

            if (userArray != null) {
                for (int i = 0; i < userArray.length(); i++) {
                    jsonObject = userArray.getJSONObject(i);
                    userAnsList.add(jsonObject.getString("answer"));

                }
            }


            adapter = new KeyAudioAdapter(getActivity(), parentList, correctAnsList, userAnsList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        tvQuestion.getSettings().setJavaScriptEnabled(true);
        tvQuestion.setText(takeExam.getQuestion());

        if (takeExam.getQuestion_file() != null) {

            uriPath = Utils.EXAM_TYPE_BASE_URL + takeExam.getQuestion_file(); //update package name
        }


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        seekBar.setOnTouchListener(this);

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

        if (isVisibleToUser) {


            try {
                mediaPlayer.setDataSource(uriPath);
                mediaPlayer.prepare();
                seekBar.setMax(mediaPlayer.getDuration());
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (!isVisibleToUser) {
            if (isPlayed) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.play);
                }
            }

        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
/** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.seekbar_audio_key) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
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


}
