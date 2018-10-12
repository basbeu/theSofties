package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentProfileLayoutBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.profileTitle, 6);
        sViewsWithIds.put(R.id.editProfile, 7);
    }
    // views
    @NonNull
    public final android.widget.TextView editProfile;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.TextView profCity;
    @NonNull
    public final android.widget.TextView profEmail;
    @NonNull
    public final android.widget.TextView profFirstName;
    @NonNull
    public final android.widget.TextView profLastName;
    @NonNull
    public final android.widget.TextView profSex;
    @NonNull
    public final android.widget.TextView profileTitle;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.ProfileFragment mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentProfileLayoutBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 5);
        final Object[] bindings = mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds);
        this.editProfile = (android.widget.TextView) bindings[7];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.profCity = (android.widget.TextView) bindings[4];
        this.profCity.setTag(null);
        this.profEmail = (android.widget.TextView) bindings[3];
        this.profEmail.setTag(null);
        this.profFirstName = (android.widget.TextView) bindings[1];
        this.profFirstName.setTag(null);
        this.profLastName = (android.widget.TextView) bindings[2];
        this.profLastName.setTag(null);
        this.profSex = (android.widget.TextView) bindings[5];
        this.profSex.setTag(null);
        this.profileTitle = (android.widget.TextView) bindings[6];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x40L;
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
            setElements((ch.epfl.sweng.favors.ProfileFragment) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setElements(@Nullable ch.epfl.sweng.favors.ProfileFragment Elements) {
        this.mElements = Elements;
        synchronized(this) {
            mDirtyFlags |= 0x20L;
        }
        notifyPropertyChanged(BR.elements);
        super.requestRebind();
    }
    @Nullable
    public ch.epfl.sweng.favors.ProfileFragment getElements() {
        return mElements;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeElementsBaseCity((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeElementsEmail((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeElementsFirstName((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeElementsLastName((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 4 :
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
    private boolean onChangeElementsEmail(android.databinding.ObservableField<java.lang.String> ElementsEmail, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsFirstName(android.databinding.ObservableField<java.lang.String> ElementsFirstName, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsLastName(android.databinding.ObservableField<java.lang.String> ElementsLastName, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsSex(android.databinding.ObservableField<java.lang.String> ElementsSex, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
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
        android.databinding.ObservableField<java.lang.String> elementsEmail = null;
        ch.epfl.sweng.favors.ProfileFragment elements = mElements;
        java.lang.String elementsSexGet = null;
        java.lang.String elementsEmailGet = null;
        android.databinding.ObservableField<java.lang.String> elementsFirstName = null;
        android.databinding.ObservableField<java.lang.String> elementsLastName = null;
        java.lang.String elementsFirstNameGet = null;
        android.databinding.ObservableField<java.lang.String> elementsSex = null;
        java.lang.String elementsLastNameGet = null;

        if ((dirtyFlags & 0x7fL) != 0) {


            if ((dirtyFlags & 0x61L) != 0) {

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
            if ((dirtyFlags & 0x62L) != 0) {

                    if (elements != null) {
                        // read elements.email
                        elementsEmail = elements.email;
                    }
                    updateRegistration(1, elementsEmail);


                    if (elementsEmail != null) {
                        // read elements.email.get()
                        elementsEmailGet = elementsEmail.get();
                    }
            }
            if ((dirtyFlags & 0x64L) != 0) {

                    if (elements != null) {
                        // read elements.firstName
                        elementsFirstName = elements.firstName;
                    }
                    updateRegistration(2, elementsFirstName);


                    if (elementsFirstName != null) {
                        // read elements.firstName.get()
                        elementsFirstNameGet = elementsFirstName.get();
                    }
            }
            if ((dirtyFlags & 0x68L) != 0) {

                    if (elements != null) {
                        // read elements.lastName
                        elementsLastName = elements.lastName;
                    }
                    updateRegistration(3, elementsLastName);


                    if (elementsLastName != null) {
                        // read elements.lastName.get()
                        elementsLastNameGet = elementsLastName.get();
                    }
            }
            if ((dirtyFlags & 0x70L) != 0) {

                    if (elements != null) {
                        // read elements.sex
                        elementsSex = elements.sex;
                    }
                    updateRegistration(4, elementsSex);


                    if (elementsSex != null) {
                        // read elements.sex.get()
                        elementsSexGet = elementsSex.get();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0x61L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profCity, elementsBaseCityGet);
        }
        if ((dirtyFlags & 0x62L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profEmail, elementsEmailGet);
        }
        if ((dirtyFlags & 0x64L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profFirstName, elementsFirstNameGet);
        }
        if ((dirtyFlags & 0x68L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profLastName, elementsLastNameGet);
        }
        if ((dirtyFlags & 0x70L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.profSex, elementsSexGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentProfileLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentProfileLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentProfileLayoutBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.fragment_profile_layout, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentProfileLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentProfileLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.fragment_profile_layout, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentProfileLayoutBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentProfileLayoutBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_profile_layout_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentProfileLayoutBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements.baseCity
        flag 1 (0x2L): elements.email
        flag 2 (0x3L): elements.firstName
        flag 3 (0x4L): elements.lastName
        flag 4 (0x5L): elements.sex
        flag 5 (0x6L): elements
        flag 6 (0x7L): null
    flag mapping end*/
    //end
}