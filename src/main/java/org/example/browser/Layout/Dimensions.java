package org.example.browser.Layout;


import org.example.browser.CSS.Values.Length;
import org.example.browser.Style.StyledNode;

public class Dimensions{
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
        res.setX( rect.x() - edge.getLeft());
        res.setY( rect.y() - edge.getTop());
        res.setWidth( rect.width() + edge.getLeft() + edge.getRight());
        res.setHeight( rect.height() + edge.getTop() + edge.getBottom());
        return res;
    }

    public Rectangle getContent() {
        return content;
    }

    public void setAll(StyledNode sn) {
        setRight(sn);
        setLeft(sn);
        setTop(sn);
        setBottom(sn);
    }

    public void setTop(StyledNode sn) {
        margin.setTop(sn.lookup(new Length(),"margin-top", "margin").toPx());
        border.setTop(sn.lookup(new Length(), "border-top-width", "border-width").toPx());
        padding.setTop(sn.lookup(new Length(), "padding-top", "padding").toPx());
    }

    public void setBottom(StyledNode sn) {
        margin.setBottom(sn.lookup(new Length(), "margin-bottom", "margin").toPx());
        border.setBottom(sn.lookup(new Length(), "border-bottom-width", "border-width").toPx());
        padding.setBottom(sn.lookup(new Length(), "padding-bottom", "padding").toPx());
    }

    public void setLeft(StyledNode sn) {
        margin.setLeft(sn.lookup(new Length(), "margin-left", "margin").toPx());
        border.setLeft(sn.lookup(new Length(), "border-left-width", "border-width").toPx());
        padding.setLeft(sn.lookup(new Length(), "padding-left", "padding").toPx());
    }

    public void setRight(StyledNode sn) {
        margin.setRight(sn.lookup(new Length(), "margin-right", "margin").toPx());
        border.setRight(sn.lookup(new Length(), "border-right-width", "border-width").toPx());
        padding.setRight(sn.lookup(new Length(), "padding-right", "padding").toPx());
    }



    public EdgeSizes getPadding() {
        return padding;
    }

    public EdgeSizes getBorder() {
        return border;
    }

    public EdgeSizes getMargin() {
        return margin;
    }
}
