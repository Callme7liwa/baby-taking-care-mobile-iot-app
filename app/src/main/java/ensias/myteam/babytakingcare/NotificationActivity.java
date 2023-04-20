package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ensias.myteam.babytakingcare.services.NotificationService;


public class NotificationActivity extends AppCompatActivity {

    private AppCompatButton button ;
    private FirebaseDatabase firebaseDatabase   ;
    private DatabaseReference databaseReference ;
    NotificationManagerCompat notificationManagerCompat ;
    Notification notification ;

    private Vibrator vibrator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //if API = 26(Oreo) or higher
            vibrator.vibrate(
                    VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE)
            );

        } else {
            //vibrate for 1 second
            vibrator.vibrate(200000000);

            //Vibration Pattern - you can create yours
            long[] pattern = {0, 200, 10, 500};
            vibrator.vibrate(pattern, -1);
        }

        initialisation();


    }

    private void initialisation()
    {
        /*button = findViewById(R.id.goWelcome);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("babies");*/

        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int notificationId = 0 ;
                for (DataSnapshot babySnapshot : snapshot.getChildren()) {
                    // Récupérer le nom du bébé
                    String babyName = babySnapshot.child("name").getValue(String.class);
                    // Parcourir la liste de températures associée à ce bébé
                    for (DataSnapshot temperatureSnapshot : babySnapshot.child("temperatures").getChildren()) {
                        // Récupérer la date et la valeur de la température
                        String date = temperatureSnapshot.child("date").getValue(String.class);
                        float temperatureValue = temperatureSnapshot.child("value").getValue(Float.class);
                        // Comparer la température avec la valeur seuil
                        if (temperatureValue > 37.0) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            {
                                NotificationChannel channel = new NotificationChannel("myCh" , "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                                NotificationManager manager = getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this, "myCh")
                                    .setSmallIcon(R.drawable.temperature_icon)
                                    .setContentTitle("Alerte température")
                                    .setContentText("La température de"+babyName+"est de"+temperatureValue+" degrés Celsius.")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH);
                            notification = builder.build();
                            notificationManagerCompat = NotificationManagerCompat.from(NotificationActivity.this);
                            notificationManagerCompat.notify(notificationId++,notification);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }
}