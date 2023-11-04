package org.example.browser.Layout;


import org.example.browser.Style.StyledNode;

public class BoxType{
    private BoxTypeName boxTypeName;
    private StyledNode styledNode;

    public BoxTypeName getBoxTypeName() {
        return boxTypeName;
    }

    public void setBoxTypeName(BoxTypeName boxTypeName) {
        this.boxTypeName = boxTypeName;
    }

    public StyledNode getStyledNode() {
        return styledNode;
    }

    public void setStyledNode(StyledNode styledNode) {
        this.styledNode = styledNode;
    }
}
