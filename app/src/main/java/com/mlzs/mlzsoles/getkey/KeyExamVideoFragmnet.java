package com.mlzs.mlzsoles.getkey;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.VideoView;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.KeyVideoAdapter;
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

public class KeyExamVideoFragmnet extends BaseFragment implements View.OnTouchListener,MediaPlayer.OnBufferingUpdateListener{

    View video_view;

    MathJaxWebView tvQuestion;
    ImageView imgPlay;


    KeyTakeExam takeExam ;
    RecyclerView recyclerView;
    String fromWich;

    List<Paragraph> parentList;
    KeyVideoAdapter adapter;
    SeekBar seekBar ;
    VideoView video;
    //private MediaController mediacontroller;
    Uri uri;

    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();

    boolean isPlayed = false ;

    String uriPath ;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        video_view = inflater.inflate(R.layout.key_exam_video,container,false);

        initUI();

        return video_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initUI(){

        tvQuestion = (MathJaxWebView)video_view.findViewById(R.id.tv_key_video_question);

        video = (VideoView)video_view.findViewById(R.id.vv_key_video_key);
        recyclerView = (RecyclerView)video_view.findViewById(R.id.rv_key_video);
        imgPlay = (ImageView)video_view.findViewById(R.id.img_key_video_play);
        seekBar = (SeekBar)video_view.findViewById(R.id.seekbar_video_key);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = this.getArguments();

        if(bundle != null) {

            takeExam =(KeyTakeExam) bundle.getSerializable("question");
            fromWich = bundle.getString("fromWich");

        }

        parentList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(takeExam.getAnswers());
            JSONArray userArray=null,correctAnsArray;
            List<String> correctAnsList = new ArrayList<String>();
            List<String> userAnsList = new ArrayList<String>();
            JSONObject jsonObject ;

            for(int i=0;i<jsonArray.length();i++){
                Gson gson = new Gson();
                Type type = new TypeToken<Paragraph>() {}.getType();
                Paragraph  questions = gson.fromJson(jsonArray.get(i).toString(), type);
                parentList.add(questions);
            }

            if(takeExam.getUser_submitted()!=null){
                userArray = new JSONArray(takeExam.getUser_submitted());
            }
            correctAnsArray = new JSONArray(takeExam.getCorrect_answers());

            for(int i=0;i<correctAnsArray.length();i++){
                jsonObject = correctAnsArray.getJSONObject(i);

                correctAnsList.add(jsonObject.getString("answer"));

            }

            if(userArray!=null){
                for (int i=0;i<userArray.length();i++){
                    jsonObject = userArray.getJSONObject(i);
                    userAnsList.add(jsonObject.getString("answer"));

                }
            }

            adapter = new KeyVideoAdapter(getActivity(),parentList,correctAnsList,userAnsList);
            recyclerView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        tvQuestion.getSettings().setJavaScriptEnabled(true);
        tvQuestion.setText(takeExam.getQuestion());

        if(takeExam.getQuestion_file()!=null){
            uriPath = Utils.EXAM_TYPE_BASE_URL + takeExam.getQuestion_file(); //update package name
            uri = Uri.parse(uriPath);
            video.setVideoURI(uri);
        }


        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

                mediaFileLengthInMilliseconds = video.getDuration();
                seekBar.setMax(video.getDuration());

            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!video.isPlaying()){
                    video.start();
                    imgPlay.setImageResource(R.drawable.pause);
                    isPlayed = true ;
                }else {
                    video.pause();
                    imgPlay.setImageResource(R.drawable.play);
                }

                primarySeekBarProgressUpdater();

            }
        });


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(!isVisibleToUser){

            if(isPlayed){
                if(video.isPlaying()){
                    video.pause();
                }
            }

        }
    }
    

    private void primarySeekBarProgressUpdater() {
        seekBar.setProgress((int) (((float) video.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (video.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.seekbar_video_key) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (video.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                video.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }
}
