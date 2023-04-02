package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout.Tab;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Adapters.BVAdapter;
import ensias.myteam.babytakingcare.fragments.MenuFragment;
import ensias.myteam.babytakingcare.fragments.NotificationFragment;

public class WelcomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    public static final String NOTIFICATION_CHANNEL_ID = "myapp.notification.channel";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    NotificationManagerCompat notificationManagerCompat ;
    Notification notification ;


    List<String> titles = List.of("Menu" , "Notifications");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        initialisation();
        getBabies();
    }

    public void push(View view)
    {
    }

    private void initialisation()
    {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        BVAdapter bvAdapter = new BVAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        bvAdapter.addFragment(new MenuFragment() , "MENU");
        bvAdapter.addFragment(new NotificationFragment() , "NOTIFICATIONS");

        viewPager.setAdapter(bvAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Désactiver les onglets qui ne sont pas sélectionnés
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setText(titles.get(i));
            if (tab != null) {
                if (i != 0) {
                    tab.view.setEnabled(false);
                }
            }
        }
        // Activer les onglets lorsqu'ils sont sélectionnés
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null) {
                        if (i == position) {
                            tab.view.setEnabled(true);
                        } else {
                            tab.view.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getBabies() {

        System.out.println("************************* okokoko ********************************");


        this.databaseReference = FirebaseDatabase.getInstance().getReference("employes");

        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("************************* okokoko ********************************");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("************************* okokoko ********************************");
            }
        });

    }

    private void alert() {
        // Récupérer la référence à la base de données Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference babiesRef = database.getReference("babies");
        // Récupérer la liste de bébés
        babiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int notificationId = 0 ;
                for (DataSnapshot babySnapshot : dataSnapshot.getChildren()) {
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
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(WelcomeActivity.this, "myCh")
                                    .setSmallIcon(R.drawable.temperature_icon)
                                    .setContentTitle("Alerte température")
                                    .setContentText("La température de"+babyName+"est de"+temperatureValue+" degrés Celsius.")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH);
                            notification = builder.build();
                            notificationManagerCompat = NotificationManagerCompat.from(WelcomeActivity.this);
                            notificationManagerCompat.notify(notificationId++,notification);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
