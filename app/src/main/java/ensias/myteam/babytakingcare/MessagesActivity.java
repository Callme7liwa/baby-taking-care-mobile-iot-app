package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Adapters.MessageAdapter;
import ensias.myteam.babytakingcare.Models.Message;

public class MessagesActivity extends AppCompatActivity {

    private DatabaseReference messagesRef;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private ArrayList<Message> messages;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messages = new ArrayList<>();

        String conversationId = getIntent().getStringExtra("conversationID");
        String fullName = getIntent().getStringExtra("fullName");
        String image = getIntent().getStringExtra("image");

        DatabaseReference unviewed_ref = FirebaseDatabase
                .getInstance()
                .getReference("babiesDb")
                .child("messages")
                .child(conversationId)
                .child("unviewed");

        unviewed_ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(0);
                } else if(currentValue > 0){
                    mutableData.setValue(0);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e("FirebaseTransaction", "Transaction failed: " + error.getMessage());
                } else {
                    Log.d("FirebaseTransaction", "Transaction completed successfully.");
                }
            }
        });


        // Récupération de la référence à la base de données
        messagesRef = FirebaseDatabase
                                        .getInstance()
                                        .getReference("babiesDb")
                                        .child("messages")
                                        .child(conversationId)
                                        .child("conversation");

        mRecyclerView = findViewById(R.id.listMessages_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(this, new ArrayList<Message>() , fullName , image);
        mRecyclerView.setAdapter(mAdapter);

        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Récupération du message
                    Message message = dataSnapshot.getValue(Message.class);
                    mAdapter.addItem(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Rien à faire ici
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Rien à faire ici
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Rien à faire ici
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Rien à faire ici
            }
        });
    }
}
