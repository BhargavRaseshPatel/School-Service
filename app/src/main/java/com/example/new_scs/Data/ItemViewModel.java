package com.example.new_scs.Data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {

    private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();

    public void setData(String item1){
        selectedItem.setValue(item1);
    }

    public LiveData<String> getSelectedItem(){
        return selectedItem;
    }
}
