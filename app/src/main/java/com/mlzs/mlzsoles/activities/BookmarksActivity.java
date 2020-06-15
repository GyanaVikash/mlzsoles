package com.mlzs.mlzsoles.activities;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.BookMarksAdapter;
import com.mlzs.mlzsoles.model.BookMarks;
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

public class BookmarksActivity extends BaseActivity {

    Toolbar toolbar;
    ImageView imgBack;
    TextView tvTitle,tvNoBookmarks;
    RecyclerView recyclerView;

    BookMarksAdapter adapter;
    List<BookMarks> bookMarksList ;

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        recyclerView = (RecyclerView)findViewById(R.id.rv_bookmarks);
        tvNoBookmarks = (TextView)findViewById(R.id.tv_no_bookmarks);

        tvTitle.setText(getString(R.string.my_bookmarks));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userID = getUserID();

        getMyBookMArks();
    }


    public void getMyBookMArks(){

        bookMarksList = new ArrayList<>();

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.BOOKMARKS+userID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        String mssg;
                        int status;
                        JSONArray jsonArray;
                        try {
                            status = response.getInt("status");
                            if(status==1){

                               jsonArray = response.getJSONArray("data");

                               for (int i=0;i<jsonArray.length();i++){
                                   Gson gson = new Gson();
                                   Type type = new TypeToken<BookMarks>() {}.getType();
                                   BookMarks myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                   bookMarksList.add(myQuestions);
                               }

                               if(bookMarksList.size()>0){
                                   tvNoBookmarks.setVisibility(View.GONE);
                                   adapter = new BookMarksAdapter(BookmarksActivity.this,bookMarksList,userID);
                                   recyclerView.setAdapter(adapter);
                               }else {
                                   tvNoBookmarks.setVisibility(View.VISIBLE);
                               }



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
