package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.BookMarks;
import com.mlzs.mlzsoles.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BookMarksAdapter extends RecyclerView.Adapter<BookMarksAdapter.ViewHolder> {

    Context context;
    List<BookMarks> list ;
    String userID;

    public BookMarksAdapter(Context context,List<BookMarks> list,String userID){

        this.context = context;
        this.list = list;
        this.userID = userID;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book_marks, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final BookMarks bookMarks = list.get(position);

        holder.tvQuestion.setText(Html.fromHtml(bookMarks.getQuestion()));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setCancelable(false);
                dialog.setMessage(context.getString(R.string.confirm_delete_this_qsn) );
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            deleteBookMark(position,bookMarks.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setNegativeButton("NO ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        ImageView imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            tvQuestion = (TextView)itemView.findViewById(R.id.tv_book_marks_question);
            imgDelete=(ImageView)itemView.findViewById(R.id.img_book_marks_delete);
        }
    }

    public void deleteBookMark(final int position, String bookMarkID) throws JSONException {

        Utils.showProgressDialog(context,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.DELETE_BOOK_MARK+bookMarkID, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg;
                        int status;

                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");

                            if(status==1){
                                list.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, mssg+" successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {


        };
        queue.add(jsonObjReq);
    }
}
