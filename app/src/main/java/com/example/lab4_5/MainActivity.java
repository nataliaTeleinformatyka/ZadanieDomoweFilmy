package com.example.lab4_5;

import android.arch.lifecycle.SingleGeneratedAdapterObserver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.renderscript.Type;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4_5.tasks.TaskListContent;
import com.example.lab4_5.tasks.TaskListContent.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;

public class MainActivity extends AppCompatActivity implements
        TaskFragment.OnListFragmentInteractionListener,
        DeleteDialog.OnDeleteDialogInteractionListener {

    public static final String taskExtra = "taskExtra";
    private final String CURRENT_TASK_KEY = "CurrentTask";
    private final String TASKS_SHARED_PREFS = "TasksSharedPrefs";
    private final String NUM_TASKS = "NumOfTasks";
    private final String TASK = "task_";
    private final String DETAIL = "desc_";
    private final String PIC = "pic_";
    private final String ID = "id_";
    private final String DATE = "date_";
    private String mCurrentPhotoPath;
    private int currentItemPosition = -1;
    private static final int ADD_CAMERA_ACTIVITY_REQUEST_CODE = 3;
    private static final int ADD_ACTIVITY_REQUEST_CODE = 2;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private TaskListContent.Task currentTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            currentTask = savedInstanceState.getParcelable(CURRENT_TASK_KEY);
        }
        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        restoreTasksFromSharedPreferences();
        FloatingActionButton addNewTask = findViewById(R.id.add_task);
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startThirdActivity();
            }
        });

        FloatingActionButton addNewTaskCamera = findViewById(R.id.add_task_camera);
        addNewTaskCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFourthActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        //restoreTasksFromSharedPreferences();
        ((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataChange();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (currentTask != null)
                displayTaskInFragment(currentTask);
        }
    }
    @Override
    protected void onDestroy() {
        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = tasks.edit();
        editor.apply();
        saveTasksToSharedPreferences();
        super.onDestroy();
    }
    @Override
    public void onListFragmentClickInteraction(Task task, int position) {
        currentTask = task;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displayTaskInFragment(task);
        } else {
            startSecondActivity(task, position);
        }
    }
    private void startSecondActivity(TaskListContent.Task task, int position) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra(taskExtra, task);
        startActivity(intent);
    }
    private void startThirdActivity() {
        Intent intentAdd = new Intent(this, AddTaskActivity.class);
        int id = 2;
        intentAdd.putExtra("id", id);
        startActivityForResult(intentAdd, ADD_ACTIVITY_REQUEST_CODE);
    }
    private void startFourthActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
           File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getString(R.string.myFileprovider), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAPTURE_IMAGE) {
            int id = ADD_CAMERA_ACTIVITY_REQUEST_CODE;

            //  ImageView image = findViewById(R.id.imageView);
            // Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            // image.setImageBitmap(myBitmap);


            Intent cameraAddIntent = new Intent(this, AddTaskActivity.class);

            cameraAddIntent.putExtra("id", id);

            Toast.makeText(this, "on ACTIVITIY PHOTO URI :::: " + mCurrentPhotoPath, Toast.LENGTH_SHORT).show();


            cameraAddIntent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);

            startActivityForResult(cameraAddIntent, ADD_CAMERA_ACTIVITY_REQUEST_CODE);
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d("Camera", " failed to create directory");
                return null;
            }
        }
        File image = new File(storageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void displayTaskInFragment(TaskListContent.Task task) {
        TaskInfoFragment taskInfoFragment = ((TaskInfoFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment));
        if (taskInfoFragment != null) {
            taskInfoFragment.displayTask(task);
        }
    }
    @Override
    public void onDeleteClickInteraction(int position) {
        showDeleteDialog();
        currentItemPosition = position;
    }
    private void showDeleteDialog() {
        DeleteDialog.newInstance().show(getSupportFragmentManager(), getString(R.string.delete_dialog_tag));
    }
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (currentItemPosition != -1 && currentItemPosition < TaskListContent.ITEMS.size()) {
            SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = tasks.edit();
            String path = tasks.getString(PIC + currentItemPosition, "88");

            editor.remove(TASK + currentItemPosition);
            editor.remove(DETAIL + currentItemPosition);
            editor.remove(DATE + currentItemPosition);
            editor.remove(PIC + currentItemPosition);
            editor.remove(ID + currentItemPosition);
            editor.apply();
            File file = new File(path);
            boolean test = file.delete();
            Toast.makeText(this, "usunieto " + test + " o adresie " + path + " numer " + currentItemPosition, Toast.LENGTH_LONG).show();

            TaskListContent.removeItem(currentItemPosition);
            //saveTasksToSharedPreferencesAfterDelete();
            ((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataChange();

        }
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        View v = findViewById(R.id.addButton);
        if (v != null) {
            Snackbar.make(v, getString(R.string.delete_cancel_msg), Snackbar.LENGTH_LONG).setAction(getString(R.string.retry_msg), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog();
                }
            }).show();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentTask != null)
            outState.putParcelable(CURRENT_TASK_KEY, currentTask);
        super.onSaveInstanceState(outState);
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
    private void saveTasksToSharedPreferencesAfterDelete() {
        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = tasks.edit();
        editor.clear();
        editor.putInt(NUM_TASKS, TaskListContent.ITEMS.size());
        Toast.makeText(this, "pozycja " + currentItemPosition + " wszystkich jest  " + tasks.getInt(NUM_TASKS,0), Toast.LENGTH_SHORT).show();

        //for(int j=currentItemPosition; j < tasks.getInt(NUM_TASKS,0); j++) {
            for (int i = currentItemPosition+1; i < TaskListContent.ITEMS.size(); i++) {
                TaskListContent.Task task = TaskListContent.ITEMS.get(i);
                Toast.makeText(this, "pozycja w forze" + currentItemPosition + " wszystkich jest  " + TaskListContent.ITEMS.get(i), Toast.LENGTH_SHORT).show();

                String title = tasks.getString(TASK + i, "1000");
                String detail = tasks.getString(DETAIL + i, "1000");
                String date = tasks.getString(DATE + i, "1000");
                String picPath = tasks.getString(PIC + i, "1000");
                String id = tasks.getString(ID + i, "1000");

                Toast.makeText(this, "tytul " + title + " o adresie " + picPath + " id " + id + "date " + date, Toast.LENGTH_LONG).show();


                /*editor.putString(TASK + i, title);
                    editor.putString(DETAIL + i, detail);
                    editor.putString(DATE + i, date);
                    editor.putString(PIC + i, picPath);
                    editor.putString(ID + i, id);*/
            }
            editor.apply();

    }
    private void restoreTasksFromSharedPreferences() {
        SharedPreferences tasks = getSharedPreferences(TASKS_SHARED_PREFS, MODE_PRIVATE);
        int numOfTasks = tasks.getInt(NUM_TASKS, 0);
        if (numOfTasks != 0) {
            TaskListContent.clearList();

            for (int i = 0; i < numOfTasks; i++) {
                String title = tasks.getString(TASK + i, "1000");
                if (title.equals("1000")) {
                    String detail = tasks.getString(DETAIL + i, "1000");
                    String date = tasks.getString(DATE + i, "1000");
                    String picPath = tasks.getString(PIC + i, "1000");
                    String id = tasks.getString(ID + i, "1000");
                    TaskListContent.addItem(new TaskListContent.Task(id, title, detail, date, picPath));
                }
            }
        }
    }
}