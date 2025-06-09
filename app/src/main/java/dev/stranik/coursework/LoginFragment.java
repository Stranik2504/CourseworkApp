package dev.stranik.coursework;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.stranik.coursework.utils.data.StaticWorkData;

public class LoginFragment extends Fragment {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.textView).setOnClickListener(this::onRegisterClick);
        view.findViewById(R.id.BtnLogin).setOnClickListener(this::onLoginClick);

        return view;
    }

    public void onRegisterClick(View view) {
        getParentFragmentManager().beginTransaction()
            .replace(R.id.content_frame, new RegistrationFragment())
            .addToBackStack(null)
            .commit();
    }

    public void onLoginClick(View view) {
        EditText login = getView().findViewById(R.id.editTextText);
        EditText password = getView().findViewById(R.id.editTextTextPassword);

        String loginText = login.getText().toString();
        String passwordText = password.getText().toString();

        if (loginText.isEmpty() || passwordText.isEmpty()) {
            login.setError("Login and password cannot be empty");
            Toast.makeText(getContext(), "Логин или пароль не должны быть пустыми", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            Pair<Boolean, String> result = StaticWorkData.loginUser(loginText, passwordText);

            mainHandler.post(() -> {
                if (!result.first) {
                    login.setError(result.second);
                    Toast.makeText(getContext(), result.second, Toast.LENGTH_SHORT).show();
                    return;
                }

                StaticWorkData.saveToken();

                MainActivity.updateNavigationMenu(StaticWorkData.getId() != -1);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AccountFragment())
                        .addToBackStack(null)
                        .commit();
            });
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        EditText login = getView().findViewById(R.id.editTextText);
        EditText password = getView().findViewById(R.id.editTextTextPassword);

        outState.putString("login", login.getText().toString());
        outState.putString("password", password.getText().toString());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            EditText login = getView().findViewById(R.id.editTextText);
            EditText password = getView().findViewById(R.id.editTextTextPassword);

            login.setText(savedInstanceState.getString("login", ""));
            password.setText(savedInstanceState.getString("password", ""));
        }
    }
}