package de.donmatheo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class UnstableRelations extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Vector3 touchPos;
    private OrthographicCamera camera;
    private Vector2 initialDotDimensions;

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
        initialDotDimensions = calculateDotsDimension();
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
        autoscale();
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

    public void autoscale() {
        float zoomX = 1.0f / initialDotDimensions.x * calculateDotsDimension().x;
        float zoomY = 1.0f / initialDotDimensions.y * calculateDotsDimension().y;

        if (zoomX > zoomY) {
            zoom = zoomX;
        } else {
            zoom = zoomY;
        }
        if (zoom > 3) {
            zoom = 3;
        }
        if (zoom < 0.5) {
            zoom = 0.5f;
        }

    }

    public Vector2 calculateDotsDimension() {
        float minX = camera.viewportWidth;
        float maxX = 0;
        for (Dot dot : dots) {
            if (dot.x < minX) {
                minX = dot.x;
            }
            if (dot.x > maxX) {
                maxX = dot.x;
            }
        }
        float minY = camera.viewportHeight;
        float maxY = 0;
        for (Dot dot : dots) {
            if (dot.y < minY) {
                minY = dot.y;
            }
            if (dot.y > maxY) {
                maxY = dot.y;
            }
        }
        return new Vector2(maxX - minX, maxY - minY);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }


}