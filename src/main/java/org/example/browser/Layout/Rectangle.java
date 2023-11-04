package org.example.browser.Layout;


public class Rectangle{
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
