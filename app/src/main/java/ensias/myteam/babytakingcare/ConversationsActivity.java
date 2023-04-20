package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ensias.myteam.babytakingcare.Adapters.ConversationAdapter;

public class ConversationsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ConversationAdapter mConversationsAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        // Get a reference to the Firebase database node for conversations
        mDatabase = FirebaseDatabase
                .getInstance().getReference("conversations");

        // Set up the RecyclerView
        mRecyclerView = findViewById(R.id.listConversation_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mConversationsAdapter = new ConversationAdapter();
        mRecyclerView.setAdapter(mConversationsAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Convert the snapshot of conversation data into a list of Conversation objects
                List<Conversation> conversations = new ArrayList<>();
                for (DataSnapshot conversationSnapshot : dataSnapshot.getChildren()) {
                    Conversation conversation = conversationSnapshot.getValue(Conversation.class);
                    conversations.add(conversation);
                }

                // Pass the list of conversations to the adapter to update the UI
                mConversationsAdapter.setConversations(conversations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    }

}