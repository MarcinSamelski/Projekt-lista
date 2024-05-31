package com.example.myapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskDeleteListener deleteListener;

    public TaskAdapter(List<Task> taskList, OnTaskDeleteListener deleteListener) {
        this.taskList = taskList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        String taskNameAndPriority = task.getPriority() + ": " + task.getName();
        holder.taskNameAndPriority.setText(taskNameAndPriority);
        holder.checkboxDone.setChecked(task.isDone());
        holder.prioritySpinner.setSelection(getPriorityIndex(task.getPriority())); // Ustawienie wybranego poziomu ważności
        holder.prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String priority = parent.getItemAtPosition(position).toString();
                task.setPriority(priority);
                String taskNameAndPriorityUpdated = task.getPriority() + ": " + task.getName();
                holder.taskNameAndPriority.setText(taskNameAndPriorityUpdated);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (!taskList.isEmpty()) {
                deleteListener.onTaskDelete(task);
                taskList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, taskList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private int getPriorityIndex(String priority) {
        switch (priority) {
            case "Normal":
                return 0;
            case "Ważne":
                return 1;
            case "Bardzo ważne":
                return 2;
            default:
                return 0;
        }
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameAndPriority;
        CheckBox checkboxDone;
        Spinner prioritySpinner;
        ImageButton deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameAndPriority = itemView.findViewById(R.id.task_name);
            checkboxDone = itemView.findViewById(R.id.checkbox_done);
            prioritySpinner = itemView.findViewById(R.id.priority_spinner);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnTaskDeleteListener {
        void onTaskDelete(Task task);
    }
}
