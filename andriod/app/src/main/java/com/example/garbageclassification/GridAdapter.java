package com.example.garbageclassification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.LinearViewHolder> {

    private Context mContext;
    private List<String> stringList;
    private OnItemClickListener mListener;

    public GridAdapter(Context context, List<String> list, OnItemClickListener listener){
        this.mContext = context;
        this.stringList = list;
        this.mListener = listener;
    }
    @NonNull
    @Override
    public GridAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GridAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.LinearViewHolder holder, int position) {
        holder.textView.setText(stringList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(stringList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.grid_tv);
        }
    }

    public interface OnItemClickListener{
        void onClick(String find);
    }
}
