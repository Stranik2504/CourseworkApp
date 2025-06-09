package dev.stranik.coursework;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.stranik.coursework.utils.MyTaskSolutionsRecyclerViewAdapter;
import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.VerticalSpaceItemDecoration;
import dev.stranik.coursework.utils.data.StaticWorkData;

public class ListTaskSolutions extends Fragment {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ListTaskSolutions() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_task_solutions_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            executor.execute(() -> {
                ArrayList<SolutionList> items = new ArrayList<>(StaticWorkData.getSolutionTasks(StaticWorkData.getId()));

                mainHandler.post(() -> {
                    if (items.isEmpty()) {
                        items.add(new SolutionList(
                                -1,
                                "Нет решений",
                                "",
                                null,
                                List.of()
                        ));
                    }

                    MyTaskSolutionsRecyclerViewAdapter adapter = new MyTaskSolutionsRecyclerViewAdapter(items);
                    recyclerView.setAdapter(adapter);

                    int space = getResources().getDimensionPixelSize(R.dimen.card_margin);
                    recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(space));
                });
            });
        }

        return view;
    }
}