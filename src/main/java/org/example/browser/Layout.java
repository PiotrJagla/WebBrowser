package org.example.browser;


import java.util.ArrayList;
import java.util.List;

import static org.example.browser.Unit.Px;


public class Layout {

    public LayoutBox layoutTree(StyledNode root, Dimensions containingBlock) {
        containingBlock.getContent().setHeight(  0.0f);
        LayoutBox layoutRoot = buildLayoutTree(root);
        layoutRoot.layout(containingBlock);
        return layoutRoot;
    }
    private LayoutBox buildLayoutTree(StyledNode styledNode) {
        LayoutBox root = new LayoutBox();
        BoxType boxType = new BoxType();
        switch(styledNode.getDisplay()) {
            case Block:
                boxType.setBoxTypeName(BoxTypeName.BlockNode);
                boxType.setStyledNode(styledNode);
                root.setBoxType(boxType);
                break;
            case Inline:
                boxType.setBoxTypeName(BoxTypeName.InlineNode);
                boxType.setStyledNode(styledNode);
                root.setBoxType(boxType);
                break;
        }

        for (StyledNode child : styledNode.getChildren()) {
            switch(child.getDisplay()) {
                case Block:
                    root.getChildren().add(buildLayoutTree(child));
                    break;
                case Inline:
                    root.getInlineContainer().getChildren().add(buildLayoutTree(child));
                    break;
                case None:
                    break;
            }
        }

        return root;
    }

}

class LayoutBox {
    private Dimensions dimensions = new Dimensions();
    private BoxType boxType = new BoxType();
    private List<LayoutBox> children = new ArrayList<>();

    public void layout(Dimensions containingBlock) {
        switch (boxType.getBoxTypeName()) {
            case BlockNode:
                layoutBlock(containingBlock);
                break;
            case InlineNode:
                break;
            case AnonymusBlock:
                break;
        }
    }

    private void layoutBlock(Dimensions containingBlock) {
        calculateBlockWidth(containingBlock);
        calculateBlockPosition(containingBlock);
        layoutBlockChildren();
        calculateBlockHeight();
    }

    private void calculateBlockWidth(Dimensions containingBlock) {
        StyledNode sn = boxType.getStyledNode();

        Keyword auto = new Keyword();
        auto.setK("auto");
        Value widthValue = sn.value("width");
        if(widthValue == null) {
            widthValue = auto;
        }

        Value marginLeft = lookup(sn, "margin-left", "margin", "", new Length());
        Value marginRight = lookup(sn, "margin-right", "margin", "", new Length());

        Value borderLeft = lookup(sn, "border-left-width", "border-width", "", new Length());
        Value borderRight = lookup(sn, "border-right-width", "border-width", "", new Length());

        Value paddingLeft = lookup(sn, "padding-left", "padding", "", new Length());
        Value paddingRight = lookup(sn, "padding-right", "padding", "", new Length());

        float total = toPx(marginLeft) + toPx(marginRight) + toPx(borderLeft) + toPx(borderRight) + toPx(paddingLeft) + toPx(paddingRight);


        if(!(widthValue instanceof Keyword) && total > containingBlock.getContent().width()) {
            if(marginLeft instanceof Keyword) {
                marginLeft = new Length();
            }
            if(marginRight instanceof Keyword) {
                marginRight = new Length();
            }
        }

        float underflow = containingBlock.getContent().width() - total;
        boolean isWidthAuto = widthValue instanceof Keyword;
        boolean isMarginLeftAuto = marginLeft instanceof Keyword;
        boolean isMarginRightAuto = marginRight instanceof Keyword;
        if(!isWidthAuto && !isMarginLeftAuto && !isMarginRightAuto) {
            marginRight = new Length(toPx(marginRight) + underflow, Px);
        }
        else if(!isWidthAuto && !isMarginLeftAuto && isMarginRightAuto) {
            marginRight = new Length(underflow, Px);
        }
        else if(!isWidthAuto && isMarginLeftAuto && !isMarginRightAuto) {
            marginLeft = new Length(underflow, Px);
        }
        else if(isWidthAuto) {
            if(marginLeft instanceof Keyword) {
                marginLeft = new Length();
            }
            if(marginRight instanceof Keyword) {
                marginRight = new Length();
            }

            if(underflow >= 0.0f) {
                widthValue = new Length(underflow, Px);
            } else {
                widthValue = new Length();
                marginRight = new Length(toPx(marginRight) + underflow, Px);
            }

        }
        else if(!isWidthAuto && isMarginLeftAuto && isMarginRightAuto) {
            marginLeft = new Length(underflow/2.0f, Px);
            marginRight = new Length(underflow/2.0f, Px);
        }

        dimensions.getContent().setWidth( toPx(widthValue));

        dimensions.getPadding().setLeft(toPx(paddingLeft));
        dimensions.getPadding().setRight(toPx(paddingRight));

        dimensions.getBorder().setLeft(toPx(borderLeft));
        dimensions.getBorder().setRight(toPx(borderRight));

        dimensions.getMargin().setLeft(toPx(marginLeft));
        dimensions.getMargin().setRight(toPx(marginRight));

    }

    private void calculateBlockPosition(Dimensions containingBlock) {
        StyledNode sn = getBoxType().getStyledNode();


        dimensions.getMargin().setTop(toPx(lookup(sn, "margin-top", "margin", "", new Length())));
        dimensions.getMargin().setBottom(toPx(lookup(sn, "margin-bottom", "margin", "", new Length())));

        dimensions.getBorder().setTop(toPx(lookup(sn, "border-top-width", "border-width", "", new Length())));
        dimensions.getBorder().setBottom(toPx(lookup(sn, "border-bottom-width", "border-width", "", new Length())));

        dimensions.getPadding().setTop(toPx(lookup(sn, "padding-top", "padding", "", new Length())));
        dimensions.getPadding().setBottom(toPx(lookup(sn, "padding-bottom", "padding", "", new Length())));

        dimensions.getContent().setX( containingBlock.getContent().x() +
                dimensions.getMargin().getLeft() +
                dimensions.getBorder().getLeft() + dimensions.getPadding().getLeft());
        dimensions.getContent().setY( containingBlock.getContent().height() + containingBlock.getContent().y() +
                dimensions.getMargin().getTop() +
                dimensions.getBorder().getTop() +
                dimensions.getPadding().getTop());

    }

    private void layoutBlockChildren() {
        for (LayoutBox child : getChildren()) {
            child.layout(dimensions);
            dimensions.getContent().setHeight( dimensions.getContent().height() +  child.getDimensions().marginBox().height());
        }

    }

    private void calculateBlockHeight() {
        if(getBoxType().getStyledNode().value("height") instanceof Length) {
            Length h = (Length) getBoxType().getStyledNode().value("height");
            if(h.getUnit() == Px) {
                dimensions.getContent().setHeight( h.getLength());
            }
        }


    }

    private Value lookup(StyledNode sn, String lookup1, String lookup2, String lookup3, Value defaultValue) {
        Value res = sn.value(lookup1);
        if(res == null) {
            res = sn.value(lookup2);
            if(res == null) {
                res = sn.value(lookup3);
                if(res == null) {
                    res = defaultValue;
                }
            }
        }
        return res;
    }

    private float toPx(Value v) {
        if(v instanceof Length) {
            return ((Length) v).getLength();
        }
        else {
            return 0.0f;
        }
    }



    public LayoutBox getInlineContainer() {
        switch (boxType.getBoxTypeName()) {
            case InlineNode:
            case AnonymusBlock:
                return this;
            case BlockNode:
                if(children.isEmpty()) {
                    BoxType bt = new BoxType();
                    bt.setBoxTypeName(BoxTypeName.AnonymusBlock);
                    LayoutBox newBox = new LayoutBox();
                    newBox.setBoxType(bt);
                    children.add(newBox);
                }
                else {
                    LayoutBox lastChild = children.get(children.size() - 1);
                    if(lastChild.getBoxType().getBoxTypeName() != BoxTypeName.AnonymusBlock) {
                        BoxType bt = new BoxType();
                        bt.setBoxTypeName(BoxTypeName.AnonymusBlock);
                        LayoutBox newBox = new LayoutBox();
                        newBox.setBoxType(bt);
                        children.add(newBox);
                    }
                }
                return children.get(children.size() - 1);
            default:
                return null;
        }
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public void setBoxType(BoxType boxType) {
        this.boxType = boxType;
    }

    public List<LayoutBox> getChildren() {
        return children;
    }

    public void setChildren(List<LayoutBox> children) {
        this.children = children;
    }
}

enum Display{
    Inline,
    Block,
    None
}

class BoxType{
    private BoxTypeName boxTypeName =BoxTypeName.BlockNode;
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

enum BoxTypeName{
    BlockNode,
    InlineNode,
    AnonymusBlock,
}


class Rectangle{
    private float x;
    private float y;
    private float width;
    private float height;

    public float x() {
        return x;
    }

    public Rectangle setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public Rectangle setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public Rectangle setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public Rectangle setHeight(float height) {
        this.height = height;
        return this;
    }
}


class Dimensions{
    private Rectangle content = new Rectangle();
    private EdgeSizes padding = new EdgeSizes();
    private EdgeSizes border = new EdgeSizes();
    private EdgeSizes margin = new EdgeSizes();


    public Rectangle paddingBox() {
        return expandRectBy(content, padding);
    }
    public Rectangle borderBox() {
        return expandRectBy(paddingBox(), border);
    }
    public Rectangle marginBox() {
        return expandRectBy(borderBox(), margin);
    }

    private Rectangle expandRectBy(Rectangle rect, EdgeSizes edge) {
        Rectangle res = new Rectangle();
        res.setX( rect.x() + edge.getLeft());
        res.setY( rect.y() + edge.getTop());
        res.setWidth( rect.width() + edge.getLeft() + edge.getRight());
        res.setHeight( rect.height() + edge.getTop() + edge.getBottom());
        return res;
    }

    public Rectangle getContent() {
        return content;
    }

    public void setContent(Rectangle content) {
        this.content = content;
    }

    public EdgeSizes getPadding() {
        return padding;
    }

    public void setPadding(EdgeSizes padding) {
        this.padding = padding;
    }

    public EdgeSizes getBorder() {
        return border;
    }

    public void setBorder(EdgeSizes border) {
        this.border = border;
    }

    public EdgeSizes getMargin() {
        return margin;
    }

    public void setMargin(EdgeSizes margin) {
        this.margin = margin;
    }
}

class EdgeSizes {
    private float left = 0.0f;
    private float right= 0.0f;
    private float top= 0.0f;
    private float bottom= 0.0f;

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
