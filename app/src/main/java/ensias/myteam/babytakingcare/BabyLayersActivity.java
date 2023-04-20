package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ensias.myteam.babytakingcare.Adapters.LayerHistoryAdapter;
import ensias.myteam.babytakingcare.dto.LayerHistoryDto;

public class BabyLayersActivity extends AppCompatActivity {

    private List<LayerHistoryDto> layerHistoryDtos  ;
    private LinearLayout currentDateContainer ;
    private TextView currentDateValue ;
    private TextView sizeListHistory ;
    private String currentDate ;
    private LayerHistoryAdapter history_adapter ;
    private RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_layers);
        initialisation();
        showLayersChangingHistory(layerHistoryDtos);
        // Obtenez une référence à votre TextView qui affiche la date actuelle

        // Ajouter un OnClickListener pour ouvrir le DatePicker dans un popup
        currentDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenir la date actuelle pour initialiser le DatePicker
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Créer un DatePickerDialog pour sélectionner une nouvelle date
                DatePickerDialog datePickerDialog = new DatePickerDialog(BabyLayersActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Récupère la date sélectionnée par l'utilisateur
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        changeHistory();
                    }
                }, year, month, day);
                // Afficher le DatePickerDialog dans un popup
                datePickerDialog.show();
            }
        });

    }

    private void  initialisation()
    {
        this.currentDate = "Wed 01-11-2023";
        this.currentDateValue = findViewById(R.id.currentDateValue);
        this.currentDateContainer = findViewById(R.id.currentDate);
        this.sizeListHistory = findViewById(R.id.size_list_history);
        this.currentDateValue.setText(currentDate);
        this.layerHistoryDtos = new ArrayList<>();
        List.of("Sun 10-11-2000 at 9:30","Sun 10-17-2000 at 12:20","Wed 10-17-2000 at 14:20","").forEach(date->{
            this.layerHistoryDtos.add(new LayerHistoryDto(date));
        });
        this.sizeListHistory.setText(""+layerHistoryDtos.size());
    }

    private void changeHistory()
    {
        this.currentDate = "Sun 10-11-2000";
        this.currentDateValue.setText(currentDate);
        List.of("Sun 10-11-2000 at 9:30","Sun 10-17-2000 at 12:20","Wed 10-17-2000 at 14:20","Wed 10-17-2000 at 14:20","Wed 10-17-2000 at 14:20","Wed 10-17-2000 at 14:20")
            .forEach(date->{
            this.layerHistoryDtos.add(new LayerHistoryDto(date));
        });
        this.history_adapter.setLayers_history(layerHistoryDtos);
        this.sizeListHistory.setText(""+layerHistoryDtos.size());
    }

    private void showLayersChangingHistory(List<LayerHistoryDto> history_layers) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = findViewById(R.id.history_layers_list);
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.VERTICAL));
        // 16px. In practice, you'll want to use getDimensionPixelSize
        history_adapter = new LayerHistoryAdapter(this, history_layers );
        recyclerView.setAdapter(history_adapter);
    }
}