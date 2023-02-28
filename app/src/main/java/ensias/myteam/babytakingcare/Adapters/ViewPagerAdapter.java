package ensias.myteam.babytakingcare.Adapters;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import ensias.myteam.babytakingcare.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private String[] titre={"Chambre enfant","Chambre simple","chambre double","Suite","Suite royale"};
    private String[] descr={"Chambre enfant bien équipée","Chambre simple avec confort","chambre double pour couple","Suite bien équipée pour famille","Suite royale de luxe" };
    private Integer [] images = {R.drawable.chambre1,R.drawable.chambre2,R.drawable.chambre3,R.drawable.chambre4,R.drawable.chambre5};

    public ViewPagerAdapter(Context context) {
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
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView=(TextView) view.findViewById ( R.id.titre );
        TextView textView2=(TextView) view.findViewById ( R.id.description );
        imageView.setImageResource(images[position]);
        textView.setText ( titre[position] );
        textView2.setText ( descr[position] );

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    Toast.makeText(context, "Chambre pour enfant", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    Toast.makeText(context, "Chambre simple", Toast.LENGTH_SHORT).show();
                } else if(position == 2){
                    Toast.makeText(context, "Chambre double", Toast.LENGTH_SHORT).show();
                }
                else if(position == 3){
                    Toast.makeText(context, "Suite", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Suite royale", Toast.LENGTH_SHORT).show();
                }

            }
        });

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

