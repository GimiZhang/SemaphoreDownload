package com.example.administrator.semaphoredownload;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ThreadNameBean extends BaseObservable{
    public ObservableField<String> firstName = new ObservableField<>();
    public ObservableField<String> secondName = new ObservableField<>();
    public ObservableField<String> thirdName = new ObservableField<>();
}
