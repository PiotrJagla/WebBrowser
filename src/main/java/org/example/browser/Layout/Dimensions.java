package org.example.browser.Layout;


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
