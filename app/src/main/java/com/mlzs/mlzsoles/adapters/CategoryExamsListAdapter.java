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

import com.mlzs.mlzsoles.activities.CheckOutActivity;
import com.mlzs.mlzsoles.activities.ExamInstructionsActivity;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.ExamCategoryList;

import java.util.ArrayList;
import java.util.List;

public class CategoryExamsListAdapter extends RecyclerView.Adapter<CategoryExamsListAdapter.ViewHolder> implements Filterable {


    Context context;
    List<ExamCategoryList> categoryExamsLists;
    List<ExamCategoryList> filteredArrayList;

    public CategoryExamsListAdapter(Context context,List<ExamCategoryList> categoryExamsLists){

        this.context = context;
        this.categoryExamsLists = categoryExamsLists;
        this.filteredArrayList = categoryExamsLists ;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_exams_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final ExamCategoryList categoryExamsList = categoryExamsLists.get(position);

        holder.tvTitle.setText(categoryExamsList.getTitle());
        holder.tvDuration.setText(categoryExamsList.getDueration()+" "+context.getString(R.string.minutes));
        holder.tvTotalQuestions.setText(categoryExamsList.getTotal_questions());

        if(categoryExamsList.getIs_paid().equals("0")){
            holder.tvExamType.setText(context.getString(R.string.free));
            holder.tvExamType.setTextColor(ContextCompat.getColor(context,R.color.green_500));
            holder.rlValidity.setVisibility(View.GONE);
        }
        else if(categoryExamsList.getIs_paid().equals("1")){
            holder.tvExamType.setText(context.getString(R.string.paid));
            holder.tvExamType.setTextColor(ContextCompat.getColor(context,R.color.google_color));

            holder.rlValidity.setVisibility(View.VISIBLE);

            holder.tvValidity.setText(" " + categoryExamsList.getValidity() + " " + context.getString(R.string.days));

            holder.tvCost.setText(" " + categoryExamsList.getCost());
        }

        holder.rlStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(categoryExamsList.getIs_paid().equals("1")){

                    if(categoryExamsList.getIs_purchased().equals("0")){
                        Intent intent = new Intent(context, CheckOutActivity.class);
                        intent.putExtra("id",categoryExamsList.getId());
                        intent.putExtra("sub_name",categoryExamsList.getTitle());
                        intent.putExtra("validity",categoryExamsList.getValidity());
                        intent.putExtra("cost",categoryExamsList.getCost());
                        intent.putExtra("slug",categoryExamsList.getSlug());
                        intent.putExtra("exam_type",categoryExamsList.getExam_type());
                        intent.putExtra("type","exam");
                        context.startActivity(intent);
                    }else {

                        Intent intent = new Intent(context, ExamInstructionsActivity.class);
                        intent.putExtra("start_exam",categoryExamsList.getSlug());
                        intent.putExtra("quiz_id",categoryExamsList.getId());
                        intent.putExtra("exam_type",categoryExamsList.getExam_type());
                        context.startActivity(intent);
                    }

                }else {

                    Intent intent = new Intent(context, ExamInstructionsActivity.class);
                    intent.putExtra("start_exam",categoryExamsList.getSlug());
                    intent.putExtra("quiz_id",categoryExamsList.getId());
                    intent.putExtra("exam_type",categoryExamsList.getExam_type());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryExamsLists.size();
    }

    @Override
    public Filter getFilter() {
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (charString.toString().length() > 0) {
                    ArrayList<ExamCategoryList> filteredItems = new ArrayList<>();

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
                categoryExamsLists = (ArrayList<ExamCategoryList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDuration, tvTotalQuestions, tvExamType, tvCost, tvValidity;
        RelativeLayout rlStartExam, rlValidity;


        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView)itemView.findViewById(R.id.tv_category_exams_list_title);
            tvExamType = (TextView)itemView.findViewById(R.id.tv_category_exams_list_exam_type);
            tvDuration = (TextView)itemView.findViewById(R.id.tv_category_exams_list_duration);
            tvTotalQuestions = (TextView)itemView.findViewById(R.id.tv_category_exams_list_total_qstns);
            rlStartExam = (RelativeLayout)itemView.findViewById(R.id.rl_category_exam_start);

            rlValidity = itemView.findViewById(R.id.rl_validity);
            tvCost = itemView.findViewById(R.id.tv_exam_series_cost);

            tvValidity = itemView.findViewById(R.id.tv_exam_series_validity);
        }
    }
}
