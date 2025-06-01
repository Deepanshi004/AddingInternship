package com.example.internshipportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInternshipActivity extends AppCompatActivity {

    private EditText titleEditText, companyEditText, descriptionEditText, stipendEditText, locationEditText;
    private Button submitButton;
    private SharedPrefManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_internship);

        session = new SharedPrefManager(this);

        titleEditText = findViewById(R.id.internshipTitle);     // Make sure your XML has these IDs
        companyEditText = findViewById(R.id.companyName);
        descriptionEditText = findViewById(R.id.internshipDescription);
        stipendEditText = findViewById(R.id.stipend);
        locationEditText = findViewById(R.id.location);
        submitButton = findViewById(R.id.applyButton);          // Match button ID with your layout

        submitButton.setOnClickListener(v -> addInternship());
    }

    private void addInternship() {
        String title = titleEditText.getText().toString().trim();
        String company = companyEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String stipend = stipendEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (title.isEmpty() || company.isEmpty() || description.isEmpty() || stipend.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Internship internship = new Internship(title, company, description, stipend, location);
        String token = session.getToken(); // Assuming Bearer token authentication

        Call<Internship> call = RetrofitClient.getApiService().addInternship("Bearer " + token, internship);

        call.enqueue(new Callback<Internship>() {
            @Override
            public void onResponse(Call<Internship> call, Response<Internship> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddInternshipActivity.this, "Internship added successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("title", title);
                    resultIntent.putExtra("company", company);
                    resultIntent.putExtra("description", description);
                    resultIntent.putExtra("stipend", stipend);
                    resultIntent.putExtra("location", location);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    try {
                        String error = response.errorBody().string();
                        Toast.makeText(AddInternshipActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddInternshipActivity.this, "Failed with code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Internship> call, Throwable t) {
                Toast.makeText(AddInternshipActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
