package com.mlzs.mlzsoles.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mlzs.mlzsoles.BuildConfig;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.adapters.PopularExamsAdapter;
import com.mlzs.mlzsoles.adapters.PopularLmsAdapter;
import com.mlzs.mlzsoles.model.CategoryListLMS;
import com.mlzs.mlzsoles.model.PopularExams;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mlzs.mlzsoles.webview.DoubtActivity;
import com.mlzs.mlzsoles.webview.StudyMaterialActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private NavigationView navigationView;

    public GoogleSignInClient mGoogleSignInClient;

    ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    NestedScrollView scrollView;
    private Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    RecyclerView popularExams, popularLMS;

    PopularExamsAdapter examsAdapter;
    PopularLmsAdapter lmsAdapter;

    CardView imgExams, imgLms, imgExamsSeries, imgLmsSeries, imgNotifications, imgAnalysis,imgDoubt,imgHistory,imgStudymaterial;

    TextView tvTitle, appVersion, noPopularExams, noPopularLMS;
    CircleImageView userPic;
    String  profilePic;

    List<PopularExams> popularExamsList;
    List<CategoryListLMS> popularLMSList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarColor(this, R.color.light_violet_color);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        scrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);

         /*
            DISABLE POPULAR EXAM AND POPULAR LMS FROM HOME SCREEN
             */
     //   popularExams = (RecyclerView) findViewById(R.id.rv_popular_exams);
      //  popularLMS = (RecyclerView) findViewById(R.id.rv_popular_lms);

        imgExams = (CardView) findViewById(R.id.img_main_exams);
        imgLms = (CardView) findViewById(R.id.img_main_lms);
        imgAnalysis = (CardView) findViewById(R.id.img_main_analysis);
        imgNotifications = (CardView) findViewById(R.id.img_main_notifications);
      //  imgDoubt = (CardView) findViewById(R.id.img_main_doubt);
        imgHistory = (CardView) findViewById(R.id.img_main_exam_history);
        imgLmsSeries = (CardView) findViewById(R.id.img_main_lms_series);
        imgExamsSeries = (CardView) findViewById(R.id.img_main_exams_series);
        imgStudymaterial = (CardView) findViewById(R.id.img_main_study_material);

      //  noPopularExams = findViewById(R.id.tv_no_popular_exams);

      //  noPopularLMS = findViewById(R.id.tv_no_popular_lms);

        appVersion = (TextView) findViewById(R.id.tv_app_version);
        appVersion.setText(getString(R.string.vernsion)+" "+ BuildConfig.VERSION_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.white_color));
/*
            DISABLE POPULAR EXAM AND POPULAR LMS FROM HOME SCREEN

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        popularExams.setLayoutManager(mLayoutManager);
        popularExams.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        popularLMS.setLayoutManager(pLayoutManager);
        popularLMS.setItemAnimator(new DefaultItemAnimator());
*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawer.closeDrawers();
                displayContentView(menuItem.getItemId());
                return true;
            }
        });

        imgExams.setOnClickListener(this);
        imgLms.setOnClickListener(this);
        imgAnalysis.setOnClickListener(this);
        imgNotifications.setOnClickListener(this);
    //    imgDoubt.setOnClickListener(this);
        imgHistory.setOnClickListener(this);
        imgExamsSeries.setOnClickListener(this);
        imgLmsSeries.setOnClickListener(this);
        imgStudymaterial.setOnClickListener(this);

/*
            DISABLE POPULAR EXAM AND POPULAR LMS FROM HOME SCREEN
        if (appState.getNetworkCheck()) {


        //    getPopularRecords();
        } else {
            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }

        GradientDrawable popularExams = (GradientDrawable) noPopularExams.getBackground();
        popularExams.setStroke(2, ContextCompat.getColor(this, R.color.orange_300));


        GradientDrawable popularLMS = (GradientDrawable) noPopularLMS.getBackground();
        popularLMS.setStroke(2, ContextCompat.getColor(this, R.color.blue_color));
*/


    }

    @Override
    protected void onStart() {
        super.onStart();

        initDrawer();

        collapsingToolbarLayout.setTitle(appState.getUserName());

    }


    public void initDrawer() {

        View headerview = navigationView.getHeaderView(0);

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(intent);
                drawer.closeDrawers();
            }
        });


        tvTitle = (TextView) headerview.findViewById(R.id.header_title_name);
        tvTitle.setText(appState.getUserName());
        setTextColorGradient(tvTitle, getResources().getColor(R.color.update_my_profile_primary), getResources().getColor(R.color.update_my_profile));

        userPic = (CircleImageView) headerview.findViewById(R.id.img_header_user_pic);

        if (sharedPreferences.contains("profilePic")) {

            profilePic = sharedPreferences.getString("profilePic", profilePic);

            String imagePath = Utils.USER_PIC_BASE_URL + profilePic;


            if (profilePic != null && !profilePic.equals("null") && !profilePic.equals("")) {
                Glide.with(this)
                        .load(imagePath)
                        .into(userPic);
            } else {
                Glide.with(this)
                        .load(Utils.USER_PIC_BASE_URL + "default.png")
                        .into(userPic);
            }

        } else {

            Glide.with(this)
                    .load(Utils.USER_PIC_BASE_URL + "default.png")
                    .into(userPic);
        }

    }

    private void displayContentView(int id) {

        Intent intent;

        switch (id) {

            case R.id.nav_home :

                if (appState.getNetworkCheck()) {
               //     getPopularRecords();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                }

                break;
/*
Disble setting

            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

 */
            case R.id.nav_examhistory:
                intent = new Intent(this, ExamsHistoryActivity.class);
                startActivity(intent);
                break;
//            case R.id.nav_feedback:
//               // intent = new Intent(this, AddFeedBackActivity.class);
//                intent = new Intent(this, DoubtActivity.class);
//                startActivity(intent);
//                break;
            case R.id.nav_bookmarks:
                intent = new Intent(this, BookmarksActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share_app:
                shareApp();
                break;
            case R.id.nav_subscription:
                intent = new Intent(this, SubscriptionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                showLogoutDialog();
                break;
            case R.id.nav_about_app:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }

    }
    public  void googleSignOut(){

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        // Toast.makeText(ActivityMain.this, "Google Sign Out done.", Toast.LENGTH_SHORT).show();
                        revokeAccess();
                    }
                });
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        // Toast.makeText(ActivityMain.this, "Google access revoked.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showLogoutDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(R.string.want_to_logout);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Toast.makeText(MainActivity.this, getResources().getString(R.string.logged_out_successfully), Toast.LENGTH_SHORT).show();

                appState.logout();

                googleSignOut();

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    public void shareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Menorah OES");
         //   String sAux = "\nLet me recommend you this application\n\n";
            String sAux = "\nMount Litera Zee School Is Providing Video Lectures for Students \n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose one"));
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.img_main_exams:
                intent = new Intent(this, ExamsActivity.class);
                startActivity(intent);
                break;
            case R.id.img_main_lms:
                intent = new Intent(this, LMSActivity.class);
                startActivity(intent);
                break;
            case R.id.img_main_analysis:
                intent = new Intent(this, AnalysisActivity.class);
                startActivity(intent);
                break;
            case R.id.img_main_notifications:
                intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                break;
//            case R.id.img_main_doubt:
//                intent = new Intent(this, DoubtActivity.class);
//                startActivity(intent);
//                break;

            case R.id.img_main_exam_history:
                intent = new Intent(this, ExamsHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.img_main_exams_series:
                intent = new Intent(this, ExamsSeriesActivity.class);
                startActivity(intent);
                break;
            case R.id.img_main_lms_series:
                intent = new Intent(this, LMSSeriesActivity.class);
                startActivity(intent);
                break;
            case R.id.img_main_study_material:
                intent = new Intent(this, StudyMaterialActivity.class);
                startActivity(intent);
                break;

        }

    }

    private long exitTime = 0;


    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public void getPopularRecords() {

        popularExamsList = new ArrayList<>();
        popularLMSList = new ArrayList<>();
        Utils.showProgressDialog(this, "Loggin/Registering...");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.POPULAR_RECORDS + "?user_id=" + getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        final JSONArray jsonArrayExams, jsonArrayLMS;
                        int status;

                        try {
                            status = response.getInt("status");

                            if (status == 1) {
                                jsonArrayExams = response.getJSONArray("exam_records");
                                jsonArrayLMS = response.getJSONArray("lms_records");

                                noPopularExams.setVisibility(View.GONE);
                                noPopularLMS.setVisibility(View.GONE);

                                for (int i = 0; i < jsonArrayExams.length(); i++) {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<PopularExams>() {}.getType();
                                    PopularExams myQuestions = gson.fromJson(jsonArrayExams.get(i).toString(), type);
                                    popularExamsList.add(myQuestions);

                                }

                                for (int i = 0; i < jsonArrayLMS.length(); i++) {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<CategoryListLMS>() {}.getType();
                                    CategoryListLMS myQuestions = gson.fromJson(jsonArrayLMS.get(i).toString(), type);
                                    popularLMSList.add(myQuestions);

                                }

                                if (popularLMSList.size() != 0) {
                                    popularLMS.setVisibility(View.VISIBLE);
                                    lmsAdapter = new PopularLmsAdapter(MainActivity.this, popularLMSList);
                                    popularLMS.setAdapter(lmsAdapter);
                                } else {
                                    popularLMS.setVisibility(View.GONE);
                                    noPopularLMS.setVisibility(View.VISIBLE);
                                }

                                if (popularExamsList.size() != 0) {
                                    popularExams.setVisibility(View.VISIBLE);
                                    examsAdapter = new PopularExamsAdapter(MainActivity.this, popularExamsList);
                                    popularExams.setAdapter(examsAdapter);
                                } else {
                                    noPopularExams.setVisibility(View.VISIBLE);
                                    popularExams.setVisibility(View.GONE);

                                }

                            } else {
                                popularExams.setVisibility(View.GONE);
                                popularLMS.setVisibility(View.GONE);
                                noPopularExams.setVisibility(View.VISIBLE);
                                noPopularLMS.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.dissmisProgress();
    }
}
