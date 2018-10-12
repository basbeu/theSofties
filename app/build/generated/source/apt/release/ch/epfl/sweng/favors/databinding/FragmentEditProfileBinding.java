package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentEditProfileBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.profileTitle, 5);
        sViewsWithIds.put(R.id.commitChanges, 6);
    }
    // views
    @NonNull
    public final android.widget.Button commitChanges;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.EditText profCityEdit;
    @NonNull
    public final android.widget.EditText profFirstNameEdit;
    @NonNull
    public final android.widget.EditText profLastNameEdit;
    @NonNull
    public final android.widget.EditText profSexEdit;
    @NonNull
    public final android.widget.TextView profileTitle;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.EditProfileFragment mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentEditProfileBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 4);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.commitChanges = (android.widget.Button) bindings[6];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.profCityEdit = (android.widget.EditText) bindings[3];
        this.profCityEdit.setTag(null);
        this.profFirstNameEdit = (android.widget.EditText) bindings[1];
        this.profFirstNameEdit.setTag(null);
        this.profLastNameEdit = (android.widget.EditText) bindings[2];
        this.profLastNameEdit.setTag(null);
        this.profSexEdit = (android.widget.EditText) bindings[4];
        this.profSexEdit.setTag(null);
        this.profileTitle = (android.widget.TextView) bindings[5];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x20L;
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
            setElements((ch.epfl.sweng.favors.EditProfileFragment) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setElements(@Nullable ch.epfl.sweng.favors.EditProfileFragment Elements) {
        this.mElements = Elements;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.elements);
        super.requestRebind();
    }
    @Nullable
    public ch.epfl.sweng.favors.EditProfileFragment getElements() {
        return mElements;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeElementsBaseCity((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeElementsFirstName((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeElementsLastName((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeElementsSex((android.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeElementsBaseCity(android.databinding.ObservableField<java.lang.String> ElementsBaseCity, int fieldId) {
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
    private boolean onChangeElementsSex(android.databinding.ObservableField<java.lang.String> ElementsSex, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
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
        android.databinding.ObservableField<java.lang.String> elementsBaseCity = null;
        java.lang.String elementsBaseCityGet = null;
        ch.epfl.sweng.favors.EditProfileFragment elements = mElements;
        java.lang.String elementsSexGet = null;
        android.databinding.ObservableField<java.lang.String> elementsFirstName = null;
        android.databinding.ObservableField<java.lang.String> elementsLastName = null;
        java.lang.String elementsFirstNameGet = null;
        android.databinding.ObservableField<java.lang.String> elementsSex = null;
        java.lang.String elementsLastNameGet = null;

        if ((dirtyFlags & 0x3fL) != 0) {


            if ((dirtyFlags & 0x31L) != 0) {

                    if (elements != null) {
                        // read elements.baseCity
                        elementsBaseCity = elements.baseCity;
                    }
                    updateRegistration(0, elementsBaseCity);


                    if (elementsBaseCity != null) {
                        // read elements.baseCity.get()
                        elementsBaseCityGet = elementsBaseCity.get();
                    }
            }
            if ((dirtyFlags & 0x32L) != 0) {

                    if (elements != null) {
                        // read elements.firstName
                        elementsFirstName = elements.firstName;
                    }
                    updateRegistration(1, elementsFirstName);


                    if (elementsFirstName != null) {
                        // read elements.firstName.get()
                        elementsFirstNameGet = elementsFirstName.get();
                    }
            }
            if ((dirtyFlags & 0x34L) != 0) {

                    if (elements != null) {
                        // read elements.lastName
                        elementsLastName = elements.lastName;
                    }
                    updateRegistration(2, elementsLastName);


                    if (elementsLastName != null) {
                        // read elements.lastName.get()
                        elementsLastNameGet = elementsLastName.get();
                    }
            }
            if ((dirtyFlags & 0x38L) != 0) {

                    if (elements != null) {
                        // read elements.sex
                        elementsSex = elements.sex;
                    }
                    updateRegistration(3, elementsSex);


                    if (elementsSex != null) {
                        // read elements.sex.get()
                        elementsSexGet = elementsSex.get();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0x31L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profCityEdit, elementsBaseCityGet);
        }
        if ((dirtyFlags & 0x32L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profFirstNameEdit, elementsFirstNameGet);
        }
        if ((dirtyFlags & 0x34L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profLastNameEdit, elementsLastNameGet);
        }
        if ((dirtyFlags & 0x38L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profSexEdit, elementsSexGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentEditProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentEditProfileBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.fragment_edit_profile, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentEditProfileBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.fragment_edit_profile, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentEditProfileBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditProfileBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_edit_profile_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentEditProfileBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements.baseCity
        flag 1 (0x2L): elements.firstName
        flag 2 (0x3L): elements.lastName
        flag 3 (0x4L): elements.sex
        flag 4 (0x5L): elements
        flag 5 (0x6L): null
    flag mapping end*/
    //end
}