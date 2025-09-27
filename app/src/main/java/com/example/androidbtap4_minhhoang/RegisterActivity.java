package com.example.androidbtap4_minhhoang;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etUsername, etPassword, etConfirmPassword;
    private TextView tvDateOfBirth;
    private Button btnSelectDate, btnRegister, btnBackToLogin;
    private TextInputLayout tilPassword, tilConfirmPassword;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupClickListeners();
        setupPasswordToggles();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnRegister = findViewById(R.id.btn_register);
        btnBackToLogin = findViewById(R.id.btn_back_to_login);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
    }

    private void setupClickListeners() {
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    private void setupPasswordToggles() {
        // Setup password toggle
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
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        // Setup confirm password toggle
        tilConfirmPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmPasswordVisible) {
                    // Hide password - show dots
                    etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tilConfirmPassword.setEndIconDrawable(R.drawable.ic_visibility_off);
                    isConfirmPasswordVisible = false;
                } else {
                    // Show password - show text
                    etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tilConfirmPassword.setEndIconDrawable(R.drawable.ic_visibility);
                    isConfirmPasswordVisible = true;
                }
                etConfirmPassword.setSelection(etConfirmPassword.getText().length());
            }
        });

        // Set initial state - passwords hidden with crossed eye
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tilPassword.setEndIconDrawable(R.drawable.ic_visibility_off);
        isPasswordVisible = false;

        etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tilConfirmPassword.setEndIconDrawable(R.drawable.ic_visibility_off);
        isConfirmPasswordVisible = false;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvDateOfBirth.setText("Ngày sinh: " + selectedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void performRegistration() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (validateInput(fullName, email, username, password, confirmPassword)) {
            // Show loading effect
            btnRegister.setText("Đang đăng ký...");
            btnRegister.setEnabled(false);

            // Simulate registration process
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveUserData(fullName, email, username, password);

                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();

                    // Return to login screen after successful registration
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }, 1500);
        }
    }

    private boolean validateInput(String fullName, String email, String username, String password, String confirmPassword) {
        // Reset all errors
        etFullName.setError(null);
        etEmail.setError(null);
        etUsername.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);

        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return false;
        }

        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return false;
        } else if (username.length() < 3) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 3 ký tự");
            etUsername.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUserData(String fullName, String email, String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", fullName);
        editor.putString("email", email);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("dateOfBirth", selectedDate);
        editor.putBoolean("isRegistered", true);
        editor.apply();
    }
}
