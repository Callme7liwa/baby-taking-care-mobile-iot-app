package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInfoActivity extends AppCompatActivity {

    private TextView emailTv , phoneTv  , babiesNumberTv , registeredAtTv , updatedAtTv , usernameTv ;
    private ImageView imageView ;
    private SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        String userId = getIntent().getStringExtra("userId");
        initialisation();
        getInformationUser(userId);
    }

    private  void initialisation()
    {
        usernameTv = findViewById(R.id.username_tv);
        emailTv = findViewById(R.id.email_tv);
        phoneTv = findViewById(R.id.phone_tv);
        babiesNumberTv = findViewById(R.id.babiesNumber_tv);
        registeredAtTv = findViewById(R.id.registeredAt_tv);
        updatedAtTv = findViewById(R.id.updatedAt_tv);
        imageView  = findViewById(R.id.image_user);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    private void getInformationUser(String userId)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("babiesDb").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String username = snapshot.child("firstName").getValue(String.class) + " " + snapshot.child("secondName").getValue(String.class);
                    long babiesNumber = snapshot.child("babies").getChildrenCount();

                    String createdOnValue = snapshot.child("createdOn").getValue(String.class);
                    String registeredAt = null;
                    if (createdOnValue != null) {
                        registeredAt = simpleDateFormat.format(new Date(Long.parseLong(createdOnValue)));
                    }

                    String updatedOnValue = snapshot.child("createdOn").getValue(String.class);
                    String updatedAt = null;
                    if (createdOnValue != null) {
                        updatedAt = simpleDateFormat.format(new Date(Long.parseLong(updatedOnValue)));
                    }

                    String imagePath = snapshot.child("image").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    usernameTv.setText(username);
                    emailTv.setText(email);
                    babiesNumberTv.setText(""+babiesNumber);
                    registeredAtTv.setText(registeredAt);
                    updatedAtTv.setText(updatedAt);

                    if(imagePath != null && !imagePath.isEmpty() )
                        imageView.setImageURI(Uri.parse(imagePath));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });    }
}