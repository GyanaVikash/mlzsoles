package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mlzs.mlzsoles.model.AnalysisBySubject;

import java.util.List;

public class SpinnerAnalysisSubjectsAdapter extends ArrayAdapter<AnalysisBySubject> {

    private List<AnalysisBySubject> subjectsList;

    public SpinnerAnalysisSubjectsAdapter(Context context, int textViewResourceId, List<AnalysisBySubject> list) {
        super(context, textViewResourceId,list);

        this.subjectsList = list;

    }

    @Override
    public int getCount(){
        return subjectsList.size();
    }

    @Override
    public AnalysisBySubject getItem(int position){
        return subjectsList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(subjectsList.get(position).getSubject_name());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(subjectsList.get(position).getSubject_name());

        return label;
    }
}
