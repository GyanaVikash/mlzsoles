package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlzs.mlzsoles.activities.YoutubePlayerActivity;
import com.mlzs.mlzsoles.activities.LMSSeriesCategoryListActivity;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.LMSSeriesCategoryList;
import com.mlzs.mlzsoles.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LMSSeriesCategoryListAdapter extends RecyclerView.Adapter<LMSSeriesCategoryListAdapter.ViewHolder> implements Filterable {

    Context context ;
    List<LMSSeriesCategoryList> lmsSeriesCategoryLists;
    List<LMSSeriesCategoryList> filteredArrayList;


    public LMSSeriesCategoryListAdapter (Context context,List<LMSSeriesCategoryList> lmsSeriesCategoryLists){

        this.context = context;
        this.lmsSeriesCategoryLists = lmsSeriesCategoryLists;
        this.filteredArrayList = lmsSeriesCategoryLists ;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lms_series_category_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final LMSSeriesCategoryList lmsSeriesCategoryList = lmsSeriesCategoryLists.get(position);

        holder.title.setText(lmsSeriesCategoryList.getTitle());


        if(lmsSeriesCategoryList.getContent_type().equals("video")){

            holder.type.setImageResource(R.drawable.video);
        }
        else if(lmsSeriesCategoryList.getContent_type().equals("file")){
            holder.type.setImageResource(R.drawable.download);
        }
        else if(lmsSeriesCategoryList.getContent_type().equals("url")){
            holder.type.setImageResource(R.drawable.url);
        }
        else if(lmsSeriesCategoryList.getContent_type().equals("iframe")){
            holder.type.setImageResource(R.drawable.youtube);
        }
        else if(lmsSeriesCategoryList.getContent_type().equals("video_url")){
            holder.type.setImageResource(R.drawable.youtube);
        }
        else if(lmsSeriesCategoryList.getContent_type().equals("audio_url")){
            holder.type.setImageResource(R.drawable.youtube);
        }
        else if(lmsSeriesCategoryList.getContent_type().equals("audio")){
            holder.type.setImageResource(R.drawable.audio);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lmsSeriesCategoryList.getContent_type().equals("video")){
                    ((LMSSeriesCategoryListActivity)context).playVideo(lmsSeriesCategoryList.getFile_path());
                }
                else if(lmsSeriesCategoryList.getContent_type().equals("url")){

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(lmsSeriesCategoryList.getFile_path()));
                    context.startActivity(intent);

                }
                else if(lmsSeriesCategoryList.getContent_type().equals("file")){
                    context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.FILE_BASE_URL+lmsSeriesCategoryList.getFile_path())));
                }
                else if(lmsSeriesCategoryList.getContent_type().equals("iframe")){
                    String iframeurl = lmsSeriesCategoryList.getFile_path();

                 Intent intent = new Intent(context, YoutubePlayerActivity.class);
                    intent.putExtra("url", iframeurl);
                    context.startActivity(intent);

                }
                else if(lmsSeriesCategoryList.getContent_type().equals("audio")){
                    ((LMSSeriesCategoryListActivity)context).playAudio(lmsSeriesCategoryList.getFile_path());
                }

                else if(lmsSeriesCategoryList.getContent_type().equals("video_url")){

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(lmsSeriesCategoryList.getFile_path()));
                    context.startActivity(intent);

                }
                else if(lmsSeriesCategoryList.getContent_type().equals("audio_url")){

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(lmsSeriesCategoryList.getFile_path()));
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return lmsSeriesCategoryLists.size();
    }

    @Override
    public Filter getFilter() {
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (charString.toString().length() > 0) {
                    ArrayList<LMSSeriesCategoryList> filteredItems = new ArrayList<>();

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
                lmsSeriesCategoryLists = (ArrayList<LMSSeriesCategoryList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView type ;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv_lms_series_category_list_title);
            type = (ImageView)itemView.findViewById(R.id.img_lms_series_category_list_type);
        }
    }
}
