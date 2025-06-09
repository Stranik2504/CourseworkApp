package dev.stranik.coursework;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.stranik.coursework.utils.MyTaskSolutionsRecyclerViewAdapter;
import dev.stranik.coursework.utils.SolutionInfo;
import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.UserSolution;
import dev.stranik.coursework.utils.UserSolutionAdapter;
import dev.stranik.coursework.utils.VerticalSpaceItemDecoration;
import dev.stranik.coursework.utils.data.StaticWorkData;

public class ViewTaskFragment extends Fragment {
    private final int _solutionId;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ViewTaskFragment(int solutionId) {
        _solutionId = solutionId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_task, container, false);

        TextView tvName = view.findViewById(R.id.TVName);
        TextView tvDescription = view.findViewById(R.id.TVDescription);
        TextView tvCountTries = view.findViewById(R.id.TVCountTries);
        RecyclerView layoutSolutions = view.findViewById(R.id.layout_solutions);
        ScrollView svSolutions = view.findViewById(R.id.SVSolutions);

        executor.execute(() -> {
            Pair<Boolean, SolutionInfo> solutionPair = StaticWorkData.getSolution(_solutionId);

            mainHandler.post(() -> {
                if (!solutionPair.first) {
                    // Handle error case, e.g., show a message to the user
                    Toast.makeText(getContext(), "Failed to load task details", Toast.LENGTH_SHORT).show();
                    return;
                }

                SolutionInfo solutionInfo = solutionPair.second;
                String attempts;

                if (solutionInfo.getCountAttempts() != -1)
                    attempts = solutionInfo.getCountUserAttempts() + "/" + solutionInfo.getCountAttempts();
                else
                    attempts = getString(R.string.info_infinity_count_attempts);

                tvName.setText(solutionInfo.getTitle());
                tvDescription.setText(solutionInfo.getDescription());
                tvCountTries.setText(getString(R.string.info_count_tries) + " " + attempts);
            });
        });

        if (StaticWorkData.isAdmin()) {
            executor.execute(() -> {
                Pair<Boolean, List<UserSolution>> solutionsPair = StaticWorkData.getUsersSolution(_solutionId);

                mainHandler.post(() -> {
                    if (!solutionsPair.first) {
                        Toast.makeText(getContext(), "Failed to load user solutions", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new TaskListFragment())
                                .addToBackStack(null)
                                .commit();
                        return;
                    }

                    ArrayList<UserSolution> items = new ArrayList<>(solutionsPair.second);

                    layoutSolutions.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    UserSolutionAdapter adapter = new UserSolutionAdapter(items, this);
                    layoutSolutions.setAdapter(adapter);

                    int space = getResources().getDimensionPixelSize(R.dimen.card_margin);
                    layoutSolutions.addItemDecoration(new VerticalSpaceItemDecoration(space));

                    svSolutions.setVisibility(View.VISIBLE);
                });
            });
        }
        else
            svSolutions.setVisibility(View.GONE);

        view.findViewById(R.id.BtnStart).setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new TaskPassingFragment(_solutionId))
                .addToBackStack(null)
                .commit();
        });

        return view;
    }
}