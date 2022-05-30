package com.example.zoo.ui.views.kindDetails;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.zoo.R;
import com.example.zoo.ZooApp;
import com.example.zoo.databinding.FragmentKindDetailsBinding;
import com.example.zoo.db.dao.AnimalDao;
import com.example.zoo.db.dao.KindDao;
import com.example.zoo.db.dao.ZooDao;
import com.example.zoo.db.entities.KindEntity;
import com.example.zoo.ui.views.kindsList.KindsListFragmentDirections;

public class KindDetailsFragment extends Fragment {

    private final AnimalDao mAnimalDao = ZooApp.appDatabase.getAnimalDao();
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
        if (currentZooId == 0) {
            return;
        }
        mBinding.includedToolBar.myToolbar.inflateMenu(R.menu.kind_details_menu);
        mBinding.includedToolBar.myToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.closeMenuOption: {
                    closeKindDetailsModal();
                    return true;
                }
                case R.id.createAnimalOption: {
                    createAnimal();
                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        });
        mBinding.kindDetailsSaveButton.setOnClickListener((view) -> {
            mKind.kindName = mBinding.kindNameEditText.getText().toString();

            if (currentKindId == 0) {
                mKindDao.insert(mKind);
            } else {
                mKindDao.save(mKind);
            }

            Navigation
                    .findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(KindsListFragmentDirections.actionGlobalKindsListFragment(currentZooId));
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void createAnimal(){
        KindDetailsFragmentDirections
                .ActionKindDetailsFragmentToAnimalDetailsFragment action = KindDetailsFragmentDirections
                .actionKindDetailsFragmentToAnimalDetailsFragment();
        action.setAnimalId(0);
        action.setKindId(currentKindId);

        Navigation
                .findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(action);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(
                        getViewLifecycleOwner(),
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {
                                closeKindDetailsModal();
                            }
                        }
                );
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

        mBinding.includedToolBar.titleTextView.setText("Виды в зоопарке");
        mBinding.includedToolBar.showDrawerButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.open();
        });

        KindDetailsFragmentDirections
                .ActionKindDetailsFragmentToAnimalDetailsFragment action = KindDetailsFragmentDirections
                .actionKindDetailsFragmentToAnimalDetailsFragment();

        action.setKindId(currentKindId);

        AnimalsListAdapter animalsListAdapter = new AnimalsListAdapter(
                getContext(),
                animalId -> {
                    action.setAnimalId(animalId);
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(action);
                },
                this::showDeleteAnimalModal
        );
        mBinding.animalsListView.setAdapter(animalsListAdapter);

        mAnimalDao.findAllByKindIdReactive(currentKindId)
                .observe(this, animalsListAdapter::updateAnimalsList);

        mBinding.includedToolBar.showDrawerButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.open();
        });

    }

    public void showDeleteAnimalModal(long currentAnimalId){
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Удалить животное?")
                .setPositiveButton("Так точно", (dialog, which) -> {
                    mAnimalDao.delete(currentAnimalId);
                })
                .setNegativeButton("Никак нет", (dialog, which) -> dialog.cancel())
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void closeKindDetailsModal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Сохранить вольер?")
                .setPositiveButton("Сохранить и выйти", (dialog, which) -> {
                    mKind.kindName = mBinding.kindNameEditText.getText().toString();

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
