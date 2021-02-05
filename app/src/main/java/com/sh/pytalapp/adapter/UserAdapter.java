package com.sh.pytalapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.pytalapp.R;
import com.sh.pytalapp.model.User;
import com.sh.pytalapp.utils.Const;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context mContext;
    private List<User> lstUsers;
    private OnUserItemClickListener listener;

    public UserAdapter(Context mContext, List<User> lstUsers, OnUserItemClickListener listener) {
        this.mContext = mContext;
        this.lstUsers = lstUsers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = lstUsers.get(position);
        holder.tvLastLogin.setText("Last: " + user.getLastLogin());
        holder.tvCodeOTP.setText("Mã code: " + user.getCodeOTP());

        String serialNumber = user.getSerialDevice();
        if (serialNumber != null && !serialNumber.isEmpty()) {
            holder.tvStatus.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tvStatus.setBackgroundColor(mContext.getResources().getColor(R.color.colorXanh));
            holder.tvLastLogin.setText("Tình trạng: Chưa sử dụng");
        }

        String roleStr;
        String role = user.getRole();
        if (Const.USER_ROLE.ADMIN.equalsIgnoreCase(role)) {
            roleStr = "Admin";
        } else {
            roleStr = "User";
        }
        holder.tvRole.setText("Vai trò: " + roleStr);
        holder.tvType.setText("");

        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        if (lstUsers != null) {
            return lstUsers.size();
        } else {
            return 0;
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvCodeOTP, tvLastLogin, tvType, tvRole, tvStatus;
        public RelativeLayout viewBackground, viewForeground;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvTypeItemCode);
            tvRole = itemView.findViewById(R.id.tvRoleItemCode);
            tvStatus = itemView.findViewById(R.id.tvStatusItemCode);
            tvCodeOTP = itemView.findViewById(R.id.tvCodeOTPItemCode);
            tvLastLogin = itemView.findViewById(R.id.tvLastLoginItemUser);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        public void bind(final User user, final OnUserItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(user));
        }

    }

    public interface OnUserItemClickListener {
        void onClickItem(User user);
    }

}