package ensias.myteam.babytakingcare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ensias.myteam.babytakingcare.MessagesActivity;
import ensias.myteam.babytakingcare.Models.Conversation;
import ensias.myteam.babytakingcare.Models.Message;
import ensias.myteam.babytakingcare.R;
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private Context mContext;
    private List<Conversation> mConversations;
    private OnConversationClickListener mConversationClickListener;

    public ConversationAdapter(Context context, List<Conversation> conversations) {
        mContext = context;
        mConversations = conversations;
    }

    public interface OnConversationClickListener {
        void onConversationClick(int position);
    }

    public void setOnConversationClickListener(OnConversationClickListener listener) {
        mConversationClickListener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_layout_conversation, null, false);
        return new ConversationViewHolder(view, mConversationClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = mConversations.get(position);
        holder.mConversationNameTextView.setText(conversation.getFullName());
        holder.mNumberMessageNotViewed.setText(conversation.getNumberMessageNotViewed().toString());
        String message = conversation.getLastMessage();
        if(message.length()>=15)
        holder.mLastMessageTextView.setText(message.substring(0,13)+"...");
        else
        holder.mLastMessageTextView.setText(message);

        if (conversation.getImage() != null && !conversation.getImage().isEmpty()) {
            holder.mParentImageView.setImageURI(Uri.parse(conversation.getImage()));
        }

        Date lastMessageDate = new Date(conversation.getLastMessageTimestamp());

        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayWeek = today.get(Calendar.WEEK_OF_YEAR);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);

        Date currentDate = new Date(todayYear - 1900, todayMonth, todayDay);

        // Check if the conversation happened on the same day as the current date
        if (lastMessageDate.getYear() == currentDate.getYear() && lastMessageDate.getMonth() == currentDate.getMonth() && lastMessageDate.getDay() == currentDate.getDay()) {
            holder.mTimestampTextView.setText(DateFormat.getTimeInstance().format(lastMessageDate));
        } else {
            // Get message date
            Date messageDate = new Date(conversation.getLastMessageTimestamp());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(messageDate);

            int messageYear = calendar.get(Calendar.YEAR);
            int messageMonth = calendar.get(Calendar.MONTH);
            int messageWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            int messageDay = calendar.get(Calendar.DAY_OF_MONTH);

            // Get today's date

            String messageDateStr;
            if (messageYear == todayYear && messageMonth == todayMonth && messageWeek == todayWeek) {
                // If the message was sent this week, show the day of the week
                messageDateStr = new SimpleDateFormat("EEEE").format(messageDate);
            } else if (messageYear == todayYear && messageMonth == todayMonth) {
                // If the message was sent this month, show the number of days ago
                int daysAgo = todayDay - messageDay;
                if (daysAgo == 1) {
                    messageDateStr = "Yesterday";
                } else {
                    messageDateStr = daysAgo + " days ago";
                }
            } else {
                // Otherwise, show the date in the format "Month Day, Year"
                messageDateStr = new SimpleDateFormat("MMM d, yyyy").format(messageDate);
            }

            holder.mTimestampTextView.setText(messageDateStr);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessagesActivity.class);
                intent.putExtra("conversationID", conversation.getConversationId());
                intent.putExtra("fullName", conversation.getFullName());
                intent.putExtra("image", conversation.getImage());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    public List<Conversation> getmConversations() {
        return mConversations;
    }

    public void setmConversations(List<Conversation> conversations) {
        this.mConversations = conversations;
        notifyDataSetChanged();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mConversationNameTextView;
        public TextView mLastMessageTextView;
        public TextView mNumberMessageNotViewed ;
        public TextView mTimestampTextView;
        public ImageView mParentImageView ;
        private OnConversationClickListener mConversationClickListener;

        public ConversationViewHolder(@NonNull View itemView, OnConversationClickListener listener) {
            super(itemView);
            mConversationNameTextView = itemView.findViewById(R.id.conversation_username);
            mLastMessageTextView = itemView.findViewById(R.id.conversation_lastMessage);
            mTimestampTextView = itemView.findViewById(R.id.conversation_lastMessage_timestamp);
            mParentImageView = itemView.findViewById(R.id.conversation_parent_image);
            mNumberMessageNotViewed = itemView.findViewById(R.id.conversation_number_messages);
            mConversationClickListener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(Conversation conversation) {
           // System.out.println("conv +=> "+conversation.getParent().getFirstName());
          //  mConversationNameTextView.setText(conversation.getParent().getFirstName() + " " + conversation.getParent().getSecondName());
            mLastMessageTextView.setText(conversation.getLastMessage());
            String timestamp = DateFormat.getDateTimeInstance().format(new Date(conversation.getLastMessageTimestamp()));
            mTimestampTextView.setText(timestamp);
        }

        @Override
        public void onClick(View v) {
            if (mConversationClickListener != null) {
                System.out.println("clicked");
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mConversationClickListener.onConversationClick(position);
                }
            }
        }
    }
}

