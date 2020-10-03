package com.raedghazal.thegametask.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.raedghazal.thegametask.R;
import com.raedghazal.thegametask.models.Game;

public class GameBoardAdapter extends BaseAdapter {
    private Context context;
    private Game game;

    public GameBoardAdapter(Context context, Game game) {
        this.context = context;
        this.game = game;
    }

    @Override
    public int getCount() {
        return 24;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            assert inflater != null;
            convertView = inflater.inflate(R.layout.board_item, null);
        }

        setViewsContent(convertView, position);
        return convertView;
    }

    private void setViewsContent(View view, int position) {
        ImageView imgPH1 = view.findViewById(R.id.img_place_holder1);
        ImageView imgPH2 = view.findViewById(R.id.img_place_holder2);
        TextView tvCellNumber = view.findViewById(R.id.tv_cell_number);

        imgPH1.setVisibility(View.INVISIBLE);
        imgPH2.setVisibility(View.INVISIBLE);
        boolean placeHolder1isUsed = false;
        int cell = getCellNumber(position);
        tvCellNumber.setText(String.valueOf(cell));

        //setting player 1 position
        if (game.getPlayer1Location() == cell) {
            imgPH1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_player_1));
            imgPH1.setVisibility(View.VISIBLE);
            placeHolder1isUsed = true;
        }

        //setting player 2 position
        if (game.getPlayer2Location() == cell) {
            if (!placeHolder1isUsed) {
                imgPH1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_player_2));
                imgPH1.setVisibility(View.VISIBLE);
            } else {
                imgPH2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_player_2));
                imgPH2.setVisibility(View.VISIBLE);
            }
        }
    }

    // gets cell number to match SnakeAndLadder cells order
    private int getCellNumber(int position) {
        int line = (int) Math.floor(position / 4.0) + 1;
        int cell;
        if (line % 2 != 0)
            cell = position + 1;
        else
            cell = 8 * line - position - 4;
        return 25 - cell;
    }
}
