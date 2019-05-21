package com.example.lab4_5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab4_5.tasks.TaskListContent;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskInfoFragment extends Fragment  {
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    private String mCurrentPhotoPath;
    private TaskListContent.Task mDisplayedTask;

    public TaskInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_info, container, false);
    }

    public void displayTask(TaskListContent.Task task) {
        FragmentActivity activity = getActivity();

        (activity.findViewById(R.id.displayFragment)).setVisibility(View.VISIBLE);
        TextView taskInfoTitle = activity.findViewById(R.id.taskInfoTitle);
        TextView taskInfoDescription = activity.findViewById(R.id.taskInfoDescription);
        TextView taskInfoDate = activity.findViewById(R.id.taskInfoDate);
        final ImageView taskInfoImage = activity.findViewById(R.id.taskInfoImage);

        taskInfoTitle.setText(task.title);
        taskInfoDescription.setText(task.details);
        taskInfoDate.setText(task.date);

        if (task.picPath != null && !task.picPath.isEmpty()) {
            if(task.picPath.contains("drawable")) {
            Drawable taskDrawable;
            switch (task.picPath) {
                case "drawable 1":
                    taskDrawable = activity.getResources().getDrawable(R.drawable.botoks);
                    break;
                case "drawable 2":
                    taskDrawable = activity.getResources().getDrawable(R.drawable.kler);
                    break;
                case "drawable 3":
                    taskDrawable = activity.getResources().getDrawable(R.drawable.kochajalborzuc);
                    break;
                case "drawable 4":
                    taskDrawable = activity.getResources().getDrawable(R.drawable.listydom3);
                    break;
                case "drawable 5":
                    taskDrawable = activity.getResources().getDrawable(R.drawable.samiswoi);
                    break;
                case "drawable 6":
                    taskDrawable = activity.getResources().getDrawable(R.drawable.upanabogawogrodku);
                    break;
                case "drawable 7":
                    taskDrawable = activity.getResources().getDrawable(R.drawable.kapitanmarvel);
                    break;
                default:
                    taskDrawable = activity.getResources().getDrawable(R.drawable.botoks);
            }
            taskInfoImage.setImageDrawable(taskDrawable);
        } else {
            Handler handler = new Handler();
            taskInfoImage.setVisibility(View.INVISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    taskInfoImage.setVisibility(View.VISIBLE);
                    Bitmap cameraImage = PicUtils.decodePic(mDisplayedTask.picPath, taskInfoImage.getWidth(), taskInfoImage.getHeight());
                    taskInfoImage.setImageBitmap(cameraImage);
                }
            }, 200);
        }
    }else {
            taskInfoImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.samiswoi));
        }
        mDisplayedTask = task;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        activity.findViewById(R.id.displayFragment).setVisibility(View.INVISIBLE);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            TaskListContent.Task receivedTask = intent.getParcelableExtra(MainActivity.taskExtra);
            if (receivedTask != null) {
                displayTask(receivedTask);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            FragmentActivity holdingActivity = getActivity();
            if(holdingActivity != null) {
                ImageView taskImage = holdingActivity.findViewById(R.id.taskInfoImage);

                Bitmap cameraImage = PicUtils.decodePic(mCurrentPhotoPath, taskImage.getWidth(), taskImage.getHeight());
                taskImage.setImageBitmap(cameraImage);
                mDisplayedTask.setPicPath(mCurrentPhotoPath);
                TaskListContent.Task task = TaskListContent.ITEM_MAP.get(mDisplayedTask.id);
                if(task != null) {
                    task.setPicPath(mCurrentPhotoPath);
                }
                if(holdingActivity instanceof MainActivity) {
                    ((TaskFragment) holdingActivity.getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataChange();
                }
            }
        }
    }
}
