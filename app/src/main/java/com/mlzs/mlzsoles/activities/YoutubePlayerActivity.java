package com.mlzs.mlzsoles.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mlzs.mlzsoles.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
 public static  final String API_KEY="AIzaSyA6anb-gbVchCMMJnZeI_G8kM5GkNrJhU4";
   public static  String VIDEO_ID=  "";

    YouTubePlayer Player;
    String filename="youtube";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String str = intent.getStringExtra("url");
        VIDEO_ID=  getVideoURL(str);
        setContentView(R.layout.activity_youtube_player);

        YouTubePlayerView youTubePlayerView=(YouTubePlayerView)findViewById(R.id.youtube_player);
       youTubePlayerView.initialize(API_KEY,this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        SetPlayer(youTubePlayer);
        if(!b){
           youTubePlayer.cueVideo(VIDEO_ID);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
           youTubePlayer.setShowFullscreenButton(false);
            try {
                int lasttime=OpenFile(VIDEO_ID);
                youTubePlayer.loadVideo(VIDEO_ID,lasttime);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            youTubePlayer.play();

        }
        else {
            youTubePlayer.play();
        }
    }
    public void SetPlayer(YouTubePlayer r){
        this.Player=r;
    }
    public YouTubePlayer getPlayer(){
       return this.Player;
    }
    private YouTubePlayer.PlaybackEventListener playbackEventListener=new YouTubePlayer.PlaybackEventListener(){

        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this,"Failer to initialize",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
       int d= getPlayer().getCurrentTimeMillis();
       Log.d("mytag123",String.valueOf(d));
            CreateFile(VIDEO_ID,d);
        getPlayer().pause();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit Lectures");
        dialog.setMessage("Are you sure you want to exit ?");
//        dialog.setNeutralButton("DOWNLOAD THIS VIDEO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
//                intent.putExtra("url", VIDEO_ID);
//                startActivity(intent);
//            }
//        });
//        dialog.setCancelable(false);
        dialog.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.setNegativeButton("KEEP WATCHING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               getPlayer().play();
            }
        }).show();
    }
    public String getVideoURL(String str){
        int findIndex=str.indexOf("embed/");
        int firstIndex=findIndex+6;
        str=str.substring(firstIndex);
        int secondIndex=str.indexOf('"');
        str=str.substring(0,secondIndex);
        return str;
    }
public void CreateFile(String id,int time) {
    String filename = id;
    String fileContents = String.valueOf(time);
    try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE)) {
        fos.write(fileContents.getBytes());
    } catch (FileNotFoundException e) {
        Log.d("myerror",e.toString());
        e.printStackTrace();
    } catch (IOException e) {
        Log.d("myerror",e.toString());
        e.printStackTrace();
    }

}

public int OpenFile(String id) throws FileNotFoundException {
        int time=0;
    String filename = id;
    FileInputStream fis = this.openFileInput(filename);
    InputStreamReader inputStreamReader =
            new InputStreamReader(fis, StandardCharsets.UTF_8);
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
        String line = reader.readLine();
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }
    } catch (IOException e) {
        Log.d("myerror",e.toString());
    } finally {
        String contents = stringBuilder.toString();
        Log.d("mystring",contents);
        time= Integer.parseInt(contents);
    }
    return time;
}
}
