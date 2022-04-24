package com.example.demo;

public enum Difficulty {
    EASY("easy"), NORMAL("normal"), HARD("hard");
    private String difficulty;
    Difficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getDifficulty() {
        return this.difficulty;
    }
}
