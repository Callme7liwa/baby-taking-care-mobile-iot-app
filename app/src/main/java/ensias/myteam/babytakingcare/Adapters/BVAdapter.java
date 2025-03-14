package ensias.myteam.babytakingcare.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class BVAdapter extends FragmentPagerAdapter {

    private  final ArrayList<Fragment> fragments  = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public BVAdapter(@NonNull FragmentManager fm , int behavior) {
        super(fm,behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment , String title)
    {
        fragments.add(fragment);
        fragmentTitle.add(title);
    }
}
