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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ensias.myteam.babytakingcare.AddBabyActivity;
import ensias.myteam.babytakingcare.AlarmChangingLayerActivity;
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
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private NotificationManagerCompat notificationManagerCompat ;
    private Notification notification ;

    @Override
    public void onCreate() {
        super.onCreate();
        initialisation();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int notificationId = 0 ;
                        boolean notificationAdded = false ;
                        for (DataSnapshot babySnapshot : snapshot.getChildren()) {
                            Boolean serviceState = babySnapshot.child("services").child("temperatures").getValue(Boolean.class) ;
                            String babyName = babySnapshot.child("name").getValue(String.class);
                            for (DataSnapshot temperatureSnapshot : babySnapshot.child("temperatures").child("2023-04-11").getChildren()) {
                                if (!temperatureSnapshot.getKey().equals("moyen")) {
                                    float temperatureValue = temperatureSnapshot.child("value").getValue(Float.class);
                                    Boolean checked = temperatureSnapshot.child("checked").getValue(Boolean.class);
                                    if (temperatureValue > 17.0 && !checked) {
                                        temperatureSnapshot.child("checked").getRef().setValue(true);
                                        checked = true;
                                        if (serviceState) {

                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                            {
                                                NotificationChannel channel = new NotificationChannel("myCh" , "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                                                NotificationManager manager = getSystemService(NotificationManager.class);
                                                manager.createNotificationChannel(channel);
                                            }
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, "myCh")
                                                    .setSmallIcon(R.drawable.temperature_icon)
                                                    .setContentTitle("Alerte température")
                                                    .setContentText("La température de "+babyName+" est de "+temperatureValue+" degrés Celsius.")
                                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                                            notification = builder.build();
                                            notificationManagerCompat = NotificationManagerCompat.from(NotificationService.this);
                                            notificationManagerCompat.notify(notificationId++,notification);
                                        }
                                        if(notificationAdded == false)
                                        {
                                            notificationAdded = true ;
                                            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                            String time = temperatureSnapshot.getKey() ;
                                            String notificationToSaveId = currentDate + "-" +  time;
                                            DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("babiesDb").child(user.getUid()).child("notifications").child(notificationToSaveId);
                                            ensias.myteam.babytakingcare.Models.Notification notification = new ensias.myteam.babytakingcare.Models.Notification (notificationToSaveId, currentDate, "mehdi suffered a temperature of "+temperatureValue);
                                            notificationRef.setValue(notification);
                                        }

                                    }
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

    private void initialisation()
    {
        auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase
                .getReference("babiesDb")
                .child(user.getUid())
                .child("babies");
        notificationManager = NotificationManagerCompat.from(this);
    }

    private void renderNotification(String babyName , float temperatureValue , int notificationId)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("myCh" , "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, "myCh")
                .setSmallIcon(R.drawable.temperature_icon)
                .setContentTitle("Alerte température")
                .setContentText("La température de "+babyName+" est de "+temperatureValue+" degrés Celsius.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(NotificationService.this);
        notificationManagerCompat.notify(notificationId++,notification);
    }

    private  void saveNotification(String babyName , float temperature)
    {
        String notificationId = UUID.randomUUID().toString();
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("babiesDb").child(this.user.getUid()).child("notifications").child(notificationId);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ensias.myteam.babytakingcare.Models.Notification notification = new ensias.myteam.babytakingcare.Models.Notification (notificationId+"11", currentDate, "mehdi suffered a temperature of "+temperature);
        notificationRef.setValue(notification);
        //DatabaseReference notificationsRef = this.firebaseDatabase.getReference("babiesDb").child(this.user.getUid()).child("notifications");
        //String notificationId = notificationsRef.push().getKey();



        /*notificationsRef.child(notificationId).setValue(notification)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("the notification has been added successfuly");
                })
                .addOnFailureListener(e -> {
                    // Error occurred while adding notification
                    System.out.println("error occured while adding a new notification ");

                });*/
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
