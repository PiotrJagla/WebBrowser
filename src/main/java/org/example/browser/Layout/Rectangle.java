package org.example.browser.Layout;


import io.github.humbleui.types.Point;
import org.w3c.dom.css.Rect;

public class Rectangle{
    private float x;
    private float y;
    private float width;
    private float height;

    public Rectangle() {

    }

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

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

    public boolean contains(Point p) {
        return p.getX() >= x && p.getX() <= x + width &&
                p.getY() >= y && p.getY() <= y + height;
    }
}
