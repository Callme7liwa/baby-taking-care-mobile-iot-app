package ensias.myteam.babytakingcare;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.*;

import ensias.myteam.babytakingcare.services.HourValueFormatter;


public class ChartsActivity extends AppCompatActivity {

    LineChart lineChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);
        /*lineChartView = (LineChart) findViewById(R.id.line_chart);
        int color = ContextCompat.getColor(this, R.color.background_app);
        lineChartView.setBackgroundColor(color);*/

        LineChart chart = findViewById(R.id.line_chart);

        // Enable touch gestures
        chart.setTouchEnabled(true);

        // Enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // Customize the chart's background color
        chart.setBackgroundColor(Color.WHITE);

        // Customize the chart's legend
        chart.getLegend().setEnabled(false);

        // Customize the chart's X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Set X-axis step to 2 hours
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.BLACK);
        // Create a list of X-axis values in hours
        // Customize the chart's Y-axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(-10f);
        yAxis.setAxisMaximum(60f);
        yAxis.setGranularity(10f); // Set Y-axis step to 10 degrees
        yAxis.setTextColor(Color.BLACK);
        yAxis.setDrawGridLines(true);
        yAxis.setGridColor(Color.LTGRAY);
        yAxis.setAxisLineColor(Color.BLACK);

        // Disable the right Y-axis
        chart.getAxisRight().setEnabled(false);

        // Generate sample temperature data
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 24; i += 4) {
            float temperature = (float) (Math.random() * (50 - (-10)) - 10);
            entries.add(new Entry(i, temperature));
        }

        // Create a dataset and configure its properties
        LineDataSet dataSet = new LineDataSet(entries, "Temperature");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);

        // Create a LineData object containing the dataset
        LineData lineData = new LineData(dataSet);

        // Set the data to the chart and refresh it
        chart.setData(lineData);
        chart.invalidate();
    }

    private void secondInitialisation()
    {
        // Définir les limites de l'axe Y
        lineChartView.getAxisLeft().setAxisMinimum(-10f);
        lineChartView.getAxisLeft().setAxisMaximum(60f);

        // Définir le nombre de libellés à afficher sur l'axe Y
        lineChartView.getAxisLeft().setLabelCount(8);

        // Diviser l'axe X en intervalles de 30 minutes
        long now = System.currentTimeMillis();
        long thirtyMinutes = 30 * 60 * 1000;
        long minX = now - (12 * thirtyMinutes);
        long maxX = now + (12 * thirtyMinutes);
        lineChartView.getXAxis().setAxisMinimum(minX);
        lineChartView.getXAxis().setAxisMaximum(maxX);
        lineChartView.getXAxis().setLabelCount(24);

        // Convertir les timestamps en libellés
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        List<String> xLabels = new ArrayList<>();
        for (long i = minX; i <= maxX; i += thirtyMinutes) {
            xLabels.add(sdf.format(new Date(i)));
        }

        LineDataSet lineDataSet = new LineDataSet(getDataValues(), "Data Set 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChartView.setData(data);
        lineChartView.invalidate();

    }

    private void firstInitialisation()
    {
        LineDataSet lineDataSet = new LineDataSet(getDataValues(), "Data Set 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChartView.setData(data);
        lineChartView.invalidate();
    }

    private ArrayList<Entry> getDataValues()
    {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 20f));
        entries.add(new Entry(1f, 25f));
        entries.add(new Entry(2f, 30f));
        entries.add(new Entry(3f, 35f));
        entries.add(new Entry(4f, 40f));
        return entries ;
    }

}
