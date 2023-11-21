package org.example.browser.CSS.Values;


import org.example.browser.Utils;

public class CSSColor extends Value{
    private int r = 0;
    private int g = 0;
    private int b = 0;
    private int a = 0;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Override
    public float toPx() {
        return 0.0f;
    }
}
