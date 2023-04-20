package ensias.myteam.babytakingcare.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;

public class VibrationService extends Service {

    private static final String TAG = "VibrationService";

    private Vibrator vibrator;
    private boolean isVibrating = false;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("hi babe ");

        Log.d(TAG, "onCreate: ");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        // Vérifiez si la vibration est déjà en cours
        if (!isVibrating) {

            // Définir le modèle de vibration
            long[] pattern = {0, 100, 1000};
            // Démarrer la vibration en boucle
            vibrator.vibrate(pattern, 1);
            // Arrêter la vibration après 10 secondes
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    vibrator.cancel();
                }
            }, 5*100000);

           /* // Définir le modèle de vibration
            long[] pattern = {0, 100, 1000};
            // Démarrer la vibration en boucle
            vibrator.vibrate(pattern, 0);
            isVibrating = true;*/
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        // Arrêter la vibration
        vibrator.cancel();
        isVibrating = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
