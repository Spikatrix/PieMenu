package com.payne.games.piemenu.codeExamples;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;


public class ClickDrag extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private Batch batch;
    private PieMenu menu;

    /* For the demonstration's purposes. Not actually necessary. */
    private float red   = .25f;
    private float green = .25f;
    private float blue  = .75f;


    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        /* Ideally, you would extract such a pixel from your Atlas instead. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion whitePixel = new TextureRegion(tmpTex);



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.separatorWidth = 2;
        style.circumferenceWidth = 2;
        style.backgroundColor = new Color(1,1,1,.1f);
        style.separatorColor = new Color(.1f,.1f,.1f,1);
        style.downColor = new Color(.5f,.5f,.5f,1);
        style.sliceColor = new Color(.33f,.33f,.33f,1);
        menu = new PieMenu(whitePixel, style, 80, 0, 90);

        /* Customizing the behavior. */
        menu.setInfiniteSelectionRange(true);
        menu.setSelectionButton(Input.Buttons.RIGHT); // right-click for interactions with the widget

        /* Setting up listeners. */
        menu.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHighlightChange(int highlightedIndex) {
                switch(highlightedIndex) {
                    case 0: // Red
                        red   = .75f;
                        green = .25f;
                        blue  = .25f;
                        break;
                    case 1: // Green
                        red   = .25f;
                        green = .75f;
                        blue  = .25f;
                        break;
                    case 2: // Blue
                        red   = .25f;
                        green = .25f;
                        blue  = .75f;
                        break;
                    default:
                        red   = .75f;
                        green = .75f;
                        blue  = .75f;
                        break;
                }
            }
        });
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - selected index: " + menu.getSelectedIndex());
                menu.setVisible(false);
                menu.remove();
            }
        });

        /* Populating the widget. */
        Label red = new Label("red", skin);
        menu.addActor(red);
        Label green = new Label("green", skin);
        menu.addActor(green);
        Label blue = new Label("blue", skin);
        menu.addActor(blue);
    }


    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(red, green, blue, 1); // updated with the menu
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        if (Gdx.input.isButtonJustPressed(menu.getSelectionButton())) {
            stage.addActor(menu);
            menu.centerOnMouse();
            menu.setVisible(true);
            transferInteraction(stage, menu);
        }
    }

    /**
     * To be used to get the user to transition directly into
     * {@link InputListener#touchDragged(InputEvent, float, float, int)}
     * as if he had triggered
     * {@link InputListener#touchDown(InputEvent, float, float, int, int)}.<br>
     * I am not certain this is the recommended way of doing this, but for the
     * purposes of this demonstration, it works!
     *
     * @param stage the stage.
     * @param widget the PieMenu on which to transfer the interaction.
     */
    private void transferInteraction(Stage stage, PieMenu widget) {
        if(widget == null) throw new IllegalArgumentException("widget cannot be null.");
        if(widget.getPieMenuListener() == null) throw new IllegalArgumentException("inputListener cannot be null.");
        stage.addTouchFocus(widget.getPieMenuListener(), widget, widget, 0, widget.getSelectionButton());
    }



    @Override
    public void dispose () {

        /* Disposing is good practice! */
        batch.dispose();
        tmpTex.dispose();
        stage.dispose();
        skin.dispose();
    }
}