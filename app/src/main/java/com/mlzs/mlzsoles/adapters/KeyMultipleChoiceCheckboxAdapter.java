package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.Options;

import java.util.ArrayList;
import java.util.List;

public class KeyMultipleChoiceCheckboxAdapter extends RecyclerView.Adapter<KeyMultipleChoiceCheckboxAdapter.ViewHolder> {

    Context context;
    List<Options> optionsList;
    List<String> correctAnsList = new ArrayList<String>();
    List<String> userAnsList = new ArrayList<String>();

    int userAns,correctAns;

    public KeyMultipleChoiceCheckboxAdapter(Context context, List<Options> optionsList, List<String> correctAnsList, List<String> userAnsList) {

        this.context = context;
        this.optionsList = optionsList;
        this.correctAnsList = correctAnsList;
        this.userAnsList = userAnsList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.key_row_multiple_choice, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Options options = optionsList.get(position);


        for(int i=0;i<correctAnsList.size();i++){
            correctAns = Integer.parseInt(correctAnsList.get(i));

            if(position==correctAns-1){
                holder.checkBox.setBackgroundColor(ContextCompat.getColor(context,R.color.green_500));
                holder.checkBox.setTextColor(ContextCompat.getColor(context,R.color.white_color));
            }
        }


        if (userAnsList.size() != 0) {

            for (int i = 0; i < userAnsList.size(); i++) {

                userAns = Integer.parseInt(userAnsList.get(i));

                if(position==userAns-1){
                    holder.checkBox.setChecked(true);
                }
            }

        }

        holder.checkBox.setText(options.getOption_value());

    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.cb_key_row_multiple_choice_cb);

        }
    }
}
