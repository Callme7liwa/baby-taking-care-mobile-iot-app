package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email,login_password  ;
    private ImageView error_image , success_image;
    private int email_state=-1 , password_state = -1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initProcess();
        inputsValidation();

    }

    private void initProcess()
    {
        login_email = findViewById(R.id.text_email);
        login_password = findViewById(R.id.text_password);
        error_image = findViewById(R.id.error_email_indecator);
        success_image = findViewById(R.id.success_email_indecator);
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