package com.example.lab4_5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4_5.tasks.TaskListContent;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static com.example.lab4_5.TaskInfoFragment.REQUEST_IMAGE_CAPTURE;

public class AddTaskActivity extends AppCompatActivity {

    private String selectedImage = "drawable 1";
    private final String TASKS_SHARED_PREFS = "TasksSharedPrefs";
    private final String NUM_TASKS = "NumOfTasks";
    private final String TASK = "task_";
    private final String DETAIL = "desc_";
    private final String PIC = "pic_";
    private final String ID = "id_";
    private final String DATE = "date_";
    private final int ADD_CAMERA_ACTIVITY_REQUEST_CODE = 3;
    private final int ADD_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        int id = getIntent().getIntExtra("id", 0);
        if (id == ADD_CAMERA_ACTIVITY_REQUEST_CODE) {
            selectedImage = getIntent().getStringExtra("mCurrentPhotoPath");
        }
        if (id == ADD_ACTIVITY_REQUEST_CODE) {
            randomImage();
        }
    }
    protected void randomImage() {
        Random rand = new Random();
        int number = rand.nextInt(6) + 1;
        selectedImage = "drawable " + number;
    }
    private void saveTasksToSharedPreferences() {
        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = tasks.edit();
        editor.clear();
        editor.putInt(NUM_TASKS, TaskListContent.ITEMS.size());
        for (int i = 0; i < TaskListContent.ITEMS.size(); i++) {
            TaskListContent.Task task = TaskListContent.ITEMS.get(i);
            editor.putString(TASK + i, task.title);
            editor.putString(DETAIL + i, task.details);
            editor.putString(DATE + i, task.date);
            editor.putString(PIC + i, task.picPath);
            editor.putString(ID + i, task.id);
        }
        editor.apply();
    }
  /*  private void restoreTasksFromSharedPreferences() {
        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        int numOfTasks = tasks.getInt(NUM_TASKS, 0);
        if (numOfTasks != 0) {
            TaskListContent.clearList();
            for (int i = 0; i < numOfTasks; i++) {
                String title = tasks.getString(TASK + i, "0");
                String detail = tasks.getString(DETAIL + i, "0");
                String date = tasks.getString(DATE + i, "0");
                String picPath = tasks.getString(PIC + i, "0");
                String id = tasks.getString(ID + i, "0");
                TaskListContent.addItem(new TaskListContent.Task(id, title, detail, date, picPath));
            }
        }
    }*/

    public void addTask(View view) {
        EditText taskTitleEditTxt = findViewById(R.id.taskTitle);
        EditText taskDescriptionEditTxt = findViewById(R.id.taskDescription);
        EditText taskDateEditTxt = findViewById(R.id.taskDate);

        String taskTitle = taskTitleEditTxt.getText().toString();
        String taskDescription = taskDescriptionEditTxt.getText().toString();
        String taskDate = taskDateEditTxt.getText().toString();

        if (taskTitle.isEmpty() && taskDescription.isEmpty() && taskDate.isEmpty()) {
            TaskListContent.addItem(new TaskListContent.Task("Task." + TaskListContent.ITEMS.size() + 1,
                    getString(R.string.default_title),
                    getString(R.string.default_description),
                    getString(R.string.default_date),
                    selectedImage));
        } else {
            if (taskTitle.isEmpty())
                taskTitle = getString(R.string.default_title);
            if (taskDescription.isEmpty())
                taskDescription = getString(R.string.default_description);
            if (taskDate.isEmpty())
                taskDate = getString(R.string.default_date);
            TaskListContent.addItem(new TaskListContent.Task("Task." + TaskListContent.ITEMS.size() + 1,
                    taskTitle,
                    taskDescription,
                    taskDate,
                    selectedImage));
        }
        taskTitleEditTxt.setText("");
        taskDescriptionEditTxt.setText("");
        taskDateEditTxt.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        saveTasksToSharedPreferences();
        finish();
    }
}
