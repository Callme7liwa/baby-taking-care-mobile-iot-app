package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Adapters.BabyAdapter;
import ensias.myteam.babytakingcare.Listener.ClickListener;
import ensias.myteam.babytakingcare.Models.Baby;

public class BabiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private BabyAdapter contactAdapter;
    private ArrayList<Baby> babies = new ArrayList<>();
    private Baby currentBaby  = new Baby() ;
    private TextView text_baby_temperature , text_baby_weight , text_baby_name , text_baby_birthday ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babies);
        initialisation();

        Baby baby_1 = new Baby("mehdi","10","36","2020-11-01");
        Baby baby_2 = new Baby("ayoub","9","34","2021-01-01");
        Baby baby_3 = new Baby("mehdi","11","37","2020-02-01");
        Baby baby_4 = new Baby("mehdi","7","34","2021-03-01");
        Baby baby_5 = new Baby("mehdi","12","35","2022-04-01");

        babies.add(baby_1);
        babies.add(baby_2);
        babies.add(baby_3);
        babies.add(baby_4);
        babies.add(baby_5);

        showBabies(babies);
        changeCurrentBaby(babies.get(0));
    }

    private void initialisation()
    {
        text_baby_name = findViewById(R.id.text_baby_name);
        text_baby_temperature = findViewById(R.id.text_baby_temperature);
        text_baby_weight = findViewById(R.id.text_baby_weight);
        text_baby_birthday = findViewById(R.id.text_baby_birthday);
    }

    private void showBabies(List<Baby> babies) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView = findViewById(R.id.babies_list);
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