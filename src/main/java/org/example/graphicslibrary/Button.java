package org.example.graphicslibrary;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Color4f;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;
import org.example.browser.Layout.Rectangle;
import org.example.browser.Utils;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.example.browser.Utils.windowHandle;
import static org.lwjgl.glfw.GLFW.*;

public class Button {

    private Rectangle bounds;
    private Runnable onClick;
    private boolean lock;

    public Button(Rectangle bounds) {
        lock = true;
        this.bounds = bounds;
    }

    public void draw(Canvas canvas, Paint rawPaint) {
        Utils.drawRect(canvas, rawPaint, bounds);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void isPressed() {
        DoubleBuffer xBuf= BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuf= BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowHandle, xBuf, yBuf);
        Point mousePos = new Point((float)xBuf.get(0),(float)yBuf.get(0));
        int state = glfwGetMouseButton(windowHandle, GLFW_MOUSE_BUTTON_LEFT);

        if(lock && state == GLFW_PRESS && bounds.contains(mousePos)) {
            onClick.run();
            lock = false;
        }
        if(state == GLFW_RELEASE) {
            lock = true;
        }

    }

}
