package dev.stranik.coursework.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

import dev.stranik.coursework.R;

public class UserSolutionAdapter extends RecyclerView.Adapter<UserSolutionAdapter.ViewHolder> {
    private ArrayList<UserSolution> _items;
    private Fragment _currFragment;

    public UserSolutionAdapter(ArrayList<UserSolution> items, Fragment currFragment) {
        _items = items;
        _currFragment = currFragment;
    }

    @NonNull
    @Override
    public UserSolutionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_solution, parent, false);

        return new UserSolutionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSolutionAdapter.ViewHolder holder, int position) {
        UserSolution item = _items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView _userIcon;
        public final TextView _username;
        public final FlexboxLayout _layout;

        ViewHolder(View view) {
            super(view);

            _userIcon = view.findViewById(R.id.IVUserIcon);
            _username = view.findViewById(R.id.TVName);
            _layout = view.findViewById(R.id.FlexboxLayout);
        }

        public void bind(UserSolution item) {
            if (item.isAdmin())
                _userIcon.setImageResource(R.drawable.admin);
            else
                _userIcon.setImageResource(R.drawable.user);

            _username.setText(item.getUsername());

            if (item.getSolutions() != null) {
                for (int i = 0; i < item.getSolutions().size(); i++) {
                    TextView solutionView = new TextView(_layout.getContext());
                    solutionView.setText(String.valueOf(i + 1));
                    solutionView.setGravity(17);
                    solutionView.setTextSize(15);
                    solutionView.setTextColor(ContextCompat.getColor(solutionView.getContext(), R.color.white));

                    ViewGroup.MarginLayoutParams params = getMarginLayoutParams(solutionView);
                    solutionView.setLayoutParams(params);

                    solutionView.setBackground(item.getSolutions().get(i) ?
                            _layout.getContext().getDrawable(R.drawable.task_complete_success) :
                            _layout.getContext().getDrawable(R.drawable.task_complete_failed)
                    );

                    _layout.addView(solutionView);
                }
            }
        }

        private ViewGroup.MarginLayoutParams getMarginLayoutParams(TextView solutionView) {
            int marginInDp = 5;
            float scale = solutionView.getContext().getResources().getDisplayMetrics().density;
            int marginInPx = (int) (marginInDp * scale + 0.5f);

            int sizeInDp = 20;
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
