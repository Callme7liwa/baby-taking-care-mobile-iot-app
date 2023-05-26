package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ensias.myteam.babytakingcare.Adapters.BVAdapter;
import ensias.myteam.babytakingcare.fragments.DiapersNotificationFragment;
import ensias.myteam.babytakingcare.fragments.NotificationFragment;

public class HistoryActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private ImageView back_btn;

    public static final String NOTIFICATION_CHANNEL_ID = "myapp.notification.channel";




    List<String> titles = List.of("Diapers" , "Temperatures");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initialisation();
    }

    private void initialisation()
    {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        back_btn = findViewById(R.id.back_btn);

        BVAdapter bvAdapter = new BVAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        bvAdapter.addFragment(new DiapersNotificationFragment() , "Diapers Notification");
        bvAdapter.addFragment(new NotificationFragment() , "Temperatures Notification ");

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

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
