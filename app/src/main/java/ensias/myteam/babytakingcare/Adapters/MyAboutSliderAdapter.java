package ensias.myteam.babytakingcare.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ensias.myteam.babytakingcare.R;

public class MyAboutSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private String[] names =
            {
                    "Adamou Yahouza",
                    "Ayoub Seddiki",
                    "Abdessamad Amzil",
                    "Maroua Faida",
            };
    private String[] emails=
            {
                    "adamou_yahouza@um5.ac.ma",
                    "ayoub_seddiki@um5.ac.ma",
                    "abdessamad_amzil@um5.ac.ma",
                    "maroua_faida@um5.ac.ma",
            };
    private String[] fields=
            {
                    "Data Science And Iot engineering",
                    "Data Science And Iot engineering",
                    "Data Science And Iot engineering",
                    "Data Science And Iot engineering",
            };
    private Integer [] images =
            {
                    R.drawable.about_yahouza,
                    R.drawable.about_ayoub,
                    R.drawable.about_abdesamad,
                    R.drawable.about_maroua
            };

    public MyAboutSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout_aboutus, null);
        /**/
        ImageView imageView = (ImageView) view.findViewById(R.id.slideImage);
        TextView emailTextView=(TextView) view.findViewById ( R.id.email_aboutUs );
        TextView fieldTextView=(TextView) view.findViewById ( R.id.field_aboutUs );
        TextView nameTextView=(TextView) view.findViewById ( R.id.username_aboutUs );

        imageView.setImageResource(images[position]);
        nameTextView.setText(names[position]);
        emailTextView.setText(emails[position]);
        fieldTextView.setText(fields[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
