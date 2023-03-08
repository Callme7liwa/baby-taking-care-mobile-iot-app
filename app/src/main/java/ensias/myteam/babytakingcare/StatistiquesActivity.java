package ensias.myteam.babytakingcare;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class StatistiquesActivity extends AppCompatActivity {
    private LineChart chart;
    private boolean isRunning;
    private Handler handler = new Handler();
    private int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        // Initialize chart view
        chart = findViewById(R.id.line_chart);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawBorders(false);

        // Add data to chart
        LineDataSet dataSet = new LineDataSet(generateData(), "Heart Beat");
        dataSet.setLineWidth(2f);
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        LineData data = new LineData(dataSet);
        chart.setData(data);

        // Start updating chart
        isRunning = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateChart();
                if (isRunning) {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void updateChart() {
        LineData data = chart.getData();
        LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);

        // Add new random value to data set
        float newValue = (float) (Math.random() * 40) + 60f;
        data.addEntry(new Entry(x++, newValue), 0);

        // Remove oldest value if more than 30 minutes of data
        if (x > 1800) {
            data.removeEntry(0, 0);
            for (Entry e : set.getValues()) {
                e.setX(e.getX() - 1);
            }
            x--;
        }

        // Notify chart data has changed
        data.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private ArrayList<Entry> generateData() {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            float value = (float) (Math.random() * 40) + 60f;
            values.add(new Entry(i, value));
        }
        return values;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }
}
