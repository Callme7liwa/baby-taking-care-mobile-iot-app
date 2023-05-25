package ensias.myteam.babytakingcare;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Adapters.UsersAdapter;
import ensias.myteam.babytakingcare.Models.Parent;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView list_users_rv;
    private UsersAdapter users_adapter;
    private DatabaseReference usersRef;
    private List<Parent> parents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initialisation();

        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && !snapshot.getKey().equals("messages")) {
                    Parent parent = snapshot.getValue(Parent.class);
                    parent.setId(snapshot.getKey());
                    System.out.print("\n the user is  \n " + parent.getId());
                    parents.add(parent);
                    users_adapter.addItem(parent);
                    users_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initialisation() {
        parents = new ArrayList<>();
        usersRef = FirebaseDatabase.getInstance().getReference("babiesDb");
        list_users_rv = findViewById(R.id.listUsers_rv);

        DividerItemDecoration divider = new DividerItemDecoration(list_users_rv.getContext(),DividerItemDecoration.VERTICAL);
        DividerItemDecoration divider2 = new DividerItemDecoration(list_users_rv.getContext(),DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_space));


        list_users_rv.setLayoutManager(new GridLayoutManager(this, 2));
        list_users_rv.addItemDecoration(divider);
        list_users_rv.addItemDecoration(divider2);
        users_adapter = new UsersAdapter(this);
        list_users_rv.setAdapter(users_adapter);
    }
}
