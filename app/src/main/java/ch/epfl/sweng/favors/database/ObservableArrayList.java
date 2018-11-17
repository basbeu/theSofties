package ch.epfl.sweng.favors.database;

import android.databinding.Observable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableArrayList<T> extends ArrayList<T> implements Observable {

    @Override
    public boolean add(T value){
        boolean out = super.add(value);
        notifyContentChange();
        return out;
    }

    @Override
    public void add(int i, T value){
        super.add(i, value);
        notifyContentChange();
    }

    @Override
    public boolean addAll(Collection<? extends T> all){
        boolean value = super.addAll(all);
        notifyContentChange();
        return value;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> all){
        boolean value = super.addAll(i, all);
        notifyContentChange();
        return value;
    }

    @Override
    public void clear(){
        super.clear();
        notifyContentChange();
    }

    @Override
    public T remove(int index)  {
        T out = super.remove(index);
        notifyContentChange();
        return out;
    }

    @Override
    public boolean remove(Object o) {
        boolean out = super.remove(o);
        notifyContentChange();
        return out;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean out = super.removeAll(c);
        notifyContentChange();
        return out;
    }

    @Override
    public void removeRange(int fromIndex, int toIndex)  {
        super.removeRange(fromIndex, toIndex);
        notifyContentChange();
    }

    List<OnPropertyChangedCallback> callbacks = new ArrayList<>();
    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        assert(callback != null);
        callbacks.add(callback);
    }
    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }
    private void notifyContentChange(){
        for (OnPropertyChangedCallback callback : callbacks){
            callback.onPropertyChanged(this, 0);
        }
    }
}
