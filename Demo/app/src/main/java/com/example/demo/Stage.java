package com.example.demo;

public class Stage {
    private int difficulty; // 0 - easy, 1 - normal, 2 - hard
    private int numStage;
    private int[] numEnemies; // 3-type of enemies

    public Stage() {
        difficulty = 0;
        numStage = 1;
    }
    public Stage(int difficulty) {
        this.difficulty = difficulty;
        numStage = 1;
    }
    public Stage(int difficulty, int numStage) {
        // If num stage is a multiple of 5, then it is a boss stage.
        this.difficulty = difficulty;
        this.numStage = numStage;
    }

    public void stageClear() {
        ++numStage;
        generateStage();
    }

    public void generateStage() {
        return;
    }

    public boolean isBossStage() {
        return numStage % 5 == 0;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setNumStage(int numStage) {
        this.numStage = numStage;
    }

    public int getNumStage() {
        return numStage;
    }

    public static Stage stageGenerator(int difficulty, int numStage) {
        Stage stage = new Stage(difficulty, numStage);
        /*
        if (stage.isBossStage()) {

        } else {

        }
         */
        return stage;
    }
}
