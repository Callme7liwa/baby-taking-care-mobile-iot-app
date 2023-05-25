package ensias.myteam.babytakingcare.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
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
import ensias.myteam.babytakingcare.Models.Message;
import ensias.myteam.babytakingcare.Models.Parent;
import ensias.myteam.babytakingcare.R;
import ensias.myteam.babytakingcare.UserInfoActivity;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    public List<Parent> parents ;
    public ClickListener<Parent> listener;
    private Context context ;

    public UsersAdapter(Context myContext)
    {
        this.context = myContext;
        this.parents = new ArrayList<>();
    }

    public UsersAdapter(Context context, List<Parent> parents  , ClickListener<Parent> listener ) {
        this.context = context;
        this.parents = parents;
        this.listener = listener ;
    }


    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_custom_layout, null, false);
        return new UsersAdapter.UserViewHolder(vue);    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String userId = parents.get(position).getId();
        String email = parents.get(position).getEmail();
        String fullName = parents.get(position).getFirstName() + " " + parents.get(position).getSecondName();
        if(email != null && !email.isEmpty())
            holder.getEmailTextView().setText(email);
        else
            holder.getEmailTextView().setText("Not Specified");


        holder.getUsernameTextView().setText(fullName);

        if(parents.get(position).getImage() != null && !parents.get(position).getImage().equals(""))
            holder.getParentImage().setImageURI(Uri.parse(parents.get(position).getImage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , UserInfoActivity.class);
                intent.putExtra("userId",userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parents.size();
    }

    public void addItem(Parent parent) {
        parents.add(parent);
        System.out.print("the user id in user adapter => " + parent.getId());
        notifyDataSetChanged();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView parentImage ;
        private TextView  usernameTextView  ;
        private TextView  emailTextView  ;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            parentImage = itemView.findViewById(R.id.image_users_ac);
            usernameTextView = itemView.findViewById(R.id.username_users_ac);
            emailTextView = itemView.findViewById(R.id.email_users_ac);
        }

        public ImageView getParentImage() {
            return parentImage;
        }

        public void setParentImage(ImageView parentImage) {
            this.parentImage = parentImage;
        }

        public TextView getUsernameTextView() {
            return usernameTextView;
        }

        public void setUsernameTextView(TextView usernameTextView) {
            this.usernameTextView = usernameTextView;
        }

        public TextView getEmailTextView() {
            return emailTextView;
        }

        public void setEmailTextView(TextView emailTextView) {
            this.emailTextView = emailTextView;
        }
    }

}
