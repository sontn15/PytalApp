package com.sh.pytalapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sh.pytalapp.R;
import com.sh.pytalapp.model.dto.NumberDTO;

import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ChooseNumberViewHolder> {
    private Context mContext;
    private List<NumberDTO> listNumbers;
    private OnNumberClickListener listener;


    public NumberAdapter(Context mContext, List<NumberDTO> listNumbers, OnNumberClickListener listener) {
        this.mContext = mContext;
        this.listNumbers = listNumbers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChooseNumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_number, parent, false);
        return new NumberAdapter.ChooseNumberViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ChooseNumberViewHolder holder, int position) {
        final NumberDTO numberDTO = listNumbers.get(position);
        holder.tvNumber.setText(numberDTO.getValue());
        if (numberDTO.isSelected()) {
            holder.tvNumber.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.tvNumber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_textview_color_app));
        } else {
            holder.tvNumber.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvNumber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_textview_number_white));
        }
        holder.itemView.setOnClickListener(view -> listener.onClickItem(numberDTO));
    }

    @Override
    public int getItemCount() {
        if (listNumbers != null) {
            return listNumbers.size();
        } else {
            return 0;
        }
    }

    public static class ChooseNumberViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvNumber;

        public ChooseNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumberItem);
        }
    }

    public interface OnNumberClickListener {
        void onClickItem(NumberDTO numberDTO);
    }

}
