package com.example.zoo.ui.views.animalDetails;

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
import com.example.zoo.databinding.FragmentAnimalDetailsBinding;
import com.example.zoo.databinding.FragmentKindDetailsBinding;
import com.example.zoo.db.dao.AnimalDao;
import com.example.zoo.db.dao.KindDao;
import com.example.zoo.db.dao.ZooDao;
import com.example.zoo.db.entities.AnimalEntity;
import com.example.zoo.db.entities.KindEntity;
import com.example.zoo.ui.views.kindDetails.KindDetailsFragmentArgs;
import com.example.zoo.ui.views.kindsList.KindsListFragmentDirections;

public class AnimalDetailsFragment extends Fragment {

    private final AnimalDao mAnimalDao = ZooApp.appDatabase.getAnimalDao();
    private final KindDao mKindDao = ZooApp.appDatabase.getKindDao();

    private FragmentAnimalDetailsBinding mBinding;

    private Long currentKindId;
    private Long currentAnimalId;
    private AnimalEntity mAnimal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        currentAnimalId = AnimalDetailsFragmentArgs.fromBundle(getArguments()).getAnimalId();
        currentKindId = AnimalDetailsFragmentArgs.fromBundle(getArguments()).getKindId();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        mBinding = FragmentAnimalDetailsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
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

        mAnimal = mAnimalDao
                .findById(currentAnimalId)
                .orElseGet(() -> {
                    AnimalEntity animal = new AnimalEntity();
                    animal.kindId = currentKindId;
                    return animal;
                });

        mBinding.animalDetailsSaveButton.setOnClickListener(view -> {
            commitAnimal();
        });

        mBinding.animalNameEditText.setText(mAnimal.name);
        if (mAnimal.aviary != null)
            mBinding.animalAviaryNumberEditText.setText(mAnimal.aviary + "");

        mBinding.includedToolBar.titleTextView.setText("Животное");

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

    private void commitAnimal() {
        mAnimal.name = mBinding.animalNameEditText.getText().toString();
        mAnimal.aviary = Integer.parseInt(mBinding.animalAviaryNumberEditText.getText().toString());

        if (currentAnimalId == 0) {
            mAnimalDao.insert(mAnimal);
        } else {
            mAnimalDao.save(mAnimal);
        }

        Navigation
                .findNavController(requireActivity(), R.id.navHostFragment)
                .popBackStack();
    }

    private void closeKindDetailsModal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Сохранить животное?")
                .setPositiveButton("Сохранить и выйти", (dialog, which) -> {
                    commitAnimal();
                })
                .setNegativeButton("Выйти без сохранения", (dialog, which) -> {
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .popBackStack();
                    dialog.cancel();
                })
                .show();
    }

}
