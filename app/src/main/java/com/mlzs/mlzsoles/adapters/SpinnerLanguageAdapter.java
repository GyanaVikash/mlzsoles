package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerLanguageAdapter extends ArrayAdapter<String> {

    private List<String> subjectsList;
    Context context;

    public SpinnerLanguageAdapter(Context context, int textViewResourceId, List<String> list) {
        super(context, textViewResourceId,list);

        this.context = context;
        this.subjectsList = list;

    }

    @Override
    public int getCount(){
        return subjectsList.size();
    }

    @Override
    public String getItem(int position){
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
        label.setText(subjectsList.get(position).toString());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(subjectsList.get(position).toString());

        return label;
    }
}
