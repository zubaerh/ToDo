package com.sorovi.android.todoappbatch1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sorovi.android.todoappbatch1.databinding.FragmentNewTodoBinding;
import com.sorovi.android.todoappbatch1.entities.Todo;
import com.sorovi.android.todoappbatch1.pickers.DatePickerDialogFragment;
import com.sorovi.android.todoappbatch1.pickers.TimePickerDialogFragment;
import com.sorovi.android.todoappbatch1.todoworkmanager.TodoWorker;
import com.sorovi.android.todoappbatch1.utils.TodoConstants;
import com.sorovi.android.todoappbatch1.viewmodels.TodoViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class NewTodoFragment extends Fragment {

    private FragmentNewTodoBinding binding;
    String dateString, timeString;
    int year, month, day, hour, minute;
    private String priority = TodoConstants.NORMAL;
    private TodoViewModel todoViewModel;
    public NewTodoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewTodoBinding.inflate(inflater,container,false);
      todoViewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);
      initDateAndTime();
      binding.priorityRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup radioGroup, int i) {
              final RadioButton rb = radioGroup.findViewById(i);
              priority = rb.getText().toString();
          }
      });


        binding.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialogFragment().show(getChildFragmentManager(),null);

            }
        });
        binding.timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialogFragment().show(getChildFragmentManager(),null);

            }
        });
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = binding.todoInputET.getText().toString();
                final Todo todo = new Todo(name,priority,dateString,timeString,false);
                todoViewModel.insertTodo(todo);
                binding.todoInputET.setText("");
                if(priority.equals(TodoConstants.HIGH)){
                    scheduleNotification(name);
                }

            }


        });
        getChildFragmentManager().setFragmentResultListener(TodoConstants.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if(result.containsKey(TodoConstants.DATE_KEY)){
                    dateString = result.getString(TodoConstants.DATE_KEY);
                    year = result.getInt(TodoConstants.YEAR);
                    month = result.getInt(TodoConstants.MONTH);
                    day = result.getInt(TodoConstants.DAY);
                    binding.dateBtn.setText(dateString);
                }else if (result.containsKey(TodoConstants.TIME_KEY)){
                    timeString = result.getString(TodoConstants.TIME_KEY);
                    hour = result.getInt(TodoConstants.HOUR);
                    minute = result.getInt(TodoConstants.MINUTE);
                    binding.timeBtn.setText(timeString);
                }
            }
        });
        return binding.getRoot();
    }
    private void scheduleNotification(String name) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(year,month,day,hour,minute);
        final Date currentDate = new Date();
        long diff = calendar.getTimeInMillis() - currentDate.getTime();
       // Log.e("todoworker","scheduleNotification"+(diff/(100*60)));

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build();
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TodoWorker.class)
                .setInitialDelay(diff, TimeUnit.MILLISECONDS)
                .setInputData(new Data.Builder().putString("name",name).build())
                .build();
        WorkManager.getInstance(requireContext()).enqueue(request);
    }
    private void initDateAndTime()
    {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        timeString = new SimpleDateFormat("hh:mm a").format(new Date());
    }
}