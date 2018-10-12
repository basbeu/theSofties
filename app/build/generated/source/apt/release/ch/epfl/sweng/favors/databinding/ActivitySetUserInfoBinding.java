package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivitySetUserInfoBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.submit, 5);
    }
    // views
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.widget.Button submit;
    @NonNull
    public final android.widget.EditText userCityEdit;
    @NonNull
    public final android.widget.EditText userFirstNameEdit;
    @NonNull
    public final android.widget.EditText userLastNameEdit;
    @NonNull
    public final android.widget.EditText userSexEdit;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.SetUserInfo mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivitySetUserInfoBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 4);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.submit = (android.widget.Button) bindings[5];
        this.userCityEdit = (android.widget.EditText) bindings[3];
        this.userCityEdit.setTag(null);
        this.userFirstNameEdit = (android.widget.EditText) bindings[1];
        this.userFirstNameEdit.setTag(null);
        this.userLastNameEdit = (android.widget.EditText) bindings[2];
        this.userLastNameEdit.setTag(null);
        this.userSexEdit = (android.widget.EditText) bindings[4];
        this.userSexEdit.setTag(null);
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
            setElements((ch.epfl.sweng.favors.SetUserInfo) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setElements(@Nullable ch.epfl.sweng.favors.SetUserInfo Elements) {
        this.mElements = Elements;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.elements);
        super.requestRebind();
    }
    @Nullable
    public ch.epfl.sweng.favors.SetUserInfo getElements() {
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
                return onChangeElementsSexe((android.databinding.ObservableField<java.lang.String>) object, fieldId);
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
    private boolean onChangeElementsSexe(android.databinding.ObservableField<java.lang.String> ElementsSexe, int fieldId) {
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
        java.lang.String elementsSexeGet = null;
        android.databinding.ObservableField<java.lang.String> elementsBaseCity = null;
        java.lang.String elementsBaseCityGet = null;
        ch.epfl.sweng.favors.SetUserInfo elements = mElements;
        android.databinding.ObservableField<java.lang.String> elementsFirstName = null;
        android.databinding.ObservableField<java.lang.String> elementsLastName = null;
        android.databinding.ObservableField<java.lang.String> elementsSexe = null;
        java.lang.String elementsFirstNameGet = null;
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
                        // read elements.sexe
                        elementsSexe = elements.sexe;
                    }
                    updateRegistration(3, elementsSexe);


                    if (elementsSexe != null) {
                        // read elements.sexe.get()
                        elementsSexeGet = elementsSexe.get();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0x31L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.userCityEdit, elementsBaseCityGet);
        }
        if ((dirtyFlags & 0x32L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.userFirstNameEdit, elementsFirstNameGet);
        }
        if ((dirtyFlags & 0x34L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.userLastNameEdit, elementsLastNameGet);
        }
        if ((dirtyFlags & 0x38L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.userSexEdit, elementsSexeGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivitySetUserInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivitySetUserInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivitySetUserInfoBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.activity_set_user_info, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivitySetUserInfoBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivitySetUserInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.activity_set_user_info, null, false), bindingComponent);
    }
    @NonNull
    public static ActivitySetUserInfoBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivitySetUserInfoBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_set_user_info_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivitySetUserInfoBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements.baseCity
        flag 1 (0x2L): elements.firstName
        flag 2 (0x3L): elements.lastName
        flag 3 (0x4L): elements.sexe
        flag 4 (0x5L): elements
        flag 5 (0x6L): null
    flag mapping end*/
    //end
}