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
    private Switch temperatures_switch , layers_switch , position_switch , voice_switch , environment_switch ;

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

        position_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    reference.child("position").setValue(Boolean.TRUE);
                } else {
                    // Switch is off
                    reference.child("position").setValue(Boolean.FALSE);
                }
            }
        });

        voice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    reference.child("voice").setValue(Boolean.TRUE);
                } else {
                    // Switch is off
                    reference.child("voice").setValue(Boolean.FALSE);
                }
            }
        });

        environment_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    reference.child("environment").setValue(Boolean.TRUE);
                } else {
                    // Switch is off
                    reference.child("environment").setValue(Boolean.FALSE);
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
        voice_switch = binding.mySwitchVoice;
        position_switch = binding.mySwitchPosition;
        environment_switch = binding.mySwitchEnvironment;
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
                Boolean positionEnabled = dataSnapshot.child("position").getValue(Boolean.class);
                Boolean voiceEnabled = dataSnapshot.child("voice").getValue(Boolean.class);
                Boolean environmentEnabled = dataSnapshot.child("environment").getValue(Boolean.class);
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
                if(positionEnabled != null && positionEnabled)
                {
                    position_switch.setChecked(true);
                }
                else {
                    position_switch.setChecked(false);
                }
                if(voiceEnabled != null && voiceEnabled)
                {
                    voice_switch.setChecked(true);
                }else {
                    voice_switch.setChecked(false);
                }
                if(environmentEnabled != null && environmentEnabled)
                {
                    environment_switch.setChecked(true);
                } else {
                    environment_switch.setChecked(false);
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