package ensias.myteam.babytakingcare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Models.Message;
import ensias.myteam.babytakingcare.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private ArrayList<Message> mData;
    private Context context ;


    public MessageAdapter(Context context , ArrayList<Message> data) {
        this.context = context ;
        System.out.println("the size is in adapter => " + data.size());
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
        System.out.println("the message is " + message.getBody());
        holder.mTextView.setText(message.getBody());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Message message) {
        mData.add(message);
        System.out.println("the size is avant " + mData.size());
        notifyItemInserted(mData.size() - 1);
        System.out.println("the size is apres " + mData.size());
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.message_content);
        }
    }
}
