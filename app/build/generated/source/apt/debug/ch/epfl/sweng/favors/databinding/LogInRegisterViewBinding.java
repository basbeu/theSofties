package ch.epfl.sweng.favors.databinding;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LogInRegisterViewBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.emailTextField, 7);
        sViewsWithIds.put(R.id.passwordTextField, 8);
        sViewsWithIds.put(R.id.resendConfirmationMailButton, 9);
        sViewsWithIds.put(R.id.logOutButton, 10);
        sViewsWithIds.put(R.id.nextButton, 11);
        sViewsWithIds.put(R.id.setUserInfo, 12);
    }
    // views
    @NonNull
    public final android.widget.Button authentificationButton;
    @NonNull
    public final android.widget.ImageView checkEmailImage;
    @NonNull
    public final android.widget.ImageView checkPasswordImage;
    @NonNull
    public final android.widget.EditText emailTextField;
    @NonNull
    public final android.widget.Button logOutButton;
    @NonNull
    public final android.widget.TextView loginMessageText;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.widget.Button nextButton;
    @NonNull
    public final android.widget.EditText passwordTextField;
    @NonNull
    public final android.widget.Button resendConfirmationMailButton;
    @NonNull
    public final android.widget.Button resetPasswordButton;
    @NonNull
    public final android.widget.Button setUserInfo;
    @NonNull
    public final android.widget.TextView userFeedback;
    // variables
    @Nullable
    private ch.epfl.sweng.favors.AuthentificationProcess mElements;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public LogInRegisterViewBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 5);
        final Object[] bindings = mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds);
        this.authentificationButton = (android.widget.Button) bindings[3];
        this.authentificationButton.setTag(null);
        this.checkEmailImage = (android.widget.ImageView) bindings[4];
        this.checkEmailImage.setTag(null);
        this.checkPasswordImage = (android.widget.ImageView) bindings[5];
        this.checkPasswordImage.setTag(null);
        this.emailTextField = (android.widget.EditText) bindings[7];
        this.logOutButton = (android.widget.Button) bindings[10];
        this.loginMessageText = (android.widget.TextView) bindings[1];
        this.loginMessageText.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.nextButton = (android.widget.Button) bindings[11];
        this.passwordTextField = (android.widget.EditText) bindings[8];
        this.resendConfirmationMailButton = (android.widget.Button) bindings[9];
        this.resetPasswordButton = (android.widget.Button) bindings[6];
        this.resetPasswordButton.setTag(null);
        this.setUserInfo = (android.widget.Button) bindings[12];
        this.userFeedback = (android.widget.TextView) bindings[2];
        this.userFeedback.setTag(null);
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
            setElements((ch.epfl.sweng.favors.AuthentificationProcess) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setElements(@Nullable ch.epfl.sweng.favors.AuthentificationProcess Elements) {
        this.mElements = Elements;
        synchronized(this) {
            mDirtyFlags |= 0x20L;
        }
        notifyPropertyChanged(BR.elements);
        super.requestRebind();
    }
    @Nullable
    public ch.epfl.sweng.favors.AuthentificationProcess getElements() {
        return mElements;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeElementsHeaderText((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeElementsValidationButton((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeElementsRequirementsText((android.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeElementsIsPasswordCorrect((android.databinding.ObservableBoolean) object, fieldId);
            case 4 :
                return onChangeElementsIsEmailCorrect((android.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeElementsHeaderText(android.databinding.ObservableField<java.lang.String> ElementsHeaderText, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsValidationButton(android.databinding.ObservableField<java.lang.String> ElementsValidationButton, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsRequirementsText(android.databinding.ObservableField<java.lang.String> ElementsRequirementsText, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsIsPasswordCorrect(android.databinding.ObservableBoolean ElementsIsPasswordCorrect, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeElementsIsEmailCorrect(android.databinding.ObservableBoolean ElementsIsEmailCorrect, int fieldId) {
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
        boolean elementsIsPasswordCorrectGet = false;
        boolean elementsIsPasswordCorrectElementsIsEmailCorrectBooleanFalse = false;
        java.lang.String elementsRequirementsTextGet = null;
        android.databinding.ObservableField<java.lang.String> elementsHeaderText = null;
        boolean elementsIsEmailCorrectGet = false;
        ch.epfl.sweng.favors.AuthentificationProcess elements = mElements;
        android.databinding.ObservableField<java.lang.String> elementsValidationButton = null;
        int elementsIsEmailCorrectViewVISIBLEViewGONE = 0;
        java.lang.String elementsValidationButtonGet = null;
        android.databinding.ObservableField<java.lang.String> elementsRequirementsText = null;
        int elementsIsPasswordCorrectViewVISIBLEViewGONE = 0;
        java.lang.String elementsHeaderTextGet = null;
        android.databinding.ObservableBoolean elementsIsPasswordCorrect = null;
        android.databinding.ObservableBoolean elementsIsEmailCorrect = null;

        if ((dirtyFlags & 0x7fL) != 0) {


            if ((dirtyFlags & 0x61L) != 0) {

                    if (elements != null) {
                        // read elements.headerText
                        elementsHeaderText = elements.headerText;
                    }
                    updateRegistration(0, elementsHeaderText);


                    if (elementsHeaderText != null) {
                        // read elements.headerText.get()
                        elementsHeaderTextGet = elementsHeaderText.get();
                    }
            }
            if ((dirtyFlags & 0x62L) != 0) {

                    if (elements != null) {
                        // read elements.validationButton
                        elementsValidationButton = elements.validationButton;
                    }
                    updateRegistration(1, elementsValidationButton);


                    if (elementsValidationButton != null) {
                        // read elements.validationButton.get()
                        elementsValidationButtonGet = elementsValidationButton.get();
                    }
            }
            if ((dirtyFlags & 0x64L) != 0) {

                    if (elements != null) {
                        // read elements.requirementsText
                        elementsRequirementsText = elements.requirementsText;
                    }
                    updateRegistration(2, elementsRequirementsText);


                    if (elementsRequirementsText != null) {
                        // read elements.requirementsText.get()
                        elementsRequirementsTextGet = elementsRequirementsText.get();
                    }
            }
            if ((dirtyFlags & 0x78L) != 0) {

                    if (elements != null) {
                        // read elements.isPasswordCorrect
                        elementsIsPasswordCorrect = elements.isPasswordCorrect;
                    }
                    updateRegistration(3, elementsIsPasswordCorrect);


                    if (elementsIsPasswordCorrect != null) {
                        // read elements.isPasswordCorrect.get()
                        elementsIsPasswordCorrectGet = elementsIsPasswordCorrect.get();
                    }
                if((dirtyFlags & 0x78L) != 0) {
                    if(elementsIsPasswordCorrectGet) {
                            dirtyFlags |= 0x100L;
                    }
                    else {
                            dirtyFlags |= 0x80L;
                    }
                }
                if((dirtyFlags & 0x68L) != 0) {
                    if(elementsIsPasswordCorrectGet) {
                            dirtyFlags |= 0x1000L;
                    }
                    else {
                            dirtyFlags |= 0x800L;
                    }
                }

                if ((dirtyFlags & 0x68L) != 0) {

                        // read elements.isPasswordCorrect.get() ? View.VISIBLE : View.GONE
                        elementsIsPasswordCorrectViewVISIBLEViewGONE = ((elementsIsPasswordCorrectGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                }
            }
            if ((dirtyFlags & 0x70L) != 0) {

                    if (elements != null) {
                        // read elements.isEmailCorrect
                        elementsIsEmailCorrect = elements.isEmailCorrect;
                    }
                    updateRegistration(4, elementsIsEmailCorrect);


                    if (elementsIsEmailCorrect != null) {
                        // read elements.isEmailCorrect.get()
                        elementsIsEmailCorrectGet = elementsIsEmailCorrect.get();
                    }
                if((dirtyFlags & 0x70L) != 0) {
                    if(elementsIsEmailCorrectGet) {
                            dirtyFlags |= 0x400L;
                    }
                    else {
                            dirtyFlags |= 0x200L;
                    }
                }


                    // read elements.isEmailCorrect.get() ? View.VISIBLE : View.GONE
                    elementsIsEmailCorrectViewVISIBLEViewGONE = ((elementsIsEmailCorrectGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished

        if ((dirtyFlags & 0x100L) != 0) {

                if (elements != null) {
                    // read elements.isEmailCorrect
                    elementsIsEmailCorrect = elements.isEmailCorrect;
                }
                updateRegistration(4, elementsIsEmailCorrect);


                if (elementsIsEmailCorrect != null) {
                    // read elements.isEmailCorrect.get()
                    elementsIsEmailCorrectGet = elementsIsEmailCorrect.get();
                }
            if((dirtyFlags & 0x70L) != 0) {
                if(elementsIsEmailCorrectGet) {
                        dirtyFlags |= 0x400L;
                }
                else {
                        dirtyFlags |= 0x200L;
                }
            }
        }

        if ((dirtyFlags & 0x78L) != 0) {

                // read elements.isPasswordCorrect.get() ? elements.isEmailCorrect.get() : false
                elementsIsPasswordCorrectElementsIsEmailCorrectBooleanFalse = ((elementsIsPasswordCorrectGet) ? (elementsIsEmailCorrectGet) : (false));
        }
        // batch finished
        if ((dirtyFlags & 0x78L) != 0) {
            // api target 1

            this.authentificationButton.setEnabled(elementsIsPasswordCorrectElementsIsEmailCorrectBooleanFalse);
        }
        if ((dirtyFlags & 0x62L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.authentificationButton, elementsValidationButtonGet);
        }
        if ((dirtyFlags & 0x70L) != 0) {
            // api target 1

            this.checkEmailImage.setVisibility(elementsIsEmailCorrectViewVISIBLEViewGONE);
            this.resetPasswordButton.setEnabled(elementsIsEmailCorrectGet);
        }
        if ((dirtyFlags & 0x68L) != 0) {
            // api target 1

            this.checkPasswordImage.setVisibility(elementsIsPasswordCorrectViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x61L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.loginMessageText, elementsHeaderTextGet);
        }
        if ((dirtyFlags & 0x64L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.userFeedback, elementsRequirementsTextGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static LogInRegisterViewBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LogInRegisterViewBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<LogInRegisterViewBinding>inflate(inflater, ch.epfl.sweng.favors.R.layout.log_in_register_view, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static LogInRegisterViewBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LogInRegisterViewBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(ch.epfl.sweng.favors.R.layout.log_in_register_view, null, false), bindingComponent);
    }
    @NonNull
    public static LogInRegisterViewBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LogInRegisterViewBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/log_in_register_view_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new LogInRegisterViewBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): elements.headerText
        flag 1 (0x2L): elements.validationButton
        flag 2 (0x3L): elements.requirementsText
        flag 3 (0x4L): elements.isPasswordCorrect
        flag 4 (0x5L): elements.isEmailCorrect
        flag 5 (0x6L): elements
        flag 6 (0x7L): null
        flag 7 (0x8L): elements.isPasswordCorrect.get() ? elements.isEmailCorrect.get() : false
        flag 8 (0x9L): elements.isPasswordCorrect.get() ? elements.isEmailCorrect.get() : false
        flag 9 (0xaL): elements.isEmailCorrect.get() ? View.VISIBLE : View.GONE
        flag 10 (0xbL): elements.isEmailCorrect.get() ? View.VISIBLE : View.GONE
        flag 11 (0xcL): elements.isPasswordCorrect.get() ? View.VISIBLE : View.GONE
        flag 12 (0xdL): elements.isPasswordCorrect.get() ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}