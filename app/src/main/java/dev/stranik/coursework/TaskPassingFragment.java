package dev.stranik.coursework;

import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.stranik.coursework.utils.AnswerOption;
import dev.stranik.coursework.utils.TaskItem;
import dev.stranik.coursework.utils.data.StaticWorkData;

public class TaskPassingFragment extends Fragment {
    private final int _solutionId;
    private int _currentQuestionIndex = 0;
    private final ArrayList<Integer> _answers;
    private List<TaskItem> _questions;

    private Button _nextButton;
    private Button _prevButton;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public TaskPassingFragment(int solutionId) {
        _solutionId = solutionId;
        _answers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_passing, container, false);

        _nextButton = view.findViewById(R.id.BtnNext);
        _prevButton = view.findViewById(R.id.BtnBack);

        executor.execute(() -> {
            Pair<Boolean, List<TaskItem>> taskItems = StaticWorkData.getTaskItems(_solutionId);

            mainHandler.post(() -> {
                if (!taskItems.first) {
                    // Handle error case, e.g., show a message to the user
                    Toast.makeText(getContext(), "Failed to load task items", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new TaskListFragment())
                            .addToBackStack(null)
                            .commit();
                    return;
                }

                _questions = taskItems.second;
                showCurrentQuestion(view);
            });
        });

        _nextButton.setVisibility(View.VISIBLE);
        _prevButton.setVisibility(View.VISIBLE);

        _nextButton.setOnClickListener(v -> {
            RadioGroup answersGroup = view.findViewById(R.id.answersGroup);
            int selectedId = answersGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedButton = view.findViewById(selectedId);
            int answerIndex = answersGroup.indexOfChild(selectedButton);

            if (_currentQuestionIndex < _answers.size()) {
                _answers.set(_currentQuestionIndex, answerIndex);
            } else {
                _answers.add(answerIndex);
            }

            if (_currentQuestionIndex + 1 >= _questions.size()) {
                executor.execute(() -> {
                    boolean result = StaticWorkData.submitAnswers(_solutionId, _answers);

                    mainHandler.post(() -> {
                        if (!result) {
                            Toast.makeText(getContext(), "Failed to submit answers", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "Answers submitted successfully", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new ListTaskSolutions())
                            .addToBackStack(null)
                            .commit();
                        MainActivity.updateNavigationMenu(R.id.solutions);
                    });
                });
                return;
            }

            if (_currentQuestionIndex + 2 >= _questions.size()) {
                _nextButton.setText(getString(R.string.finish_button));
            }

            _currentQuestionIndex++;
            showCurrentQuestion(view);
            _prevButton.setVisibility(View.VISIBLE);
        });

        _prevButton.setOnClickListener(v -> {
            if (_currentQuestionIndex <= 0) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ViewTaskFragment(_solutionId))
                        .addToBackStack(null)
                        .commit();
                return;
            }

            _currentQuestionIndex--;
            showCurrentQuestion(view);

            if (_currentQuestionIndex + 1 < _questions.size()) {
                _nextButton.setText(getString(R.string.next_button));
            }
        });

        return view;
    }

    private void showCurrentQuestion(View view) {
        if (_currentQuestionIndex >= _questions.size()) {
            Toast.makeText(getContext(), "All questions answered", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new ViewTaskFragment(_solutionId))
                    .addToBackStack(null)
                    .commit();
            return;
        }

        TextView tvQuestion = view.findViewById(R.id.TVQuestion);
        RadioGroup answersGroup = view.findViewById(R.id.answersGroup);

        TaskItem currentItem = _questions.get(_currentQuestionIndex);
        tvQuestion.setText(currentItem.getQuestion());
        answersGroup.removeAllViews();
        answersGroup.clearCheck();
        int i = 0;

        for (AnswerOption option : currentItem.getOptions()) {
            RadioButton button = new RadioButton(getContext());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            button.setLayoutParams(params);
            button.setText(option.getText());
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            button.setTextSize(16);
            button.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.white));

            answersGroup.addView(button);

            if (_currentQuestionIndex < _answers.size() && _answers.get(_currentQuestionIndex) == i) {
                button.setChecked(true);
            }

            i++;
        }
    }
}