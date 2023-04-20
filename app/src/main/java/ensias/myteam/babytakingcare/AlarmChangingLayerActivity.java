package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import ensias.myteam.babytakingcare.services.VibrationService;

public class AlarmChangingLayerActivity extends AppCompatActivity {

    private TextView dateLayerText ;
    private TextView babyNameText ;
    private AppCompatButton button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startVibration();
        setContentView(R.layout.activity_alarm_changing_layer);
        Intent intent = getIntent();
        String babyName = intent.getStringExtra("nom");
        String date  = intent.getStringExtra("date");
        initialisation(babyName , date);
    }

    private void initialisation(String babyName , String date) {
        dateLayerText  = findViewById(R.id.text_dateLayer);
        babyNameText   = findViewById(R.id.text_babyNameLayer);
        button  = findViewById(R.id.stopAlarm_layer);
        dateLayerText.setText(date);
        babyNameText.setText(babyName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hi im here");
                stopService(new Intent(AlarmChangingLayerActivity.this, VibrationService.class));
                // Cancel the vibration
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.cancel();
                }
                finish();
                onBackPressed();
            }
        });
    }

    private void startVibration(){
        Intent intent = new Intent(this, VibrationService.class);
        startService(intent);
    }
}