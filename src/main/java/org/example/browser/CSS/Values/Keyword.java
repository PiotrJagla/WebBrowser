package org.example.browser.CSS.Values;


public class Keyword extends Value{
    private String k;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    @Override
    public float toPx() {
        return 0.0f;
    }
}
