package de.donmatheo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Random;

public class UnstableRelations extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Vector3 touchPos;
    private OrthographicCamera camera;

    private Array<Dot> dots = new Array<Dot>();
    private Color yellow = new Color(157, 162, 0, 255);
    private Color blue = new Color(0, 162, 232, 255);

    private int selectedDot = -1;
    private float offsetX;
    private float offsetY;
    private float zoom = 1.0f;

    @Override
    public void create() {

        // setup camera and shapeRenderer
        float screen_width = Gdx.graphics.getWidth();
        float screen_height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screen_width, screen_height);
        touchPos = new Vector3();
        shapeRenderer = new ShapeRenderer();
        Random random = new Random();

        // create list of dots
        int numberOfDots = 5;
        for (int i = 0; i < numberOfDots; i++) {
            dots.add(new Dot());
        }
        // add random relations
        for (int i = 0; i < dots.size; i++) {
            int number1 = i;
            int number2 = i;
            while (number1 == i) {
                number1 = random.nextInt(dots.size);
            }
            while (number2 == i || number2 == number1) {
                number2 = random.nextInt(dots.size);
            }
            dots.get(i).setRelation1(dots.get(number1));
            dots.get(i).setRelation2(dots.get(number2));
        }

        // random layout of dots
        for (Dot dot : dots) {
            dot.x = dot.radius + random.nextInt((int) (camera.viewportWidth - 2 * dot.radius));
            dot.y = dot.radius + random.nextInt((int) (camera.viewportHeight - 2 * dot.radius));
        }
    }

    @Override
    public void render() {

        // release dragged dot
        if (!Gdx.input.isTouched()) {
            selectedDot = -1;
        }

        // drag touched dot
        if (Gdx.input.isTouched()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            for (int i = 0; i < dots.size; i++) {
                Dot dot = dots.get(i);
                if (dot.contains(touchPos.x, touchPos.y) && selectedDot == -1) {
                    selectedDot = i;
                    offsetX = touchPos.x - dot.x;
                    offsetY = touchPos.y - dot.y;
                }
                if (selectedDot == i) {
                    dot.x = touchPos.x - offsetX;
                    dot.y = touchPos.y - offsetY;
                }
            }
        }

        // begin autozoom
        // default zoom is positive (=zoom in)
        int zoomstate = 1;
        // calculate scaled radius (dots get smaller when zoomed out)
        float scaledDotRadius = Dot.DEFAULTRADIUS / zoom;

        // check if any dot leaves inner boundary
        // define inner boundary (rectangle with 2 * scaled dotradius distance to border) (y is pointing down!)
        Vector3 innerLowerLeft = camera.unproject(new Vector3(scaledDotRadius * 2,camera.viewportHeight - scaledDotRadius * 2, 0));
        Vector3 innerUpperRight = camera.unproject(new Vector3(camera.viewportWidth -scaledDotRadius * 2, scaledDotRadius * 2,0));

        for(Dot dot: dots) {
            float[] distances = new float[4];
            distances[0] = dot.x - innerLowerLeft.x;
            distances[1] = innerUpperRight.x - dot.x;
            distances[2] = dot.y - innerLowerLeft.y;
            distances[3] = innerUpperRight.y - dot.y;
            Arrays.sort(distances);
            // zoom out
            if (distances[0] < 0) {
                zoomstate = 0;
                break;
            }
        }

        // check if any dot goes out of outer boundary
        // define outer boundary (rectangle with 1 * scaled dotradius distance to border) (y is pointing down!)
        Vector3 outerLowerLeft = camera.unproject(new Vector3(scaledDotRadius,camera.viewportHeight - scaledDotRadius, 0));
        Vector3 outerUpperRight = camera.unproject(new Vector3(camera.viewportWidth - scaledDotRadius,scaledDotRadius,0));

        for(Dot dot: dots) {
            float[] distances = new float[4];
            distances[0] = dot.x - outerLowerLeft.x;
            distances[1] = outerUpperRight.x - dot.x;
            distances[2] = dot.y - outerLowerLeft.y;
            distances[3] = outerUpperRight.y - dot.y;
            Arrays.sort(distances);
            // zoom out
            if (distances[0] < 0) {
                zoomstate = -1;
                break;
            }
        }

        // increment zoom according to state
        if (zoomstate < 0) {
            zoom += 0.015;
        }
        if (zoomstate > 0) {
            zoom -= 0.015;
        }
        // end of autozoom

        camera.zoom = zoom;
        camera.update();

        // render all dots
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(yellow);
        for (Dot dot : dots) {
            if (dot.hasIsoscelesRelations()) {
                shapeRenderer.rectLine(dot.x, dot.y, dot.getRelation1().x, dot.getRelation1().y, 5);
                shapeRenderer.rectLine(dot.x, dot.y, dot.getRelation2().x, dot.getRelation2().y, 5);
                shapeRenderer.circle(dot.x, dot.y, dot.radius + 4);
            }
        }

        shapeRenderer.setColor(blue);
        for (int i = 0; i < dots.size; i++) {
            Dot dot = dots.get(i);
            if (selectedDot == i && !dot.hasIsoscelesRelations()) {
                shapeRenderer.rectLine(dot.x, dot.y, dot.getRelation1().x, dot.getRelation1().y, 3);
                shapeRenderer.rectLine(dot.x, dot.y, dot.getRelation2().x, dot.getRelation2().y, 3);
            }
            shapeRenderer.circle(dot.x, dot.y, dot.radius);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }


}