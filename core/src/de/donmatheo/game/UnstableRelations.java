package de.donmatheo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class UnstableRelations extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Array<Dot> dots;
    private Color color;
    private Vector3 touchPos;
    private OrthographicCamera camera;

    @Override
	public void create () {
        // setup camera and shapeRenderer
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
		shapeRenderer = new ShapeRenderer();

        // create list of dots
        dots = new Array<Dot>();
        int numberOfDots = 5;
        for (int i=0; i<numberOfDots; i++) {
            dots.add(new Dot(120 + i*120,camera.viewportHeight/2));
        }

        // add random relations
        Random random = new Random();
        for(int i=0;i<dots.size; i++) {
            int number1 = i;
            int number2 = i;
            while(number1==i) {
                number1 = random.nextInt(dots.size);
            }

            while(number2==i || number2==number1) {
                number2 = random.nextInt(dots.size);
            }
            dots.get(i).setRelation1(dots.get(number1));
            dots.get(i).setRelation2(dots.get(number2));
        }

	}

	@Override
	public void render () {

        // drag touched dot
        if(Gdx.input.isTouched()) {
            touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            for(Dot dot: dots) {
                    if (dot.contains(touchPos.x, touchPos.y)) {
                        dot.x = touchPos.x;
                        dot.y = touchPos.y;
                    }
            }
        }
        camera.update();

        // render all dots
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(Dot dot: dots) {
            color = new Color(0,162,232,255);
            if(Gdx.input.isTouched()) {
                    if (dot.contains(touchPos.x, touchPos.y)) {
                        color = new Color(157, 162, 0, 255);

                        shapeRenderer.rectLine(dot.x, dot.y, dot.getRelation1().x, dot.getRelation1().y, 4);
                        shapeRenderer.rectLine(dot.x, dot.y, dot.getRelation2().x, dot.getRelation2().y, 4);
                    }
            }
            shapeRenderer.setColor(color);
            shapeRenderer.circle(dot.x, dot.y, dot.radius);
        }
        shapeRenderer.end();
	}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
