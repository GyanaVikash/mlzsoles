package com.mlzs.mlzsoles.activities;


import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.LMSSeriesCategoryListAdapter;
import com.mlzs.mlzsoles.model.CategoryListLMS;
import com.mlzs.mlzsoles.model.LMSSeriesCategoryList;
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

public class LMSSeriesCategoryListActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    TextView noSeries,tvTitle;
    ImageView imgBack;
    Toolbar toolbar;
    VideoView videoView;
    RecyclerView recyclerView;
    RelativeLayout rlVideoView;

    List<LMSSeriesCategoryList> lmsSeriesCategoryLists;
    LMSSeriesCategoryListAdapter adapter;

    MediaController mediaController ;
    Uri uri ;

    CategoryListLMS lmsList;


    String color ;

    LinearLayout llAudio;
    ImageButton imgPlay;
    SeekBar seekBar ;

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds
    private final Handler handler = new Handler();

    SearchView searchView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmsseries_category_list);

        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        rlVideoView = (RelativeLayout)findViewById(R.id.rl_video_view);
        videoView = (VideoView)findViewById(R.id.vv_lms_series);
        noSeries = (TextView)findViewById(R.id.tv_no_lms_series_category_list);
        recyclerView = (RecyclerView)findViewById(R.id.rv_lms_series_category_list);
        llAudio = (LinearLayout)findViewById(R.id.ll_type_audio);
        imgPlay = (ImageButton)findViewById(R.id.img_audio_play_pause);
        seekBar = (SeekBar)findViewById(R.id.SeekBarTestPlay);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if(getIntent().getSerializableExtra("lms_list")!=null){

            lmsList = (CategoryListLMS)getIntent().getSerializableExtra("lms_list");

        }

        tvTitle.setText(lmsList.getTitle());


        if(appState.getNetworkCheck()){
            getSeriesData();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        seekBar.setMax(99); // It means 100% .0-99
        seekBar.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        setSupportActionBar(toolbar);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void getSeriesData(){

        lmsSeriesCategoryLists = new ArrayList<>();
        Utils.showProgressDialog(this,"");
        Utils.showProgress();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_LMS_SERIES_LIST+lmsList.getSlug()+"?user_id="+getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        String mssg;
                        int status;
                        final JSONArray jsonArray ;
                        try {
                            status = response.getInt("status");
                            if(status==1){
                                jsonArray    = response.getJSONArray("list");
                                noSeries.setVisibility(View.GONE);

                                for (int i=0;i<jsonArray.length();i++){
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<LMSSeriesCategoryList>() {}.getType();
                                    LMSSeriesCategoryList myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    lmsSeriesCategoryLists.add(myQuestions);

                                }

                                if(lmsSeriesCategoryLists.size()!=0){
                                    adapter = new LMSSeriesCategoryListAdapter(LMSSeriesCategoryListActivity.this,lmsSeriesCategoryLists);
                                    recyclerView.setAdapter(adapter);
                                }else {

                                    noSeries.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }

                            }else {
                                mssg = response.getString("message");
                                noSeries.setVisibility(View.VISIBLE);
                                noSeries.setText(mssg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);


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

    public void playVideo(String string){

        if(mediaPlayer.isPlaying()){

            mediaPlayer.stop();
            llAudio.setVisibility(View.GONE);

        }

        rlVideoView.setVisibility(View.VISIBLE);

        uri  = Uri.parse(Utils.FILE_BASE_URL+string) ;
        videoView.setVideoURI(uri);
        videoView.start();
    }

    public void playAudio(String strPath){

        if(videoView.isPlaying()){

            videoView.pause();
            rlVideoView.setVisibility(View.GONE);

        }

        llAudio.setVisibility(View.VISIBLE);


        try {
            mediaPlayer.setDataSource(Utils.FILE_BASE_URL+strPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

        imgPlay.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        rlVideoView.setVisibility(View.GONE);
        llAudio.setVisibility(View.GONE);

        if(videoView.isPlaying()){
            videoView.pause();
        }

        llAudio.setVisibility(View.GONE);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.img_audio_play_pause){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                imgPlay.setImageResource(R.drawable.play);
            }else if(!mediaPlayer.isPlaying()){
                imgPlay.setImageResource(R.drawable.pause);
                mediaPlayer.start();
            }

            primarySeekBarProgressUpdater();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.SeekBarTestPlay) {
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if(lmsSeriesCategoryLists.size()!=0){
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed

                if(lmsSeriesCategoryLists.size()!=0){
                    adapter.getFilter().filter(query);
                }

                return false;
            }
        });
        return true;
    }
}
