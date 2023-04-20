package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ensias.myteam.babytakingcare.databinding.ActivityAboutServicesBinding;

public class AboutServicesActivity extends AppCompatActivity {

    private ImageView backBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_services);
        initialisation();
    }

    private void initialisation()
    {
        backBtn = findViewById(R.id.go_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}