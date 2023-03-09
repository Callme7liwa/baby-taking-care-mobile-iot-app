package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout.Tab;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import ensias.myteam.babytakingcare.Adapters.BVAdapter;
import ensias.myteam.babytakingcare.fragments.MenuFragment;
import ensias.myteam.babytakingcare.fragments.NotificationFragment;

public class WelcomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

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
}
