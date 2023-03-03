package com.example.main.ui;


import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;

import java.util.ArrayList;

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ViewHolder> {
    private ArrayList<Review> reviews;
    private LayoutInflater inflater;
    private FragmentActivity i;

    public ReviewsRecyclerAdapter( LayoutInflater l, FragmentActivity i) {

        this.inflater = l;
        this.i = i;
    }

    @NonNull
    @Override
    public ReviewsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item,parent,false);
        ReviewsRecyclerAdapter.ViewHolder holder = new ReviewsRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsRecyclerAdapter.ViewHolder holder, int position) {
        String a,b,c;
        a= holder.txtName.getText().toString();
        a+= reviews.get(holder.getAdapterPosition()).getName();
        c = holder.txtType.getText().toString();
        c+=reviews.get(holder.getAdapterPosition()).getType();
        holder.txtName.setText(a);
        holder.txtType.setText(c);
        holder.rating.setRating(reviews.get(holder.getAdapterPosition()).getRating());
        holder.txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Details about the review");
                // set the custom layout
                final View customLayout =inflater.inflate(R.layout.simple_details_layout, null);
                TextView txtDet = customLayout.findViewById(R.id.details);
                txtDet.setText(reviews.get(holder.getAdapterPosition()).getDetails());
                builder.setView(customLayout);
                AlertDialog dialog = builder.create();
                dialog.show();
                Button btn = customLayout.findViewById(R.id.okBtn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{ //view holder to set the txt views and listeners
        private TextView txtName,txtType,txtDetails;
        private RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName =  itemView.findViewById(R.id.txtCount);
            txtType = itemView.findViewById(R.id.txtTime);
            txtDetails = itemView.findViewById(R.id.txtdet);
            rating = itemView.findViewById(R.id.reviewRating);
        }
    }

}
