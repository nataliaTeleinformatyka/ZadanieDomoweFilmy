package com.example.lab4_5;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab4_5.TaskFragment.OnListFragmentInteractionListener;
import com.example.lab4_5.tasks.TaskListContent;
import com.example.lab4_5.tasks.TaskListContent.Task;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private final List<TaskListContent.Task> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTaskRecyclerViewAdapter(List<Task> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Task task = mValues.get(position);
        holder.mItem = task;
        holder.mContentView.setText(task.title);
        final String picPath = task.picPath;
        final Context context = holder.mView.getContext();

        if (picPath != null && !picPath.isEmpty()) {
            if (picPath.contains("drawable")) {
                int id;
                Resources resource = context.getResources();
                switch (picPath) {
                    case "drawable 1":
                        id = R.drawable.botoks;
                        break;
                    case "drawable 2":
                        id = R.drawable.kler;
                        break;
                    case "drawable 3":
                        id = R.drawable.kochajalborzuc;
                        break;
                    case "drawable 4":
                        id = R.drawable.listydom3;
                        break;
                    case "drawable 5":
                        id = R.drawable.samiswoi;
                        break;
                    case "drawable 6":
                        id = R.drawable.upanabogawogrodku;
                        break;
                    default:
                        id = R.drawable.upanabogawogrodku;
                }
                Bitmap bmp = BitmapFactory.decodeResource(resource, id);
                int width = 200;
                int height = 200;
                Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
                holder.mItemImageView.setImageBitmap(resizedbitmap);
            } else {
                Bitmap cameraImage = PicUtils.decodePic(picPath, 200, 200);
                holder.mItemImageView.setImageBitmap(cameraImage);
            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentClickInteraction(holder.mItem, position);
                }
            }
        });
        holder.mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onDeleteClickInteraction(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mItemImageView;
        public final ImageView mDeleteImage;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mItemImageView= (ImageView)  view.findViewById(R.id.item_image);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDeleteImage = (ImageView) view.findViewById(R.id.deleteBasket);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
