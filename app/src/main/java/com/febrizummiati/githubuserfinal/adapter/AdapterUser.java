package com.febrizummiati.githubuserfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserViewHolder> {

    @SerializedName("items")
    @Expose
    private List<User> lists;
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AdapterUser() {
    }

    public List<User> getLists() {
        return lists;
    }

    public void setLists(List<User> listUsers) {
        this.lists = listUsers;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    @NonNull
    @Override
    public AdapterUser.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.UserViewHolder holder, int position) {
        holder.bind(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return this.lists.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final CircleImageView imgAvatar;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.list_name);
            imgAvatar = itemView.findViewById(R.id.list_avatar);
        }

        void bind(final User user) {
            tvName.setText(user.getLogin());
            Glide.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .apply(new RequestOptions().override(80, 80))
                    .into(imgAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(lists.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
