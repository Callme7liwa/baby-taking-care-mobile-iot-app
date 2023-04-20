package ensias.myteam.babytakingcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Locale;

import ensias.myteam.babytakingcare.Adapters.MyAboutSliderAdapter;
import ensias.myteam.babytakingcare.Adapters.MyCustomSliderAdapter;

public class AboutUsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_about_us);
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
                    if(nextButton.getVisibility() == View.GONE )
                        nextButton.setVisibility(View.VISIBLE);
                }
                if(nextPosition >= dotscount-1){
                    nextButton.setVisibility(View.GONE);
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

    }

    private void initProcess() {
        //
        currentPosition = 0;
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        prevButton.setVisibility(View.GONE);
        //
        servicesViewPager = findViewById(R.id.view_page_contributors);
        sliderDotspanel = findViewById(R.id.SliderDots);
        MyAboutSliderAdapter aboutSliderAdapter = new MyAboutSliderAdapter(this);
        servicesViewPager.setAdapter(aboutSliderAdapter);
        dotscount = aboutSliderAdapter.getCount();
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
            nextButton.setVisibility(View.VISIBLE);
            prevButton.setVisibility(View.GONE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }
        if (currentPosition >= dotscount - 1) {
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
        for(int i = 0; i< dotscount; i++){
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
    }




}