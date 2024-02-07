package com.example.cs2340_firstproject.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class ClassListViewModel extends ViewModel {
    private MutableLiveData<List<ClassItem>> classItemList = new MutableLiveData<>(new ArrayList<>());

    public void addClassItem(ClassItem newItem) {
        List<ClassItem> currentList = classItemList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(newItem);
        classItemList.setValue(currentList);
    }

    public LiveData<List<ClassItem>> getClassItemList() {
        return classItemList;
    }

    public void updateClassItem(int position, ClassItem updatedClass) {
        List<ClassItem> currentList = classItemList.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.set(position, updatedClass);
            classItemList.setValue(currentList);
        }
    }

    public void removeClassItems(List<ClassItem> itemsToRemove) {
        List<ClassItem> currentItems = classItemList.getValue();
        if (currentItems != null) {
            currentItems.removeAll(itemsToRemove);
            classItemList.setValue(currentItems); // Update LiveData
        }
    }
}
