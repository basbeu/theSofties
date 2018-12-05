package ch.epfl.sweng.favors.database;

import android.databinding.Observable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableArrayList<T> extends ArrayList<T> implements Observable {

    public enum ContentChangeType{All, Clear, Add, AddAll, Remove, Update};

    @Override
    public boolean add(T value){
        boolean out = super.add(value);
        notifyContentChange(ContentChangeType.Add);
        return out;
    }

    @Override
    public void add(int i, T value){
        super.add(i, value);
        notifyContentChange(ContentChangeType.Add);
    }

    @Override
    public boolean addAll(Collection<? extends T> all){
        boolean value = super.addAll(all);
        notifyContentChange(ContentChangeType.AddAll);
        return value;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> all){
        boolean value = super.addAll(i, all);
        notifyContentChange(ContentChangeType.AddAll);
        return value;
    }

    @Override
    public void clear(){
        super.clear();
        notifyContentChange(ContentChangeType.Clear);
    }

    @Override
    public T remove(int index)  {
        T out = super.remove(index);
        notifyContentChange(ContentChangeType.Remove);
        return out;
    }

    @Override
    public boolean remove(Object o) {
        boolean out = super.remove(o);
        notifyContentChange(ContentChangeType.Remove);
        return out;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean out = super.removeAll(c);
        notifyContentChange(ContentChangeType.Remove);
        return out;
    }

    public void update(Collection<? extends T> all){
        super.clear();
        super.addAll(all);
        notifyContentChange(ContentChangeType.Update);
    }

    @Override
    public void removeRange(int fromIndex, int toIndex)  {
        super.removeRange(fromIndex, toIndex);
        notifyContentChange(ContentChangeType.Remove);
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
    private void notifyContentChange(ContentChangeType type){
        for (OnPropertyChangedCallback callback : callbacks){
            callback.onPropertyChanged(this, type.ordinal());
        }
        for (OnPropertyChangedCallback callback : callbacks){
            callback.onPropertyChanged(this, ContentChangeType.All.ordinal());
        }
    }
    public void changeOnPropertyChangedCallback(OnPropertyChangedCallback callback){
        callbacks.clear();
        addOnPropertyChangedCallback(callback);
    }

}
