package org.example.browser.Painting;


import org.example.browser.CSS.Values.CSSColor;
import org.example.browser.Layout.Rectangle;

public class SolidColor extends DisplayCommand {
    private CSSColor color = new CSSColor();
    private Rectangle rect = new Rectangle();

    public CSSColor getColor() {
        return color;
    }

    public void setColor(CSSColor color) {
        this.color = color;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
}
