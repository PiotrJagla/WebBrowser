package org.example.browser.Style;


import org.example.browser.CSS.Values.Keyword;
import org.example.browser.HTML.Node;
import org.example.browser.CSS.Values.Value;
import org.example.browser.Layout.Display;

import java.util.ArrayList;
import java.util.List;

public class StyledNode {
    private Node node;
    private PropertyMap specifiedValues = new PropertyMap();
    private List<StyledNode> children = new ArrayList<>();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public PropertyMap getSpecifiedValues() {
        return specifiedValues;
    }

    public void setSpecifiedValues(PropertyMap specifiedValues) {
        this.specifiedValues = specifiedValues;
    }

    public List<StyledNode> getChildren() {
        return children;
    }

    public void setChildren(List<StyledNode> children) {
        this.children = children;
    }

    public Value value(String str) {
        return specifiedValues.getMap().getOrDefault(str,null);
    }

    public Display getDisplay() {
        if(!(value("display") instanceof Keyword)) {
            return Display.Block;
        }
        Keyword k = (Keyword) value("display");
        if(k.getK().equals("inline")) {
            return Display.Inline;
        } else if (k.getK().equals("none")) {
            return Display.None;
        }
        return Display.Block;
    }
}
