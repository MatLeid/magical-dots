package de.donmatheo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class UnstableRelations extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Array<Dot> dots = new Array<Dot>();
    private Color yellow = new Color(157, 162, 0, 255);
    private Color blue = new Color(0, 162, 232, 255);
    private Vector3 touchPos;
    private OrthographicCamera camera;
    private int selectedDot = -1;
    private float offsetX;
    private float offsetY;
    private InputMultiplexer inputMultiplexer;
    private MyInputProcessor inputProcessor;
    private MyGestureHandler gestureHandler;

    public float zoom = 1.0f;



    @Override
    public void create() {

        initaliseInputProcessors();

        // setup camera and shapeRenderer
        float screen_width = Gdx.graphics.getWidth();
        float screen_height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screen_width, screen_height);
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
            dot.x = 35 + random.nextInt((int) camera.viewportWidth - 70);
            dot.y = 35 + random.nextInt((int) camera.viewportHeight - 70);
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
            touchPos = new Vector3();
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


    public void initaliseInputProcessors() {

        inputMultiplexer = new InputMultiplexer();

        Gdx.input.setInputProcessor(inputMultiplexer);

        inputProcessor = new MyInputProcessor();
        gestureHandler = new MyGestureHandler();

        inputMultiplexer.addProcessor(new GestureDetector(gestureHandler));
        inputMultiplexer.addProcessor(inputProcessor);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }


    class MyInputProcessor implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {

            //Zoom out
            if (amount > 0 && zoom < 2) {
                zoom += 0.1f;
            }

            //Zoom in
            if (amount < 0 && zoom > 1.1) {
                zoom -= 0.1f;
            }

            return true;
        }

    }


    class MyGestureHandler implements GestureDetector.GestureListener {

        public float initialScale = 1.0f;

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {

            initialScale = zoom;

            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {

            //Calculate pinch to zoom
            float ratio = initialDistance / distance;

            //Clamp range and set zoom
            zoom = MathUtils.clamp(initialScale * ratio, 1.0f, 2.0f);

            return true;
        }

    }
}