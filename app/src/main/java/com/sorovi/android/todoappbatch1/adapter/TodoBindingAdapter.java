package com.sorovi.android.todoappbatch1.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.sorovi.android.todoappbatch1.R;
import com.sorovi.android.todoappbatch1.utils.TodoConstants;

public class TodoBindingAdapter {
    @BindingAdapter(value = "app:setPriorityIcon")
    public static void setPriorityIcon(ImageView imageView,String priority){
        int icon;
        switch (priority){
            case TodoConstants.LOW:
                icon = R.drawable.ic_todo_priority_low_24;
                break;
            case TodoConstants.NORMAL:
                icon = R.drawable.ic_todo_priority_normal_24;
                break;

            default:
                icon = R.drawable.ic_todo_priority_high_24;
                break;
        }
        imageView.setImageResource(icon);
    }
}
