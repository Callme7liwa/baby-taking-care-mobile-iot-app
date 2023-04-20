package ensias.myteam.babytakingcare.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ensias.myteam.babytakingcare.Models.Notification;
import ensias.myteam.babytakingcare.R;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationsList;

    public NotificationsListAdapter(Context context, List<Notification> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
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
            String id = notification.getId();
            String description = notification.getDescription();
            String date = notification.getDate();

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            holder.date.setText(date);
            holder.description.setText(description);
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

        TextView date, description;

        public NotificationViewHolder(@Nullable View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.notificationDate);
            description = itemView.findViewById(R.id.notificationDescription);
        }
    }
}
