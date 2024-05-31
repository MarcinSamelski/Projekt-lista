package com.example.myapp2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskDeleteListener {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TASK_LIST_KEY = "taskList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        Button addTaskButton = findViewById(R.id.dodajZadanieButton);

        loadTasks();

        taskAdapter = new TaskAdapter(taskList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
    }

    private void showAddTaskDialog() {
        final EditText taskInput = new EditText(this);
        taskInput.setHint("Wpisz zadanie");

        new AlertDialog.Builder(this)
                .setTitle("Dodaj nowe zadanie")
                .setView(taskInput)
                .setPositiveButton("Dodaj", (dialog, which) -> {
                    String taskName = taskInput.getText().toString().trim();
                    if (!TextUtils.isEmpty(taskName)) {
                        taskList.add(new Task(taskName));
                        taskAdapter.notifyItemInserted(taskList.size() - 1);
                        saveTasks();
                    } else {
                        Toast.makeText(MainActivity.this, "Nazwa zadania nie może być pusta", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> taskSet = new HashSet<>();
        for (Task task : taskList) {
            taskSet.add(task.getName() + "," + task.isDone());
        }

        editor.putStringSet(TASK_LIST_KEY, taskSet);
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Set<String> taskSet = sharedPreferences.getStringSet(TASK_LIST_KEY, new HashSet<>());

        taskList = new ArrayList<>();
        for (String taskString : taskSet) {
            String[] taskData = taskString.split(",");
            String taskName = taskData[0];
            boolean done = taskData.length > 1 ? Boolean.parseBoolean(taskData[1]) : false; // Obsługa braku drugiego elementu
            Task task = new Task(taskName);
            task.setDone(done);
            taskList.add(task);
        }
    }

    @Override
    public void onTaskDelete(Task task) {
        taskList.remove(task);
        saveTasks();
    }
}
