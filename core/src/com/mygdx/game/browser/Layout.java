package com.mygdx.game.browser;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.browser.Unit.Px;

public class Layout {
    public LayoutBox buildLayoutTree(StyledNode styledNode) {
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

        Length zero = new Length();
        zero.setUnit(Px);
        zero.setLength(0.0f);

        Value marginLeft = lookup(sn, "margin-left", "margin", "", zero);
        Value marginRight = lookup(sn, "margin-right", "margin", "", zero);

        Value borderLeft = lookup(sn, "border-left-width", "border-width", "", zero);
        Value borderRight = lookup(sn, "border-right-width", "border-width", "", zero);

        Value paddingLeft = lookup(sn, "padding-left", "padding", "", zero);
        Value paddingRight = lookup(sn, "padding-right", "padding", "", zero);

        float total = toPx(marginLeft) + toPx(marginRight) + toPx(borderLeft) + toPx(borderRight) + toPx(paddingLeft) + toPx(paddingRight);


        if(!(widthValue instanceof Keyword) && total > containingBlock.getContent().width) {
            if(marginLeft instanceof Keyword) {
                marginLeft = zero;
            }
            if(marginRight instanceof Keyword) {
                marginRight = zero;
            }
        }

        float underflow = containingBlock.getContent().width - total;
        boolean isWidthAuto = widthValue instanceof Keyword;
        boolean isMarginLeftAuto = marginLeft instanceof Keyword;
        boolean isMarginRightAuto = marginRight instanceof Keyword;
        if(!isWidthAuto && !isMarginLeftAuto && !isMarginRightAuto) {
            Length l = new Length();
            l.setLength(toPx(marginRight) + underflow);
            l.setUnit(Px);
            marginRight = l;
        }
        else if(!isWidthAuto && !isMarginLeftAuto && isMarginRightAuto) {
            Length l = new Length();
            l.setLength(underflow);
            l.setUnit(Px);
            marginRight = l;
        }
        else if(!isWidthAuto && isMarginLeftAuto && !isMarginRightAuto) {
            Length l = new Length();
            l.setLength(underflow);
            l.setUnit(Px);
            marginLeft = l;
        }
        else if(isWidthAuto) {
            if(marginLeft instanceof Keyword) {
                Length l = new Length();
                l.setLength(0.0f);
                l.setUnit(Px);
                marginLeft = l;
            }
            if(marginRight instanceof Keyword) {
                Length l = new Length();
                l.setLength(0.0f);
                l.setUnit(Px);
                marginRight = l;
            }

            if(underflow >= 0.0f) {
                Length l = new Length();
                l.setLength(underflow);
                l.setUnit(Px);
                widthValue = l;
            } else {
                Length l = new Length();
                l.setLength(0.0f);
                l.setUnit(Px);
                widthValue = l;

                l = new Length();
                l.setLength(toPx(marginRight) + underflow);
                l.setUnit(Px);
                marginRight = l;
            }

        }
        else if(!isWidthAuto && isMarginLeftAuto && isMarginRightAuto) {
            Length l = new Length();
            l.setLength(underflow / 2.0f);
            l.setUnit(Px);
            marginLeft = l;

            l = new Length();
            l.setLength(underflow / 2.0f);
            l.setUnit(Px);
            marginRight = l;

        }

    }

    private void calculateBlockPosition(Dimensions containingBlock) {
        StyledNode sn = getBoxType().getStyledNode();
        Dimensions d = getDimensions();

        Length zero = new Length();
        zero.setUnit(Px);
        zero.setLength(0.0f);

        d.getMargin().setTop(toPx(lookup(sn, "margin-top", "margin", "", zero)));
        d.getMargin().setBottom(toPx(lookup(sn, "margin-bottom", "margin", "", zero)));

        d.getBorder().setTop(toPx(lookup(sn, "border-top-width", "border-width", "", zero)));
        d.getBorder().setBottom(toPx(lookup(sn, "border-bottom-width", "border-width", "", zero)));

        d.getPadding().setTop(toPx(lookup(sn, "padding-top", "padding", "", zero)));
        d.getPadding().setBottom(toPx(lookup(sn, "padding-bottom", "padding", "", zero)));

        d.getContent().x = containingBlock.getContent().x + d.getMargin().getLeft() + d.getBorder().getLeft() + d.getPadding().getLeft();
        d.getContent().y = containingBlock.getContent().height + containingBlock.getContent().y + d.getMargin().getTop() + d.getBorder().getTop() + d.getPadding().getTop();

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


    private void layoutBlockChildren() {
        Dimensions d = getDimensions();
        for (LayoutBox child : getChildren()) {
            child.layout(d);
            d.getContent().height = d.getContent().height + child.getDimensions().marginBox().height;
        }

    }

    private void calculateBlockHeight() {
        if(getBoxType().getStyledNode().value("height") instanceof Length) {
            Length h = (Length) getBoxType().getStyledNode().value("height");
            if(h.getUnit() == Px) {
                dimensions.getContent().height = h.getLength();
            }
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

enum BoxTypeName{
    BlockNode,
    InlineNode,
    AnonymusBlock,
}

class Dimensions{
    private Rectangle content = new Rectangle();
    private EdgeSizes padding = new EdgeSizes();
    private EdgeSizes border = new EdgeSizes();
    private EdgeSizes margin = new EdgeSizes();

    public Rectangle marginBox() {
        return expandBy(margin);
    }
    public Rectangle paddingBox() {
        return expandBy(padding);
    }
    public Rectangle borderBox() {
        return expandBy(border);
    }

    private Rectangle expandBy(EdgeSizes edge) {
        Rectangle res = new Rectangle();
        res.x = content.x + edge.getLeft();
        res.y = content.y + edge.getTop();
        res.width = content.width + edge.getLeft() + edge.getRight();
        res.height = content.height + edge.getTop() + edge.getBottom();
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
