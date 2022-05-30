package com.example.zoo.ui.views.kindsList.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zoo.R;
import com.example.zoo.db.entities.AnimalEntity;

import java.util.List;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.ViewHolder> {

    List<AnimalEntity> mAnimals;
    Context mContext;

    public ChildRecyclerAdapter(Context context, List<AnimalEntity> animals) {
        mContext = context;
        mAnimals = animals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.animal_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(mAnimals.get(position).name);
        holder.aviaryNumberTextView.setText(mAnimals.get(position).aviary + "");
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView aviaryNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.animalName);
            aviaryNumberTextView = itemView.findViewById(R.id.aviaryNumber);
        }
    }
}
