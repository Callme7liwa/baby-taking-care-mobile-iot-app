package ensias.myteam.babytakingcare.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ensias.myteam.babytakingcare.Models.Notification;
import ensias.myteam.babytakingcare.R;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationsList;
    private String notificationType;

    public NotificationsListAdapter(Context context, List<Notification> notificationsList,String notificationType) {
        this.context = context;
        this.notificationsList = notificationsList;
        this.notificationType = notificationType;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notification_view, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        if (notificationsList != null && position < notificationsList.size()) {
            Notification notification = notificationsList.get(position);
            String description = notification.getDescription();
            String date = notification.getDate();
            holder.date.setText(date);
            holder.description.setText(description);
            if(notificationType.equals("temperatures")) {
                holder.title_notification.setText("Temperature Notification");
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.temperature_icon);
                holder.icon.setImageDrawable(drawable);
            }
            else {
                holder.title_notification.setText("Diaper Notification");
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.layer_baby);
                holder.icon.setImageDrawable(drawable);
            }
        }
    }

    public void updateNotifications(List<Notification> notifications) {
        this.notificationsList = notifications;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView date, description , title_notification;
        ImageView icon ;


        public NotificationViewHolder(@Nullable View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.notificationDate);
            description = itemView.findViewById(R.id.notificationDescription);
            title_notification = itemView.findViewById(R.id.notification_title);
            icon = itemView.findViewById(R.id.notification_icon);
        }
    }
}
