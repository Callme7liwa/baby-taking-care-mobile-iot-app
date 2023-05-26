package ensias.myteam.babytakingcare.fragments;

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
import ensias.myteam.babytakingcare.Models.Notification;
import ensias.myteam.babytakingcare.R;


public class DiapersNotificationFragment extends Fragment {

    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private FirebaseDatabase db ;
    private DatabaseReference reference ;
    private List<Notification> notifications ;
    private Notification notification ;
    private RecyclerView notificationsList_rv ;
    private NotificationsListAdapter notificationsListAdapter ;
    private TextView notificationsNumber ;
    int clickNumbers = 1 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_notification_diapers, container, false);

        initialisation(view);
        this.reference = db
                .getReference("babiesDb")
                .child(this.user.getUid())
                .child("notifications")
                .child("diapers");
        this.reference.addValueEventListener(new  ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
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
        this.notificationsList_rv = view.findViewById(R.id.notificationDiapers_rv);
        this.notificationsNumber = view.findViewById(R.id.notificationsNumber);
    }

    private void renderNotifications()
    {
        if(notifications.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            notificationsList_rv.setLayoutManager(linearLayoutManager);
            notificationsList_rv.setHasFixedSize(true);
            notificationsListAdapter = new NotificationsListAdapter(getActivity(), notifications, "diapers");
            notificationsList_rv.setAdapter(notificationsListAdapter);
            this.notificationsNumber.setText(String.valueOf(notifications.size()));
        }

    }
}