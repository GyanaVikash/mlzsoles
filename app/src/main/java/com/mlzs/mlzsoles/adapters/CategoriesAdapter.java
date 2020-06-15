package com.mlzs.mlzsoles.adapters;


import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mlzs.mlzsoles.activities.CategoryExamsListActivity;
import com.mlzs.mlzsoles.activities.LMSCategoryList;
import com.mlzs.mlzsoles.activities.LMSSeriesCategoryListActivity;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.Categories;
import com.mlzs.mlzsoles.utils.Utils;

import android.widget.Filterable;
import android.widget.Filter;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> implements Filterable{


    Activity context;
    private ArrayList<Categories>categoriesList;
    private ArrayList<Categories>filteredArrayList;
    String fromWhich;

    public CategoriesAdapter(Activity context, ArrayList<Categories> categoriesList, String fromWhich){

        this.context = context ;
        this.categoriesList=categoriesList;
        this.filteredArrayList = categoriesList ;
        this.fromWhich=fromWhich;

    }


    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_categories, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int position) {

        final Categories categories = categoriesList.get(position);

        holder.name.setText(categories.getCategory());

        if(fromWhich.equals("exams")){
            if(categories.getImage()!=null&&!categories.getImage().equals("")){
                Glide.with(context)
                        .load(Utils.IMAGE_BASE_URL+"exams/categories/"+categories.getImage())
                        .into(holder.img);
            }else {
                Glide.with(context)
                        .load(Utils.IMAGE_BASE_URL+"exams/categories/default.png")
                        .into(holder.img);
            }
        }
        else {
            if(categories.getImage()!=null&&!categories.getImage().equals("")){
                Glide.with(context)
                        .load(Utils.IMAGE_BASE_URL+"lms/categories/"+categories.getImage())
                        .into(holder.img);
            }else {
                Glide.with(context)
                        .load(Utils.IMAGE_BASE_URL+"lms/categories/default.png")
                        .into(holder.img);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fromWhich.equals("lms_series")){

                    Intent intent = new Intent(context, LMSSeriesCategoryListActivity.class);
                    intent.putExtra("category",categories);
                    context.startActivity(intent);

                }
                else if(fromWhich.equals("exams")){
                    Intent intent = new Intent(context, CategoryExamsListActivity.class);
                    intent.putExtra("category",categories);
                    context.startActivity(intent);
                    context.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
                else if(fromWhich.equals("lms")){
                    Intent intent = new Intent(context, LMSCategoryList.class);
                    intent.putExtra("category",categories);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (charString.toString().length() > 0) {
                    ArrayList<Categories> filteredItems = new ArrayList<>();

                    for (int i = 0, l = filteredArrayList.size(); i < l; i++) {
                        String nameList = filteredArrayList.get(i).getCategory();
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
                categoriesList = (ArrayList<Categories>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.tv_exam_category_name);
            img = (ImageView)itemView.findViewById(R.id.img_exam_category);
        }
    }


}
