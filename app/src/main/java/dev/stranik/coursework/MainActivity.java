package dev.stranik.coursework;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import dev.stranik.coursework.utils.data.StaticWorkData;

public class MainActivity extends AppCompatActivity {
    private static final String ADMIN = "Admin";
    private static final String USER = "User";

    private static BottomNavigationView _bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedAdmin = getSharedPreferences(ADMIN, MODE_PRIVATE);
        SharedPreferences sharedUser = getSharedPreferences(USER, MODE_PRIVATE);

        StaticWorkData.setAdmin(sharedAdmin.getBoolean("admin", false));
        StaticWorkData.setId(sharedUser.getInt("userId", -1));

        StaticWorkData.setSharedPreferences(List.of(sharedAdmin, sharedUser));
        StaticWorkData.loadToken();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        _bottomNavigationView = bottomNavigationView;
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment currentFragment = null;

            if (item.getItemId() == R.id.home) {
                currentFragment = new TaskListFragment();
            }
            else if (item.getItemId() == R.id.solutions) {
                currentFragment = new ListTaskSolutions();
            }
            else if (item.getItemId() == R.id.account) {
                if (StaticWorkData.getId() == -1) {
                    currentFragment = new LoginFragment();
                } else {
                    currentFragment = new AccountFragment();
                }
            }

            if (currentFragment == null)
                return false;

            setFragment(currentFragment);
            return true;
        });

        updateNavigationMenu(StaticWorkData.getId() != -1);

        setFragment(new TaskListFragment());
    }

    public static void updateNavigationMenu(boolean isLoggedIn) {
        if (_bottomNavigationView == null) return;

        int id = _bottomNavigationView.getSelectedItemId();
        _bottomNavigationView.getMenu().clear();
        _bottomNavigationView.inflateMenu(isLoggedIn ? R.menu.register_user_menu : R.menu.unregister_user_menu);

        _bottomNavigationView.setSelectedItemId(id);
    }

    public static void updateNavigationMenu(int selectedItemId) {
        if (_bottomNavigationView == null) return;
        _bottomNavigationView.setSelectedItemId(selectedItemId);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, null);
        fragmentTransaction.commit();
    }
}