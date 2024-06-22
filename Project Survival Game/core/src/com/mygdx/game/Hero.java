package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.css.Rect;
import sun.jvm.hotspot.debugger.win32.coff.ExportDirectoryTable;

import java.util.ArrayList;

public class Hero extends Entity implements  EntitiyAction{
    private int maxHp;
    private int xp;
    private int maxXp;
    private int level;
    ArrayList<Skill> skills;
    private Texture mcImage;
    private Animation<TextureRegion> mcUp, mcDown, mcLeft, mcRight, mcNortheast, mcNorthwest, mcSoutheast, mcSouthWest;
    private float stateTime;
    private String mcState = "default";
    private LeveledUpScreen levelScreen;
    private GameScreen gameScreen;
    public Hero(LeveledUpScreen levelScreen, GameScreen gameScreen) {
        super(1000, 100);
        maxHp = 1000;
        xp = 0;
        maxXp = 100;
        level = 1;
        skills = new ArrayList<Skill>(){{

        }};
        create();
        this.levelScreen = levelScreen;
        this.gameScreen = gameScreen;
    }


    public void create(){
        mcImage = new Texture(Gdx.files.internal("hero/mc.png"));

        //make mc animation North South
        Texture rawNS = new Texture("hero/N-S.png");
        TextureRegion[][] NSframes = TextureRegion.split(rawNS, rawNS.getWidth() / 11, rawNS.getHeight() / 2);
        TextureRegion[] north = new TextureRegion[9];
        TextureRegion[] south = new TextureRegion[10];
        int idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 9; j++) {
                north[idx] = NSframes[i][j];
                idx++;
            }
        }
        idx = 0;
        for (int i = 1; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                south[idx] = NSframes[i][j];
                idx++;
            }
        }
        mcUp = new Animation<>(.08f, north);
        mcDown = new Animation<>(.08f, south);

        //make mc animation West East
        Texture rawWE = new Texture("hero/W-E.png");
        TextureRegion[][] WEframes = TextureRegion.split(rawWE, rawWE.getWidth() / 11, rawWE.getHeight() / 2);
        TextureRegion[] west = new TextureRegion[8];
        TextureRegion[] east = new TextureRegion[8];
        idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 8; j++) {
                west[idx] = WEframes[i][j];
                idx++;
            }
        }
        idx = 0;
        for (int i = 1; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                east[idx] = WEframes[i][j];
                idx++;
            }
        }
        mcLeft = new Animation<>(.08f, west);
        mcRight = new Animation<>(.08f, east);

        //make mc animation Southeast SouthWest
        Texture rawSWSE = new Texture("hero/SW-SE.png");
        TextureRegion[][] SWSEframes = TextureRegion.split(rawSWSE, rawSWSE.getWidth() / 11, rawSWSE.getHeight() / 2);
        TextureRegion[] southwest = new TextureRegion[8];
        TextureRegion[] southeast = new TextureRegion[8];
        idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 8; j++) {
                southwest[idx] = SWSEframes[i][j];
                idx++;
            }
        }
        idx = 0;
        for (int i = 1; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                southeast[idx] = SWSEframes[i][j];
                idx++;
            }
        }
        mcSouthWest = new Animation<>(.08f, southwest);
        mcSoutheast = new Animation<>(.08f, southeast);

        //make mc animation Northwest, Northeast
        Texture rawNWNE = new Texture("hero/NW-NE.png");
        TextureRegion[][] NWNEframes = TextureRegion.split(rawNWNE, rawNWNE.getWidth() / 11, rawNWNE.getHeight() / 2);
        TextureRegion[] northwest = new TextureRegion[8];
        TextureRegion[] northeast = new TextureRegion[8];
        idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 8; j++) {
                northwest[idx] = NWNEframes[i][j];
                idx++;
            }
        }
        idx = 0;
        for (int i = 1; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                northeast[idx] = NWNEframes[i][j];
                idx++;
            }
        }
        mcNorthwest = new Animation<>(.08f, northwest);
        mcNortheast = new Animation<>(.08f, northeast);

        stateTime = 0f;
    }
    @Override
    public void isAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }

    public void calculateXp(int ammountXp) {
        this.setXp(this.getXp() + ammountXp);
        if(this.getXp() >= this.maxXp){ //leveling up
            level++; //naikin lvlnya
            this.setXp(this.getXp()-maxXp); // buat Xp nya ulang dari 0 atau berapapun kalau ada sisanya
            this.maxXp += 100 * this.level-1;
            levelScreen.updateTable(new Chest(0,0).getList3Skill());
            GameScreen.leveledUp = true;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxXp() {
        return maxXp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setMaxXp(int maxXp) {
        this.maxXp = maxXp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill _skill){
        for (Skill s:skills) {
            if(s.getClass() == _skill.getClass()){
                s.upgradeSkill();
                System.out.println("Upgrade Skill");
                return;
            }
        }
        skills.add(_skill);
    }

    public Animation<TextureRegion> getMcUp() {
        return mcUp;
    }

    public void setMcUp(Animation<TextureRegion> mcUp) {
        this.mcUp = mcUp;
    }

    public Animation<TextureRegion> getMcDown() {
        return mcDown;
    }

    public void setMcDown(Animation<TextureRegion> mcDown) {
        this.mcDown = mcDown;
    }

    public Animation<TextureRegion> getMcLeft() {
        return mcLeft;
    }

    public void setMcLeft(Animation<TextureRegion> mcLeft) {
        this.mcLeft = mcLeft;
    }

    public Animation<TextureRegion> getMcRight() {
        return mcRight;
    }

    public void setMcRight(Animation<TextureRegion> mcRight) {
        this.mcRight = mcRight;
    }

    public Animation<TextureRegion> getMcNortheast() {
        return mcNortheast;
    }

    public void setMcNortheast(Animation<TextureRegion> mcNortheast) {
        this.mcNortheast = mcNortheast;
    }

    public Animation<TextureRegion> getMcNorthwest() {
        return mcNorthwest;
    }

    public void setMcNorthwest(Animation<TextureRegion> mcNorthwest) {
        this.mcNorthwest = mcNorthwest;
    }

    public Animation<TextureRegion> getMcSoutheast() {
        return mcSoutheast;
    }

    public void setMcSoutheast(Animation<TextureRegion> mcSoutheast) {
        this.mcSoutheast = mcSoutheast;
    }

    public Animation<TextureRegion> getMcSouthWest() {
        return mcSouthWest;
    }

    public void setMcSouthWest(Animation<TextureRegion> mcSouthWest) {
        this.mcSouthWest = mcSouthWest;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public String getMcState() {
        return mcState;
    }

    public void setMcState(String mcState) {
        this.mcState = mcState;
    }

    public Texture getMcImage() {
        return mcImage;
    }

    public void setMcImage(Texture mcImage) {
        this.mcImage = mcImage;
    }

    public void drawMove(SpriteBatch batch, Rectangle mcRectangle){
        //Draw Hero based on mcState
        batch.begin();
        if (mcState.equals("NW")) {
            TextureRegion currentState = mcNorthwest.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("SW")) {
            TextureRegion currentState = mcSouthWest.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("NE")) {
            TextureRegion currentState = mcNortheast.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("SE")) {
            TextureRegion currentState = mcSoutheast.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Left")) {
            TextureRegion currentState = mcLeft.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Right")) {
            TextureRegion currentState = mcRight.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Down")) {
            TextureRegion currentState = mcDown.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Up")) {
            TextureRegion currentState = mcUp.getKeyFrame(stateTime, true);
            batch.draw(currentState, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        } else batch.draw(this.mcImage, mcRectangle.x, mcRectangle.y, 64, (float) (64 * 157) / 120);
        batch.end();
    }

    public void move(Rectangle mcRectangle){
        //Move Hero using WASD on keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            mcRectangle.x -= 150 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            mcRectangle.x += 150 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            mcRectangle.y -= 150 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            mcRectangle.y += 150 * Gdx.graphics.getDeltaTime();
        }
    }
}
