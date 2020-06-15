package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.Options;

import java.util.List;

public class KeyMultipleChoiceRadioAdapter extends RecyclerView.Adapter<KeyMultipleChoiceRadioAdapter.ViewHolder> {

    Context context;
    List<Options> list;

    String questionID, strCorrectAns,strUserAns;

    int selectAns,correctAns;

    public KeyMultipleChoiceRadioAdapter(Context context, List<Options> optionsList, String questionID, String correctAns,String userAns) {

        this.context = context;
        this.list = optionsList;
        this.questionID = questionID;
        this.strCorrectAns = correctAns;
        this.strUserAns = userAns ;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_multiple_choice, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Options options = list.get(position);


        holder.radioButton.setText(options.getOption_value().replaceAll("\\\\", ""));

        try {
            if(strUserAns!=null){

                selectAns = Integer.parseInt(strUserAns);

                if(position == selectAns-1){
                    holder.radioButton.setChecked(true);
                }
            }else {
                holder.radioButton.setChecked(false);
            }
            correctAns = Integer.parseInt(strCorrectAns);

        } catch(NumberFormatException nfe) {
        }


        if(position == correctAns-1){
            holder.radioButton.setBackgroundColor(ContextCompat.getColor(context,R.color.green_500));
            holder.radioButton.setTextColor(ContextCompat.getColor(context,R.color.white_color));

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);

            radioButton = (RadioButton) itemView.findViewById(R.id.rb_row_multiple_choice_option);

        }
    }


}
