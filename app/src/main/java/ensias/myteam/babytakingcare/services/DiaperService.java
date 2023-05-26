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

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ensias.myteam.babytakingcare.AlarmChangingLayerActivity;
import ensias.myteam.babytakingcare.R;

public class DiaperService extends Service {

    private FirebaseDatabase database ;
    private DatabaseReference reference ;
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private NotificationManagerCompat notificationManagerCompat ;
    private NotificationManagerCompat notificationManager;
    private Notification notification ;
    private Handler handler;
    private Runnable runnable;
    private int diaper_limit  = 90 ;


    public DiaperService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
            System.out.println("im in Diaper  service ");
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
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println(this.getClass().getName() + " INSIDE BD CALL ");
                        int notificationId = 0 ;
                        for (DataSnapshot babySnapshot : snapshot.getChildren()) {
                            Boolean serviceState = babySnapshot.child("services").child("layers").getValue(Boolean.class) ;
                            if( serviceState && babySnapshot.hasChild("diaper")) {
                                float humidityValue = babySnapshot.child("diaper").child("value").getValue(Float.class);
                                Boolean isAlerted = babySnapshot.child("diaper").child("alerted").getValue(Boolean.class);
                                if(humidityValue > diaper_limit && !isAlerted)
                                {
                                    babySnapshot.child("diaper").child("alerted").getRef().setValue(true);
                                    // Display Changing layer notifications :
                                    String babyName = babySnapshot.child("name").getValue(String.class);
                                    Intent intent = new Intent(getApplicationContext(), AlarmChangingLayerActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("nom", babyName);
                                    intent.putExtra("date", getCurrentDate() + " at " + getCurrentTime());
                                    startActivity(intent);
                                    // Formater l'heure au format "HH:mm"
                                    String currentTime = getCurrentTime();
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    {
                                        NotificationChannel channel = new NotificationChannel("myCh" , "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                                        NotificationManager manager = getSystemService(NotificationManager.class);
                                        manager.createNotificationChannel(channel);
                                    }
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DiaperService.this, "myCh")
                                            .setSmallIcon(R.drawable.layer_baby)
                                            .setContentTitle(" Diaper Alert  ")
                                            .setContentText("  You have to change the diaper now  , for " +  babyName + " at " + currentTime)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                                    notification = builder.build();
                                    notificationManagerCompat = NotificationManagerCompat.from(DiaperService.this);
                                    notificationManagerCompat.notify(notificationId++,notification);

                                    saveNotification(babyName,currentTime);
                                }
                            }
                        }
                        reference.removeEventListener(this);
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

    private  void saveNotification(String babyName , String time )
    {
        String notificationId = UUID.randomUUID().toString();
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("babiesDb").child(this.user.getUid()).child("notifications").child("diapers").child(notificationId);
        ensias.myteam.babytakingcare.Models.Notification notification = new ensias.myteam.babytakingcare.Models.Notification ();
        notification.setDate(getCurrentDate());
        notification.setDescription(babyName + " changed the layer at " + time);
        notification.setTime(time);
        notificationRef.setValue(notification);
    }

    private String  getCurrentDate()
    {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    private String getCurrentTime()
    {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String currentTimeValue = currentTime.format(formatter);
        return currentTimeValue;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Démarrer l'exécution toutes les 15 secondes
        handler.postDelayed(runnable, 10000);

        return START_NOT_STICKY;
    }

}