package com.example.zoo.ui.views.kindsList;

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

@RequiresApi(api = Build.VERSION_CODES.N)
public class AnimalsSubListAdapter extends BaseAdapter {

    private final Context context;

    private final List<AnimalEntity> mAnimalsList = new ArrayList<>();

    public AnimalsSubListAdapter(Context context) {
        this.context = context;
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
        animalListItemBinding.aviaryNumber.setText(animal.aviary + "");
        return animalListItemBinding.getRoot();
    }

    public void updateAnimalsList(List<AnimalEntity> animals){
        mAnimalsList.clear();
        mAnimalsList.addAll(animals);
        notifyDataSetChanged();
    }

}
