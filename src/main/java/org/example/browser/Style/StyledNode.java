package org.example.browser.Style;


import org.example.browser.CSS.Values.Keyword;
import org.example.browser.HTML.Node;
import org.example.browser.CSS.Values.Value;
import org.example.browser.Layout.Display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StyledNode {
    private Node node;
    private HashMap<String,Value> specifiedValues = new HashMap<>();
    private List<StyledNode> children = new ArrayList<>();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public HashMap<String,Value> getSpecifiedValues() {
        return specifiedValues;
    }

    public void setSpecifiedValues(HashMap<String,Value> specifiedValues) {
        this.specifiedValues = specifiedValues;
    }

    public List<StyledNode> getChildren() {
        return children;
    }

    public void setChildren(List<StyledNode> children) {
        this.children = children;
    }

    public Value value(String str) {
        return specifiedValues.getOrDefault(str,null);
    }

    public Value lookup(Value defaultValue, String... lookups) {
        Value result;
        for (int i = 0; i < lookups.length; i++) {
            result = specifiedValues.getOrDefault(lookups[i],null);
            if(result != null) {
                return result;
            }

        }
        return defaultValue;
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
