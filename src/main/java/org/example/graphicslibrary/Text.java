package org.example.graphicslibrary;

import io.github.humbleui.skija.*;
import io.github.humbleui.types.Rect;
import org.example.browser.Layout.Rectangle;

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

    public Text(String text, float x, float y, Font f) {
        this.text = new StringBuilder(text);
        this.font = f;
        this.x = x;
        this.y = y;
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
        TextBlob tb = TextBlob.makeFromPosH(glyphs, glyphsXPositions, 0, font);
        canvas.drawTextBlob(tb, textBounds.x(),textBounds.y() + properTextShift, rawPaint);
    }

}
