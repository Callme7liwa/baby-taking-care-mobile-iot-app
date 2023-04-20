package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ensias.myteam.babytakingcare.databinding.ActivityBabyServiceBinding;
import ensias.myteam.babytakingcare.databinding.ActivityTestBinding;

public class BabyServiceActivity extends AppCompatActivity {

    private ActivityBabyServiceBinding binding ;

    //
    private FirebaseDatabase db ;
    private DatabaseReference reference ;
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    //
    private ImageView backBtn ;
    private String babyId ;
    private Switch temperatures_switch ;
    private Switch layers_switch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBabyServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialisation();

        temperatures_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    reference.child("temperatures").setValue(Boolean.TRUE);
                } else {
                    // Switch is off
                    reference.child("temperatures").setValue(Boolean.FALSE);
                }
            }
        });

        layers_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    reference.child("layers").setValue(Boolean.TRUE);
                } else {
                    // Switch is off
                    reference.child("layers").setValue(Boolean.FALSE);
                }
            }
        });


    }

    private void initialisation()
    {
        //
        Intent intent = getIntent();
        babyId = intent.getStringExtra("babyId");
        //
        backBtn = binding.backBtn;
        temperatures_switch = binding.mySwitchTemperature ;
        layers_switch = binding.mySwitchLayers ;
        //
        auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance() ;
        //
        reference = db.getReference().child("babiesDb").child(user.getUid()).child("babies").child(babyId).child("services");
        //
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Récupérer la valeur du champ "temperatures"
                Boolean temperaturesEnabled = dataSnapshot.child("temperatures").getValue(Boolean.class);
                Boolean layersEnabled = dataSnapshot.child("layers").getValue(Boolean.class);
                if (temperaturesEnabled != null && temperaturesEnabled) {
                    temperatures_switch.setChecked(true);
                } else {
                    temperatures_switch.setChecked(false);
                }
                if(layersEnabled != null && layersEnabled){
                    layers_switch.setChecked(true);
                }  else {
                    layers_switch.setChecked(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer les erreurs de lecture de la base de données
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}