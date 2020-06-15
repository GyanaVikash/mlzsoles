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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mlzs.mlzsoles.activities.CheckOutActivity;
import com.mlzs.mlzsoles.activities.LMSSeriesCategoryListActivity;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.CategoryListLMS;
import com.mlzs.mlzsoles.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LMSSeriesAdapter extends RecyclerView.Adapter<LMSSeriesAdapter.ViewHolder> implements Filterable {

    List<CategoryListLMS> lmsSeriesList;

    List<CategoryListLMS> filteredArrayList;
    Context context;

    public LMSSeriesAdapter(Context context,List<CategoryListLMS> lmsSeriesList){

        this.context = context;
        this.lmsSeriesList = lmsSeriesList ;
        this.filteredArrayList = lmsSeriesList ;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lms_series, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CategoryListLMS lmsSeries = lmsSeriesList.get(position);

        holder.tvTitle.setText(lmsSeries.getTitle());
        holder.tvItems.setText(lmsSeries.getTotal_items());

        if(lmsSeries.getIs_paid().equals("0")){
            holder.tvType.setText(context.getString(R.string.free));
            holder.tvType.setTextColor(ContextCompat.getColor(context,R.color.google_color));
        }else {
            holder.tvType.setText(context.getString(R.string.paid));
            holder.tvType.setTextColor(ContextCompat.getColor(context,R.color.green_500));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lmsSeries.getIs_paid().equals("0")){

                    Intent intent = new Intent(context, LMSSeriesCategoryListActivity.class);
                    intent.putExtra("lms_list",lmsSeries);
                    context.startActivity(intent);
                }

                else {

                    if(lmsSeries.getIs_purchased().equals("0")){
                        Intent intent = new Intent(context, CheckOutActivity.class);
                        intent.putExtra("id",lmsSeries.getId());
                        intent.putExtra("sub_name",lmsSeries.getTitle());
                        intent.putExtra("validity",lmsSeries.getValidity());
                        intent.putExtra("cost",lmsSeries.getCost());
                        intent.putExtra("slug",lmsSeries.getSlug());
                        intent.putExtra("type", "lms");
                        intent.putExtra("exam_type","lms_series");
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context,LMSSeriesCategoryListActivity.class);
                        intent.putExtra("lms_list",lmsSeries);
                        context.startActivity(intent);
                    }

                }


            }
        });


        if(lmsSeries.getImage()!=null && !lmsSeries.getImage().equals("")){

            Glide.with(context)
                 .load(Utils.LMS_SERIES_BASE_URL+lmsSeries.getImage())
                 .into(holder.imgSeries);
        }else {
            Glide.with(context)
                    .load(Utils.IMAGE_BASE_URL+"lms/categories/default.png")
                    .into(holder.imgSeries);
        }
    }

    @Override
    public int getItemCount() {
        return lmsSeriesList.size();
    }

    @Override
    public Filter getFilter() {
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();


                FilterResults result = new FilterResults();
                if (charString.toString().length() > 0) {
                    ArrayList<CategoryListLMS> filteredItems = new ArrayList<>();

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
                lmsSeriesList = (List<CategoryListLMS>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeries;
        TextView tvTitle,tvItems,tvType;

        RelativeLayout rlNext;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSeries = (ImageView)itemView.findViewById(R.id.img_lms_series);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_lms_series_title);
            tvItems = (TextView)itemView.findViewById(R.id.tv_lms_series_total_items);
            tvType = (TextView)itemView.findViewById(R.id.tv_lms_series_type);

            rlNext = (RelativeLayout)itemView.findViewById(R.id.rl_lms_series_next);
        }
    }



}
