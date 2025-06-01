package com.example.internshipportal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_INTERNSHIP_REQUEST = 1;

    RecyclerView recyclerView;
    InternshipAdapter adapter;
    Button addBtn;

    SharedPrefManager session;
    ArrayList<Internship> internshipList;
    private ActivityResultLauncher<Intent> addInternshipLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SharedPrefManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        addBtn = findViewById(R.id.addInternshipBtn);

        internshipList = new ArrayList<>();
        adapter = new InternshipAdapter(internshipList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String role = session.getRole();
        if (role != null && role.trim().equalsIgnoreCase("user")) {
            addBtn.setVisibility(View.GONE);
        } else {
            addBtn.setVisibility(View.VISIBLE);
        }

        addInternshipLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String title = data.getStringExtra("title");
                        String company = data.getStringExtra("company");
                        String description = data.getStringExtra("description");
                        String stipend = data.getStringExtra("stipend");
                        String location = data.getStringExtra("location");

                        Internship internship = new Internship(title, company, description, stipend, location);
                        internshipList.add(internship);
                        adapter.notifyItemInserted(internshipList.size() - 1);
                        recyclerView.scrollToPosition(internshipList.size() - 1);
                    }
                }
        );

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddInternshipActivity.class);
            addInternshipLauncher.launch(intent);
        });

        loadInternshipsFromAPI();
    }


    private void loadInternshipsFromAPI() {
        String token = session.getToken();
        Log.d("TOKEN", "Token: " + token);

        Call<List<Internship>> call = RetrofitClient.getApiService().getInternships("Bearer " + token);
        call.enqueue(new Callback<List<Internship>>() {
            @Override
            public void onResponse(Call<List<Internship>> call, Response<List<Internship>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    internshipList.clear();
                    internshipList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Code: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to load internships", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Internship>> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Only Admin can add
        if (requestCode == ADD_INTERNSHIP_REQUEST && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String company = data.getStringExtra("company");
            String description = data.getStringExtra("description");
            String stipend = data.getStringExtra("stipend");
            String location = data.getStringExtra("location");

            Internship internship = new Internship(title, company, description, stipend, location);
            internshipList.add(internship);
            adapter.notifyItemInserted(internshipList.size() - 1);
        }
    }
}
