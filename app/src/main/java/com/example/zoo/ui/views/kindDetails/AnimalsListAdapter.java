package com.example.zoo.ui.views.kindDetails;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;

import com.example.zoo.databinding.AnimalListItemBinding;
import com.example.zoo.db.entities.AnimalEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AnimalsListAdapter extends BaseAdapter {

    private final Context context;
    private final Consumer<Long> onEditAction;
    private final Consumer<Long> onDeleteAction;

    private final List<AnimalEntity> mAnimalsList = new ArrayList<>();

    public AnimalsListAdapter(
            Context context,
            Consumer<Long> onEditAction,
            Consumer<Long> onDeleteAction
    ) {
        this.context = context;
        this.onEditAction = onEditAction;
        this.onDeleteAction = onDeleteAction;
    }

    @Override
    public int getCount() {
        return mAnimalsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAnimalsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AnimalListItemBinding animalListItemBinding;

        if (convertView == null) {
            animalListItemBinding = AnimalListItemBinding.inflate(
                    LayoutInflater.from(context), parent, false
            );
        } else {
            animalListItemBinding = AnimalListItemBinding.bind(convertView);
        }
        AnimalEntity animal = (AnimalEntity) getItem(position);

        animalListItemBinding.animalName.setText(animal.name);

        animalListItemBinding.animalEditButton
                .setOnClickListener(v -> onEditAction.accept(animal.id));
        animalListItemBinding.animalDeleteButton
                .setOnClickListener(v -> onDeleteAction.accept(animal.id));

        return animalListItemBinding.getRoot();
    }

    public void updateAnimalsList(List<AnimalEntity> animals){
        mAnimalsList.clear();
        mAnimalsList.addAll(animals);
        notifyDataSetChanged();
    }

}
