package com.raedghazal.thegametask.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.raedghazal.thegametask.models.Player;
import com.raedghazal.thegametask.R;
import com.raedghazal.thegametask.SharedPreferencesHandler;
import com.raedghazal.thegametask.db.DatabaseController;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFullname, etUsername, etPassword;
    private DatabaseController databaseController;
    private SharedPreferencesHandler sharedPreferencesHandler;
    private boolean passwordVisibility = false;
    private ImageButton imgShowHidePassword;


    private InputFilter spaceFilter = (source, start, end, dest, dstart, dend) -> {
        String blockCharacterSet = " ";
        if (source != null && blockCharacterSet.contains(("" + source))) {
            return "";
        }
        return null;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialization();
    }

    private void initialization() {
        databaseController = new DatabaseController(this);
        sharedPreferencesHandler = new SharedPreferencesHandler(this);
        etFullname = findViewById(R.id.et_fullname);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        imgShowHidePassword = findViewById(R.id.img_show_hide_password);

        //set filter to avoid using spaces in username and password
        etUsername.setFilters(new InputFilter[]{spaceFilter});
        etPassword.setFilters(new InputFilter[]{spaceFilter});

        imgShowHidePassword.setVisibility(View.INVISIBLE);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().toString().isEmpty())
                    imgShowHidePassword.setVisibility(View.INVISIBLE);
                else
                    imgShowHidePassword.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        manageNavigatingToLoginScreen();
    }

    private void manageNavigatingToLoginScreen() {
        TextView tvLoginMessage = findViewById(R.id.tv_login_message);
        SpannableString strLogin = new SpannableString(getResources().getString(R.string.login_message));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(true);
            }
        };
        strLogin.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLoginMessage.setText(strLogin);
        tvLoginMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void ShowHidePassword(View view) {
        ImageView img = (ImageView) view;
        if (passwordVisibility) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye));
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_off));
            etPassword.setTransformationMethod(null);
        }
        etPassword.setSelection(etPassword.getText().toString().length());
        passwordVisibility = !passwordVisibility;
    }

    private boolean isEtsEmpty() {
        boolean empty = false;
        if (etFullname.getText().toString().trim().isEmpty()) {
            etFullname.setError("Username can not be empty!");
            empty = true;
        }
        if (etUsername.getText().toString().trim().isEmpty()) {
            etUsername.setError("Username can not be empty!");
            empty = true;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Password can not be empty!");
            empty = true;
        }
        return empty;
    }

    private void register(Player player) {
        player.setPlayerID(databaseController.addPlayer(player));
        sharedPreferencesHandler.login(player.getPlayerID(), player.getUsername());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void clickRegister(View view) {
        if (!isEtsEmpty()) {
            String fullname = etFullname.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (databaseController.checkUsernameIfExist(username)) {
                etUsername.setError("Username already exists!");
            } else {
                Player player = new Player();
                player.setFullName(fullname);
                player.setUsername(username);
                player.setPassword(password);
                register(player);
            }
        }
    }
}