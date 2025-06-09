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
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.stranik.coursework.utils.MyTaskSolutionsRecyclerViewAdapter;
import dev.stranik.coursework.utils.MyTaskViewRecyclerViewAdapter;
import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.VerticalSpaceItemDecoration;
import dev.stranik.coursework.utils.data.StaticWorkData;


public class TaskListFragment extends Fragment {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

        if (recyclerView != null) {
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            executor.execute(() -> {
                ArrayList<SolutionList> items = new ArrayList<>(StaticWorkData.getTasks(StaticWorkData.getId()));

                mainHandler.post(() ->{
                    MyTaskViewRecyclerViewAdapter adapter = new MyTaskViewRecyclerViewAdapter(items, this);
                    recyclerView.setAdapter(adapter);

                    int space = view.getResources().getDimensionPixelSize(R.dimen.card_margin);
                    recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(space));
                });
            });
        }

        FloatingActionButton fab = view.findViewById(R.id.fab_add_task);

        if (StaticWorkData.isAdmin())
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.GONE);

        fab.setOnClickListener(v -> {
            if (!StaticWorkData.isAdmin())
                return;

            getParentFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new CreateTaskFragment())
                .addToBackStack(null)
                .commit();
        });

        return view;
    }
}