package org.example.browser.Painting;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Color4f;
import io.github.humbleui.skija.Paint;
import org.example.graphicslibrary.Text;

public class SolidText extends DisplayCommand{
    private Text text;

    public Text getText() {
        return text;
    }

    public SolidText setText(Text text) {
        this.text = text;
        return this;
    }

    @Override
    public void display(Canvas canvas, Paint rawPaint) {
        rawPaint.setColor4f(new Color4f(0,0,0,255));
        text.draw(canvas,rawPaint);
    }
}
