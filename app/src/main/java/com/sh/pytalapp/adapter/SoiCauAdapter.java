package com.sh.pytalapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.pytalapp.R;
import com.sh.pytalapp.model.SoiCau;

import java.util.List;

public class SoiCauAdapter extends RecyclerView.Adapter<SoiCauAdapter.SoiCauMbViewHolder> {
    private final Context mContext;
    private final List<SoiCau> lstSoiCaus;
    private final OnSoiCauItemClickListener listener;

    public SoiCauAdapter(Context mContext, List<SoiCau> lstSoiCaus, OnSoiCauItemClickListener listener) {
        this.mContext = mContext;
        this.lstSoiCaus = lstSoiCaus;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SoiCauMbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_soicau, parent, false);
        return new SoiCauMbViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoiCauMbViewHolder holder, int position) {
        SoiCau soiCau = lstSoiCaus.get(position);
        holder.tvDate.setText(soiCau.getCreatedDate());
        holder.tvBachThu.setText("Bạch thủ lô: " + soiCau.getBachThuLo());
        holder.tvSongThu.setText("Song thủ lô: " + soiCau.getSongThuLo1() + "  " + soiCau.getSongThuLo2());
        if (soiCau.getNhaDai() != null && !soiCau.getNhaDai().isEmpty()) {
            holder.tvMien.setText("Đài: " + soiCau.getNhaDai() + " - " + soiCau.getMien());
        } else {
            holder.tvMien.setText(soiCau.getMien());
        }
        holder.bind(soiCau, listener);
    }

    @Override
    public int getItemCount() {
        if (lstSoiCaus != null) {
            return lstSoiCaus.size();
        } else {
            return 0;
        }
    }

    public static class SoiCauMbViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvDate, tvBachThu, tvSongThu, tvMien;
        protected Button btnEdit;

        public SoiCauMbViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateSoiCauItem);
            tvBachThu = itemView.findViewById(R.id.tvBachThuLoSoiCauItem);
            tvSongThu = itemView.findViewById(R.id.tvSongThuLoSoiCauItem);
            btnEdit = itemView.findViewById(R.id.btnEditSoiCauItem);
            tvMien = itemView.findViewById(R.id.tvBaMienSoiCauItem);
        }

        public void bind(final SoiCau soiCau, final OnSoiCauItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(soiCau));
            btnEdit.setOnClickListener(v -> listener.onClickButtonEditItem(soiCau));
        }
    }

    public interface OnSoiCauItemClickListener {
        void onClickItem(SoiCau soiCau);

        void onClickButtonEditItem(SoiCau soiCau);
    }
}

