package com.example.androidbtap4_minhhoang;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvAppTitle, tvWelcome;
    private TextInputLayout tilPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
        setupWelcomeAnimation();
        setupPasswordToggle();
    }

    private void initViews() {
        tvAppTitle = findViewById(R.id.tv_app_title);
        tvWelcome = findViewById(R.id.tv_welcome);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tilPassword = findViewById(R.id.til_password);
    }

    private void setupPasswordToggle() {
        tilPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password - show dots
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tilPassword.setEndIconDrawable(R.drawable.ic_visibility_off);
                    isPasswordVisible = false;
                } else {
                    // Show password - show text
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tilPassword.setEndIconDrawable(R.drawable.ic_visibility);
                    isPasswordVisible = true;
                }
                // Move cursor to end
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        // Set initial state - password hidden with crossed eye
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tilPassword.setEndIconDrawable(R.drawable.ic_visibility_off);
        isPasswordVisible = false;
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    private void performLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Reset errors
        etUsername.setError(null);
        etPassword.setError(null);

        if (validateInput(username, password)) {
            // Show loading effect
            btnLogin.setText("Đang đăng nhập...");
            btnLogin.setEnabled(false);

            // Simulate login process
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Reset button
                    btnLogin.setText("Đăng nhập");
                    btnLogin.setEnabled(true);

                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();

                    // Clear input fields after successful login
                    etUsername.setText("");
                    etPassword.setText("");
                }
            }, 1500);
        }
    }

    private boolean validateInput(String username, String password) {
        boolean isValid = true;

        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            isValid = false;
        } else if (username.length() < 3) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 3 ký tự");
            etUsername.requestFocus();
            isValid = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            if (isValid) etPassword.requestFocus();
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            if (isValid) etPassword.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private void goToRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void setupWelcomeAnimation() {
        // Simple fade in animation for welcome text
        tvWelcome.setAlpha(0f);
        tvWelcome.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();
    }
}
