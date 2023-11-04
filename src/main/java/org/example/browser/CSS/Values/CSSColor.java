package org.example.browser.CSS.Values;


import org.example.browser.Utils;

public class CSSColor extends Value{
    private int r = Utils.getRandomInt(0,255);
    private int g = Utils.getRandomInt(0,255);
    private int b = Utils.getRandomInt(0,255);
    private int a = 255;

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
}
