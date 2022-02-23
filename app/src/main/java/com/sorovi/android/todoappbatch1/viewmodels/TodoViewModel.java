package com.sorovi.android.todoappbatch1.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sorovi.android.todoappbatch1.entities.Todo;
import com.sorovi.android.todoappbatch1.repos.TodoRepository;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository repository;
    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
    }
    public void insertTodo(Todo todo){
        repository.insertTodo(todo);
    }
    public void updateTodo(Todo todo){
        repository.updateTodo(todo);
    }
    public void deleteTodo(Todo todo){
        repository.deleteTodo(todo);
    }
    public LiveData<List<Todo>> getAlltodos(){
        return repository.getAlltodos();
    }
    public LiveData<List<Todo>> getAllDoneTodos(){
        return repository.getAllDonetodos();
    }
}
