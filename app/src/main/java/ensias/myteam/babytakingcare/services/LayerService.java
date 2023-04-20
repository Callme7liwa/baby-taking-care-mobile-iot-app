package ensias.myteam.babytakingcare.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
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

import ensias.myteam.babytakingcare.AddBabyActivity;
import ensias.myteam.babytakingcare.AlarmChangingLayerActivity;
import ensias.myteam.babytakingcare.NotificationActivity;
import ensias.myteam.babytakingcare.R;

public class LayerService extends Service {
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
        System.out.println("hi bro im in Layer Service ");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase
                .getReference("babiesDb")
                .child("LFjcB6q28EOYIp6iHxThjmzbi9c2")
                .child("babies");
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
                            for (DataSnapshot humiditySnapshot : babySnapshot.child("humidity").child("2023-04-08").getChildren()) {
                                // Récupérer la date et la valeur de la température
                                String time = humiditySnapshot.getKey();
                                float humidityValue = humiditySnapshot.child("value").getValue(Float.class);
                                Boolean changed = humiditySnapshot.child("changed").getValue(Boolean.class);
                                Boolean alerted = humiditySnapshot.child("alerted").getValue(Boolean.class);
                                // Comparer la température avec la valeur seuil
                                if (humidityValue > 67.0 && !changed && !alerted ) {

                                    //
                                    Intent intent = new Intent(getApplicationContext(), AlarmChangingLayerActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("nom", babyName);
                                    intent.putExtra("date", "2023-04-08" + " at " + time);
                                    startActivity(intent);
                                    //

                                    humiditySnapshot.child("changed").getRef().setValue(true);
                                    humiditySnapshot.child("alerted").getRef().setValue(true);

                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    {
                                        NotificationChannel channel = new NotificationChannel("channelLayers" , "My Channel Layers", NotificationManager.IMPORTANCE_DEFAULT);
                                        NotificationManager manager = getSystemService(NotificationManager.class);
                                        manager.createNotificationChannel(channel);
                                    }
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(LayerService.this, "channelLayers")
                                            .setSmallIcon(R.drawable.layer_baby)
                                            .setContentTitle("Layer Alert ")
                                            .setContentText("the layer of "+babyName+" must be changed now !")
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                                    notification = builder.build();
                                    notificationManagerCompat = NotificationManagerCompat.from(LayerService.this);
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
