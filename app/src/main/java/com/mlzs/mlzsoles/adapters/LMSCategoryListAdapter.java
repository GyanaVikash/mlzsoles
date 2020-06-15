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
import com.mlzs.mlzsoles.activities.LMSSeriesCategoryListActivity;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.CategoryListLMS;
import com.mlzs.mlzsoles.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LMSCategoryListAdapter extends RecyclerView.Adapter<LMSCategoryListAdapter.ViewHolder> implements Filterable {

    Context context;
    List<CategoryListLMS> categoryLists;
    List<CategoryListLMS> filteredArrayList;

    public LMSCategoryListAdapter(Context context,List<CategoryListLMS> categoryLists){

        this.context = context ;
        this.categoryLists = categoryLists ;
        this.filteredArrayList = categoryLists ;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lms_category_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CategoryListLMS list = categoryLists.get(position);

        holder.name.setText(list.getTitle());


        if(list.getIs_paid().equals("0")){
            holder.lmsType.setText(context.getString(R.string.free));
            holder.lmsType.setTextColor(ContextCompat.getColor(context,R.color.google_color));
        }else {
            holder.lmsType.setText(context.getString(R.string.paid));
            holder.lmsType.setTextColor(ContextCompat.getColor(context,R.color.green_500));
        }


        if(list.getImage()!=null && !list.getImage().equals("")){

            Glide.with(context)
                    .load(Utils.LMS_SERIES_BASE_URL+list.getImage())
                    .into(holder.imageView);
        }else {
            Glide.with(context)
                    .load(Utils.LMS_SERIES_BASE_URL+"exams/categories/default.png")
                    .into(holder.imageView);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(list.getIs_paid().equals("0")){

                    Intent intent = new Intent(context, LMSSeriesCategoryListActivity.class);
                    intent.putExtra("lms_list",list);
                    intent.putExtra("color","lms");
                    context.startActivity(intent);
                }

                else {

                    if(list.getIs_purchased().equals("0")){
                        Intent intent = new Intent(context, CheckOutActivity.class);
                        intent.putExtra("id",list.getId());
                        intent.putExtra("sub_name",list.getTitle());
                        intent.putExtra("validity",list.getValidity());
                        intent.putExtra("cost",list.getCost());
                        intent.putExtra("slug",list.getSlug());
                        intent.putExtra("type", "lms");
                        intent.putExtra("exam_type","lms");
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context,LMSSeriesCategoryListActivity.class);
                        intent.putExtra("lms_list",list);
                        context.startActivity(intent);
                    }

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
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
                categoryLists = (ArrayList<CategoryListLMS>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,lmsType;

        ImageView imageView ;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.tv_lms_series_category_list_name);
            lmsType = (TextView)itemView.findViewById(R.id.tv_lms_type);

            imageView = (ImageView)itemView.findViewById(R.id.img_lms);

        }
    }
}
