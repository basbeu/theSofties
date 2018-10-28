package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ch.epfl.sweng.favors.database.Favor;

/**
 * This class allows for a shared view on the same favor between different fragments in the app.
 * Any fragment can call the method select to select a favor that will be shared by all the different fragments that have this ViewModel
 */
public class SharedViewFavor extends ViewModel {
    private final MutableLiveData<Favor> favor = new MutableLiveData<>();

    /**
     * Take a favor and sets it as the favor that all fragments can get by calling get getFavor
     * @param item the favor that will be shared with another fragment
     */
    public void select(Favor item) {
        favor.setValue(item);
    }

    /**
     * Gets a live view on the favor in this class. If the favor changes the liveView will of course be updated automatically without having to query for new changes
     * @return the favor that is shared
     */
    public LiveData<Favor> getFavor() {
        return favor;
    }
}
