package com.example.internshipportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameET, emailET, passwordET;
    private Button registerBtn;
    private Spinner roleSpinner;
    private TextView loginSwitchTV;

    private final String[] roles = {"Select Role", "Admin", "User"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind views
        usernameET = findViewById(R.id.etUsername);
        emailET = findViewById(R.id.etEmail);
        passwordET = findViewById(R.id.etPassword);
        registerBtn = findViewById(R.id.btnRegister);
        loginSwitchTV = findViewById(R.id.textloginSwitch);
        roleSpinner = findViewById(R.id.spinnerRole);

        // Set spinner data manually (in case not using XML binding)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Redirect to login activity
        loginSwitchTV.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        // Handle register button click
        registerBtn.setOnClickListener(v -> {
            String username = usernameET.getText().toString().trim();
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || role.equals("Select Role")) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest user = new RegisterRequest(username, email, password, role.toLowerCase());

            ApiService api = RetrofitClient.getApiService();
            api.registerUser(user).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String message = response.body() != null ? response.body().getMessage() : "Success";
                            if (message == null || message.trim().isEmpty()) {
                                message = "Registration completed.";
                            }
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace(); // helpful for logs
                        }

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
