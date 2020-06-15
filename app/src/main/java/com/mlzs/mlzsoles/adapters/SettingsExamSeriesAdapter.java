package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.activities.SettingsActivity;
import com.mlzs.mlzsoles.model.Settings;

import java.util.List;

public class SettingsExamSeriesAdapter extends RecyclerView.Adapter<SettingsExamSeriesAdapter.ViewHolder> {

    Context context;
    List<Settings> list;

    List<String> selectedExamsList;

    public SettingsExamSeriesAdapter(Context context,List<Settings> list,List<String> selectedExamsList){

        this.context = context;
        this.list =list;
        this.selectedExamsList = selectedExamsList;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_settings, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final Settings settings = list.get(position);

        holder.seriesName.setText(settings.getCategory());

        if(selectedExamsList.size()!=0){
            for(int i=0;i<selectedExamsList.size();i++){

                if(selectedExamsList.get(i).equals(list.get(position).getId())){
                    holder.swChecked.setChecked(true);
                    ((SettingsActivity)context).updateExamSettings(settings.getId(),"add");
                }

            }
        }

        holder.swChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    ((SettingsActivity)context).updateExamSettings(settings.getId(),"add");

                }else {
                    ((SettingsActivity)context).updateExamSettings(settings.getId(),"remove");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView seriesName;
        Switch swChecked;
        public ViewHolder(View itemView) {
            super(itemView);

            seriesName = (TextView)itemView.findViewById(R.id.tv_settings_series_name);
            swChecked = (Switch)itemView.findViewById(R.id.switch_settings);
        }
    }
}
