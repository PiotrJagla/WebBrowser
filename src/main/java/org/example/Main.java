package org.example;
import io.github.humbleui.skija.*;
import io.github.humbleui.types.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Main {
    public static void main(String[] args) {
        var width = 640;
        var height = 480;


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
        Canvas canvas = surface.getCanvas();
        Paint rawPaint = new Paint().setColor(0xFFFFFFFF);

// Render loop
        while (!glfwWindowShouldClose(windowHandle)) {

            // DRAW HERE!!!
            canvas.clear(0x00000000);
            canvas.drawRect(new Rect(10,10,100,100), rawPaint);
            surface.draw(canvas, 0,0,rawPaint);


            context.flush();
            glfwSwapBuffers(windowHandle); // wait for v-sync
            glfwPollEvents();
        }
    }
}







