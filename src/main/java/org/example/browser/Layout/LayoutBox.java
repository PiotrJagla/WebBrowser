package org.example.browser.Layout;


import org.example.browser.CSS.Values.Keyword;
import org.example.browser.CSS.Values.Length;
import org.example.browser.CSS.Values.Value;
import org.example.browser.Style.StyledNode;

import java.util.ArrayList;
import java.util.List;

import static org.example.browser.CSS.Values.Unit.Px;

public class LayoutBox {
    private Dimensions dimensions = new Dimensions();
    private BoxType boxType = new BoxType();
    private List<LayoutBox> children = new ArrayList<>();

    public void layout(Dimensions containingBlock) {
        switch (boxType.getBoxTypeName()) {
            case BlockNode:
                layoutBlock(containingBlock);
                break;
            case InlineNode:
                System.out.println("There is inline node");
                //TODO: implement inline display
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
