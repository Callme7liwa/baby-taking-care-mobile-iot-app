package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import ensias.myteam.babytakingcare.Models.Parent;

public class UserDetailsActivity extends AppCompatActivity {

    private ImageView back_btn , userImage ;
    private TextView fullName_details , birthday_detail  ,phoneNumber_details , location_details ,
            babiesNumber_details , registeredAt_details , updatedAt_details ;
    //
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    //
    private SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initialisation();
        getUserInfoAuthenticated();
    }

    private void initialisation()
    {
        back_btn = findViewById(R.id.back_btn);
        fullName_details = findViewById(R.id.fullName_details);
        birthday_detail = findViewById(R.id.birthday_details);
        phoneNumber_details = findViewById(R.id.phoneNumber_details);
        location_details = findViewById(R.id.location_details);
        babiesNumber_details = findViewById(R.id.babiesNumber_details);
        registeredAt_details = findViewById(R.id.registeredAt_details);
        updatedAt_details = findViewById(R.id.updatedAt_details);
        userImage = findViewById(R.id.userImage_details);
        //
        auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser() ;
        //
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getUserInfoAuthenticated()
    {
        String id = user.getUid();
        DatabaseReference babiesDbRef = FirebaseDatabase.getInstance().getReference("babiesDb").child(id);

        babiesDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value of the babiesDb element at the specified id
                Parent parent = dataSnapshot.getValue(Parent.class);
                fullName_details.setText(parent.getFirstName() + " " +parent.getSecondName());
                if(parent.getBirthday() != null && !parent.getBirthday().equals(""))
                    birthday_detail.setText(parent.getBirthday());
                else
                    birthday_detail.setText("Not specified");
                if(parent.getPhoneNumber() != null && ! parent.getPhoneNumber().equals(""))
                    phoneNumber_details.setText(parent.getPhoneNumber());
                else
                    phoneNumber_details.setText("Not Specified");
                if(parent.getLocation() != null && !parent.getLocation().equals(""))
                    location_details.setText(parent.getLocation());
                else
                    location_details.setText("Not Specified");
                if(parent.getCreatedOn() != null && !parent.getCreatedOn().equals(""))
                {
                    String date_created = simpleDateFormat.format(new Date(Long.parseLong(parent.getCreatedOn())));
                    registeredAt_details.setText(date_created);
                }
                else
                    registeredAt_details.setText("Not Specified");
                if(updatedAt_details != null && ! updatedAt_details.getText().toString().equals(""))
                {
                    String date_updated = simpleDateFormat.format(new Date(Long.parseLong(parent.getUpdatedOn())));
                    updatedAt_details.setText(date_updated);
                }
                else
                    updatedAt_details.setText("Not Specified");
                if (parent.getImage() != null ) {
                    userImage.setImageURI(Uri.parse(parent.getImage()));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("errors here ");
            }
        });

    }
}