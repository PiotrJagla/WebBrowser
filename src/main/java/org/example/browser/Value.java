package org.example.browser;

public abstract class Value {
}

class Keyword extends Value{
    private String k;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }
}

class Length extends Value{
    private float length;
    private Unit unit;

    public Length() {
        length = 0.0f;
        unit = Unit.Px;

    }
    public Length(float length, Unit unit) {
        this.length = length;
        this.unit = unit;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}


enum Unit{
    Px
}

class CSSColor extends Value{
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