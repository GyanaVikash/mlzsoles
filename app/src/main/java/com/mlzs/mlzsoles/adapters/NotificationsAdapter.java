package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.Notifications;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    Context context;
    List<Notifications> notificationsList ;


    public NotificationsAdapter(Context context,List<Notifications> notificationsList){

        this.context = context;
        this.notificationsList = notificationsList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notifications, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final Notifications notifications = notificationsList.get(position);

        holder.title.setText(notifications.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(notifications.getUrl()!=null && !notifications.getUrl().equals("")){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(notifications.getUrl()));
                    context.startActivity(intent);
                }else {

                    Toast.makeText(context, "No resource link found", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,description;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv_notification_title);
            description = (TextView)itemView.findViewById(R.id.tv_notification_description);
        }
    }
}
