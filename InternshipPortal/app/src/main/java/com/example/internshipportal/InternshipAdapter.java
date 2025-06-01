package com.example.internshipportal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.ViewHolder> {

    private List<Internship> internshipList;

    public InternshipAdapter(List<Internship> internships) {
        this.internshipList = internships;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ✅ Use proper layout for a single internship card
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_internship_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Internship i = internshipList.get(position);
        holder.title.setText("Title: " + i.getTitle());
        holder.company.setText("Company: " + i.getCompany());
        holder.location.setText("Location: " + i.getLocation());
        holder.stipend.setText("Stipend: ₹" + i.getStipend());
        holder.description.setText("Description: " + i.getDescription());
    }

    @Override
    public int getItemCount() {
        return internshipList != null ? internshipList.size() : 0;
    }

    public void setInternshipList(List<Internship> list) {
        this.internshipList = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, company, location, stipend, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.internshipTitle);
            company = itemView.findViewById(R.id.companyName);
            location = itemView.findViewById(R.id.location);
            stipend = itemView.findViewById(R.id.stipend);
            description = itemView.findViewById(R.id.internshipDescription);
        }
    }
}
