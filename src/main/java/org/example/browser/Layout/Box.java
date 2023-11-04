package org.example.browser.Layout;


import org.example.browser.Style.StyledNode;

public class Box {
    private BoxType boxType;
    private StyledNode styledNode;

    public Box(BoxType boxTypeName, StyledNode styledNode) {
        this.boxType = boxTypeName;
        this.styledNode = styledNode;
    }

    public Box() {

    }

    public BoxType getBoxType() {
        return boxType;
    }

    public void setBoxType(BoxType boxTypeName) {
        this.boxType = boxTypeName;
    }

    public StyledNode getStyledNode() {
        return styledNode;
    }

    public void setStyledNode(StyledNode styledNode) {
        this.styledNode = styledNode;
    }
}
