package com.febrizummiati.githubuserfinal.adapter;

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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private ArrayList<User> listUser = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;


    public FavoriteAdapter() {
    }

    public ArrayList<User> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<User> listUser) {
        this.listUser.clear();
        this.listUser.addAll(listUser);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(listUser.get(position));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        final TextView tvName;
        final CircleImageView tvAvatar;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.ls_name);
            tvAvatar = itemView.findViewById(R.id.ls_avatar);
        }

        void bind (final User user) {
            tvName.setText(user.getLogin());
            Glide.with(itemView.getContext())
                    .load(user.getUrlAvatar())
                    .apply(new RequestOptions().override(60, 60))
                    .into(tvAvatar);

            itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listUser.get(FavoriteViewHolder.this.getAdapterPosition())));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
