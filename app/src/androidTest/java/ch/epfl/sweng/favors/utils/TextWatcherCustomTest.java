package ch.epfl.sweng.favors.utils;

import org.junit.Test;

public class TextWatcherCustomTest {

    class TWC extends TextWatcherCustom {}

    @Test
    public void allMethodsExist(){
        TWC twc = new TWC();
        twc.beforeTextChanged(null, 0, 0, 0);
        twc.onTextChanged(null, 0, 0, 0);
        twc.afterTextChanged(null);
    }
}