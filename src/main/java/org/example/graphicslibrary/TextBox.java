package org.example.graphicslibrary;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Color4f;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Paint;
import org.example.browser.Layout.Rectangle;
import org.example.browser.Utils;

import static org.example.browser.Utils.windowHandle;
import static org.lwjgl.glfw.GLFW.*;


public class TextBox {
    private Rectangle bounds;
    Text text;

    public TextBox(Rectangle bounds) {
        this.bounds = bounds;
        Font f = new Font();
        f.setSize(18);
        text = new Text("base", bounds.x(), bounds.y(), f);
        text.setTextColor(new Color4f(4,65,140, 255));
    }

    public void update() {
        int key= glfwGetMouseButton(windowHandle, GLFW_MOUSE_BUTTON_LEFT);

    }

    public void draw(Canvas canvas, Paint paint) {
        Utils.drawRect(canvas,paint,bounds);
        text.draw(canvas, paint);
    }
}
