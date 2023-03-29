package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ensias.myteam.babytakingcare.Adapters.BabyAdapter;
import ensias.myteam.babytakingcare.Listener.ClickListener;
import ensias.myteam.babytakingcare.Models.Baby;
import ensias.myteam.babytakingcare.Models.Parent;

public class BabiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private BabyAdapter contactAdapter;
    private ArrayList<Baby> babies = new ArrayList<>();
    private Baby currentBaby  = new Baby() ;
    private TextView text_baby_temperature , text_baby_weight , text_baby_name , text_baby_birthday ;
    private ImageView go_back ;
    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    private LinearLayout noDataFound ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babies);

        initialisation();
        getBabies();

    }

    private void initialisation()
    {
        text_baby_name = findViewById(R.id.text_baby_name);
        text_baby_temperature = findViewById(R.id.text_baby_temperature);
        text_baby_weight = findViewById(R.id.text_baby_weight);
        text_baby_birthday = findViewById(R.id.text_baby_birthday);
        go_back = findViewById(R.id.go_back);
        scrollView = findViewById(R.id.list_info);
        recyclerView = findViewById(R.id.babies_list);
        noDataFound = findViewById(R.id.noBabiesHasFound);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getBabies()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registration");
        progressDialog.setMessage("Please wait while u be registred ...");
        progressDialog.show();

        String userId = "Oh2WjmJLpkURvcNKHeCu8gON5H6z1";
        DatabaseReference babiesRef =
                         FirebaseDatabase.getInstance().getReference()
                        .child("babiesDb").child(userId).child("babies");

        babiesRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot babySnapshot : snapshot.getChildren()) {
                    Baby baby = babySnapshot.getValue(Baby.class);
                    babies.add(baby);
                }
                progressDialog.dismiss();
                if(babies.size() > 0 )
                {
                    showBabies(babies);
                    changeCurrentBaby(babies.get(0));
                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                TastyToast.makeText(BabiesActivity.this, ""+error.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }
        });
    }

    private void showBabies(List<Baby> babies) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
       /* recyclerView = findViewById(R.id.babies_list);*/
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL)); // 16px. In practice, you'll want to use getDimensionPixelSize
        contactAdapter = new BabyAdapter(this, babies , new ClickListener<Baby>() {
            @Override
            public void onClick(Baby baby) {
                changeCurrentBaby(baby);
            }
        });
        recyclerView.setAdapter(contactAdapter);
    }

    private void changeCurrentBaby(Baby baby)
    {
        this.currentBaby.setName(baby.getName());
        this.currentBaby.setBirthday(baby.getBirthday());
        this.currentBaby.setTemperature(baby.getTemperature());
        this.currentBaby.setWeight(baby.getWeight());

        this.text_baby_name.setText(this.currentBaby.getName());
        this.text_baby_temperature.setText(this.currentBaby.getTemperature());
        this.text_baby_weight.setText(this.currentBaby.getWeight());
        this.text_baby_birthday.setText(this.currentBaby.getBirthday());
    }
}