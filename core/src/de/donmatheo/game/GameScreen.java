package de.donmatheo.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Arrays;

public class GameScreen implements Screen {

    private final UnstableRelations game;

    private OrthographicCamera camera;

    private DotController dotController = new DotController();
    private Array<Dot> dots = new Array<Dot>();

    private float offsetX;
    private float offsetY;
    private float zoom = 1.0f;

    private World world;

    private RayHandler lighting;
    private Stage stage;

    private InputMultiplexer inputMultiplexer;
    private MyGestureHandler gestureHandler;
    private MyInputProcessor inputProcessor;

    public GameScreen(final UnstableRelations game) {
        this.game = game;
        // setup camera
        float screen_width = Gdx.graphics.getWidth();
        float screen_height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screen_width, screen_height);

        // setup viewport and stage
        FitViewport viewport = new FitViewport(800, 480);
        viewport.setCamera(camera);
        stage = new Stage(viewport);

        initaliseInputProcessors();

        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Dot touched = (Dot) stage.hit(event.getStageX(), event.getStageY(), true);
                if (touched != null) {
                    touched.setTouched(true);
                    offsetX = x - touched.getX();
                    offsetY = y - touched.getY();
                }
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Dot touched = (Dot) stage.hit(event.getStageX(), event.getStageY(), true);
                if (touched != null) {
                    touched.setTouched(false);
                }
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                for (Dot dot: dots) {
                    if (dot.isTouched())
                        dot.updatePosition(event.getStageX() - offsetX, event.getStageY() - offsetY);
                }
            }
        });

        // setup lighting
        world = new World(new Vector2(0, -9.8f), false);
        lighting = new RayHandler(world);
        lighting.setCombinedMatrix(camera.combined);
        lighting.setAmbientLight(UnstableRelations.DARKGREY);

        // setup dots
        dots = dotController.createDots(5, lighting);
        dotController.setRandomRelations();
        dotController.setRandomLayout(camera.viewportWidth, camera.viewportHeight);
        dotController.addAllToStage(stage);

    }

    public void initaliseInputProcessors() {

        inputMultiplexer = new InputMultiplexer();

        Gdx.input.setInputProcessor(inputMultiplexer);

        gestureHandler = new MyGestureHandler(this);
        inputProcessor = new MyInputProcessor(this);

        inputMultiplexer.addProcessor(inputProcessor);
        inputMultiplexer.addProcessor(new GestureDetector(gestureHandler));
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // set zoom and update camera
        camera.zoom = zoom;
        camera.update();

        // render background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render lights
        lighting.setCombinedMatrix(camera.combined);
        lighting.updateAndRender();

        // render dots
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        lighting.dispose();
        stage.dispose();
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
