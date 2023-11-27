package org.example.graphicslibrary;

import io.github.humbleui.skija.*;
import io.github.humbleui.types.Rect;
import org.example.browser.Layout.Rectangle;
import org.example.browser.Utils;

public class Text {
    private StringBuilder text;
    private float x;
    private float y;
    private Font font;

    private short[] glyphs;
    private float[] glyphsWidths;
    private float[] glyphsXPositions;
    private Rectangle textBounds;

    private float properTextShift;

    private Color4f textColor;

    public Text(String text, float x, float y, Font f) {
        this.text = new StringBuilder(text);
        this.font = f;
        this.x = x;
        this.y = y;
        this.textColor = new Color4f(255,255,255,255);
        setupText(text);
    }

    private void setupText(String text) {
        if(text.equals("")){
            textBounds = new Rectangle(x,y,0,0);
            return;
        }

        glyphs = font.getStringGlyphs(text);
        glyphsWidths = font.getWidths(glyphs);
        glyphsXPositions = new float[glyphsWidths.length];
        float distance = 0;
        for (int i = 0; i < glyphsWidths.length; i++) {
            if (i > 0) {
                distance += 1; //TODO: add proper letter spacing
            }
            glyphsXPositions[i] = distance;
            distance += glyphsWidths[i];
        }
        Rect r = calculateBounds();
        properTextShift = -r.getTop();
        textBounds = new Rectangle(x, y, distance, r.getBottom() - r.getTop());
    }
    public void setText(String newText) {
        text = new StringBuilder(newText);
        setupText(newText);
    }

    public void setTextColor(Color4f textColor) {
        this.textColor = textColor;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
        setupText(text.toString());
    }

    private Rect calculateBounds() {
        Rect[] rects = font.getBounds(glyphs);
        float minTop = Float.MAX_VALUE;
        float maxBottom = Float.MIN_VALUE;
        for (int i = 0; i < rects.length; i++) {
            Rect r = rects[i];
            if(minTop > r.getTop()) {
                minTop = r.getTop();
            }
            if(maxBottom < r.getBottom()) {
                maxBottom = r.getBottom();
            }
        }
        return new Rect(x, minTop, x + glyphsXPositions[glyphsXPositions.length-1], maxBottom);
    }

    public String getText() {
        return text.toString();
    }

    public Rectangle getBounds() {
        return textBounds;
    }

    public void renderText(Canvas canvas, Paint rawPaint){
        Color4f prevColor = rawPaint.getColor4f();
        rawPaint.setColor4f(textColor);
        TextBlob tb = TextBlob.makeFromPosH(glyphs, glyphsXPositions, 0, font);
        canvas.drawTextBlob(tb, textBounds.x(),textBounds.y() + properTextShift, rawPaint);
        rawPaint.setColor4f(prevColor);
    }

}
