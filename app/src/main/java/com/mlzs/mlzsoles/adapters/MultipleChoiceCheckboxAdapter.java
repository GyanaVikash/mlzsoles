package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.fragments.ExamCheckBoxFragment;
import com.mlzs.mlzsoles.model.Options;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MultipleChoiceCheckboxAdapter extends RecyclerView.Adapter<MultipleChoiceCheckboxAdapter.ViewHolder> {

    Context context;
    List<Options> optionsList;

    ExamCheckBoxFragment fragment;

    int languageSelected = 0 ;

    public MultipleChoiceCheckboxAdapter(Context context, List<Options> optionsList,ExamCheckBoxFragment fragment,int language){

        this.context=context;
        this.optionsList=optionsList;
        this.fragment = fragment ;

        this.languageSelected = language;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_multiple_choice_checkbox, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Options options = optionsList.get(position);

        final int count = position+1;

        if(languageSelected==0){
            holder.checkBox.setText(options.getOption_value());
        }else {
            try {
                String   name = new String(options.getOptionl2_value().getBytes("UTF-16"), "UTF-16");

                holder.checkBox.setText(name);

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fragment.addCheckBoxValues(String.valueOf(count),"add");
                }
                else {
                    fragment.addCheckBoxValues(String.valueOf(count),"remove");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox)itemView.findViewById(R.id.cb_row_multiple_choice_cb);

        }
    }
}
