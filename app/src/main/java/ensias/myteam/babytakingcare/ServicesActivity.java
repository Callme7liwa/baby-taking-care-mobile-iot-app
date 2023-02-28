package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ensias.myteam.babytakingcare.Adapters.MyCustomSliderAdapter;
import ensias.myteam.babytakingcare.Adapters.ViewPagerAdapter;
public class ServicesActivity extends AppCompatActivity {

    private ViewPager servicesViewPager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private ImageView backImage ;
    private AppCompatButton nextButton;
    private AppCompatButton prevButton;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        initProcess();

        servicesViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateButtonsState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPosition = currentPosition + 1;
                if (nextPosition < dotscount) {
                    servicesViewPager.setCurrentItem(nextPosition);
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prevPosition = currentPosition - 1;
                if (prevPosition >= 0) {
                    servicesViewPager.setCurrentItem(prevPosition);
                }
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServicesActivity.this , MainActivity.class));
                finish();
            }
        });
    }

    private void initProcess() {
        //
        currentPosition = 0;
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        prevButton.setVisibility(View.GONE);
        backImage = findViewById(R.id.back_to_welcome_page);
        //
        servicesViewPager = findViewById(R.id.view_page_services);
        sliderDotspanel = findViewById(R.id.SliderDots);
        MyCustomSliderAdapter customSliderAdapter = new MyCustomSliderAdapter(this);
        servicesViewPager.setAdapter(customSliderAdapter);
        dotscount = customSliderAdapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
    }

    private void updateButtonsState(int position) {
        currentPosition = position;
        if (currentPosition <= 0) {
            prevButton.setVisibility(View.GONE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }
        if (currentPosition >= dotscount - 1) {
            nextButton.setText("login");
        } else {
            if (nextButton.getText().toString().toLowerCase(Locale.ROOT).equals("login")) {
                nextButton.setText("next");
            }
        }
        for(int i = 0; i< dotscount; i++){
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
    }
}

