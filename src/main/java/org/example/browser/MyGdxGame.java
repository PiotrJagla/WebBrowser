package org.example.browser;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.BufferedReader;
import java.io.FileReader;
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
		String CSSInput = "";
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
		viewport.getContent().width = 700;
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
