package ensias.myteam.babytakingcare.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class LineChartView extends LineChart {

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public void init() {
        // Configuration du graphique
        setDrawGridBackground(false);
        setDrawBorders(false);
        setDescription(null);
        setTouchEnabled(false);
        setDragEnabled(false);
        setScaleEnabled(false);
        setPinchZoom(false);
        setDoubleTapToZoomEnabled(false);

        // Configuration de l'axe X
        XAxis xAxis = getXAxis();
        if (xAxis != null) {
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    // Formater l'axe X avec la date ou l'heure
                    return DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date((long) value));
                }
            });
        }

        // Configuration de l'axe Y
        YAxis leftAxis = getAxisLeft();
        if (leftAxis != null) {
            leftAxis.setDrawGridLines(false);
            leftAxis.setDrawAxisLine(false);
            leftAxis.setDrawLabels(false);
        }
    }


    public void setData(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "Température");
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.BLUE);

        LineData data = new LineData(dataSet);
        setData(data);
    }
}
