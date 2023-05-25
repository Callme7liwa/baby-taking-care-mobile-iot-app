package ensias.myteam.babytakingcare.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Models.Message;
import ensias.myteam.babytakingcare.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private ArrayList<Message> mData;
    private String fullName ;
    private String image ;
    private Context context ;


    public MessageAdapter(Context context , ArrayList<Message> data , String fullName , String image ) {
        this.context = context ;
        this.fullName = fullName ;
        this.image = image ;
        mData = data;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout, null, false);
        return new MessageViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mData.get( position);
        holder.messageTextView.setText(message.getBody());
        holder.timestampTextView.setText(message.getTimestamp().toString());
        if (image != null && !image.isEmpty()) {
            holder.imageView.setImageURI(Uri.parse(image));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Message message) {
        mData.add(message);
        notifyItemInserted(mData.size() - 1);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView , timestampTextView;
        public ImageView imageView ;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_content);
            timestampTextView = itemView.findViewById(R.id.message_time_stamp);
            imageView = itemView.findViewById(R.id.message_image_user);
        }
    }
}
