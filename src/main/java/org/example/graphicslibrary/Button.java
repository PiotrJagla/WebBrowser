package org.example.graphicslibrary;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Color4f;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;
import org.example.browser.Layout.Rectangle;
import org.example.browser.Utils;

public class Button {

    private Rectangle bounds;
    private Runnable onClick;

    public Button(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void draw(Canvas canvas, Paint rawPaint) {
        Utils.drawRect(canvas, rawPaint, bounds);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void isPressed() {

    }

}
