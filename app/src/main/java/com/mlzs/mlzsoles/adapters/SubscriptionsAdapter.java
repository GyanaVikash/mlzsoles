package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.Subscriptions;
import com.mlzs.mlzsoles.utils.BaseActivity;

import java.util.List;

public class SubscriptionsAdapter extends RecyclerView.Adapter<SubscriptionsAdapter.ViewHolder> {

    Context context;
    List<Subscriptions> subscriptionsList;

    int count = 1;

    public SubscriptionsAdapter(Context context,List<Subscriptions> subscriptionsList){

        this.context=context;
        this.subscriptionsList =subscriptionsList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subscriptions, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Subscriptions subscriptions= subscriptionsList.get(position);

        holder.name.setText(subscriptions.getItem_name());
        holder.paidFrom.setText(subscriptions.getPayment_gateway());
        holder.status.setText(subscriptions.getPayment_status());
        holder.tvPlanType.setText(subscriptions.getPlan_type());

        holder.tvCost.setText(context.getString(R.string.cost)+"-"+subscriptions.getCost()+" ,");
        holder.tvDiscount.setText(context.getString(R.string.after_dis)+"-"+subscriptions.getAfter_discount()+" ,");
        holder.tvPaid.setText(context.getString(R.string.paid)+"-"+subscriptions.getPaid_amount());


        holder.tvTransDate.setText(context.getString(R.string.transaction_date)+subscriptions.getUpdated_at());


        holder.tvTransDate.setText(((BaseActivity)context).dateFormat(subscriptions.getUpdated_at()));


        holder.startDate.setText(((BaseActivity)context).dateFormat(subscriptions.getStart_date()));
        holder.endDate.setText(((BaseActivity)context).dateFormat(subscriptions.getEnd_date()));

        if(subscriptions.getPayment_status().equals("failure")){
            holder.status.setTextColor(ContextCompat.getColor(context,R.color.failure_color));
        }
        else if(subscriptions.getPayment_status().equals("pending")){
            holder.status.setTextColor(ContextCompat.getColor(context,R.color.exams_bg));
        }
        else if(subscriptions.getPayment_status().equals("cancelled")){
            holder.status.setTextColor(ContextCompat.getColor(context,R.color.failure_color));
        }

        else {
            holder.status.setTextColor(ContextCompat.getColor(context,R.color.success_color));
            holder.tvView.setVisibility(View.VISIBLE);
        }

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                if(subscriptions.getPayment_status().equals("success")){
                    if(count%2==0){
                        holder.rlAmounts.setVisibility(View.VISIBLE);
                        holder.llDates.setVisibility(View.VISIBLE);
                        holder.tvView.setText(R.string.view_less);
                    }
                    else {
                        holder.rlAmounts.setVisibility(View.GONE);
                        holder.llDates.setVisibility(View.GONE);
                        holder.tvView.setText(R.string.view);

                    }
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return subscriptionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,startDate,endDate,paidFrom,status,tvCost,tvDiscount,tvPaid,tvTransDate,tvPlanType,tvView;
        LinearLayout llDates ;

        RelativeLayout rlAmounts;
        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.tv_subscriptions_name);

            startDate = (TextView)itemView.findViewById(R.id.tv_subscriptions_from_date);
            endDate = (TextView)itemView.findViewById(R.id.tv_subscriptions_end_date);
            paidFrom = (TextView)itemView.findViewById(R.id.tv_subscriptions_paid_from);
            status = (TextView)itemView.findViewById(R.id.tv_subscriptions_status);

            tvCost = (TextView)itemView.findViewById(R.id.tv_subscriptions_cost);
            tvDiscount = (TextView)itemView.findViewById(R.id.tv_subscriptions_after_discount);
            tvPaid = (TextView)itemView.findViewById(R.id.tv_subscriptions_paid_amount);
            tvTransDate = (TextView)itemView.findViewById(R.id.tv_subsciptions_trans_date);
            tvPlanType = (TextView)itemView.findViewById(R.id.tv_subsciptions_plan_type);


            llDates = itemView.findViewById(R.id.ll_subscriptions_dates);

            rlAmounts = itemView.findViewById(R.id.rl_subscriptions_amounts);

            tvView = itemView.findViewById(R.id.tv_subscriptions_view);
        }
    }
}
