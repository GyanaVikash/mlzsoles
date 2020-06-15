package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.AnalysisByExam;

import java.util.List;

public class AnalysisByExamAdapter extends RecyclerView.Adapter<AnalysisByExamAdapter.ViewHolder> {

    List<AnalysisByExam> analysisByExamList;
    Context context ;

    public  AnalysisByExamAdapter (Context context,List<AnalysisByExam> analysisByExamList){

        this.context = context ;
        this.analysisByExamList = analysisByExamList;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_analysis_by_exam, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        AnalysisByExam analysisByExam = analysisByExamList.get(position);

        holder.tvTitle.setText(analysisByExam.getTitle());
        holder.tvMarks.setText(analysisByExam.getTotal_marks());
        holder.tvDuration.setText(analysisByExam.getDueration());
        holder.tvAttempts.setText(analysisByExam.getAttempts());

        if(analysisByExam.getIs_paid().equals("0")){
            holder.tvType.setText(context.getString(R.string.free));
           holder.tvType.setTextColor(ContextCompat.getColor(context,R.color.green_500));
        }else {
            holder.tvType.setText(context.getString(R.string.paid));
        holder.tvType.setTextColor(ContextCompat.getColor(context,R.color.google_color));
        }
    }

    @Override
    public int getItemCount() {
        return analysisByExamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvType,tvDuration,tvAttempts,tvMarks;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.row_analysis_title);
            /*
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent = new Intent(context, ExamsHistoryByExamActivity.class);
                   intent.putExtra("exam_name",tvTitle.getText().toString());
                    context.startActivity(intent);
                }
            });
             */
            tvType = (TextView)itemView.findViewById(R.id.row_analysis_type);
            tvDuration = (TextView)itemView.findViewById(R.id.row_analysis_duration);
            tvAttempts = (TextView)itemView.findViewById(R.id.row_analysis_attempts);
            tvMarks = (TextView)itemView.findViewById(R.id.row_analysis_marks);
        }
    }
}
