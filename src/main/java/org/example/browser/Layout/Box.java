package org.example.browser.Layout;


import org.example.browser.Style.StyledNode;

public class Box {
    private BoxType boxType;
    private StyledNode styledNode = new StyledNode();

    public Box(BoxType boxTypeName, StyledNode styledNode) {
        this.boxType = boxTypeName;
        this.styledNode = styledNode;
    }

    public Box() {

    }

    public BoxType getBoxType() {
        return boxType;
    }

    public Box setBoxType(BoxType boxType) {
        this.boxType = boxType;
        return this;
    }

    public StyledNode getStyledNode() {
        return styledNode;
    }

    public Box setStyledNode(StyledNode styledNode) {
        this.styledNode = styledNode;
        return this;
    }
}
