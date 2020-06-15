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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mlzs.mlzsoles.activities.CheckOutActivity;
import com.mlzs.mlzsoles.activities.ExamsSeriesList;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.ExamsSeries;
import com.mlzs.mlzsoles.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ExamSeriesAdapter extends RecyclerView.Adapter<ExamSeriesAdapter.ViewHolder> implements Filterable {

    Context context;
    List<ExamsSeries> examsSeriesList;
    List<ExamsSeries> filteredArrayList;

    public ExamSeriesAdapter(Context context,List<ExamsSeries> examsSeries){

        this.context = context;
        this.examsSeriesList = examsSeries;
        this.filteredArrayList = examsSeries ;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_series, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final ExamsSeries examsSeries = examsSeriesList.get(position);


        holder.tvTitle.setText(examsSeries.getTitle());
        holder.tvQuizes.setText(examsSeries.getTotal_exams()+context.getString(R.string.quizes));
        holder.tvQuestions.setText(examsSeries.getTotal_questions()+context.getString(R.string.questions));

        if(examsSeries.getIs_paid().equals("0")){
            holder.tvSeriesType.setText(context.getString(R.string.free));
            holder.tvSeriesType.setTextColor(ContextCompat.getColor(context,R.color.google_color));
        }else {
            holder.tvSeriesType.setText(context.getString(R.string.premium));
            holder.tvSeriesType.setTextColor(ContextCompat.getColor(context,R.color.green_500));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(examsSeries.getIs_paid().equals("1")){

                    if(examsSeries.getIs_purchased().equals("0")){
                        Intent intent = new Intent(context, CheckOutActivity.class);
                        intent.putExtra("id",examsSeries.getId());
                        intent.putExtra("sub_name",examsSeries.getTitle());
                        intent.putExtra("validity",examsSeries.getValidity());
                        intent.putExtra("cost",examsSeries.getCost());
                        intent.putExtra("slug",examsSeries.getSlug());
                        intent.putExtra("type", "combo");
                        intent.putExtra("exam_type","exam_series");
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context,ExamsSeriesList.class);
                        intent.putExtra("exam_series_list",examsSeries);
                        context.startActivity(intent);
                    }

                }else {
                    Intent intent = new Intent(context,ExamsSeriesList.class);
                    intent.putExtra("exam_series_list",examsSeries);
                    context.startActivity(intent);
                }

            }
        });

        if(examsSeries.getImage()!=null && !examsSeries.getImage().equals("")){

            Glide.with(context)
                 .load(Utils.EXAM_SERIES_BASE_URL+examsSeries.getImage())
                 .into(holder.imgSeries);
        }else {
            Glide.with(context)
                    .load(Utils.IMAGE_BASE_URL+"exams/categories/default.png")
                    .into(holder.imgSeries);
        }

    }

    @Override
    public int getItemCount() {
        return examsSeriesList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (charString.toString().length() > 0) {
                    ArrayList<ExamsSeries> filteredItems = new ArrayList<>();

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
                examsSeriesList = (ArrayList<ExamsSeries>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeries;

        TextView tvTitle,tvSeriesType,tvQuestions,tvQuizes;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSeries = (ImageView)itemView.findViewById(R.id.img_exams_series);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_exams_series_title);
            tvSeriesType = (TextView)itemView.findViewById(R.id.tv_exams_series_type);
            tvQuestions = (TextView)itemView.findViewById(R.id.tv_exams_series_questions);
            tvQuizes = (TextView)itemView.findViewById(R.id.tv_exams_series_total_quizes);
        }
    }
}
