package com.raedghazal.thegametask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raedghazal.thegametask.R;
import com.raedghazal.thegametask.models.SnakeLadder;

import java.util.ArrayList;

public class SnakeAndLadderAdapter extends RecyclerView.Adapter<SnakeAndLadderAdapter.MyViewHolder> {
    private ArrayList<SnakeLadder> snakeLadders;
    private Context context;

    public SnakeAndLadderAdapter(Context context, ArrayList<SnakeLadder> snakeLadders) {
        this.context = context;
        this.snakeLadders = snakeLadders;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFrom, tvTo;
        private ImageView imgSnakeLadder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
            imgSnakeLadder = itemView.findViewById(R.id.img_snake_ladder);
        }
    }

    @NonNull
    @Override
    public SnakeAndLadderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snake_ladder_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SnakeAndLadderAdapter.MyViewHolder holder, int position) {
        SnakeLadder snakeLadder = snakeLadders.get(position);
        if (snakeLadder.getType() == SnakeLadder.Type.SNAKE) {
            holder.tvFrom.setText(String.valueOf(snakeLadders.get(position).getFrom()));
            holder.tvTo.setText(String.valueOf(snakeLadders.get(position).getTo()));
        } else {
            holder.imgSnakeLadder.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_snake));
            holder.tvFrom.setText(String.valueOf(snakeLadders.get(position).getFrom()));
            holder.tvTo.setText(String.valueOf(snakeLadders.get(position).getTo()));
            holder.imgSnakeLadder.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_ladder));
        }
    }

    @Override
    public int getItemCount() {
        return snakeLadders.size();
    }

}
