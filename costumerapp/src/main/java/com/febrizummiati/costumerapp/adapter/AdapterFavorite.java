package com.febrizummiati.costumerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.febrizummiati.costumerapp.R;
import com.febrizummiati.costumerapp.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.FavoriteViewHolder> {
    private ArrayList<User> listUser = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    public AdapterFavorite() {
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


    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        final TextView listName;
        final CircleImageView imageAvatar;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            imageAvatar = itemView.findViewById(R.id.list_avatar);
        }

        void bind(final User user) {
            listName.setText(user.getLogin());
            Glide.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .apply(new RequestOptions().override(60, 60))
                    .into(imageAvatar);

            itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listUser.get(FavoriteViewHolder.this.getAdapterPosition())));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
