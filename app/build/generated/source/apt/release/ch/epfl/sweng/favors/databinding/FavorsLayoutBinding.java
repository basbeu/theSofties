package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FavorsLayoutBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.createFavorTitle, 1);
        sViewsWithIds.put(R.id.titleFavor, 2);
        sViewsWithIds.put(R.id.imageView3, 3);
        sViewsWithIds.put(R.id.floatingActionButton, 4);
        sViewsWithIds.put(R.id.descriptionFavor, 5);
        sViewsWithIds.put(R.id.addFavor, 6);
    }
    // views
    @NonNull
    public final android.widget.Button addFavor;
    @NonNull
    public final android.widget.TextView createFavorTitle;
    @NonNull
    public final android.widget.EditText descriptionFavor;
    @NonNull
    public final android.support.design.widget.FloatingActionButton floatingActionButton;
    @NonNull
    public final android.widget.ImageView imageView3;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.EditText titleFavor;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.FavorsFragment mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FavorsLayoutBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.addFavor = (android.widget.Button) bindings[6];
        this.createFavorTitle = (android.widget.TextView) bindings[1];
        this.descriptionFavor = (android.widget.EditText) bindings[5];
        this.floatingActionButton = (android.support.design.widget.FloatingActionButton) bindings[4];
        this.imageView3 = (android.widget.ImageView) bindings[3];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.titleFavor = (android.widget.EditText) bindings[2];
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
            setElements((ch.epfl.sweng.favors.FavorsFragment) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setElements(@Nullable ch.epfl.sweng.favors.FavorsFragment Elements) {
        this.mElements = Elements;
    }
    @Nullable
    public ch.epfl.sweng.favors.FavorsFragment getElements() {
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
    public static FavorsLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FavorsLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FavorsLayoutBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.favors_layout, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FavorsLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FavorsLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.favors_layout, null, false), bindingComponent);
    }
    @NonNull
    public static FavorsLayoutBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FavorsLayoutBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/favors_layout_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FavorsLayoutBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}