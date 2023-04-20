package ensias.myteam.babytakingcare.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RootService extends Service {

    private static final String TAG = "RootService";
    private NotificationManager notificationManager;
    private FirebaseDatabase firebaseDatabase   ;
    private DatabaseReference databaseReference ;


    @Override
    public void onCreate() {
        super.onCreate();
        initialisation();
        // Récupération du gestionnaire de notifications

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent service1Intent = new Intent(this, NotificationService.class);
        startService(service1Intent);
        Intent service2Intent = new Intent(this, LayerService.class);
        startService(service2Intent);
    }

    private void initialisation()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       /* Intent service1Intent = new Intent(this, NotificationService.class);
        startService(service1Intent);

        Intent service2Intent = new Intent(this, LayerService.class);
        startService(service2Intent);*/

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}