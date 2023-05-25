package ensias.myteam.babytakingcare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ensias.myteam.babytakingcare.Adapters.ConversationAdapter;
import ensias.myteam.babytakingcare.Listener.OnLatestMessageFetchedListener;
import ensias.myteam.babytakingcare.Models.Conversation;
import ensias.myteam.babytakingcare.Models.Message;
import ensias.myteam.babytakingcare.Models.Parent;
import ensias.myteam.babytakingcare.R;

public class ConversationsActivity extends AppCompatActivity   {

    private RecyclerView mRecyclerView;
    private ConversationAdapter conversationAdapter ;
    private List<Conversation> conversations ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        // Initialize the RecyclerView

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("babiesDb").child("messages");
        Query conversationsQuery = messagesRef.orderByChild("lastMessageTimestamp");


        mRecyclerView = findViewById(R.id.listConversation_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        conversations = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(this,conversations);
        mRecyclerView.setAdapter(conversationAdapter);
        int value=0 ;

        FirebaseDatabase.getInstance().getReference("babiesDb").child("messages")
                .orderByChild("lastMessageTimestamp")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         conversations.clear();
                        for (DataSnapshot conversationSnapshot : snapshot.getChildren()) {
                            String conversationId = conversationSnapshot.getKey();
                            DataSnapshot conversationDataSnapshot = conversationSnapshot.child("conversation");
                            Iterable<DataSnapshot> messageSnapshots = conversationDataSnapshot.getChildren();
                            Message lastMessage = null;
                            Long lastMessageTimestamp = conversationSnapshot.child("lastMessageTimestamp").getValue(Long.class);
                            Integer numberMessagesNotViewed = conversationSnapshot.child("unviewed").getValue(Integer.class);

                            for (DataSnapshot messageSnapshot : messageSnapshots) {
                                Message message = messageSnapshot.getValue(Message.class);
                                if (lastMessage == null || message.getTimestamp() > lastMessage.getTimestamp()) {
                                    lastMessage = message;
                                }
                            }
                            if (lastMessage != null) {
                                Conversation conversation = new Conversation();
                                conversation.setConversationId(conversationId);
                                conversation.setNumberMessageNotViewed(numberMessagesNotViewed);
                                conversation.setLastMessage(lastMessage.getBody());
                                conversation.setLastMessageTimestamp(lastMessageTimestamp);
                                getInformationUser(conversationId, new OnUserLoadedListener() {
                                    @Override
                                    public void onUserLoaded(Parent parent) {
                                        conversation.setInfoParent(parent.getFirstName() , parent.getSecondName() , parent.getImage());
                                        conversationAdapter.notifyDataSetChanged();
                                    }
                                });
                                conversations.add(conversation);
                            }
                        }
                        Collections.sort(conversations, new Comparator<Conversation>() {
                            @Override
                            public int compare(Conversation o1, Conversation o2) {
                                return Long.compare(o2.getLastMessageTimestamp(), o1.getLastMessageTimestamp());
                            }
                        });
                        conversationAdapter.setmConversations(conversations);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error here
                    }
                });
    }

    private void getInformationUser(String userId, OnUserLoadedListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("babiesDb").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Parent parent = dataSnapshot.getValue(Parent.class);
                listener.onUserLoaded(parent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur while retrieving the user data
            }
        });
    }

    public interface OnUserLoadedListener {
        void onUserLoaded(Parent parent);
    }




}
