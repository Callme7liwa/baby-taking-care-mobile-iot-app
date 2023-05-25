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

import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import ensias.myteam.babytakingcare.R;

public class VoiceService extends Service {

    private FirebaseDatabase database ;
    private DatabaseReference reference ;
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private NotificationManagerCompat notificationManagerCompat ;
    private NotificationManagerCompat notificationManager;
    private Notification notification ;
    private Handler handler;
    private Runnable runnable;


    @Override
    public void onCreate() {
        super.onCreate();
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
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int notificationId = 0 ;
                        for (DataSnapshot babySnapshot : snapshot.getChildren()) {
                            Boolean serviceState = babySnapshot.child("services").child("voice").getValue(Boolean.class) ;
                            String babyName = babySnapshot.child("name").getValue(String.class);
                            if( serviceState && babySnapshot.hasChild("is_crying")) {
                                Boolean isCrying = babySnapshot.child("is_crying").child("value").getValue(Boolean.class);
                                Boolean isAlerted = babySnapshot.child("is_crying").child("alerted").getValue(Boolean.class);
                                if(isCrying && !isAlerted)
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
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(VoiceService.this, "myCh")
                                            .setSmallIcon(R.drawable.icon_crying)
                                            .setContentTitle("Crying alert ")
                                            .setContentText("the baby "+babyName+" is crying now at " + formattedTime)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                                    notification = builder.build();
                                    notificationManagerCompat = NotificationManagerCompat.from(VoiceService.this);
                                    notificationManagerCompat.notify(notificationId++,notification);

                                    babySnapshot.child("is_crying").child("alerted").getRef().setValue(true);
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

    public VoiceService() {
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