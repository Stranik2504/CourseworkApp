package dev.stranik.coursework.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.stranik.coursework.R;
import dev.stranik.coursework.ViewTaskFragment;
import dev.stranik.coursework.utils.data.StaticWorkData;

public class MyTaskViewRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskViewRecyclerViewAdapter.ViewHolder> {
    private ArrayList<SolutionList> _items;
    private Fragment _currFragment;

    public MyTaskViewRecyclerViewAdapter(ArrayList<SolutionList> items, Fragment currFragment) {
        _items = items;
        _currFragment = currFragment;
    }

    @NonNull
    @Override
    public MyTaskViewRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);

        view.setOnClickListener(v -> {
            int position = parent.indexOfChild(v);

            if (StaticWorkData.getId() == -1) {
                Toast.makeText(
                        _currFragment.getContext(),
                        "Чтобы просмотреть задачи, необходимо войти в систему",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (position != RecyclerView.NO_POSITION) {
                SolutionList item = _items.get(position);

                _currFragment.getParentFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ViewTaskFragment(item.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return new MyTaskViewRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTaskViewRecyclerViewAdapter.ViewHolder holder, int position) {
        SolutionList item = _items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView _tVName;
        public final TextView _tVDescription;
        public final TextView _tVId;

        ViewHolder(View view) {
            super(view);

            _tVName = view.findViewById(R.id.TVName);
            _tVDescription = view.findViewById(R.id.TVDescription);
            _tVId = view.findViewById(R.id.TVId);

            ImageButton button = view.findViewById(R.id.btn_delete);
            button.setOnClickListener(v -> {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    int id = _items.get(position).getId();

                    executor.execute(() -> {
                        boolean result = StaticWorkData.deleteTask(id);

                        // Вернуться в UI-поток
                        _currFragment.requireActivity().runOnUiThread(() -> {
                            if (result) {
                                Toast.makeText(
                                        v.getContext(),
                                        "Задача удалена",
                                        Toast.LENGTH_SHORT
                                ).show();

                                _items.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                Toast.makeText(
                                        v.getContext(),
                                        "Ошибка при удалении задачи",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                    });
                }
            });

            if (StaticWorkData.isAdmin())
                button.setVisibility(View.VISIBLE);
            else
                button.setVisibility(View.GONE);
        }

        @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
        public void bind(SolutionList item) {
            _tVName.setText(item.getTitle());
            _tVDescription.setText(item.getDescription());
            _tVId.setText(item.getDescription());
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
