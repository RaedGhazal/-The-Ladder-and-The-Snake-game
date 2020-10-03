package com.raedghazal.thegametask;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SaveGameDialog extends Dialog {
    public enum Action {
        SAVE, DISCARD, CANCEL
    }

    private Action action = Action.CANCEL;

    public SaveGameDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_game);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDiscard = findViewById(R.id.btn_discard);

        btnSave.setOnClickListener(v -> {
            action = Action.SAVE;
            dismiss();
        });
        btnDiscard.setOnClickListener(v -> {
            action = Action.DISCARD;
            dismiss();
        });
    }

    public Action getAction() {
        return action;
    }
}
