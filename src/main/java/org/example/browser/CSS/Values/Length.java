package org.example.browser.CSS.Values;


public class Length extends Value{
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

    @Override
    public float toPx() {
        return length;
    }
}
