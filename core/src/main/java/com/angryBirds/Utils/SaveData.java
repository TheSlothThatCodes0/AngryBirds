package com.angryBirds.Utils;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String levelName;
    public ArrayList<GameObjectData> birds = new ArrayList<>();
    public ArrayList<GameObjectData> blocks = new ArrayList<>();
    public ArrayList<GameObjectData> pigs = new ArrayList<>();

    public static class GameObjectData implements Serializable {
        public String type;
        public float x;
        public float y;
        public float angle;

        public GameObjectData() {
            this.type = "";
            this.x = 0;
            this.y = 0;
            this.angle = 0;
        }

        public GameObjectData(String type, float x, float y, float angle) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }
}
