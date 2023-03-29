package ensias.myteam.babytakingcare.services;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HourValueFormatter extends ValueFormatter {
    private final SimpleDateFormat mFormat;
    private final Calendar mCalendar;

    public HourValueFormatter() {
        mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        mCalendar = Calendar.getInstance();
    }

    @Override
    public String getFormattedValue(float value) {
        mCalendar.setTimeInMillis((long) value * 60 * 60 * 1000);
        Date date = mCalendar.getTime();
        return mFormat.format(date);
    }
}
