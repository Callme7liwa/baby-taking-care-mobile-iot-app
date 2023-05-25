package ensias.myteam.babytakingcare.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import ensias.myteam.babytakingcare.R;

public class EnvironmentService extends Service {


    private FirebaseDatabase database ;
    private DatabaseReference reference ;
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private NotificationManagerCompat notificationManagerCompat ;
    private NotificationManagerCompat notificationManager;
    private Notification notification ;
    private Handler handler;
    private Runnable runnable;
    private int quality_limit = 37 ;


    public EnvironmentService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("im in environment quality service ");
        auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        reference = database
                    .getReference("babiesDb")
                    .child(user.getUid())
                    .child("babies");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(this.getClass().getName() + " RUN ");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println(this.getClass().getName() + " INSIDE BD CALL ");
                        int notificationId = 0 ;
                        for (DataSnapshot babySnapshot : snapshot.getChildren()) {
                            Boolean serviceState = babySnapshot.child("services").child("environment").getValue(Boolean.class) ;
                            if( serviceState && babySnapshot.hasChild("env_quality")) {
                                float qualityValue = babySnapshot.child("env_quality").child("value").getValue(Float.class);
                                Boolean isAlerted = babySnapshot.child("env_quality").child("alerted").getValue(Boolean.class);
                                if(qualityValue > quality_limit && !isAlerted)
                                {
                                    // Formater l'heure au format "HH:mm"
                                    LocalTime currentTime = LocalTime.now();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    String formattedTime = currentTime.format(formatter);

                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    {
                                        NotificationChannel channel = new NotificationChannel("myCh" , "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                                        NotificationManager manager = getSystemService(NotificationManager.class);
                                        manager.createNotificationChannel(channel);
                                    }
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(EnvironmentService.this, "myCh")
                                            .setSmallIcon(R.drawable.icon_dislike)
                                            .setContentTitle(" Environment  Quality Alert ")
                                            .setContentText("  Take attention the environment quality is not good at  " + formattedTime)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                                    notification = builder.build();
                                    notificationManagerCompat = NotificationManagerCompat.from(EnvironmentService.this);
                                    notificationManagerCompat.notify(notificationId++,notification);

                                    babySnapshot.child("env_quality").child("alerted").getRef().setValue(true);
                                }
                                else {
                                    System.out.println("baby is not crying");
                                }
                            }
                            else {
                                System.out.println("not having a key nammed is crying");
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
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Démarrer l'exécution toutes les 15 secondes
        handler.postDelayed(runnable, 10000);

        return START_NOT_STICKY;
    }

}