package ensias.myteam.babytakingcare.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ensias.myteam.babytakingcare.NotificationActivity;
import ensias.myteam.babytakingcare.R;

public class NotificationService extends Service {
    private static final String TAG = "MyService";
    private NotificationManagerCompat notificationManager;
    private int notificationId = 0;
    private Handler handler;
    private Runnable runnable;
    private FirebaseDatabase firebaseDatabase   ;
    private DatabaseReference databaseReference ;
    private NotificationManagerCompat notificationManagerCompat ;
    private Notification notification ;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("babies");
        notificationManager = NotificationManagerCompat.from(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                databaseReference.addValueEventListener(new ValueEventListener() {
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
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, "myCh")
                                            .setSmallIcon(R.drawable.temperature_icon)
                                            .setContentTitle("Alerte température")
                                            .setContentText("La température de"+babyName+"est de"+temperatureValue+" degrés Celsius.")
                                            .setPriority(NotificationCompat.PRIORITY_HIGH);
                                    notification = builder.build();
                                    notificationManagerCompat = NotificationManagerCompat.from(NotificationService.this);
                                    notificationManagerCompat.notify(notificationId++,notification);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





































                // Répéter l'exécution toutes les 15 secondes
                handler.postDelayed(this, 10000);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Démarrer l'exécution toutes les 15 secondes
        handler.postDelayed(runnable, 10000);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Arrêter l'exécution
        handler.removeCallbacks(runnable);
    }
}
