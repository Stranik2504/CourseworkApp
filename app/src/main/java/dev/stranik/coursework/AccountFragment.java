package dev.stranik.coursework;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.stranik.coursework.utils.UserInfo;
import dev.stranik.coursework.utils.data.StaticWorkData;
import kotlin.Triple;

public class AccountFragment extends Fragment {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public AccountFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        view.findViewById(R.id.BtnExit).setOnClickListener(this::onLogoutClick);

        executor.execute(() -> {
            Pair<Boolean, UserInfo> userData = StaticWorkData.getUserInfo();

            mainHandler.post(() -> {
                if (!userData.first) {
                    Toast.makeText(getContext(), "Ошибка загрузки данных пользователя", Toast.LENGTH_SHORT).show();
                    return;
                }

                TextView tvUsername = view.findViewById(R.id.TVUsername);
                TextView tvPassPercent = view.findViewById(R.id.TVPassPercent);
                TextView tvErrorPercent = view.findViewById(R.id.TVErrorPercent);
                ShapeableImageView ivUserIcon = view.findViewById(R.id.IVUserIcon);

                tvUsername.setText(userData.second.getUsername());
                tvPassPercent.setText(userData.second.getStatPass() + "%");
                tvErrorPercent.setText(userData.second.getStatError() + "%");

                if (StaticWorkData.isAdmin()) {
                    ivUserIcon.setImageResource(R.drawable.admin);
                } else {
                    ivUserIcon.setImageResource(R.drawable.user);
                }

                ivUserIcon.setShapeAppearanceModel(
                        ivUserIcon.getShapeAppearanceModel()
                                .toBuilder()
                                .setAllCornerSizes(ShapeAppearanceModel.PILL)
                                .build()
                );
            });
        });

        return view;
    }

    public void onLogoutClick(View view) {
        StaticWorkData.reset();

        MainActivity.updateNavigationMenu(false);

        getParentFragmentManager().beginTransaction()
            .replace(R.id.content_frame, new LoginFragment())
            .addToBackStack(null)
            .commit();
    }
}