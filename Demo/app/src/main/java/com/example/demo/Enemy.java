package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

public class Enemy extends AppCompatActivity {
    private int health;
    private int speed;
    private float position; // y position of the enemy
    private int viewId;

    public Enemy() {
        this(0, 0, 0);
    }

    public Enemy(int health, int speed, int viewId) {
        this.health = health;
        this.speed = speed;
        this.viewId = viewId;
        this.position = 0;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void decreaseHealth(int by) {
        this.health -= by;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getPosition() {
        return position;
    }

    public int getViewId() {
        return viewId;
    }

    public void setPosition(float position) {
        this.position = position;
    }
}