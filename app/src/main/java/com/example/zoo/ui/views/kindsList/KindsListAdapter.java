package com.example.zoo.ui.views.kindsList;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;

import com.example.zoo.R;
import com.example.zoo.databinding.KindListItemBinding;
import com.example.zoo.db.entities.KindEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.N)
public class KindsListAdapter extends BaseAdapter {

    private final Context context;
    private final Consumer<Long> onEditAction;
    private final Consumer<Long> onDeleteAction;

    private final List<KindEntity> mKindsList = new ArrayList<>();

    private SortField sortField = SortField.AVIARY_NUMBER;

    private final Comparator<String> nullsLastStringComparator = Comparator
            .nullsLast((Comparator<String>) String::compareTo);

    public KindsListAdapter(
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

        KindListItemBinding groupListItemBinding;

        if (convertView == null) {
            groupListItemBinding = KindListItemBinding.inflate(
                    LayoutInflater.from(context), parent, false
            );
        } else {
            groupListItemBinding = KindListItemBinding.bind(convertView);
        }
        KindEntity kind = (KindEntity) getItem(position);

        groupListItemBinding.kindName.setText(kind.kindName);
        groupListItemBinding.aviaryNumber.setText(kind.aviaryNumber + "");

        groupListItemBinding.kindEditButton
                .setOnClickListener(v -> onEditAction.accept(kind.id));
        groupListItemBinding.kindDeleteButton.
                setOnClickListener(v -> onDeleteAction.accept(kind.id));

        groupListItemBinding.kindNameLayout
                .setOnLongClickListener(view -> {
                    handleSortAction(view);
                    return true;
                });

        groupListItemBinding.kindNumberLayout
                .setOnLongClickListener(view -> {
                    handleSortAction(view);
                    return true;
                });

        return groupListItemBinding.getRoot();
    }

    public void updateKindsList(List<KindEntity> kindsList) {
        this.mKindsList.clear();
        this.mKindsList.addAll(kindsList);
        this.sortKinds();
        notifyDataSetChanged();
    }

    private void handleSortAction(View view) {
        switch (view.getId()) {
            case R.id.kindNameLayout: {
                sortField = SortField.KIND_NAME;
                break;
            }
            case R.id.kindNumberLayout: {
                sortField = SortField.AVIARY_NUMBER;
                break;
            }
        }
        sortKinds();
        notifyDataSetChanged();
    }

    private void sortKinds() {
        Collections.sort(this.mKindsList, (o1, o2) -> {
            switch (sortField) {
                case KIND_NAME: {
                    return nullsLastStringComparator.compare(o1.kindName, o2.kindName);
                }
                case AVIARY_NUMBER: {
                    return o1.aviaryNumber.compareTo(o2.aviaryNumber);
                }
            }
            return 0;
        });
    }

    private enum SortField {
        KIND_NAME, AVIARY_NUMBER;
    }
}
