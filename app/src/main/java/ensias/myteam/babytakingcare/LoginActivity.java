package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdsmdg.tastytoast.TastyToast;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email,login_password  ;
    private String emailValue , passwordValue ;
    private LinearLayout login_button ;
    private ImageView error_image , success_image;
    private TextView goToRegister;

    private int email_state=-1 , password_state = -1 ;

    private FirebaseAuth auth ;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initProcess();
        inputsValidation();
    }

    private void initProcess()
    {
        auth = FirebaseAuth.getInstance();
        login_email = findViewById(R.id.text_email);
        login_password = findViewById(R.id.text_password);
        error_image = findViewById(R.id.error_email_indecator);
        success_image = findViewById(R.id.success_email_indecator);
        goToRegister = findViewById(R.id.go_to_register);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registration");
        progressDialog.setMessage("Please wait we are checking ur email and password...");
        progressDialog.show();

        emailValue = login_email.getText().toString().trim();
        passwordValue = login_password.getText().toString().trim();

        if (TextUtils.isEmpty(emailValue)) {
            TastyToast.makeText(this, "Please enter your email  !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            TastyToast.makeText(this, "invalid email format!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }
        if (TextUtils.isEmpty(passwordValue)) {
            TastyToast.makeText(this, "Please enter your baby name!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            return;
        }

        auth.signInWithEmailAndPassword(emailValue, passwordValue)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                        TastyToast.makeText(LoginActivity.this, "Logged in successfully" , TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        startActivity(intent);
                        finish();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    TastyToast.makeText(LoginActivity.this, "" + e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            });

    }

    private void inputsValidation()
    {
        login_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    login_email.setBackgroundResource(R.drawable.input_background);
                    if (email_state==1) {
                        success_image.setVisibility(View.VISIBLE);
                        error_image.setVisibility(View.GONE);
                    } else if(email_state==0){
                        error_image.setVisibility(View.VISIBLE);
                        success_image.setVisibility(View.GONE);
                    }
                }
            }
        });
        login_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    // Si l'adresse email est valide, on met un fond bleu
                    login_email.setBackgroundResource(R.drawable.input_background_border_blue);
                    email_state=1;
                } else {
                    // Sinon, on met un fond rouge
                    login_email.setBackgroundResource(R.drawable.input_background_border_red);
                    email_state=0;
                }
            }
        });
    }
}