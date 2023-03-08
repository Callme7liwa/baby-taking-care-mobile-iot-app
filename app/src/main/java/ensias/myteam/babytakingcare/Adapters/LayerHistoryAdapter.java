package ensias.myteam.babytakingcare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ensias.myteam.babytakingcare.R;
import ensias.myteam.babytakingcare.dto.LayerHistoryDto;

public class LayerHistoryAdapter extends RecyclerView.Adapter<LayerHistoryAdapter.LayerHistoryViewHolder> {

    private List<LayerHistoryDto> layers_history  ;
    private Context context ;

    public LayerHistoryAdapter(Context context , List<LayerHistoryDto> layers_history)
    {
        this.context = context ;
        this.layers_history = layers_history ;
    }

    public void setLayers_history(List<LayerHistoryDto> layers_history)
    {
        this.layers_history = layers_history;
        System.out.println("*************** IM HERE BRO ******************* " + layers_history.size());

        layers_history.forEach(history->{
            System.out.println("the date => "+history.getDate());
        });
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LayerHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layer_history_baby, parent, false);
        return new LayerHistoryViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull LayerHistoryViewHolder holder, int position) {
        holder.changingDate.setText(layers_history.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return this.layers_history.size();
    }


    public class LayerHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView changingDate ;


        public LayerHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.changingDate = itemView.findViewById(R.id.changing_date);
        }
    }
}
