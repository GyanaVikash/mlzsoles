package com.mlzs.mlzsoles.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.mlzs.mlzsoles.utils.AppController;
import com.mlzs.mlzsoles.utils.Utils;

import org.jsoup.Jsoup;

public class BaseFragment extends Fragment {


    public AppController appState;

    public Response.ErrorListener errorListener;

    public SharedPreferences sharedpreferences;
    public SharedPreferences.Editor editor;



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appState = ((AppController) getActivity().getApplicationContext());

        sharedpreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                Utils.dissmisProgress();
            }
        };


    }

   public String htmlToString(String hstmString){

//       String html=(hstmString);
//       html = html.replaceAll("<(.*?)\\>","");
//       html = html.replaceAll("<(.*?)\\\n","");
//       html = html.replaceAll("<(.*?)\\\r","");
//       html = html.replaceFirst("(.*?)\\>", "");
//       html = html.replaceAll("&nbsp;","");
//       html = html.replaceAll("&amp;","");
//       html=html.replaceAll("<h2>","");
//       html=html.replaceAll("<p>","");
//       html=html.replaceAll("</p>","");
//       html=html.replaceAll("</h2>","");

       String text = Jsoup.parse(hstmString).text();

       return text ;
   }

    AlertDialog.Builder builder;
   public void setHint(String hint){

       builder = new AlertDialog.Builder(getActivity());
       builder.setMessage(hint);
       AlertDialog alert = builder.create();
       alert.setTitle("Hint");
       alert.show();
   }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


}
