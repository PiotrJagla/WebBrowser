package org.example.browser;
import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.*;
import org.example.browser.CSS.CSSParser;
import org.example.browser.CSS.Stylesheet;
import org.example.browser.CSS.Values.CSSColor;
import org.example.browser.HTML.HTMLParser;
import org.example.browser.HTML.Node;
import org.example.browser.Layout.Dimensions;
import org.example.browser.Layout.Layout;
import org.example.browser.Layout.LayoutBox;
import org.example.browser.Layout.Rectangle;
import org.example.browser.Painting.DisplayCommand;
import org.example.browser.Painting.RenderingPaint;
import org.example.browser.Painting.SolidColor;
import org.example.browser.Style.Style;
import org.example.browser.Style.StyledNode;
import org.example.graphicslibrary.Text;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.windows.MOUSEINPUT;


import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;



public class Main {

    public static void main(String[] args) {

        var width = 800;
        var height = 600;


// Create window
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        long windowHandle = glfwCreateWindow(width, height, "Skija LWJGL Demo", NULL, NULL);
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(windowHandle);

// Initialize OpenGL
// Do once per app launch
        GL.createCapabilities();

// Create Skia OpenGL context
// Do once per app launch
        DirectContext context = DirectContext.makeGL();

// Create render target, surface and retrieve canvas from it
// .close() and recreate on window resize
        int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
        BackendRenderTarget renderTarget = BackendRenderTarget.makeGL(
                width,
                height,
                /*samples*/ 0,
                /*stencil*/ 8,
                fbId,
                FramebufferFormat.GR_GL_RGBA8);

// .close() and recreate on window resize
        Surface surface = Surface.makeFromBackendRenderTarget(
                context,
                renderTarget,
                SurfaceOrigin.BOTTOM_LEFT,
                SurfaceColorFormat.RGBA_8888,
                ColorSpace.getSRGB());

// do not .close() — Surface manages its lifetime here

        Integer[] sdfs = new Integer[]{};
        List<Integer> sdfsdf = Arrays.stream(sdfs).collect(Collectors.toList());

        Integer[] ccccc = sdfsdf.toArray(Integer[]::new);



        //HTML htmlParser
        String HTMLInput = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("index.html"));
            String line;
            while((line = br.readLine()) != null) {
                HTMLInput += line;
            }
        } catch (Exception e) {
        }
        HTMLParser htmlParser = new HTMLParser(HTMLInput);
        Node DOMRoot = htmlParser.parse();

//        CSSParser
        String CSSInput = htmlParser.getStyleContent();
        try {
            BufferedReader br = new BufferedReader(new FileReader("style.css"));
            String line;
            while((line = br.readLine()) != null) {
                CSSInput += line;
            }
        } catch (Exception e) {
        }
        CSSParser cssParser = new CSSParser(CSSInput);
        Stylesheet stylesheet = cssParser.parse();

        //Styling DOM tree
        Style style = new Style();
        StyledNode styledTreeRoot = style.styleTree(DOMRoot,stylesheet);

        //Layout
        Layout layout = new Layout();
        Dimensions viewport = new Dimensions();
        viewport.getContent().setWidth( 700);
        viewport.getContent().setHeight( 500);
        viewport.getContent().setX(40);
        viewport.getContent().setY(40);
        LayoutBox layoutRoot = layout.layoutTree(styledTreeRoot, viewport);

        //Paint
        RenderingPaint renderingPaint = new RenderingPaint();
        List<DisplayCommand> list = renderingPaint.buildDisplayList(layoutRoot);

        Canvas canvas = surface.getCanvas();
        Paint rawPaint = new Paint().setColor(0xFFFFFFFF);

// Render loop
        while (!glfwWindowShouldClose(windowHandle)) {



            // DRAW HERE!!!

            DoubleBuffer xBuf= BufferUtils.createDoubleBuffer(2);
            DoubleBuffer yBuf= BufferUtils.createDoubleBuffer(2);
            glfwGetCursorPos(windowHandle, xBuf, yBuf);
            double x = xBuf.get(0);
            double y = yBuf.get(0);

//            System.out.println("Mouse X pos: " + x);
//            System.out.println("Mouse Y pos: " + y);
            int state = glfwGetMouseButton(windowHandle, GLFW_MOUSE_BUTTON_RIGHT);
            if(state == GLFW_PRESS) {
                System.out.println("pressed");
            }

            canvas.clear(0x00000000);
            rawPaint.setColor4f(new Color4f(255, 255, 255, 155));
            canvas.drawRect(new Rect(40,40, 40+700, 40+500), rawPaint);
            for (DisplayCommand d : list) {
                SolidColor sc = (SolidColor) d;
                Rectangle r = sc.getRect();

                CSSColor c = sc.getColor();
                Color4f c4f = new Color4f(c.getR()/255.0f, c.getG()/255.0f, c.getB()/255.0f, c.getA()/255.0f);
                rawPaint.setColor4f(c4f);
                canvas.drawRect(new Rect(r.x(),r.y(),r.x()+r.width(),r.y()+r.height()), rawPaint);

                if(x <= r.x() + r.width() && x >= r.x() && y >= r.y() && y <= r.y() + r.height()) {
                    System.out.println("Collision");
                }


            }
            rawPaint.setColor4f(new Color4f(0,255,255,255));
            canvas.drawRect(new Rect(0,300, 40, 340), rawPaint);
            canvas.drawRect(new Rect(0,300, 40, 340), rawPaint);
            surface.draw(canvas, 0,0,rawPaint);

            Text t = new Text("apjgq", 0,0, new Font());

            Rectangle r = t.getBounds();
            rawPaint.setColor4f(new Color4f(255,255,255,255));
            canvas.drawRect(new Rect(r.x(), r.y(), r.x() + r.width(), r.y() + r.height()),rawPaint);

            rawPaint.setColor4f(new Color4f(0,255,255,255));
            t.renderText(canvas,rawPaint);

            t = new Text("aaaaAA", 0,20, new Font());

            r = t.getBounds();
            rawPaint.setColor4f(new Color4f(255,255,255,255));
            canvas.drawRect(new Rect(r.x(), r.y(), r.x() + r.width(), r.y() + r.height()),rawPaint);

            rawPaint.setColor4f(new Color4f(0,255,255,255));
            t.renderText(canvas,rawPaint);




            context.flush();
            glfwSwapBuffers(windowHandle); // wait for v-sync
            glfwPollEvents();
        }
    }

    private static void renderText(Font font, Canvas canvas, Paint rawPaint, float x, float y, String text) {
        short[] glyphs = font.getStringGlyphs(text);
        float[] glyphsWidths = font.getWidths(glyphs);
        float[] glyphPositions = new float[glyphsWidths.length];
        float distance = 0;
        for (int i = 0; i < glyphsWidths.length; i++) {
            if (i > 0) {
                distance += 1;
            }
            glyphPositions[i] = distance;
            distance += glyphsWidths[i];
        }

        Rectangle r = getTextBounds(x,y, text, font);
        canvas.drawRect(new Rect(r.x(), r.y(), r.x()+r.width(), r.y()+r.height()), rawPaint);

        TextBlob tb = TextBlob.makeFromPosH(glyphs, glyphPositions, 0, font);
        rawPaint.setColor4f(new Color4f(0,0,0,255));
        canvas.drawTextBlob(tb, x,y, rawPaint);

    }

    private static Rectangle getTextBounds(float x, float y, String text, Font f) {
        short[] glyphs = f.getStringGlyphs(text);
        float[] glyphsWidths = f.getWidths(glyphs);
        float[] glyphPositions = new float[glyphsWidths.length];
        float distance = 0;
        for (int i = 0; i < glyphsWidths.length; i++) {
            if (i > 0) {
                distance += 1;
            }
            glyphPositions[i] = distance;
            distance += glyphsWidths[i];
        }
        Rect[] rects = f.getBounds(glyphs);
        float minLeft = Float.MAX_VALUE;
        float maxRight = Float.MIN_VALUE;
        float minTop = Float.MAX_VALUE;
        float maxBottom = Float.MIN_VALUE;
        for (int i = 0; i < rects.length; i++) {
            Rect r = rects[i];
            float currentLeft = x + glyphPositions[i] + r.getLeft();
            float currentTop = y+r.getTop();
            float currentRight = x+r.getRight()+glyphPositions[i];
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


}







