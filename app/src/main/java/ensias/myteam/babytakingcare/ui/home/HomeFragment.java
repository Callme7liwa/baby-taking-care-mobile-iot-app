package ensias.myteam.babytakingcare.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdsmdg.tastytoast.TastyToast;

import ensias.myteam.babytakingcare.AcceuilActivity;
import ensias.myteam.babytakingcare.AddBabyActivity;
import ensias.myteam.babytakingcare.BabiesActivity;
import ensias.myteam.babytakingcare.LoginActivity;
import ensias.myteam.babytakingcare.TestActivity;
import ensias.myteam.babytakingcare.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LinearLayout list_babies , add_baby ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list_babies = binding.listBabies;
        add_baby = binding.addNewBaby;

        list_babies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BabiesActivity.class);
                startActivity(intent);
            }
        });

        add_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddBabyActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}