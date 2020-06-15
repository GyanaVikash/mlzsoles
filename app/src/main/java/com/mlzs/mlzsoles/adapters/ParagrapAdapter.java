package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.fragments.ExamParagraphFragment;
import com.mlzs.mlzsoles.model.Paragraph;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ParagrapAdapter extends RecyclerView.Adapter<ParagrapAdapter.ViewHolder> {


    Context context;
    List<Paragraph> parentList;

    ExamParagraphFragment fragment;
    int languageSelected = 0;


    public ParagrapAdapter(Context context,List<Paragraph> parentList,ExamParagraphFragment fragment,int language){

        this.context = context;
        this.parentList = parentList ;
        this.fragment = fragment ;
        this.languageSelected = language ;
    }


    @Override
    public ParagrapAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paragraph, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ParagrapAdapter.ViewHolder holder, final int position) {

        Paragraph paragraph = parentList.get(position);


        if(languageSelected==0){
            holder.textView_parentName.setText(paragraph.getQuestion());
        }
        else {

            if(paragraph.getOptionsl2()!=null&&!paragraph.getOptionsl2().equals("")){
                try {
                    String   name = new String(paragraph.getQuestionl2().getBytes("UTF-16"), "UTF-16");

                    holder.textView_parentName.setText("Q : "+name);

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
            }else {
                holder.textView_parentName.setText("Q : "+paragraph.getQuestion());
            }


        }

        int noOfChildTextViews = holder.linearLayout_childItems.getChildCount();
        int noOfChild = paragraph.getOptions().size();

        if (noOfChild < noOfChildTextViews) {
            for (int index = noOfChild; index < noOfChildTextViews; index++) {
                RadioButton currentTextView = (RadioButton) holder.linearLayout_childItems.getChildAt(index);
                currentTextView.setVisibility(View.GONE);
            }
        }
        for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {

            final RadioButton currentTextView = (RadioButton) holder.linearLayout_childItems.getChildAt(textViewIndex);

            if(languageSelected==0){
                currentTextView.setText(paragraph.getOptions().get(textViewIndex).toString());
            }else {
                try {

                    if(paragraph.getOptionsl2()!=null&&!paragraph.getOptionsl2().equals("")){
                        String   name = new String(paragraph.getOptionsl2().get(textViewIndex).toString().getBytes("UTF-16"), "UTF-16");

                        currentTextView.setText(name);

                    }else {
                        currentTextView.setText(paragraph.getOptions().get(textViewIndex).toString());
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
            }


            final  int pos = textViewIndex+1;


            currentTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        fragment.addParagraphAns(String.valueOf(pos),"add",position);
                    }
                    else {
                        fragment.addParagraphAns(String.valueOf(pos),"remove",position);
                    }
                }
            });


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
                textView.setPadding(0, 20, 0, 20);
                textView.setGravity(Gravity.LEFT);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                linearLayout_childItems.addView(textView, layoutParams);



            }


        }
    }
}
