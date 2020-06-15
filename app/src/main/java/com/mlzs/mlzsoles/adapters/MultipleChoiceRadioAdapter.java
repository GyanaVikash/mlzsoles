package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.activities.TakeExamActivity;
import com.mlzs.mlzsoles.activities.TakeExamSectionWiseActivity;
import com.mlzs.mlzsoles.activities.TakeExamSectionWiseTimeActivity;
import com.mlzs.mlzsoles.model.Options;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MultipleChoiceRadioAdapter extends RecyclerView.Adapter<MultipleChoiceRadioAdapter.ViewHolder> {

    Context context;
    List<Options> list;

    private int lastSelectedPosition = -1;

    String questionID, fromWich;

    int selectedLanguage;

    public MultipleChoiceRadioAdapter(Context context, List<Options> optionsList, String questionID, String fromWich, int language) {

        this.context = context;
        this.list = optionsList;
        this.questionID = questionID;

        this.fromWich = fromWich;
        this.selectedLanguage = language;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_multiple_choice, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Options options = list.get(position);

        final int count = position + 1;

        if (selectedLanguage == 0) {
            holder.radioButton.setText(options.getOption_value().replaceAll("\\\\", ""));
        } else {


            try {
                String   name = new String(options.getOptionl2_value().getBytes("UTF-16"), "UTF-16");

                holder.radioButton.setText(name);

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

        }

        holder.radioButton.setChecked(lastSelectedPosition == position);


        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    if (fromWich.equals("NSNT")) {
                       ((TakeExamActivity) context).addRadioAns(questionID, String.valueOf(count));
                    } else if (fromWich.equals("SNT")) {
                        ((TakeExamSectionWiseActivity) context).addRadioAns(questionID, String.valueOf(count));
                    } else if (fromWich.equals("ST")) {
                        ((TakeExamSectionWiseTimeActivity) context).addRadioAns(questionID, String.valueOf(count));
                    }

                }

            }
        });


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


            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });

        }
    }
}
