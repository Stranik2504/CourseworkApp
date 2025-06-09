package dev.stranik.coursework.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.stranik.coursework.R;

public class AnswerOptionsAdapter extends RecyclerView.Adapter<AnswerOptionsAdapter.ViewHolder> {

    private final List<AnswerOption> answerOptions;

    public AnswerOptionsAdapter(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public void addOption() {
        answerOptions.add(new AnswerOption("", answerOptions.isEmpty()));
        notifyItemInserted(answerOptions.size() - 1);
    }

    public void removeOption(int position) {
        if (position >= 0 && position < answerOptions.size()) {
            boolean needSetNewCorrect = answerOptions.get(position).isCorrect() && answerOptions.size() > 1;
            answerOptions.remove(position);

            if (needSetNewCorrect) {
                answerOptions.getFirst().setCorrect(true);
                notifyItemChanged(position == 0 ? 1 : 0);
            }

            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnswerOption option = answerOptions.get(position);
        holder.etOptionText.setText(option.getText());
        holder.rbIsCorrect.setChecked(option.isCorrect());

        holder.rbIsCorrect.setOnClickListener(v -> {
            for (int i = 0; i < answerOptions.size(); i++) {
                answerOptions.get(i).setCorrect((i == holder.getAdapterPosition()));
            }

            notifyDataSetChanged();
        });

        holder.btnDeleteOption.setOnClickListener(v -> removeOption(holder.getAdapterPosition()));

        holder.etOptionText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                option.setText(holder.etOptionText.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return answerOptions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbIsCorrect;
        EditText etOptionText;
        Button btnDeleteOption;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rbIsCorrect = itemView.findViewById(R.id.rb_is_correct);
            etOptionText = itemView.findViewById(R.id.et_option_text);
            btnDeleteOption = itemView.findViewById(R.id.btn_delete_option);
        }
    }
}