package com.mlzs.mlzsoles.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mlzs.mlzsoles.BuildConfig;
import com.mlzs.mlzsoles.R;

public class AboutActivity extends AppCompatActivity {
    TextView app_ver_1,app_ver_2,appVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final Intent intent=new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("https://madhabapatra.github.io");
        intent.setData(data);

 app_ver_1= findViewById(R.id.developer);
 app_ver_2= findViewById(R.id.developer_by);
        appVersion = (TextView) findViewById(R.id.tv_about_version);
        appVersion.setText(getString(R.string.vernsion)+" "+ BuildConfig.VERSION_NAME);
        app_ver_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(intent, "Madhaba Patra "));
            }
        });
        app_ver_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(intent, "Madhaba Patra "));
            }
        });
    }
}
