package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mlzs.mlzsoles.activities.ExamInstructionsActivity;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.ExamSeriesList;

import java.util.ArrayList;
import java.util.List;

public class ExamSeriesListAdapter extends RecyclerView.Adapter<ExamSeriesListAdapter.ViewHolder> implements Filterable{

    Context context;
    List<ExamSeriesList> list;
    List<ExamSeriesList> filteredArrayList;


    public ExamSeriesListAdapter(Context context, List<ExamSeriesList>list){

        this.context=context;
        this.list=list;
        this.filteredArrayList = list ;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_series_type, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final ExamSeriesList examSeries = list.get(position);

        holder.title.setText(examSeries.getTitle());

        holder.tvDuration.setText(examSeries.getDueration());
        holder.tvTotalQuestions.setText(examSeries.getTotal_questions());

        if(examSeries.getIs_paid().equals("1")){
            holder.tvExamType.setText(context.getString(R.string.paid));
            holder.tvExamType.setTextColor(ContextCompat.getColor(context,R.color.green_500));
        }
        else if(examSeries.getIs_paid().equals("0")){
            holder.tvExamType.setText(context.getString(R.string.free));
            holder.tvExamType.setTextColor(ContextCompat.getColor(context,R.color.google_color));
        }

        holder.llTakeExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ExamInstructionsActivity.class);
                intent.putExtra("start_exam",examSeries.getSlug());
                intent.putExtra("quiz_id",examSeries.getId());
                intent.putExtra("exam_type",examSeries.getExam_type());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (charString.toString().length() > 0) {
                    ArrayList<ExamSeriesList> filteredItems = new ArrayList<>();

                    for (int i = 0, l = filteredArrayList.size(); i < l; i++) {
                        String nameList = filteredArrayList.get(i).getTitle();
                        if (nameList.toLowerCase().contains(charString))
                            filteredItems.add(filteredArrayList.get(i));
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                } else {
                    synchronized (this) {
                        result.values = filteredArrayList;
                        result.count = filteredArrayList.size();
                    }
                }
                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<ExamSeriesList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,tvDuration,tvTotalQuestions,tvExamType;
        RelativeLayout llTakeExam;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv_exam_series_title);
            tvExamType = (TextView)itemView.findViewById(R.id.tv_exam_series_exam_type);
            tvDuration = (TextView)itemView.findViewById(R.id.tv_exam_series_duration);
            tvTotalQuestions = (TextView)itemView.findViewById(R.id.tv_exam_series_total_qstns);
            llTakeExam = (RelativeLayout)itemView.findViewById(R.id.rl_exam_series_start);
        }
    }
}
