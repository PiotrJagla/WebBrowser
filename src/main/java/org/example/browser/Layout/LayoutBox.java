package org.example.browser.Layout;


import org.example.browser.CSS.Values.Keyword;
import org.example.browser.CSS.Values.Length;
import org.example.browser.CSS.Values.Value;
import org.example.browser.HTML.TextNode;
import org.example.browser.Style.StyledNode;

import java.util.ArrayList;
import java.util.List;

import static org.example.browser.CSS.Values.Unit.Px;

public class LayoutBox {
    private Dimensions dimensions = new Dimensions();
    private Box box = new Box();
    private LayoutBox parentBox;
    private List<LayoutBox> children = new ArrayList<>();

    public LayoutBox() {

    }

    public LayoutBox(Box box) {
        this.box = box;
    }


    public void layout(Dimensions containingBlock) {


        switch (box.getBoxType()) {
            case AnonymousBlockNode:
            case BlockNode:
                if(isLineBox()) {
                    layoutBlock_InlineContext(containingBlock);
                }
                else{
                    layoutBlock_BlockContext(containingBlock);
                }
                break;
            case InlineNode:
            case AnonymousInlineNode:
                if(isLineBox()) {
                    layoutInline_InlineContext(containingBlock);
                }
                else {
                    layoutInline_BlockContext(containingBlock);
                }
                break;
        }
    }

    private void layoutInline_BlockContext(Dimensions containingBlock) {
        StyledNode sn = box.getStyledNode();

        //calculate width
        Value marginLeft = sn.lookup(new Length(), "margin-left", "margin");
        Value marginRight= sn.lookup(new Length(), "margin-right", "margin");

        Value paddingLeft = sn.lookup(new Length(), "padding-left", "padding");
        Value paddingRight = sn.lookup(new Length(), "padding-right", "padding");

        Value borderLeft = sn.lookup(new Length(), "border-left-width", "border-width");
        Value borderRight = sn.lookup(new Length(), "border-right-width", "border-width");

        dimensions.getPadding().setLeft(paddingLeft.toPx());
        dimensions.getPadding().setRight(paddingRight.toPx());

        dimensions.getMargin().setLeft(marginLeft.toPx());
        dimensions.getMargin().setRight(marginRight.toPx());

        dimensions.getBorder().setLeft(borderLeft.toPx());
        dimensions.getBorder().setRight(borderRight.toPx());

        float total = (float)List.of(marginLeft, marginRight, borderLeft, borderRight, paddingLeft, paddingRight).stream().mapToDouble(e -> e.toPx()).sum();
        float underflow = containingBlock.getContent().width() - total;
        dimensions.getContent().setWidth(underflow);

        //calculate position

        dimensions.getMargin().setTop(sn.lookup(new Length(), "margin-top", "margin").toPx());
        dimensions.getMargin().setBottom(sn.lookup(new Length(), "margin-bottom", "margin").toPx());

        dimensions.getPadding().setTop(sn.lookup(new Length(), "padding-top", "padding").toPx());
        dimensions.getPadding().setBottom(sn.lookup(new Length(), "padding-bottom", "padding").toPx());

        dimensions.getBorder().setTop(sn.lookup(new Length(), "border-top-width", "border-width").toPx());
        dimensions.getBorder().setBottom(sn.lookup(new Length(), "border-bottom-width", "border-width").toPx());

        dimensions.getContent().setX(
                dimensions.getMargin().getLeft() + dimensions.getPadding().getLeft() + dimensions.getBorder().getLeft()
                        + containingBlock.getContent().width() + containingBlock.getContent().x()
        );
        dimensions.getContent().setY(
                dimensions.getMargin().getTop() + dimensions.getBorder().getTop()
                    + containingBlock.getContent().y()
        );

        //layout children
        for (LayoutBox child : children) {
            child.layout(dimensions);
            dimensions.getContent().setHeight(dimensions.getContent().height() + child.getDimensions().marginBox().height());
        }

    }

    private void layoutInline_InlineContext(Dimensions containingBlock) {
        StyledNode sn = box.getStyledNode();

        //calculate width
        dimensions.setAll(sn);

        dimensions.getContent().setX(
                dimensions.getMargin().getLeft() + dimensions.getPadding().getLeft() + dimensions.getBorder().getLeft()
                        + containingBlock.getContent().width() + containingBlock.getContent().x()
        );
        dimensions.getContent().setY(
                dimensions.getMargin().getTop() + dimensions.getBorder().getTop()
                        + containingBlock.getContent().y()
        );

        //layout children
        for (LayoutBox child : children) {
            child.layout(dimensions);
            dimensions.getContent().setWidth(dimensions.getContent().width() + child.getDimensions().marginBox().width());
        }
        double minY = children.stream().mapToDouble(c -> c.getDimensions().marginBox().y()).min().getAsDouble();
        double maxY = children.stream().mapToDouble(c -> c.getDimensions().marginBox().y() + c.getDimensions().getContent().height()).max().getAsDouble();
        dimensions.getContent().setHeight((float) (maxY - minY));
    }


    private boolean isLineBox() {
        if(children.isEmpty()) {
            return false;
        }
        if(children.get(0).getBox().getBoxType().equals(BoxType.BlockNode)) {
            return false;
        }
        return !children.stream().
                anyMatch(c -> c.getBox().getBoxType().equals(BoxType.BlockNode) ||
                        c.getBox().getBoxType().equals(BoxType.AnonymousBlockNode)
                );
    }

    private void layoutBlock_InlineContext(Dimensions containingBlock) {
        StyledNode sn = box.getStyledNode();

        dimensions.setAll(sn);

        //calculate position
        dimensions.getContent().setX(
                dimensions.getMargin().getLeft() + dimensions.getBorder().getLeft() + dimensions.getPadding().getLeft()
                + containingBlock.getContent().x()
        );

        dimensions.getContent().setY(
                dimensions.getMargin().getTop() + dimensions.getBorder().getTop() + dimensions.getPadding().getTop()
                        + containingBlock.getContent().y() + containingBlock.getContent().height()
        );

        //layout children
        for (LayoutBox child : children) {
            child.layout(dimensions);
            dimensions.getContent().setWidth(dimensions.getContent().width() + child.getDimensions().marginBox().width());
        }
        double minY = children.stream().mapToDouble(c -> c.getDimensions().marginBox().y()).min().getAsDouble();
        double maxY = children.stream().mapToDouble(c -> c.getDimensions().marginBox().y() + c.getDimensions().getContent().height()).max().getAsDouble();
        dimensions.getContent().setHeight((float) (maxY - minY));

        //calculate inline width

        float total = dimensions.getBorder().getLeft() + dimensions.getBorder().getRight()
                + dimensions.getMargin().getLeft() + dimensions.getMargin().getRight()
                + dimensions.getPadding().getLeft() + dimensions.getPadding().getRight();

        dimensions.getContent().setWidth(containingBlock.getContent().width() - total);


    }

    private void layoutBlock_BlockContext(Dimensions containingBlock) {
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


        dimensions.setTop(sn);
        dimensions.setBottom(sn);

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
        if(getBox().getStyledNode().value("height") instanceof Length) {
            Length h = (Length) getBox().getStyledNode().value("height");
            if(h.getUnit() == Px) {
                dimensions.getContent().setHeight( h.getLength());
            }
        }
    }


    public LayoutBox getInlineContainer(List<StyledNode> siblings, StyledNode child) {
        if(!siblings.stream().anyMatch(n -> n.getDisplay().equals(Display.Block))) {
            if(child.getNode() instanceof TextNode) {
                LayoutBox anonInlineBox = new LayoutBox();
                anonInlineBox.setBox(new Box(BoxType.AnonymousInlineNode, child));
                anonInlineBox.setParentBox(this);
                children.add(anonInlineBox);
                return anonInlineBox;
            }
            return this;
        }
        switch (box.getBoxType()) {
            case InlineNode:
            case AnonymousInlineNode:
                return this;
            case BlockNode:
            case AnonymousBlockNode:
                if(children.isEmpty()) {
                    children.add(new LayoutBox(new Box().setBoxType(BoxType.AnonymousBlockNode)));
                }
                else {
                    LayoutBox lastChild = children.get(children.size() - 1);
                    if(lastChild.getBox().getBoxType() != BoxType.AnonymousBlockNode) {
                        children.add(new LayoutBox(new Box().setBoxType(BoxType.AnonymousBlockNode)));
                    }
                }
                children.get(children.size() - 1).setParentBox(this);
                return children.get(children.size() - 1);
            default:
                return null;
        }
    }

    public LayoutBox getParentBox() {
        return parentBox;
    }

    public LayoutBox setParentBox(LayoutBox parentBox) {
        this.parentBox = parentBox;
        return this;
    }


    public Dimensions getDimensions() {
        return dimensions;
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
    public void addChild(LayoutBox child) {
        child.setParentBox(this);
        children.add(child);
    }

}
