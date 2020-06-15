package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class KeyMatchRightAdapter extends RecyclerView.Adapter<KeyMatchRightAdapter.ViewHolder> {

    Context context;
    List<String> list;

    List<String> correctAnsList = new ArrayList<String>();
    List<String> userAnsList = new ArrayList<String>();

    public KeyMatchRightAdapter(Context context, List<String> list,List<String> correctAnsList, List<String> userAnsList){

        this.context = context ;
        this.list = list ;
        this.correctAnsList = correctAnsList;
        this.userAnsList = userAnsList;
    }
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.key_row_match_right, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        try {
            String   name = new String(list.get(position).getBytes("UTF-16"), "UTF-16");

            holder.tvListOptions.setText(name);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        if(userAnsList.size()!=0){
            for(int i=0;i<userAnsList.size();i++){
                if(position==i){
                    holder.edAns.setText(userAnsList.get(i));
                }
            }

        }else {
            holder.edAns.setText("");
        }

        for(int i =0 ;i<correctAnsList.size();i++){
            holder.tvCorrectAns.setText(correctAnsList.get(i));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvListOptions,tvCorrectAns,edAns;

        public ViewHolder(View itemView) {
            super(itemView);

            tvListOptions = (TextView)itemView.findViewById(R.id.tv_key_row_match_right);
            edAns = (TextView) itemView.findViewById(R.id.ed_key_row_match_ans);
            tvCorrectAns = (TextView)itemView.findViewById(R.id.tv_key_row_match_correct_ans);

        }
    }
}
