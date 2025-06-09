package dev.stranik.coursework.utils;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.jspecify.annotations.NonNull;


import java.util.List;

import dev.stranik.coursework.R;

public class MyTaskSolutionsRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskSolutionsRecyclerViewAdapter.ViewHolder> {
    private List<SolutionList> items;

    public MyTaskSolutionsRecyclerViewAdapter(List<SolutionList> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MyTaskSolutionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.fragment_list_task_solutions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTaskSolutionsRecyclerViewAdapter.ViewHolder holder, int position) {
        SolutionList item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView _tVName;
        public final TextView _tVDescription;
        public final FlexboxLayout _flexboxLayout;

        ViewHolder(View view) {
            super(view);

            _tVName = view.findViewById(R.id.TVName);
            _tVDescription = view.findViewById(R.id.TVDescription);
            _flexboxLayout = view.findViewById(R.id.FlexboxLayout);
        }

        @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
        public void bind(SolutionList item) {
            _tVName.setText(item.getTitle());
            _tVDescription.setText(item.getDescription());

            for (int i = 0; i < item.getSolutions().size(); i++) {
                TextView solutionView = new TextView(_flexboxLayout.getContext());
                solutionView.setText(String.valueOf(i + 1));
                solutionView.setGravity(17);
                solutionView.setTextSize(20);
                solutionView.setTextColor(ContextCompat.getColor(solutionView.getContext(), R.color.white));

                ViewGroup.MarginLayoutParams params = getMarginLayoutParams(solutionView);
                solutionView.setLayoutParams(params);

                solutionView.setBackground(item.getSolutions().get(i) ?
                        _flexboxLayout.getContext().getDrawable(R.drawable.task_complete_success) :
                        _flexboxLayout.getContext().getDrawable(R.drawable.task_complete_failed)
                );

                _flexboxLayout.addView(solutionView);
            }
        }

        private ViewGroup.MarginLayoutParams getMarginLayoutParams(TextView solutionView) {
            int marginInDp = 5;
            float scale = solutionView.getContext().getResources().getDisplayMetrics().density;
            int marginInPx = (int) (marginInDp * scale + 0.5f);

            int sizeInDp = 35;
            scale = solutionView.getContext().getResources().getDisplayMetrics().density;
            int sizeInPx = (int) (sizeInDp * scale + 0.5f);

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(sizeInPx, sizeInPx);
            params.setMarginEnd(marginInPx);
            return params;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}