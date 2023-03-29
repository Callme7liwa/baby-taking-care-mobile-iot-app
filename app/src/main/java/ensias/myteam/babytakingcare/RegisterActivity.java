package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdsmdg.tastytoast.TastyToast;

import ensias.myteam.babytakingcare.Models.Parent;

public class RegisterActivity extends AppCompatActivity {

    private TextView firstName , secondName , email , password , passwordConfirmation ;
    private String firstNameValue , secondNameValue , emailValue , passwordValue , passwordConfirmationValue ;
    private AppCompatButton buttonRegister ;

    private FirebaseAuth auth ;
    private FirebaseUser user ;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialisation();
    }

    private void initialisation()
    {
        firstName = findViewById(R.id.text_firstName_register);
        secondName  = findViewById(R.id.text_secondName_register);
        email = findViewById(R.id.text_email_register);
        password = findViewById(R.id.text_password_register);
        passwordConfirmation =  findViewById(R.id.text_passwordConfirmation_register);
        buttonRegister  = findViewById(R.id.button_register);

        auth = FirebaseAuth.getInstance() ;

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData()
    {
        firstNameValue = firstName.getText().toString().trim();
        secondNameValue = secondName.getText().toString().trim();
        emailValue  = email.getText().toString().trim();
        passwordValue = password.getText().toString().trim();
        passwordConfirmationValue = passwordConfirmation.getText().toString().trim();

        if (TextUtils.isEmpty(firstNameValue)) {
            TastyToast.makeText(this, "Please enter first name!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        if (TextUtils.isEmpty(secondNameValue)) {
            TastyToast.makeText(this, "Please enter your second name !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        if (TextUtils.isEmpty(emailValue)) {
            TastyToast.makeText(this, "Please enter email!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            TastyToast.makeText(this, "invalid email format!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        if (TextUtils.isEmpty(passwordValue)) {
            TastyToast.makeText(this, "Please enter your password  !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        if(!passwordValue.matches(""+passwordConfirmationValue))
        {
            TastyToast.makeText(this, " passwords does not match !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        registerParent();
    }

    private void registerParent()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registration");
        progressDialog.setMessage("Please wait while u be registred ...");
        progressDialog.show();

        Parent parent  = new Parent() ;
        parent.setFirstName(firstNameValue);
        parent.setSecondName(secondNameValue);

        auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        user = auth.getCurrentUser();
                        TastyToast.makeText(this, "Registered succesfuly ", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        TastyToast.makeText(this, errorMessage, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }
                });
    }

}