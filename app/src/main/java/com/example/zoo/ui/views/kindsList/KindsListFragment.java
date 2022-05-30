package com.example.zoo.ui.views.kindsList;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.zoo.R;
import com.example.zoo.ZooApp;
import com.example.zoo.databinding.FragmentKindsListBinding;
import com.example.zoo.db.dao.KindDao;
import com.example.zoo.db.dao.ZooDao;
import com.example.zoo.db.entities.ZooEntity;

public class KindsListFragment extends Fragment {

    private final ZooDao mZooDao = ZooApp.appDatabase.getZooDao();
    private final KindDao mKindDao = ZooApp.appDatabase.getKindDao();

    private FragmentKindsListBinding mBinding;

    private Long currentZooId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentZooId = KindsListFragmentArgs.fromBundle(getArguments()).getZooId();
    }

    @Nullable
    @Override
    public View onCreateView(
            @Nullable LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        mBinding = FragmentKindsListBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (currentZooId == 0) {
            return;
        }

        mBinding.includedToolBar.myToolbar
                .inflateMenu(R.menu.kinds_list_menu);

        mBinding.includedToolBar.myToolbar
                .setOnMenuItemClickListener(item -> {

                    switch (item.getItemId()) {
                        case R.id.addKindMenuOption: {
                            showAddKindModal();
                            return true;
                        }
                        case R.id.deleteZooMenuOption: {
                            showDeleteZooModal();
                            return true;
                        }
                        default:
                            return super.onOptionsItemSelected(item);
                    }

                });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        KindsListFragmentDirections
                .ActionKindsListFragmentToKindDetailsFragment action = KindsListFragmentDirections
                .actionKindsListFragmentToKindDetailsFragment();

        action.setZooId(currentZooId);

        if (currentZooId != 0) {
            ZooEntity zoo = mZooDao.findById(currentZooId).get();
            mBinding.includedToolBar.titleTextView.setText("Виды в зоопарке \n" + zoo.name);
        } else {
            mBinding.includedToolBar.titleTextView.setText("Выберите зоопарк");
        }
        KindsListAdapter kindsListAdapter = new KindsListAdapter(
                getContext(),
                kindId -> {
                    action.setKindId(kindId);
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(action);
                },
                this::showDeleteKindModal
        );
        mBinding.kindsListView.setAdapter(kindsListAdapter);

        mKindDao.findAllByZooIdReactive(currentZooId)
                .observe(this, kindsListAdapter::updateKindsList);

        mBinding.includedToolBar.showDrawerButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.open();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void showAddKindModal() {
        KindsListFragmentDirections
                .ActionKindsListFragmentToKindDetailsFragment action = KindsListFragmentDirections
                .actionKindsListFragmentToKindDetailsFragment();

        action.setZooId(currentZooId);

        Navigation
                .findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(action);
    }

    private void showDeleteZooModal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удалить зоопарк?")
                .setPositiveButton("Так точно", (dialog, which) -> {
                    mZooDao.delete(currentZooId);
                    Navigation
                            .findNavController(
                                    requireActivity(),
                                    R.id.navHostFragment
                            )
                            .navigate(
                                    KindsListFragmentDirections.actionGlobalKindsListFragment(0)
                            );
                })
                .setNegativeButton("Никак нет", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showDeleteKindModal(Long kindId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удалить вольер?")
                .setPositiveButton(
                        "Так точно",
                        (dialog, which) -> mKindDao.delete(kindId)
                )
                .setNegativeButton(
                        "Никак нет",
                        (dialog, which) -> dialog.cancel()
                )
                .show();
    }

}
