package com.example.zoo.ui.views.kindsList.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zoo.R;
import com.example.zoo.ZooApp;
import com.example.zoo.db.dao.AnimalDao;
import com.example.zoo.db.dao.KindDao;
import com.example.zoo.db.entities.AnimalEntity;
import com.example.zoo.db.entities.KindEntity;

import java.util.List;
import java.util.function.Consumer;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private Long currentZooId;

    KindDao mKindDao;
    AnimalDao mAnimalDao;

    List<KindEntity> mKindsList;

    private final Context mContext;
    private final Consumer<Long> onEditAction;
    private final Consumer<Long> onDeleteAction;


    public MainRecyclerAdapter(
            Context context,
            Long zooId,
            Consumer<Long> onEditAction,
            Consumer<Long> onDeleteAction
    ) {
        mContext = context;

        mKindDao = ZooApp.appDatabase.getKindDao();
        mAnimalDao = ZooApp.appDatabase.getAnimalDao();

        currentZooId = zooId;

        mKindsList = mKindDao.findAllByZooId(currentZooId);

        this.onEditAction = onEditAction;
        this.onDeleteAction = onDeleteAction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.kind_list_item, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KindEntity kind = mKindDao.findAll().get(position);

        String kindName = kind.kindName;
        List<AnimalEntity> animals = mAnimalDao.findAllByKindId(kind.id);

        holder.kindNameTextView.setText(kindName);

        holder.kindEditButton
                .setOnClickListener(v -> onEditAction.accept(kind.id));
        holder.kindDeleteButton.
                setOnClickListener(v -> onDeleteAction.accept(kind.id));

        ChildRecyclerAdapter childRecyclerAdapter = new ChildRecyclerAdapter(mContext, animals);
        holder.childRecyclerView.setAdapter(childRecyclerAdapter);
    }

    @Override
    public int getItemCount() {
        return mKindDao.findAll().size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateKindsList(List<KindEntity> kindEntities) {
        this.mKindsList.clear();
        this.mKindsList.addAll(kindEntities);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView kindNameTextView;
        RecyclerView childRecyclerView;

        AppCompatImageButton kindEditButton;
        AppCompatImageButton kindDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            kindNameTextView = itemView.findViewById(R.id.kindName);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);

            kindEditButton = itemView.findViewById(R.id.kindEditButton);
            kindDeleteButton = itemView.findViewById(R.id.kindDeleteButton);
        }
    }
}
