package com.example.lab4_5.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TaskListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Task> ITEMS = new ArrayList<Task>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Task> ITEM_MAP = new HashMap<String, Task>();

    private static final int COUNT = 6;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static void clearList() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }
    public static void addItem(Task item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Task createDummyItem(int position) {
        return new Task(String.valueOf(position), "Item " + position, "Stage manager " + position /* makeDetails(position)*/,"Date " + position );
    }

    /*private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Stage Manager").append(position);
       // for (int i = 0; i < position; i++) {
            builder.append("\nStage Manager of Movie:");
       // }
        return builder.toString();
    }
    private static String makeDate(int position) {
        StringBuilder builderDate = new StringBuilder();
        builderDate.append("Date: ").append(position);
        for (int i = 0; i < position; i++) {
            builderDate.append("\nDate:");
        }
        return builderDate.toString();
    }*/
    public static void removeItem(int position) {
        String itemId = ITEMS.get(position).id;
        ITEMS.remove(position);
        ITEM_MAP.remove(itemId);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Task implements Parcelable {
        public final String id;
        public final String title;
        public final String details;
        public final String date;
        public String picPath;

        public Task(String id, String title, String details, String date) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.date = date;
            this.picPath ="";
        }
        public Task(String id, String title, String details,String date,String picPath) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.date = date;
            this.picPath =picPath;
        }

        protected Task(Parcel in) {
            id = in.readString();
            title = in.readString();
            details = in.readString();
            date = in.readString();
            picPath = in.readString();
        }

        public void setPicPath(String path) {
            this.picPath = path;
        }
        public static final Creator<Task> CREATOR = new Creator<Task>() {
            @Override
            public Task createFromParcel(Parcel in) {
                return new Task(in);
            }

            @Override
            public Task[] newArray(int size) {
                return new Task[size];
            }
        };

        @Override
        public String toString() {
            return title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(title);
            dest.writeString(details);
            dest.writeString(date);
            dest.writeString(picPath);
        }
    }
}
