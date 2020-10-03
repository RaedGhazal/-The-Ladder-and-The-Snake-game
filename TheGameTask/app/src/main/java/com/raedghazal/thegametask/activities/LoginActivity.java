package com.raedghazal.thegametask.activities;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.raedghazal.thegametask.models.Player;
import com.raedghazal.thegametask.R;
import com.raedghazal.thegametask.SharedPreferencesHandler;
import com.raedghazal.thegametask.db.DatabaseController;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
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
        setContentView(R.layout.activity_login);

        initialization();
    }

    private void initialization() {
        databaseController = new DatabaseController(this);
        sharedPreferencesHandler = new SharedPreferencesHandler(this);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        imgShowHidePassword = findViewById(R.id.img_show_hide_password);

        //set filter to avoid using spaces in username and password
        etUsername.setFilters(new InputFilter[]{spaceFilter});
        etPassword.setFilters(new InputFilter[]{spaceFilter});

        //show imgShowHidePassword only if etPassword contains text
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
        manageNavigatingToRegisterScreen();
    }

    private void manageNavigatingToRegisterScreen() {
        //make "Register" clickable and change its color
        TextView tvRegisterMessage = findViewById(R.id.tv_register_message);
        SpannableString strRegister = new SpannableString(getResources().getString(R.string.register_message));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(true);
            }
        };
        strRegister.setSpan(clickableSpan, 23, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegisterMessage.setText(strRegister);
        tvRegisterMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void ShowHidePassword(View view) {
        ImageButton img = (ImageButton) view;
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

    private void login(Player player) {
        sharedPreferencesHandler.login(player.getPlayerID(), player.getUsername());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void clickLogin(View view) {
        if (!isEtsEmpty()) {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (databaseController.checkPasswordIfCorrect(username, password)) {
                Player player = databaseController.getPlayerByUsername(username);
                login(player);
            } else if (!databaseController.checkUsernameIfExist(username))
                etUsername.setError("Username does not exist!");
            else
                etPassword.setError("incorrect password!");
        }
    }
}