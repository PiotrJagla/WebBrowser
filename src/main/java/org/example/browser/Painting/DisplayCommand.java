package org.example.browser.Painting;


import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;

public abstract class DisplayCommand {

    public abstract void display(Canvas canvas, Paint rawPaint);

}
