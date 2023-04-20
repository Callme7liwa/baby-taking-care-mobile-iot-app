package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        // Récupération de la référence à la base de données
        String userId = "7aae99b8-0fb1-465e-9be8-9b0db3acdc6d";
        messagesRef = FirebaseDatabase
                                        .getInstance()
                                        .getReference("babiesDb")
                                        .child("messages")
                                        .child(userId)
                                        .child("conversation");

        //initialisation();

        mRecyclerView = findViewById(R.id.listMessages_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(this, new ArrayList<Message>());
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

    // Écouteur pour les modifications de la base de données
       /* messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the messages from the dataSnapshot object
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                showMessages();
                System.out.println("the size is " + messages.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }*/

    private void initialisation()
    {
        // Configuration de la RecyclerView
        mRecyclerView = findViewById(R.id.listMessages_rv);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setReverseLayout(true); // inverser l'ordre des éléments
        mLayoutManager.setStackFromEnd(true); // empiler les nouveaux éléments en bas
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void showMessages()
    {
        System.out.println("rendered ");
        mAdapter = new MessageAdapter(MessagesActivity.this , messages);
        mRecyclerView.setAdapter(mAdapter);
    }
}
