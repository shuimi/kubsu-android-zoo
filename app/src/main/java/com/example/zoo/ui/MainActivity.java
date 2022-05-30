package com.example.zoo.ui;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import com.example.zoo.R;
import com.example.zoo.ZooApp;
import com.example.zoo.db.dao.ZooDao;
import com.example.zoo.db.entities.ZooEntity;
import com.example.zoo.ui.views.kindsList.KindsListFragmentDirections;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final ZooDao mZooDao = ZooApp.appDatabase.getZooDao();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setupNavigationDrawer();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        Menu menu = navigationView.getMenu();

        menu.add(0, 0, Menu.NONE, "Добавить зоопарк");

        mZooDao
                .findAllReactive()
                .observe(this, zoos -> {
                    menu.removeGroup(1);

                    zoos.forEach(zoo -> {
                        menu.add(1, zoo.id.intValue(), Menu.NONE, zoo.name);
                    });
                });

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getGroupId() == 0) {
                showAddKindModal();
            }
            else {
                Navigation
                        .findNavController(this, R.id.navHostFragment)
                        .navigate(KindsListFragmentDirections.actionGlobalKindsListFragment(item.getItemId()));
                drawerLayout.close();
            }
            return false;
        });
    }

    private void showAddKindModal() {

        ZooEntity zoo = new ZooEntity();

        EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Добавить зоопарк")
                .setView(editText)
                .setPositiveButton("Создать", (dialog, which) -> {
                    zoo.name = editText.getText().toString();
                    mZooDao.insert(zoo);
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }
}