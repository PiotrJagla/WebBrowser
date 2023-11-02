package com.mygdx.game.browser;


import java.util.List;

public class Rule {
    private List<CSSSelector> selectors;
    private List<Declaration> declarations;

    public List<CSSSelector> getSelectors() {
        return selectors;
    }

    public void setSelectors(List<CSSSelector> selectors) {
        this.selectors = selectors;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<Declaration> declarations) {
        this.declarations = declarations;
    }
}