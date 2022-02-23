package com.sorovi.android.todoappbatch1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorovi.android.todoappbatch1.adapter.TodoAdapter;
import com.sorovi.android.todoappbatch1.databinding.FragmentDoneTodoListBinding;
import com.sorovi.android.todoappbatch1.entities.Todo;
import com.sorovi.android.todoappbatch1.listeners.OnTodoStatusChangeListener;
import com.sorovi.android.todoappbatch1.viewmodels.TodoViewModel;


public class DoneTodoListFragment extends Fragment implements OnTodoStatusChangeListener {

   private FragmentDoneTodoListBinding binding;
   private TodoViewModel viewModel;
    public DoneTodoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoneTodoListBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);

        final TodoAdapter adapter = new TodoAdapter(this);
        binding.doneTodoListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.doneTodoListRV.setAdapter(adapter);

        viewModel.getAllDoneTodos().observe(getViewLifecycleOwner(),todoList -> {
            adapter.submitList(todoList);
            if(todoList.isEmpty()){
                binding.emptyListMsgTV.setVisibility(View.VISIBLE);
            }else {
                binding.emptyListMsgTV.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onTodoStatusChange(Todo todo) {
        viewModel.updateTodo(todo);
    }
}