package org.example.browser;
import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.*;
import org.example.browser.Dimensions;
import org.example.browser.LayoutBox;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

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
        Node root = htmlParser.parse();

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
        StyledNode styledTreeRoot = style.styleTree(root,stylesheet);

        //Layout
        Layout layout = new Layout();
        Dimensions viewport = new Dimensions();
        viewport.getContent().setWidth( 700);
        viewport.getContent().setHeight( 600);
        LayoutBox layoutRoot = layout.layoutTree(styledTreeRoot, viewport);

        //Paint
        RenderingPaint renderingPaint = new RenderingPaint();
        List<DisplayCommand> list = renderingPaint.buildDisplayList(layoutRoot);

        Canvas canvas = surface.getCanvas();
        Paint rawPaint = new Paint().setColor(0xFFFFFFFF);

// Render loop
        while (!glfwWindowShouldClose(windowHandle)) {

            // DRAW HERE!!!
            canvas.clear(0x00000000);
            for (DisplayCommand d : list) {
                SolidColor sc = (SolidColor) d;
                Rectangle r = sc.getRect();

                CSSColor c = sc.getColor();
                Color4f c4f = new Color4f(c.getR()/255.0f, c.getG()/255.0f, c.getB()/255.0f, c.getA()/255.0f);
                rawPaint.setColor4f(c4f);
                canvas.drawRect(new Rect(r.x(),r.y(),r.x()+r.width(),r.y()+r.height()), rawPaint);

            }
            surface.draw(canvas, 0,0,rawPaint);


            context.flush();
            glfwSwapBuffers(windowHandle); // wait for v-sync
            glfwPollEvents();
        }
    }
}







