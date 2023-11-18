package org.example.browser.Painting;


import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Color4f;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.Rect;
import org.example.browser.CSS.Values.CSSColor;
import org.example.browser.Layout.Rectangle;

public class SolidColor extends DisplayCommand {
    private CSSColor color = new CSSColor();
    private Rectangle rect = new Rectangle();

    public CSSColor getColor() {
        return color;
    }

    public void setColor(CSSColor color) {
        this.color = color;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    @Override
    public void display(Canvas canvas, Paint rawPaint) {
        Color4f c4f = new Color4f(color.getR()/255.0f, color.getG()/255.0f, color.getB()/255.0f, color.getA()/255.0f);
        rawPaint.setColor4f(c4f);
        canvas.drawRect(new Rect(rect.x(),rect.y(),rect.x()+rect.width(),rect.y()+rect.height()), rawPaint);
    }
}
