package ensias.myteam.babytakingcare.fragments;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ensias.myteam.babytakingcare.Adapters.NotificationsListAdapter;
import ensias.myteam.babytakingcare.Models.Baby;
import ensias.myteam.babytakingcare.Models.Notification;
import ensias.myteam.babytakingcare.R;

public class NotificationFragment extends Fragment {

    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private FirebaseDatabase db ;
    private DatabaseReference reference ;
    private List<Notification> notifications ;
    private Notification notification ;
    private RecyclerView notificationsList_rv ;
    private NotificationsListAdapter notificationsListAdapter ;
    private LinearLayout buttonSeeMore ;
    private TextView notificationsNumber ;
    int clickNumbers = 1 ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        initialisation(view);
        this.reference = db
                            .getReference("babiesDb")
                            .child(this.user.getUid())
                            .child("notifications");
        this.reference.addValueEventListener(new  ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                    notification = notificationSnapshot.getValue(Notification.class);
                    notifications.add(notification);
                }
                renderNotifications();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(clickNumbers * 3 < notifications.size())
                {

                }
            }
        });

        return view ;

    }

    private void initialisation(View view)
    {
        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.db = FirebaseDatabase.getInstance() ;
        this.notifications = new ArrayList<>();
        this.buttonSeeMore = view.findViewById(R.id.buttonSeeMore);
        this.notificationsList_rv = view.findViewById(R.id.notificationsList_rv);
        this.notificationsNumber = view.findViewById(R.id.notificationsNumber);

        buttonSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentSize = notificationsListAdapter.getItemCount();
                int newSize = currentSize + 1;
                if (newSize >= notifications.size()) {
                    buttonSeeMore.setVisibility(View.GONE);
                    newSize = notifications.size();
                }
                else if(buttonSeeMore.getVisibility() == View.GONE){
                    buttonSeeMore.setVisibility(View.VISIBLE);
                }
                notificationsListAdapter.updateNotifications(notifications.subList(0, newSize));
            }
        });

    }

    private void renderNotifications()
    {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        notificationsList_rv.setLayoutManager(linearLayoutManager);
        notificationsList_rv.setHasFixedSize(true);
        if(notifications.size()<=3)
        {
            this.buttonSeeMore.setVisibility(View.GONE);
            notificationsListAdapter = new NotificationsListAdapter(getActivity(), notifications);
        }
        else{
            this.buttonSeeMore.setVisibility(View.VISIBLE);
            notificationsListAdapter = new NotificationsListAdapter(getActivity(), notifications.subList(0,3));
        }
        //
        notificationsList_rv.setAdapter(notificationsListAdapter);
        this.notificationsNumber.setText(String.valueOf(notifications.size()));

    }
}