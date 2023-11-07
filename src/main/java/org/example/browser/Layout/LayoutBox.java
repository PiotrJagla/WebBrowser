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
    private Box box = new Box();
    private List<LayoutBox> children = new ArrayList<>();

    public void layout(Dimensions containingBlock) {

        traverse()

        switch (box.getBoxType()) {
            case BlockNode:
                layoutBlock(containingBlock);
                break;
            case InlineNode:
                layoutInline(containingBlock);
                //TODO: implement inline display
                break;
            case AnonymusBlock:
                break;
        }
    }

    private void layoutInline(Dimensions containingBlock) {
        System.out.println("laying out inline");
    }

    private void layoutBlock(Dimensions containingBlock) {
        calculateBlockWidth(containingBlock);
        calculateBlockPosition(containingBlock);
        layoutBlockChildren();
        calculateBlockHeight();
    }

    private void calculateBlockWidth(Dimensions containingBlock) {
        StyledNode sn = box.getStyledNode();

        Keyword auto = new Keyword();
        auto.setK("auto");
        Value widthValue = sn.value("width");
        if(widthValue == null) {
            widthValue = auto;
        }

        Value marginLeft = sn.lookup(new Length(), "margin-left", "margin");
        Value marginRight = sn.lookup(new Length(), "margin-right", "margin");

        Value borderLeft = sn.lookup(new Length(), "border-left-width", "border-width");
        Value borderRight = sn.lookup(new Length(), "border-right-width", "border-width");

        Value paddingLeft = sn.lookup(new Length(), "padding-left", "padding");
        Value paddingRight = sn.lookup(new Length(), "padding-right", "padding");

        float total = (float)List.of(marginLeft, marginRight, borderLeft, borderRight, paddingLeft, paddingRight).stream().mapToDouble(e -> e.toPx()).sum();


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
            marginRight = new Length(marginRight.toPx() + underflow, Px);
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
                marginRight = new Length(marginRight.toPx() + underflow, Px);
            }

        }
        else if(!isWidthAuto && isMarginLeftAuto && isMarginRightAuto) {
            marginLeft = new Length(underflow/2.0f, Px);
            marginRight = new Length(underflow/2.0f, Px);
        }

        dimensions.getContent().setWidth( widthValue.toPx());

        dimensions.getPadding().setLeft(paddingLeft.toPx());
        dimensions.getPadding().setRight(paddingRight.toPx());

        dimensions.getBorder().setLeft(borderLeft.toPx());
        dimensions.getBorder().setRight(borderRight.toPx());

        dimensions.getMargin().setLeft(marginLeft.toPx());
        dimensions.getMargin().setRight(marginRight.toPx());

    }

    private void calculateBlockPosition(Dimensions containingBlock) {
        StyledNode sn = getBox().getStyledNode();


        dimensions.getMargin().setTop(sn.lookup(new Length(),"margin-top", "margin").toPx());
        dimensions.getMargin().setBottom(sn.lookup(new Length(), "margin-bottom", "margin").toPx());

        dimensions.getBorder().setTop(sn.lookup(new Length(), "border-top-width", "border-width").toPx());
        dimensions.getBorder().setBottom(sn.lookup(new Length(), "border-bottom-width", "border-width").toPx());

        dimensions.getPadding().setTop(sn.lookup(new Length(), "padding-top", "padding").toPx());
        dimensions.getPadding().setBottom(sn.lookup(new Length(), "padding-bottom", "padding").toPx());

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
            if(child.getBox().getBoxType() == BoxType.InlineNode) {
                int x = 0;
            }
            child.layout(dimensions);
            dimensions.getContent().setHeight( dimensions.getContent().height() +  child.getDimensions().marginBox().height());
        }

    }

    private void calculateBlockHeight() {
        if(getBox().getStyledNode().value("height") instanceof Length) {
            Length h = (Length) getBox().getStyledNode().value("height");
            if(h.getUnit() == Px) {
                dimensions.getContent().setHeight( h.getLength());
            }
        }


    }




    public LayoutBox getInlineContainer() {
        switch (box.getBoxType()) {
            case InlineNode:
            case AnonymusBlock:
                return this;
            case BlockNode:
                if(children.isEmpty()) {
                    LayoutBox newBox = new LayoutBox();
                    newBox.setBox(new Box(BoxType.AnonymusBlock, new StyledNode()));
                    children.add(newBox);
                }
                else {
                    LayoutBox lastChild = children.get(children.size() - 1);
                    if(lastChild.getBox().getBoxType() != BoxType.AnonymusBlock) {
                        LayoutBox newBox = new LayoutBox();
                        newBox.setBox(new Box(BoxType.AnonymusBlock, new StyledNode()));
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

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public List<LayoutBox> getChildren() {
        return children;
    }

    public void setChildren(List<LayoutBox> children) {
        this.children = children;
    }
}
