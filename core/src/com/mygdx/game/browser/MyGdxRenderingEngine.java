package com.mygdx.game.browser;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.browser.*;

import java.util.List;

import static com.mygdx.game.browser.BrowserMain.printTree;

public class MyGdxRenderingEngine extends ApplicationAdapter {

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        // load the images for the droplet and the bucket, 64x64 pixels each
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }

    @Override
    public void render() {

        camera.update();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();

        //HTML parser
        HTMLParser parser = new HTMLParser("<html>" +
                "<body>" +
                "<h1>Title</h1>" +
                "<div id=\"main\" class=\"ltest\">" +
                "<p>Hello <em>world</em>!</p>" +
                "</div>" +
                "</body>" +
                "</html>");
        HTMLParser parser1 = new HTMLParser("<div name=\"this is name\" class=\"firstClass\" > Hello </div>");
        Node root = parser.parse();
//        printTree(root);

//        CSSParser
        CSSParser cssParser = new CSSParser(
                "h1 { padding: auto; }" +
                        ".ltest {color:#cc0011; margin: 20px; }" +
                        "p{display:block; padding: 20px; }"
        );
        Stylesheet stylesheet = cssParser.parse();

        //Styling DOM tree
        Style style = new Style();
        StyledNode styledTreeRoot = style.styleTree(root,stylesheet);
        System.out.println("Styling done");

        //Layout
        Layout layout = new Layout();
        LayoutBox layoutRoot = layout.buildLayoutTree(styledTreeRoot);
        System.out.println("Layout done");
        Paint paint = new Paint();
        List<DisplayCommand> list = paint.buildDisplayList(layoutRoot);
        System.out.print("Display commands done");

        for (DisplayCommand dc : list) {
            SolidColor sc = (SolidColor) dc;
            Rectangle r = sc.getRect();
            Color c = sc.getColor();

            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }

        shapeRenderer.rect(0,0,100,100);
        shapeRenderer.end();

    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        shapeRenderer.dispose();
    }

}
