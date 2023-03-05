package com.example.garbageclassification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context mContext;
    private List<String> stringList;
    private String want;
    private OnItemClickListener mListener;

    public LinearAdapter(Context context,List<String> list,String want,OnItemClickListener listener){
        this.mContext = context;
        this.stringList = list;
        this.want = want;
        this.mListener = listener;
    }
    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, int position) {
        holder.textView.setText(stringList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick();
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
            textView = itemView.findViewById(R.id.item_tv);
        }
    }

    public interface OnItemClickListener{
        void onClick();
    }
}
