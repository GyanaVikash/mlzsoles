package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.getkey.KeyTakeExamActivity;
import com.mlzs.mlzsoles.getkey.KeyTakeExamSectionWiseActivity;
import com.mlzs.mlzsoles.getkey.KeyTakeExamSectionWiseTimeActivity;
import com.mlzs.mlzsoles.model.ExamHistory;

import java.util.List;

public class ExamsHistoryAdapter extends RecyclerView.Adapter<ExamsHistoryAdapter.ViewHolder> {

    List<ExamHistory> examsHistoryList;
    Context context;

    public ExamsHistoryAdapter(Context context, List<ExamHistory> list) {

        this.context = context;
        this.examsHistoryList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exams_history, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ExamHistory examHistory = examsHistoryList.get(position);

        holder.tvTitle.setText(examHistory.getTitle());
        holder.tvMarks.setText(context.getString(R.string.marks)+": " + examHistory.getMarks_obtained() + " / " + examHistory.getTotal_marks());

        if (examHistory.getExam_status().equals("pass")) {
            holder.tvResult.setText(context.getString(R.string.pass));
            holder.tvResult.setBackgroundColor(ContextCompat.getColor(context, R.color.green_500));
        } else {
            holder.tvResult.setText(context.getString(R.string.fail));
            holder.tvResult.setBackgroundColor(ContextCompat.getColor(context, R.color.failure_color));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (examHistory.getExam_type().equals("NSNT")) {
                    Intent intent = new Intent(context, KeyTakeExamActivity.class);
                    intent.putExtra("result_slug",examHistory.getResultsslug());
                    intent.putExtra("sub_name",examHistory.getTitle());
                    context.startActivity(intent);

                } else if (examHistory.getExam_type().equals("SNT")){
                    Intent intent = new Intent(context, KeyTakeExamSectionWiseActivity.class);
                    intent.putExtra("result_slug",examHistory.getResultsslug());
                    intent.putExtra("sub_name",examHistory.getTitle());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, KeyTakeExamSectionWiseTimeActivity.class);
                    intent.putExtra("result_slug",examHistory.getResultsslug());
                    intent.putExtra("sub_name",examHistory.getTitle());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return examsHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvMarks, tvResult;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_row_exams_history_title);
            tvMarks = (TextView) itemView.findViewById(R.id.tv_row_exams_history_marks);
            tvResult = (TextView) itemView.findViewById(R.id.tv_row_exams_history_result);
        }
    }
}
