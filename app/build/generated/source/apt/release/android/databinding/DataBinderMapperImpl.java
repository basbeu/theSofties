
package android.databinding;
import ch.epfl.sweng.favors.BR;
class DataBinderMapperImpl extends android.databinding.DataBinderMapper {
    public DataBinderMapperImpl() {
    }
    @Override
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case ch.epfl.sweng.favors.R.layout.activity_main:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/activity_main_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.ActivityMainBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
                }
                case ch.epfl.sweng.favors.R.layout.fragment_edit_profile:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/fragment_edit_profile_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.FragmentEditProfileBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for fragment_edit_profile is invalid. Received: " + tag);
                }
                case ch.epfl.sweng.favors.R.layout.nav_header:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/nav_header_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.NavHeaderBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for nav_header is invalid. Received: " + tag);
                }
                case ch.epfl.sweng.favors.R.layout.favors_layout:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/favors_layout_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.FavorsLayoutBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for favors_layout is invalid. Received: " + tag);
                }
                case ch.epfl.sweng.favors.R.layout.log_in_register_view:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/log_in_register_view_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.LogInRegisterViewBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for log_in_register_view is invalid. Received: " + tag);
                }
                case ch.epfl.sweng.favors.R.layout.fragment_profile_layout:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/fragment_profile_layout_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.FragmentProfileLayoutBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for fragment_profile_layout is invalid. Received: " + tag);
                }
                case ch.epfl.sweng.favors.R.layout.activity_set_user_info:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/activity_set_user_info_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.ActivitySetUserInfoBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for activity_set_user_info is invalid. Received: " + tag);
                }
                case ch.epfl.sweng.favors.R.layout.activity_logged_in__screen:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/activity_logged_in__screen_0".equals(tag)) {
                            return new ch.epfl.sweng.favors.databinding.ActivityLoggedInScreenBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for activity_logged_in__screen is invalid. Received: " + tag);
                }
        }
        return null;
    }
    @Override
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    @Override
    public int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case 423753077: {
                if(tag.equals("layout/activity_main_0")) {
                    return ch.epfl.sweng.favors.R.layout.activity_main;
                }
                break;
            }
            case -1206964977: {
                if(tag.equals("layout/fragment_edit_profile_0")) {
                    return ch.epfl.sweng.favors.R.layout.fragment_edit_profile;
                }
                break;
            }
            case 2001258847: {
                if(tag.equals("layout/nav_header_0")) {
                    return ch.epfl.sweng.favors.R.layout.nav_header;
                }
                break;
            }
            case 1750078080: {
                if(tag.equals("layout/favors_layout_0")) {
                    return ch.epfl.sweng.favors.R.layout.favors_layout;
                }
                break;
            }
            case -1992869768: {
                if(tag.equals("layout/log_in_register_view_0")) {
                    return ch.epfl.sweng.favors.R.layout.log_in_register_view;
                }
                break;
            }
            case -126025317: {
                if(tag.equals("layout/fragment_profile_layout_0")) {
                    return ch.epfl.sweng.favors.R.layout.fragment_profile_layout;
                }
                break;
            }
            case -192458357: {
                if(tag.equals("layout/activity_set_user_info_0")) {
                    return ch.epfl.sweng.favors.R.layout.activity_set_user_info;
                }
                break;
            }
            case -1883206892: {
                if(tag.equals("layout/activity_logged_in__screen_0")) {
                    return ch.epfl.sweng.favors.R.layout.activity_logged_in__screen;
                }
                break;
            }
        }
        return 0;
    }
    @Override
    public String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"
            ,"elements"};
    }
}