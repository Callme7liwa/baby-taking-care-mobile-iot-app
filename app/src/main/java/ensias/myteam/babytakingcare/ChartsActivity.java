package ensias.myteam.babytakingcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import ensias.myteam.babytakingcare.databinding.ActivityAlarmChangingLayerBinding;



public class ChartsActivity extends AppCompatActivity {

    private LineChart chart;
    private List<Entry> entries;
    //
    private FirebaseUser user  ;
    private FirebaseAuth auth ;
    private FirebaseDatabase db ;
    private DatabaseReference reference ;
    //
    private String babyId ;
    private String currentDate = "" ;
    //
    private TextView babyNameText , dateText  , currentDateSelected ;
    private LinearLayout currentDateContainer ;
    //
    private CardView backBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        initialisation();
        getTemperatures();

        currentDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenir la date actuelle pour initialiser le DatePicker
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Créer un DatePickerDialog pour sélectionner une nouvelle date
                DatePickerDialog datePickerDialog = new DatePickerDialog(ChartsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // Récupère le nom du jour de la semaine
                        String dayName = selectedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);

                        // Formate la date
                        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        currentDate = dateFormat.format(selectedDate.getTime());


                        currentDateSelected.setText(dayName + " " + formattedDate);

                        getTemperatures();
                    }
                }, year, month, day);
                // Afficher le DatePickerDialog dans un popup
                datePickerDialog.show();
            }
        });

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initialisation()
    {
        Intent intent = getIntent();
        babyId = intent.getStringExtra("babyId");
        this.db = FirebaseDatabase.getInstance() ;
        this.auth = FirebaseAuth.getInstance() ;
        this.user = this.auth.getCurrentUser();
        this.currentDateContainer = findViewById(R.id.currentDate);
        this.currentDateSelected  = findViewById(R.id.currentDateValue);
        this.babyNameText = findViewById(R.id.babyName);
        this.dateText = findViewById(R.id.date_temperatures);
        this.backBtn = findViewById(R.id.back_btn_temperatures);
        setupChart();
    }

    private void getTemperatures()
    {
        if(currentDate.equals(""))
        {
            DatabaseReference ref =  db
                                        .getReference("babiesDb")
                                        .child(this.user.getUid())
                                        .child("babies")
                                        .child(babyId)
                                        .child("temperatures");

            Query latestDateQuery = ref.orderByChild("date").limitToLast(1);

            latestDateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            currentDate = snapshot.getKey();
                        }
                        retrieveData();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // handle error
                }
            });
        }
        else
        retrieveData();
    }

    private void retrieveData ()
    {
        reference = db
                .getReference("babiesDb")
                .child(this.user.getUid())
                .child("babies")
                .child(babyId)
                .child("temperatures")
                .child(currentDate)
                .child("moyen");

        entries = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=4 ;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String timeRange = snapshot.getKey();
                    float value = snapshot.child("value").getValue(Float.class);
                    System.out.println("the value : " + value + " time range " + timeRange);
                    entries.add(new Entry(i, value));
                    i=i+4 ;
                }
                // Mettre à jour le graphique avec les nouvelles données
                updateChart(entries);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Gestion d'erreur
            }
        });
    }


    private void setupChart() {
        chart = findViewById(R.id.chart);

        LineDataSet dataSet = new LineDataSet(entries, "Moyen");
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.BLUE);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        chart.getAxisRight().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }

    private void updateChart(List<Entry> entries) {
        // Créer un ensemble de données à partir des entrées et des labels
        LineDataSet dataSet = new LineDataSet(entries, "Température");
        // Définir les labels pour l'axe X
        configureAxisX();
        //
        configureAxisY();
        // Créer un ensemble de données pour l'axe des y
        LineData lineData = new LineData(dataSet);
        // Mettre à jour le graphique avec les nouvelles données
        chart.setData(lineData);
        chart.invalidate();
    }

    private void configureAxisX()
    {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(4f);
        xAxis.setAxisMaximum(24f);
        xAxis.setLabelCount(6, true);
    }

    private void configureAxisY()
    {
        // Configurer l'axe y
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(50f);
    }
}


