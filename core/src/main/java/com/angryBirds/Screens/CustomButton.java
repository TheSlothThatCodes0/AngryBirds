package com.angryBirds.Screens;
import com.angryBirds.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;


public class CustomButton extends Image {
    private float HOVER_SCALE = 1.05f;
    private Main game;
    private Runnable onClickAction;
    private Texture defaultTexture;
    private Texture pressedTexture;
    private Texture hoverTexture;
    private Rectangle bounds;
    private boolean isPressed = false;
    // System.out.println("")

    public CustomButton(Main game,Rectangle bounds, Texture defaultTexture, Texture pressedTexture,Texture hoverTexture,Runnable onClickAction) {
        super(defaultTexture);
        this.game = game;
        this.defaultTexture = defaultTexture;
        this.pressedTexture = pressedTexture;
        this.onClickAction = onClickAction;
        this.bounds = bounds;
        this.hoverTexture = hoverTexture;

        setPosition(bounds.x,bounds.y);
        setSize(bounds.width, bounds.height);


    }

    public void Draw(Main x){
        x.batch.draw(defaultTexture,bounds.x,bounds.y,bounds.width,bounds.height);
    }

    public boolean isHover(Vector3 vec){
        return bounds.contains(vec.x,vec.y);
    }

    public void workHover(Main x, boolean hover,Vector3 vec){
        if(isPressed){
            x.batch.draw(pressedTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
        else if(hover){
            x.batch.draw(defaultTexture,bounds.x - ((bounds.width*HOVER_SCALE - bounds.width)/2) , bounds.y - ((bounds.height*HOVER_SCALE - bounds.height)/2),bounds.width*HOVER_SCALE,bounds.height*HOVER_SCALE);
        }
        else{
            x.batch.draw(hoverTexture,bounds.x,bounds.y,bounds.width,bounds.height);
        }


    }

    public void workClick(Main x, Vector3 vec) {
        if (bounds.contains(vec.x, vec.y)) {
            isPressed = true; // Set the pressed state
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isPressed = false; // Reset the pressed state after delay
                    Gdx.app.postRunnable(onClickAction); // Perform the click action
                }
            }, 0.2f); // 0.5 seconds delay
        }
    }

    public void setScale(float fract){
        this.HOVER_SCALE = fract;
    }

}
