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

    public Text(String text, float x, float y, Font f) {
        //TODO: calculating bounds opitimization note: we have to only calculate minTop
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

        this.y += calculateMaxHeight();

        glyphs = font.getStringGlyphs(text);
        glyphsWidths = font.getWidths(glyphs);
        glyphsXPositions = new float[glyphsWidths.length];
        distance = 0;
        for (int i = 0; i < glyphsWidths.length; i++) {
            if (i > 0) {
                distance += 1; //TODO: add proper letter spacing
            }
            glyphsXPositions[i] = distance;
            distance += glyphsWidths[i];
        }
        textBounds = calculateBounds();
    }

    private float calculateMaxHeight() {
        Rect[] rects = font.getBounds(glyphs); //format is: x:0, y:0, width:something, height: something
        float minTop = Float.MAX_VALUE;
        for (int i = 0; i < rects.length; i++) {
            Rect r = rects[i];
            float currentTop = r.getTop();
            if(minTop > currentTop) {
                minTop = currentTop;
            }
        }
        return -1*minTop;
    }

    private Rectangle calculateBounds(){
        Rect[] rects = font.getBounds(glyphs);
        float minLeft = Float.MAX_VALUE;
        float maxRight = Float.MIN_VALUE;
        float minTop = Float.MAX_VALUE;
        float maxBottom = Float.MIN_VALUE;
        for (int i = 0; i < rects.length; i++) {
            Rect r = rects[i];
            float currentLeft = x + glyphsXPositions[i] + r.getLeft();
            float currentTop = y+r.getTop();
            float currentRight = x+r.getRight()+glyphsXPositions[i];
            float currentBottom = y+r.getBottom();
            if(minLeft > currentLeft) {
                minLeft = currentLeft;
            }
            if(maxRight < currentRight) {
                maxRight = currentRight;
            }
            if(minTop > currentTop) {
                minTop = currentTop;
            }
            if(maxBottom < currentBottom) {
                maxBottom = currentBottom;
            }
        }
        return new Rectangle(minLeft, minTop, maxRight - minLeft ,maxBottom - minTop);
    }

    public Rectangle getBounds() {
        return textBounds;
    }

    public void renderText(Canvas canvas, Paint rawPaint){
        TextBlob tb = TextBlob.makeFromPosH(glyphs, glyphsXPositions, 0, font);
        canvas.drawTextBlob(tb, x,y, rawPaint);
    }

}
