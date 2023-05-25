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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private TextView text_baby_temperature , text_baby_environment , text_baby_position , text_baby_voice ,text_baby_weight , text_baby_name , text_baby_birthday ;
    private ImageView go_back ;
    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    private LinearLayout noDataFound ;
    //
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    //
    private LinearLayout temperatures_baby  , services_baby  , history_baby ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babies);
        initialisation();
        getBabies();

    }

    private void initialisation()
    {
        //
        temperatures_baby = findViewById(R.id.temperatures_baby);
        services_baby = findViewById(R.id.services_baby);
        history_baby = findViewById(R.id.histories_baby);

        //
        text_baby_name = findViewById(R.id.text_baby_name);
        text_baby_temperature = findViewById(R.id.text_baby_temperature);
        text_baby_weight = findViewById(R.id.text_baby_weight);
        text_baby_birthday = findViewById(R.id.text_baby_birthday);
        text_baby_environment = findViewById(R.id.text_baby_environment);
        text_baby_position = findViewById(R.id.text_baby_position);
        text_baby_voice = findViewById(R.id.text_baby_voice);
        go_back = findViewById(R.id.go_back);
        scrollView = findViewById(R.id.list_info);
        recyclerView = findViewById(R.id.babies_list);
        noDataFound = findViewById(R.id.noBabiesHasFound);
        //
        auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser();
        //
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //
       temperatures_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BabiesActivity.this, ChartsActivity.class);
                intent.putExtra("babyId", currentBaby.getId().toString());
                startActivity(intent);
                finish();
            }
        });
       //
       services_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BabiesActivity.this, BabyServiceActivity.class);
                intent.putExtra("babyId", currentBaby.getId().toString());
                startActivity(intent);
                finish();
            }
        });
       //
        history_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BabiesActivity.this, HistoryActivity.class);
                intent.putExtra("babyId", currentBaby.getId().toString());
                startActivity(intent);
                finish();
            }
        });
        //
    }

    private void getBabies()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("getting babies");
        progressDialog.setMessage("Please wait while data will be extracted from db  ...");
        progressDialog.show();

        String userId = user.getUid();
        DatabaseReference babiesRef =
                                    FirebaseDatabase.getInstance().getReference()
                                    .child("babiesDb").child(userId).child("babies");

        babiesRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot babySnapshot : snapshot.getChildren()) {
                    Baby baby = babySnapshot.getValue(Baby.class);
                    baby.setId(babySnapshot.getKey());
                    baby.setEnvironment(babySnapshot.child("env_quality").child("value").getValue(Float.class));
                    baby.setVoice(babySnapshot.child("is_crying").child("value").getValue(Boolean.class));
                    baby.setPosition(babySnapshot.child("position").getValue(String.class));
                    System.out.println("the value of  position  env "+babySnapshot.child("position").getValue(String.class));

                    System.out.println("the value of is crying is "+babySnapshot.child("is_crying").child("value").getValue(Boolean.class) );
                    System.out.println("the value of  position  env "+baby.getPosition());
                    babies.add(baby);
                }
                progressDialog.dismiss();
                if(babies.size() > 0 )
                {
                    changeCurrentBaby(babies.get(0));
                    showBabies(babies);
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
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
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
        this.currentBaby.setId(baby.getId());
        this.currentBaby.setName(baby.getName());
        this.currentBaby.setBirthday(baby.getBirthday());
        this.currentBaby.setTemperature(baby.getTemperature());
        this.currentBaby.setWeight(baby.getWeight());
        this.currentBaby.setPosition(baby.getPosition());
        this.currentBaby.setEnvironment(baby.getEnvironment());
        this.currentBaby.setVoice(baby.getVoice());

        this.text_baby_name.setText(this.currentBaby.getName());
        this.text_baby_temperature.setText(this.currentBaby.getTemperature());
        this.text_baby_weight.setText(this.currentBaby.getWeight());
        this.text_baby_birthday.setText(this.currentBaby.getBirthday());

        float env_value = this.currentBaby.getEnvironment() ;
        if(env_value > 45 )
            this.text_baby_environment.setText("not good");
        else
            this.text_baby_environment.setText("good");

        switch (this.currentBaby.getPosition())
        {
            case "00" : this.text_baby_position.setText("Back");break ;
            case "01" : this.text_baby_position.setText("Left side");break ;
            case "10" : this.text_baby_position.setText("Right side");break ;
            case "11" : this.text_baby_position.setText("Stomach");break ;
            default : break ;
        }

        if(this.currentBaby.getVoice())
            this.text_baby_voice.setText("is crying");
        else
            this.text_baby_voice.setText("normal");


    }
}