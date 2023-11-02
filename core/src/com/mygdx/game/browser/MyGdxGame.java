package com.mygdx.game.browser;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
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
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
