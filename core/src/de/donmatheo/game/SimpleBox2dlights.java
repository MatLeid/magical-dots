package de.donmatheo.game;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by donmatheo on 16.11.2014.
 */
public class SimpleBox2dlights implements ApplicationListener {

    OrthographicCamera camera;

    float screen_width, screen_height;

    FPSLogger logger;
    World world;
    Box2DDebugRenderer renderer;

    ShapeRenderer shapeRenderer;

    Body circleBody;

    RayHandler handler;
    PointLight pointLight;

    Color background;

    @Override
    public void create() {
        screen_width = Gdx.graphics.getWidth() / 5;
        screen_height = Gdx.graphics.getHeight() / 5;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screen_width, screen_height);
        camera.update();

        world = new World(new Vector2(0, -9.8f), false);

        renderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();

        logger = new FPSLogger();

        BodyDef circleDef = new BodyDef();
        circleDef.type = BodyDef.BodyType.DynamicBody;
        circleDef.position.set(screen_width / 2, screen_height / 2);

        circleBody = world.createBody(circleDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3f);

        FixtureDef circleFixture = new FixtureDef();
        circleFixture.shape = circleShape;
        circleFixture.density = 0.4f;
        circleFixture.friction = 0.2f;
        circleFixture.restitution = 0.8f;

        circleBody.createFixture(circleFixture);

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 3);

        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth * 2, 3.0f);
        groundBody.createFixture(groundBox, 0.0f);

        handler = new RayHandler(world);
        handler.setCombinedMatrix(camera.combined);

        background = parseColor("D6147E");
        handler.setAmbientLight(background);

        pointLight = new PointLight(handler, 200, parseColor("FFF01E"), 50, circleBody.getPosition().x, circleBody.getPosition().y);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       // renderer.render(world, camera.combined);

        pointLight.setPosition(circleBody.getPosition());


        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(parseColor("FF971E"));
        shapeRenderer.circle(circleBody.getPosition().x, circleBody.getPosition().y, 3f );
        shapeRenderer.end();

        handler.updateAndRender();

        world.step(1/60f, 6, 2);

        logger.log();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        world.dispose();
        handler.dispose();
    }

    public static Color parseColor(String hex) {
        String s1 = hex.substring(0, 2);
        int v1 = Integer.parseInt(s1, 16);
        float f1 = (float) v1 / 255f;
        String s2 = hex.substring(2, 4);
        int v2 = Integer.parseInt(s2, 16);
        float f2 = (float) v2 / 255f;
        String s3 = hex.substring(4, 6);
        int v3 = Integer.parseInt(s3, 16);
        float f3 = (float) v3 / 255f;
        return new Color(f1, f2, f3, 1);
    }


}
