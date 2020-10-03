package com.raedghazal.thegametask.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raedghazal.thegametask.R;
import com.raedghazal.thegametask.activities.GameActivity;
import com.raedghazal.thegametask.db.DatabaseHelper;
import com.raedghazal.thegametask.models.Game;

import java.util.ArrayList;

public class SavedGamesAdapter extends RecyclerView.Adapter<SavedGamesAdapter.MyViewHolder> {
    private ArrayList<Game> games;
    private Context context;

    public SavedGamesAdapter(Context context, ArrayList<Game> games) {
        this.context = context;
        this.games = games;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSavedDate;
        private Button btnLoad;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSavedDate = itemView.findViewById(R.id.tv_saved_date);
            btnLoad = itemView.findViewById(R.id.btn_load);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_games_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Game game = games.get(position);
        String gameDate = DatabaseHelper.SQLITE_FORMAT.format(game.getDate());
        holder.tvSavedDate.setText(String.format(context.getResources().getString(R.string.savedGamesMessage), gameDate));
        holder.btnLoad.setOnClickListener(v -> {
            Intent intent = new Intent(context, GameActivity.class);
            intent.putExtra("isLoaded", true);
            intent.putExtra("gameId", game.getGameId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

}
