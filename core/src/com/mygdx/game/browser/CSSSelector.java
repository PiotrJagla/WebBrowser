package com.mygdx.game.browser;

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



class SimpleSelector extends CSSSelector {
    private String tagName = "";
    private String id = "";
    private List<String> classes = new ArrayList<>();


    @Override
    public Specifity specifity() {
        int a = getId().equals("") ? 1 : 0;
        int b = getClasses().size();
        int c = getTagName().equals("") ? 1 : 0;
        return new Specifity(a,b,c);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}

class Specifity{
    private int a;
    private int b;
    private int c;

    public Specifity(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
}
