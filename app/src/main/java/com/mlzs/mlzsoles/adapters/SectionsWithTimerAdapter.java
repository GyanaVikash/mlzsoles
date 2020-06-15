package com.mlzs.mlzsoles.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.activities.TakeExamSectionWiseTimeActivity;
import com.mlzs.mlzsoles.getkey.KeyTakeExamSectionWiseTimeActivity;
import com.mlzs.mlzsoles.model.Sections;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SectionsWithTimerAdapter extends RecyclerView.Adapter<SectionsWithTimerAdapter.ViewHolder> {

    List<Sections> sectionsList ;
    Context context ;

    int row_index=0 ;
    int previousPosition ;
    String fromWhich;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public SectionsWithTimerAdapter(Context context, List<Sections> list,String from){
        this.context=context;
        this.sectionsList=list;
        this.fromWhich =from;
    }

    @Override
    public SectionsWithTimerAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sections_with_timer, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SectionsWithTimerAdapter.ViewHolder holder, final int position) {

        final Sections sections = sectionsList.get(position);

        holder. tvTitle.setText(sections.getSection_name());
        if(row_index==position){


            if(fromWhich.equals("exam")){
                int minutesToMs = Integer.parseInt(sections.getSection_time()) * 60000;
                CountDownTimer countDownTimer = new CountDownTimer(minutesToMs, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished;
                        //Convert milliseconds into hour,minute and seconds
                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        holder.tvTimer.setText(hms);//set text
                    }
                    public void onFinish() {

                        holder. tvTimer.setText("0"); //On finish change timer text
                        sections.setSection_time("0");

                        if(position!=sectionsList.size()-1){
                            startSection(position+1);
                        }
                    }
                }.start();
            }

            else {
                holder.tvTimer.setVisibility(View.GONE);
            }

            holder.tvTitle.setBackgroundColor(ContextCompat.getColor(context,R.color.blue_color));
            holder. tvTitle.setTextColor(ContextCompat.getColor(context,R.color.white_color));

        }
        else
        {
            holder.  tvTitle.setBackgroundColor(ContextCompat.getColor(context,R.color.white_color));
            holder.  tvTitle.setTextColor(ContextCompat.getColor(context,R.color.grey_color));
        }
    }

    @Override
    public int getItemCount() {
        return sectionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvTimer;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView)itemView.findViewById(R.id.tv_row_section_with_time_name);
            tvTimer = (TextView)itemView.findViewById(R.id.tv_row_section_with_time);
        }
    }

    public void startSection(int pos) {


        if(pos>=1){
            previousPosition = pos-1;
        }

        if(fromWhich.equals("exam")){
            if(sectionsList.get(previousPosition).getSection_time().equals("0")){

                if(sectionsList.get(pos).getSection_time().equals("0")){
                    Toast.makeText(context, "This section is already completed", Toast.LENGTH_SHORT).show();
                }else {
                    row_index = pos;
                    notifyDataSetChanged();

                    ((TakeExamSectionWiseTimeActivity)context).sectionSelected(sectionsList.get(pos).getQuestions());

                }
            }
            else {
                Toast.makeText(context, "Present section time is not over", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            row_index = pos;
            notifyDataSetChanged();
            ((KeyTakeExamSectionWiseTimeActivity)context).sectionSelected(sectionsList.get(pos).getQuestions());
        }


    }

}
