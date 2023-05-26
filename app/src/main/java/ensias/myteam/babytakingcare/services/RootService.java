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



    @Override
    public void onCreate() {
        System.out.println("im in root service ");
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationService = new Intent(this, NotificationService.class);
        startService(notificationService);
        Intent diapersService = new Intent(this, DiaperService.class);
        startService(diapersService);
        Intent voiceService = new Intent(this, VoiceService.class);
        startService(voiceService);
        Intent environmentService= new Intent(this, EnvironmentService.class);
        startService(environmentService);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}