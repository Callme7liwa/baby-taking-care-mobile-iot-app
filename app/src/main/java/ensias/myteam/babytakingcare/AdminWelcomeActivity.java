package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminWelcomeActivity extends AppCompatActivity {
    
    private LinearLayout admin_layout_messageLinearLayout , admin_layout_usersLinearLayout;
    private TextView titleTv , usernameTv ;
    private ImageView imageView ;
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcom);
        initialisation();
    }

    private void initialisation()
    {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser() ;

        titleTv = findViewById(R.id.title_admin_wp);
        usernameTv = findViewById(R.id.username_admin_wp);
        imageView = findViewById(R.id.image_admin_wp);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("babiesDb").child(user.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String username = snapshot.child("firstName").getValue(String.class) + " " + snapshot.child("secondName").getValue(String.class);
                    String imagePath = snapshot.child("image").getValue(String.class);

                    usernameTv.setText(username);
                    if(imagePath != null && !imagePath.isEmpty() )
                        imageView.setImageURI(Uri.parse(imagePath));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        admin_layout_messageLinearLayout = findViewById(R.id.admin_layout_message);
        admin_layout_usersLinearLayout = findViewById(R.id.admin_layout_users);

        admin_layout_messageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminWelcomeActivity.this, ConversationsActivity.class);
                AdminWelcomeActivity.this.startActivity(intent);
            }
        });

        admin_layout_usersLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminWelcomeActivity.this, UsersActivity.class);
                AdminWelcomeActivity.this.startActivity(intent);
            }
        });
    }
}