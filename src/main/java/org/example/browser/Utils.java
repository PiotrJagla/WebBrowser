package org.example.browser;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;
import org.example.browser.Layout.Rectangle;

import java.util.Random;

public class Utils {
    public static long windowHandle;
    public static int getRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(min, max - min) + min;
    }

    public static void drawRect(Canvas canvas, Paint paint, Rectangle rect) {
        canvas.drawRect(new Rect(rect.x(), rect.y(), rect.x()+rect.width(), rect.y() + rect.height()), paint);
    }

}
