package org.example.browser.CSS;

import java.util.ArrayList;
import java.util.List;

public abstract class CSSSelector {

    private SimpleSelector simpleSelector;

    public CSSSelector(SimpleSelector simpleSelector) {
        this.simpleSelector = simpleSelector;
    }

    public CSSSelector(){
    }

    public abstract Specifity specifity();
}




