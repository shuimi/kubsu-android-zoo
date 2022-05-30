package com.example.zoo.ui.views.kindsList;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;

import com.example.zoo.ZooApp;
import com.example.zoo.databinding.KindListItemBinding;
import com.example.zoo.db.dao.AnimalDao;
import com.example.zoo.db.entities.AnimalEntity;
import com.example.zoo.db.entities.KindEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.N)
public class KindsListAdapter extends BaseAdapter {

    private final Context context;
    private final Consumer<Long> onEditAction;
    private final Consumer<Long> onDeleteAction;

    private final List<KindEntity> mKindsList = new ArrayList<>();

    AnimalsSubListAdapter animalsListAdapter;

    public KindsListAdapter(
            Context context,
            Consumer<Long> onEditAction,
            Consumer<Long> onDeleteAction
    ) {
        this.context = context;
        this.onEditAction = onEditAction;
        this.onDeleteAction = onDeleteAction;
        this.animalsListAdapter = new AnimalsSubListAdapter(context);
    }

    @Override
    public int getCount() {
        return mKindsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mKindsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        KindListItemBinding kindListItemBinding;

        if (convertView == null) {
            kindListItemBinding = KindListItemBinding.inflate(
                    LayoutInflater.from(context), parent, false
            );
        } else {
            kindListItemBinding = KindListItemBinding.bind(convertView);
        }

        KindEntity kind = (KindEntity) getItem(position);

        kindListItemBinding.kindName.setText(kind.kindName);
        kindListItemBinding.animalsListPreview.setAdapter(animalsListAdapter);

        kindListItemBinding.kindEditButton
                .setOnClickListener(v -> onEditAction.accept(kind.id));
        kindListItemBinding.kindDeleteButton.
                setOnClickListener(v -> onDeleteAction.accept(kind.id));

        return kindListItemBinding.getRoot();
    }

    public void updateKindsList(List<KindEntity> kindsList) {
        this.mKindsList.clear();
        this.mKindsList.addAll(kindsList);

        AnimalDao animals = ZooApp.appDatabase.getAnimalDao();

        for (KindEntity k : mKindsList) {
            animalsListAdapter
                    .updateAnimalsList(animals.findAllByKindId(k.id));
            notifyDataSetChanged();
        }
    }

}
