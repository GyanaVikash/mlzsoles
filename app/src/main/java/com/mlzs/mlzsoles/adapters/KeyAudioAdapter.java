package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.Paragraph;

import java.util.List;

public class KeyAudioAdapter extends RecyclerView.Adapter<KeyAudioAdapter.ViewHolder> {

    Context context;
    List<Paragraph> parentList;
    List<String> correctAnsList;
    List<String> userAnsList;

    int userAns, correctAns;
    RadioButton currentTextView;

    public KeyAudioAdapter(Context context, List<Paragraph> parentList, List<String> correctAnsList, List<String> userAnsList) {

        this.context = context;
        this.parentList = parentList;
        this.correctAnsList = correctAnsList;
        this.userAnsList = userAnsList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paragraph, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Paragraph paragraph = parentList.get(position);
        holder.textView_parentName.setText(paragraph.getQuestion());

        int noOfChildTextViews = holder.linearLayout_childItems.getChildCount();
        int noOfChild = paragraph.getOptions().size();

        if (noOfChild < noOfChildTextViews) {
            for (int index = noOfChild; index < noOfChildTextViews; index++) {
                RadioButton currentTextView = (RadioButton) holder.linearLayout_childItems.getChildAt(index);
                currentTextView.setVisibility(View.GONE);
            }
        }
        for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {
            currentTextView = (RadioButton) holder.linearLayout_childItems.getChildAt(textViewIndex);
            currentTextView.setClickable(false);

            currentTextView.setText(paragraph.getOptions().get(textViewIndex).toString());

        }

        for (int i = 0; i < correctAnsList.size(); i++) {

            correctAns = Integer.parseInt(correctAnsList.get(i));


            if (position == i) {

                currentTextView = (RadioButton) holder.linearLayout_childItems.getChildAt(correctAns - 1);
                currentTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.green_500));
                currentTextView.setTextColor(ContextCompat.getColor(context, R.color.white_color));

            }

        }

        if(userAnsList.size()>0){
            for (int i = 0; i < userAnsList.size(); i++) {

                userAns = Integer.parseInt(userAnsList.get(i));

                if (position == i) {
                    currentTextView = (RadioButton) holder.linearLayout_childItems.getChildAt(userAns - 1);
                    currentTextView.setChecked(true);

                }

            }
        }else {
            currentTextView.setChecked(false);
        }



    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_parentName;
        private LinearLayout linearLayout_childItems;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_parentName = itemView.findViewById(R.id.tv_parentName);
            linearLayout_childItems = itemView.findViewById(R.id.ll_child_items);

            int intMaxNoOfChild = 0;
            for (int index = 0; index < parentList.size(); index++) {
                int intMaxSizeTemp = parentList.get(index).getOptions().size();
                if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;
            }
            for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {
                RadioButton textView = new RadioButton(context);
                textView.setId(indexView);
                textView.setPadding(0, 20, 10, 20);
                textView.setGravity(Gravity.LEFT);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout_childItems.addView(textView, layoutParams);


            }


        }
    }
}
