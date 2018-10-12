package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityLoggedInScreenBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 1);
        sViewsWithIds.put(R.id.fragment_container, 2);
        sViewsWithIds.put(R.id.nav_view, 3);
    }
    // views
    @NonNull
    public final android.support.v4.widget.DrawerLayout drawerLayout;
    @NonNull
    public final android.widget.FrameLayout fragmentContainer;
    @NonNull
    public final android.support.design.widget.NavigationView navView;
    @NonNull
    public final android.support.v7.widget.Toolbar toolbar;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.Logged_in_Screen mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityLoggedInScreenBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds);
        this.drawerLayout = (android.support.v4.widget.DrawerLayout) bindings[0];
        this.drawerLayout.setTag(null);
        this.fragmentContainer = (android.widget.FrameLayout) bindings[2];
        this.navView = (android.support.design.widget.NavigationView) bindings[3];
        this.toolbar = (android.support.v7.widget.Toolbar) bindings[1];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
            setElements((ch.epfl.sweng.favors.Logged_in_Screen) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setElements(@Nullable ch.epfl.sweng.favors.Logged_in_Screen Elements) {
        this.mElements = Elements;
    }
    @Nullable
    public ch.epfl.sweng.favors.Logged_in_Screen getElements() {
        return mElements;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivityLoggedInScreenBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityLoggedInScreenBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityLoggedInScreenBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.activity_logged_in__screen, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivityLoggedInScreenBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityLoggedInScreenBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.activity_logged_in__screen, null, false), bindingComponent);
    }
    @NonNull
    public static ActivityLoggedInScreenBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityLoggedInScreenBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_logged_in__screen_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityLoggedInScreenBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}