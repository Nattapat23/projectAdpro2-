package se233.project_2.controller;


import se233.project_2.model.character.GameCharacter;
import se233.project_2.view.GameStage;

public class DrawingLoop implements Runnable{
    private GameStage gameStage;
    private int frameRate;
    private float interval;
    private boolean running;
    public DrawingLoop(GameStage gameStage){
        this.gameStage = gameStage;
        frameRate = 60;
        interval = 1000.0f/frameRate;
        running = true;
    }
    private void checkDrawCollistions(GameCharacter gameCharacter){
        gameCharacter.checkReachGameWall();
        gameCharacter.checkReachHighest();
        gameCharacter.checkReachFloor();
    }
    private void paint(GameCharacter gameCharacter){
        gameCharacter.update();
    }
    public void stop() {
        running = false;
    }
    @Override
    public void run() {
        while(running){
            float time = System.currentTimeMillis();
            checkDrawCollistions(gameStage.getGameCharacter());
            paint(gameStage.getGameCharacter());
            time = System.currentTimeMillis() - time;
            if(time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                try{
                    Thread.sleep((long) - (interval % time));
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
