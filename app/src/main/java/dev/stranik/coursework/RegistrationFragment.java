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

public class RegistrationFragment extends Fragment {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        view.findViewById(R.id.BtnRegistration).setOnClickListener(this::onRegistrationClick);

        return view;
    }

    public void onRegistrationClick(View view) {
        EditText login = getView().findViewById(R.id.editTextText);
        EditText password = getView().findViewById(R.id.editTextTextPassword);
        EditText password2 = getView().findViewById(R.id.editTextTextPassword2);

        String loginText = login.getText().toString();
        String passwordText = password.getText().toString();
        String passwordText2 = password2.getText().toString();

        if (loginText.isEmpty() || passwordText.isEmpty() || passwordText2.isEmpty()) {
            login.setError("Login and password cannot be empty");
            Toast.makeText(getContext(), "Логин или пароль не должны быть пустыми", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordText.equals(passwordText2)) {
            password2.setError("Passwords do not match");
            Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            Pair<Boolean, String> result = StaticWorkData.registerUser(loginText, passwordText);

            mainHandler.post(() -> {
                if (!result.first) {
                    login.setError(result.second);
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
        EditText password2 = getView().findViewById(R.id.editTextTextPassword2);

        outState.putString("login", login.getText().toString());
        outState.putString("password", password.getText().toString());
        outState.putString("password2", password2.getText().toString());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            EditText login = getView().findViewById(R.id.editTextText);
            EditText password = getView().findViewById(R.id.editTextTextPassword);
            EditText password2 = getView().findViewById(R.id.editTextTextPassword2);

            login.setText(savedInstanceState.getString("login", ""));
            password.setText(savedInstanceState.getString("password", ""));
            password2.setText(savedInstanceState.getString("password2", ""));
        }
    }
}