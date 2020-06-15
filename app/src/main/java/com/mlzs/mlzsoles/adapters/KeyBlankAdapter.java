package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;

import java.util.List;

public class KeyBlankAdapter extends RecyclerView.Adapter<KeyBlankAdapter.ViewHolder> {

    List<String> correctAns;
    List<String> userAns;
    Context context ;


    public KeyBlankAdapter (Context context,List<String>correctAns,   List<String> userAns){

        this.context = context;
        this.correctAns = correctAns ;
        this.userAns = userAns ;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_blank, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        int index = position + 1;

        holder.tvCorrectAns.setText(" "+ index +" "+ context.getString(R.string.correct_ans)+correctAns.get(position).toString());
        holder.tvIndex.setText(" "+ index + " ");

        for(int i=0 ; i<userAns.size();i++){


            if(position == i){
                holder.edBlanAns.setText(userAns.get(i).toString());
            }

        }

    }

    @Override
    public int getItemCount() {
        return correctAns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText edBlanAns;
        TextView tvCorrectAns,tvIndex;

        public ViewHolder(View itemView) {
            super(itemView);

            edBlanAns = (EditText)itemView.findViewById(R.id.ed_row_blank_ans);
            tvCorrectAns = (TextView)itemView.findViewById(R.id.tv_row_blank_correct_ans);
            tvIndex = (TextView)itemView.findViewById(R.id.tv_blanck_index);

            tvCorrectAns.setVisibility(View.VISIBLE);
            edBlanAns.setEnabled(false);
            edBlanAns.setClickable(false);

            edBlanAns.setHint("ans");
        }
    }
}
