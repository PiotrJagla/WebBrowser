package org.example.browser.CSS;


import java.util.ArrayList;
import java.util.List;

public class SimpleSelector extends CSSSelector {
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

}
