package ensias.myteam.babytakingcare.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Listener.ClickListener;
import ensias.myteam.babytakingcare.Models.Baby;
import ensias.myteam.babytakingcare.R;

public class BabyAdapter extends RecyclerView.Adapter<BabyAdapter.BabyViewHolder> {

    public List<Baby> babies ;
    public Baby currentBaby ;
    public ClickListener<Baby> listener;
    private Context context ;
    private int currentPosition=0;

    public BabyAdapter(Context context, List<Baby> babies  , ClickListener<Baby> listener ) {
        this.context = context;
        this.babies = babies;
        currentBaby = babies.get(0);
        this.listener = listener ;
    }



    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public BabyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BabyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baby, null));
    }

    @Override
    public void onBindViewHolder(@NonNull BabyViewHolder holder, int position) {
        Log.println(Log.INFO,"VIEW HOLDER", "this is the name => "+position+babies.get(position).getName() );
        holder.textView.setText(babies.get(position).getName());
        // Vérifie si la position actuelle correspond à la position sélectionnée
        if (position == currentPosition) {
            holder.itemView.setBackgroundResource(R.drawable.input_background_border_blue);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.input_background);
        }
    }

    @Override
    public int getItemCount() {
        return babies.size();
    }

    public class BabyViewHolder extends RecyclerView.ViewHolder {
        private ImageView babyImage ;
        private TextView  textView  ;

        public BabyViewHolder(@NonNull View itemView) {
            super(itemView);
            babyImage = itemView.findViewById(R.id.baby_image);
            textView = itemView.findViewById(R.id.baby_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        currentPosition = position;
                        Baby selectedBaby = babies.get(position);
                        listener.onClick(selectedBaby);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

}
