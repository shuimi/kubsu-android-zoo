package com.example.zoo.ui.views.kindDetails;

import android.app.AlertDialog;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.zoo.R;
import com.example.zoo.ZooApp;
import com.example.zoo.databinding.FragmentKindDetailsBinding;
import com.example.zoo.db.dao.KindDao;
import com.example.zoo.db.dao.ZooDao;
import com.example.zoo.db.entities.KindEntity;
import com.example.zoo.ui.views.kindsList.KindsListFragmentDirections;

public class KindDetailsFragment extends Fragment {

    private final KindDao mKindDao = ZooApp.appDatabase.getKindDao();
    private final ZooDao mZooDao = ZooApp.appDatabase.getZooDao();

    private FragmentKindDetailsBinding mBinding;

    private Long currentKindId;
    private Long currentZooId;
    private KindEntity mKind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        currentZooId = KindDetailsFragmentArgs.fromBundle(getArguments()).getZooId();
        currentKindId = KindDetailsFragmentArgs.fromBundle(getArguments()).getKindId();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        mBinding = FragmentKindDetailsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (currentZooId == 0){
            return;
        }
        mBinding.includedToolBar.myToolbar.inflateMenu(R.menu.kinds_list_menu);
        mBinding.includedToolBar.myToolbar.setOnMenuItemClickListener(item -> {
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
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(KindsListFragmentDirections.actionGlobalKindsListFragment(0));
                })
                .setNegativeButton("Никак нет", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showDeleteGroupModal(Long groupId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удалить вольер?")
                .setPositiveButton("Так точно", (dialog, which) -> {
                    mKindDao.delete(groupId);
                })
                .setNegativeButton("Никак нет", (dialog, which) -> dialog.cancel())
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        mKind = mKindDao
                .findById(currentKindId)
                .orElseGet(() -> {
                    KindEntity kind = new KindEntity();
                    kind.zooId = currentZooId;
                    return kind;
                });

        mBinding.kindNameEditText.setText(mKind.kindName);
        mBinding.aviaryNumberEditText.setText(mKind.aviaryNumber.intValue());

        mBinding.includedToolBar.titleTextView.setText("Вольер");
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

    private void closeGroupDetailsModal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Сохранить вольер?")
                .setPositiveButton("Сохранить и выйти", (dialog, which) -> {
                    mKind.kindName = mBinding.kindNameEditText.getText().toString();
                    mKind.aviaryNumber = Integer.parseInt(mBinding.aviaryNumberEditText.getText().toString());

                    if (currentKindId == 0) {
                        mKindDao.insert(mKind);
                    } else {
                        mKindDao.save(mKind);
                    }

                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(KindsListFragmentDirections.actionGlobalKindsListFragment(currentZooId));
                })
                .setNegativeButton("Выйти без сохранения", (dialog, which) -> {
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(KindsListFragmentDirections.actionGlobalKindsListFragment(currentZooId));
                    dialog.cancel();
                })
                .show();
    }

}
