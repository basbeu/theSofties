package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class NavHeaderBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView1;
    @NonNull
    private final android.widget.TextView mboundView2;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.Logged_in_Screen mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public NavHeaderBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 3);
        final Object[] bindings = mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
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
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.elements);
        super.requestRebind();
    }
    @Nullable
    public ch.epfl.sweng.favors.Logged_in_Screen getElements() {
        return mElements;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeElementsEmail((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeElementsFirstName((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeElementsLastName((android.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeElementsEmail(android.databinding.ObservableField<java.lang.String> ElementsEmail, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsFirstName(android.databinding.ObservableField<java.lang.String> ElementsFirstName, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsLastName(android.databinding.ObservableField<java.lang.String> ElementsLastName, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
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
        android.databinding.ObservableField<java.lang.String> elementsEmail = null;
        ch.epfl.sweng.favors.Logged_in_Screen elements = mElements;
        java.lang.String elementsEmailGet = null;
        android.databinding.ObservableField<java.lang.String> elementsFirstName = null;
        java.lang.String elementsFirstNameCharElementsLastName = null;
        android.databinding.ObservableField<java.lang.String> elementsLastName = null;
        java.lang.String elementsFirstNameGet = null;
        java.lang.String elementsFirstNameChar = null;
        java.lang.String elementsLastNameGet = null;

        if ((dirtyFlags & 0x1fL) != 0) {


            if ((dirtyFlags & 0x19L) != 0) {

                    if (elements != null) {
                        // read elements.email
                        elementsEmail = elements.email;
                    }
                    updateRegistration(0, elementsEmail);


                    if (elementsEmail != null) {
                        // read elements.email.get()
                        elementsEmailGet = elementsEmail.get();
                    }
            }
            if ((dirtyFlags & 0x1eL) != 0) {

                    if (elements != null) {
                        // read elements.firstName
                        elementsFirstName = elements.firstName;
                        // read elements.lastName
                        elementsLastName = elements.lastName;
                    }
                    updateRegistration(1, elementsFirstName);
                    updateRegistration(2, elementsLastName);


                    if (elementsFirstName != null) {
                        // read elements.firstName.get()
                        elementsFirstNameGet = elementsFirstName.get();
                    }
                    if (elementsLastName != null) {
                        // read elements.lastName.get()
                        elementsLastNameGet = elementsLastName.get();
                    }


                    // read (elements.firstName.get()) + (' ')
                    elementsFirstNameChar = (elementsFirstNameGet) + (' ');


                    // read ((elements.firstName.get()) + (' ')) + (elements.lastName.get())
                    elementsFirstNameCharElementsLastName = (elementsFirstNameChar) + (elementsLastNameGet);
            }
        }
        // batch finished
        if ((dirtyFlags & 0x1eL) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, elementsFirstNameCharElementsLastName);
        }
        if ((dirtyFlags & 0x19L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView2, elementsEmailGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static NavHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static NavHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<NavHeaderBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.nav_header, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static NavHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static NavHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.nav_header, null, false), bindingComponent);
    }
    @NonNull
    public static NavHeaderBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static NavHeaderBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/nav_header_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new NavHeaderBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements.email
        flag 1 (0x2L): elements.firstName
        flag 2 (0x3L): elements.lastName
        flag 3 (0x4L): elements
        flag 4 (0x5L): null
    flag mapping end*/
    //end
}