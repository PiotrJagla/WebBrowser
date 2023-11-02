package com.mygdx.game.browser;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Node {
}
class ElementNode extends Node{

    private String tagName = "";

    private Map<String,String> attrMap = new HashMap<>();
    private List<Node> children ;

    public ElementNode(String tagName, Map<String, String> attrMap, List<Node> children) {
        this.tagName = tagName;
        this.attrMap = attrMap;
        this.children = children;
    }

    public String id() {
        return attrMap.getOrDefault("id", "");
    }

    public HashSet<String> classes() {
        return new HashSet<>(Arrays.stream(attrMap.getOrDefault("class", "").split(" ")).collect(Collectors.toSet()));
    }


    public void setNodes(List<Node> children) {
        this.children = children;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public void addAttr(String key, String value) {
        attrMap.put(key, value);
    }

    public String getAttr(String key) {
        return attrMap.get(key);
    }

    public Map<String,String> getAttrMap() {
        return attrMap;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}

class TextNode extends Node{
    private String text;

    public TextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
