package de.donmatheo.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        Gdx.input.setInputProcessor(stage);

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
        lighting.setAmbientLight(UnstableRelations.PURPLE);

        // setup dots
        dots = dotController.createDots(5, lighting);
        dotController.setRandomRelations();
        dotController.setRandomLayout(camera.viewportWidth, camera.viewportHeight);
        dotController.addAllToStage(stage);

    }

    @Override
    public void render(float delta) {
        // set zoom and update camera
        autozoom();
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

    private void autozoom() {
        // default zoom is positive (=zoom in)
        int zoomstate = 1;
        // calculate scaled radius (dots get smaller when zoomed out)
        float scaledDotRadius = Dot.DEFAULTRADIUS / zoom;

        // check if any dot leaves inner boundary
        // define inner boundary (rectangle with 2 * scaled dotradius distance to border) (y is pointing down!)
        Vector3 innerLowerLeft = camera.unproject(new Vector3(scaledDotRadius * 2, camera.viewportHeight - scaledDotRadius * 2, 0));
        Vector3 innerUpperRight = camera.unproject(new Vector3(camera.viewportWidth - scaledDotRadius * 2, scaledDotRadius * 2, 0));
        zoomstate = detectZoomState(innerLowerLeft, innerUpperRight) ? 0 : 1;

        // check if any dot goes out of outer boundary
        // define outer boundary (rectangle with 1 * scaled dotradius distance to border) (y is pointing down!)
        Vector3 outerLowerLeft = camera.unproject(new Vector3(scaledDotRadius, camera.viewportHeight - scaledDotRadius, 0));
        Vector3 outerUpperRight = camera.unproject(new Vector3(camera.viewportWidth - scaledDotRadius, scaledDotRadius, 0));
        zoomstate = detectZoomState(outerLowerLeft, outerUpperRight) ? -1 : zoomstate;

        // increment zoom according to state
        if (zoomstate < 0) {
            zoom += Gdx.graphics.getDeltaTime() * 1.05;
        }
        if (zoomstate > 0) {
            zoom -= Gdx.graphics.getDeltaTime() * 1.05;
        }
        camera.zoom = zoom;
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

    public boolean detectZoomState(Vector3 lowerLeft, Vector3 upperRight) {
        for (Dot dot : dots) {
            float[] distances = new float[4];
            distances[0] = dot.getX() - lowerLeft.x;
            distances[1] = upperRight.x - dot.getX();
            distances[2] = dot.getY() - lowerLeft.y;
            distances[3] = upperRight.y - dot.getY();
            Arrays.sort(distances);
            if (distances[0] < 0)
                return true;
        }
        return false;
    }
}
