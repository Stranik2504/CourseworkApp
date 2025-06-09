package dev.stranik.coursework.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import dev.stranik.coursework.R;
import dev.stranik.coursework.utils.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvUserName.setText(user.getName());
        holder.cbUserSelected.setChecked(user.isSelected());
        holder.cbUserSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            user.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateList(List<User> newUsers) {
        users = newUsers;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbUserSelected;
        TextView tvUserName;

        UserViewHolder(View itemView) {
            super(itemView);
            cbUserSelected = itemView.findViewById(R.id.cb_user_selected);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
        }
    }
}