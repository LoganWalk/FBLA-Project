import java.util.ArrayList;

public class Character {
    private Tile tile;
    private int speed;
    private Player player;
    private boolean hasBall;
    private boolean isSelected;
    private ArrayList<Character> characters;

    public Character(Tile tile, boolean isSelected, boolean hasBall, Player player) {
        this.player = player;
        this.speed = generateSpeed();
        this.isSelected = isSelected;
        this.hasBall = hasBall;
        characters.add(this);

    }

    public int getSpeed() {
        return this.speed;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean getHasBall() {
        return this.hasBall;
    }

    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public int generateSpeed() {
        return 1;
    }
}