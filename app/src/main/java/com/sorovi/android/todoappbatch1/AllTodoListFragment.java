package com.sorovi.android.todoappbatch1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sorovi.android.todoappbatch1.adapter.TodoAdapter;
import com.sorovi.android.todoappbatch1.databinding.FragmentAllTodoListBinding;
import com.sorovi.android.todoappbatch1.entities.Todo;
import com.sorovi.android.todoappbatch1.listeners.OnTodoStatusChangeListener;
import com.sorovi.android.todoappbatch1.viewmodels.TodoViewModel;

import java.util.List;

public class AllTodoListFragment extends Fragment implements OnTodoStatusChangeListener {

    private FragmentAllTodoListBinding binding;
    private TodoViewModel todoViewModel;
    public AllTodoListFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllTodoListBinding.inflate(inflater,container,false);
        todoViewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);
        final TodoAdapter adapter = new TodoAdapter(this);
        binding.allTodoRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.allTodoRV.setAdapter(adapter);

        todoViewModel.getAlltodos().observe(getViewLifecycleOwner(),todoList ->{
            adapter.submitList(todoList);
        });
        return binding.getRoot();
    }

    @Override
    public void onTodoStatusChange(Todo todo) {
        todoViewModel.updateTodo(todo);
    }
}