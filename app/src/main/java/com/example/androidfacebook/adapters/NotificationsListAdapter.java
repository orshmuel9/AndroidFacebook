package com.example.androidfacebook.adapters;

import static com.example.androidfacebook.login.Login.ServerIP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.addspages.EditUser;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.comments.CommentPage;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.notification.NotificationPage;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationViewHolder> {
    // Set the image view with the bytes array
    public void setImageViewWithBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or leave it empty
            imageView.setImageDrawable(null);
        }
    }
    public byte[] convertBase64ToByteArray(String base64Image) {
        if (base64Image != null && base64Image.startsWith("data:image/jpeg;base64,")) {
            String base64EncodedImage = base64Image.substring("data:image/jpeg;base64,".length());
            return Base64.decode(base64EncodedImage, Base64.DEFAULT);
        }
        return null;
    }
    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconUser;
        private final TextView tvAuthor;
        private final ImageView approveButton;
        private final ImageView declineButton;

        // this is the constructor of the class
        private NotificationViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            iconUser=itemView.findViewById(R.id.iconUser);
            approveButton = itemView.findViewById(R.id.approve);
            declineButton = itemView.findViewById(R.id.decline);
        }
    }



    private final LayoutInflater mInflater;
    private List<ClientUser> notifications;
    private ClientUser userLoggedIn;
    public NotificationsListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    // this method is used to create the view holder for the notifications
    @Override
    public NotificationsListAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.notification_item,parent,false);
        return new NotificationsListAdapter.NotificationViewHolder(itemView);
    }
    // this method is used to bind the view holder with the notifications
    public void onBindViewHolder(NotificationsListAdapter.NotificationViewHolder holder, int position) {
        if (notifications != null) {
            final ClientUser current = notifications.get(position);
            holder.tvAuthor.setText(current.getDisplayName());
            String imgNotif = current.getPhoto();
            byte[] iconBytes = convertBase64ToByteArray(current.getPhoto());
            setImageViewWithBytes(holder.iconUser, iconBytes);



            // Approve Button Click Listener
            holder.approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    UserAPI userAPI = new UserAPI(ServerIP);
                    String token=DataHolder.getInstance().getToken();
                    userAPI.acceptFriendRequest(token, userLoggedIn.getId(), current.getId(), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            int statusCode = response.code();
                            if(statusCode == 200){
                                notifications.remove(current);
                                Toast.makeText(context, "added to your friends successfully", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "Failed to accept friend!!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // Decline Button Click Listener
            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    UserAPI userAPI = new UserAPI(ServerIP);
                    String token=DataHolder.getInstance().getToken();
                    userAPI.deleteFriendRequest(token, userLoggedIn.getId(), current.getId(), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            int statusCode = response.code();
                            if(statusCode == 200){
                                notifications.remove(current);
                                Toast.makeText(context, "declined successfully", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "Failed to decline friend!!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }
    }


    public void setNotifications(List<ClientUser> s, ClientUser user){
        this.userLoggedIn = user;
        this.notifications = s;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if(notifications!=null){
            return notifications.size();
        }
        else{
            return 0;
        }
    }
    public List<ClientUser> getNotifications() {return notifications;}
}
