package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityMainBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.welcomeMessageText, 4);
    }
    // views
    @NonNull
    public final android.widget.Button loginButton;
    @NonNull
    public final android.widget.Button logoutButton;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.widget.Button registerButton;
    @NonNull
    public final android.widget.TextView welcomeMessageText;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.FavorsMain mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityMainBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.loginButton = (android.widget.Button) bindings[1];
        this.loginButton.setTag(null);
        this.logoutButton = (android.widget.Button) bindings[3];
        this.logoutButton.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.registerButton = (android.widget.Button) bindings[2];
        this.registerButton.setTag(null);
        this.welcomeMessageText = (android.widget.TextView) bindings[4];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.elements == variableId) {
            setElements((ch.epfl.sweng.favors.FavorsMain) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setElements(@Nullable ch.epfl.sweng.favors.FavorsMain Elements) {
        this.mElements = Elements;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.elements);
        super.requestRebind();
    }
    @Nullable
    public ch.epfl.sweng.favors.FavorsMain getElements() {
        return mElements;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeElementsIsConnected((android.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeElementsIsConnected(android.databinding.ObservableBoolean ElementsIsConnected, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        ch.epfl.sweng.favors.FavorsMain elements = mElements;
        boolean elementsIsConnectedGet = false;
        android.databinding.ObservableBoolean elementsIsConnected = null;
        boolean ElementsIsConnected1 = false;

        if ((dirtyFlags & 0x7L) != 0) {



                if (elements != null) {
                    // read elements.isConnected
                    elementsIsConnected = elements.isConnected;
                }
                updateRegistration(0, elementsIsConnected);


                if (elementsIsConnected != null) {
                    // read elements.isConnected.get()
                    elementsIsConnectedGet = elementsIsConnected.get();
                }


                // read !elements.isConnected.get()
                ElementsIsConnected1 = !elementsIsConnectedGet;
        }
        // batch finished
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            this.loginButton.setEnabled(ElementsIsConnected1);
            this.logoutButton.setEnabled(elementsIsConnectedGet);
            this.registerButton.setEnabled(ElementsIsConnected1);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivityMainBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityMainBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityMainBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.activity_main, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivityMainBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityMainBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.activity_main, null, false), bindingComponent);
    }
    @NonNull
    public static ActivityMainBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityMainBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_main_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityMainBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements.isConnected
        flag 1 (0x2L): elements
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}