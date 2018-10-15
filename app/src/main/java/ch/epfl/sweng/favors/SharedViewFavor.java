package ch.epfl.sweng.favors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ch.epfl.sweng.favors.database.Favor;

public class SharedViewFavor extends ViewModel {
    private final MutableLiveData<Favor> favor = new MutableLiveData<>();

    public void select(Favor item) {
        favor.setValue(item);
    }

    public LiveData<Favor> getfavor() {
        return favor;
    }
}
