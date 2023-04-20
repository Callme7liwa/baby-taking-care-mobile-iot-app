package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.UUID;

import ensias.myteam.babytakingcare.Models.Baby;
import ensias.myteam.babytakingcare.Models.Message;

public class ContactUsActivity extends AppCompatActivity {

    private TextView email,message;
    private FloatingActionButton submit ;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        initialisation();
    }

    private void initialisation()
    {
        email = findViewById(R.id.email_contact_us);
        message = findViewById(R.id.message_contact_us);
        submit = findViewById(R.id.submit_send_message);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(ContactUsActivity.this);
                progressDialog.setTitle("Registration");
                progressDialog.setMessage("Please wait while THE baby registered ...");
                progressDialog.show();

                String emailValue = email.getText().toString();
                String messageValue = message.getText().toString();

                if(emailValue.equals("") || messageValue.equals(""))
                    TastyToast.makeText(ContactUsActivity.this, "please fill all the inputs" , TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                else
                    TastyToast.makeText(ContactUsActivity.this, "Your message was submit successfully" , TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                saveMessage(emailValue,messageValue);
                progressDialog.dismiss();
            }
        });
    }

    private void saveMessage(String email , String body)
    {
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = UUID.randomUUID().toString();
        DatabaseReference babiesRef = FirebaseDatabase
                .getInstance()
                .getReference("babiesDb")
                .child("messages")
                .child(userId)
                .child("conversation")
                .child(UUID.randomUUID().toString());
        DatabaseReference timeStampRef = FirebaseDatabase
                .getInstance()
                .getReference("babiesDb")
                .child("messages")
                .child(userId)
                .child("lastMessageTimestamp");
        String timestamp = String.valueOf(System.currentTimeMillis());
        Message message = new Message();
        message.setEmail(email);
        message.setBody(body);
        message.setTimestamp(timestamp);
        babiesRef.setValue(message);
        timeStampRef.setValue(timestamp);
    }
}