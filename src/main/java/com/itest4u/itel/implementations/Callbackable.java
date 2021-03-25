package com.itest4u.itel.implementations;

import com.itest4u.itel.interfaces.ICallback4Callbackable;
import com.itest4u.itel.interfaces.ICallbackable;

public class Callbackable implements ICallbackable {
    private ICallback4Callbackable callbackable = null;


    @Override
    public void setCallback(ICallback4Callbackable callback) {
        callbackable=callback;
    }

    @Override
    public ICallback4Callbackable getCallback() {
        return callbackable;
    }
}
