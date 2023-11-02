package com.mygdx.game.browser;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	Texture img;
	OrthographicCamera camera;
	List<DisplayCommand> list;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");
		//HTML parser
//		String HTMLinput = "<html>" +
//				"<body>" +
//				"<h1>Title</h1>" +
//				"<div id=\"main\" class=\"ltest\">" +
//				"<p>Hello <em>world</em>!</p>" +
//				"</div>" +
//				"</body>" +
//				"</html>";
		String HTMLinput = "<div><div><div><div><div class=\"a\"></div></div></div></div></div>";
		HTMLParser parser = new HTMLParser(HTMLinput);
		Node root = parser.parse();

//        CSSParser
//		String cssInput =
//				"h1 { padding: auto; }" +
//						".ltest {color:#cc0011; margin: 20px; }" +
//						"p{display:block; padding: 20px; }";

//		String cssInput ="* {padding: 10px;} .a {height: 20px;}";
		String cssInput ="* {padding: 10px;} .a {height: 20px;}";

		CSSParser cssParser = new CSSParser(
				cssInput
		);
		Stylesheet stylesheet = cssParser.parse();

		//Styling DOM tree
		Style style = new Style();
		StyledNode styledTreeRoot = style.styleTree(root,stylesheet);

		//Layout
		Layout layout = new Layout();
		Dimensions viewport = new Dimensions();
		viewport.getContent().width = 800;
		viewport.getContent().height = 600;
		LayoutBox layoutRoot = layout.layoutTree(styledTreeRoot, viewport);

		//Paint
		Paint paint = new Paint();
		list = paint.buildDisplayList(layoutRoot);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		for (int i = 0 ; i < list.size() ; ++i){
			SolidColor sc = (SolidColor)list.get(i);
			Rectangle r = sc.getRect();
			CSSColor c = sc.getColor();
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(new Color(c.getR() / 255.0f,c.getG() / 255.0f,c.getB() / 255.0f,c.getA()/255));
			shapeRenderer.rect(r.x, r.y, r.width, r.height);
//			shapeRenderer.rect(0,0,100,100);
			shapeRenderer.end();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
