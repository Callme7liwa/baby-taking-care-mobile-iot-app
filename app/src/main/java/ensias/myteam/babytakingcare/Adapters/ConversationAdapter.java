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

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private ArrayList<Message> mData;
    private Context context ;

    public ConversationAdapter() {
    }

    public ConversationAdapter(Context context , ArrayList<Message> data) {
        this.context = context ;
        mData = data;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_conversation, null, false);
        return new ConversationViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
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
        notifyItemInserted(mData.size() - 1);
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.message_content);
        }
    }
}
