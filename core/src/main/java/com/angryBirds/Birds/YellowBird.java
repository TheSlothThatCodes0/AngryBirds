//package com.angryBirds.Birds;
//
//import com.angryBirds.Main;
//import com.badlogic.gdx.graphics.Texture;
//
//public class YellowBird extends Bird {
//
//    public YellowBird(Main game) {
//        super(game);
////        loadTexture();
//    }
//
//    @Override
//    protected void loadTexture() {
//        birdTexture = game.assets.get("yellow_bird.png", Texture.class);
//        updateTexture();
//    }
//}
package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class YellowBird extends Bird {
    public YellowBird(Main game, float x, float y) {
        super(game, x, y);
    }

    @Override
    protected void loadTexture() {
        birdTexture = game.assets.get("yellow_bird.png", Texture.class);
        updateTexture();
    }
}
