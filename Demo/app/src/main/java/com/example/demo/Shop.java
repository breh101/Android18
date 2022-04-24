package com.example.demo;

public class Shop {
    private int money;
    private int miniTower;
    private int midTower;
    private int massiveTower;
    private int[] towerCosts;

    public Shop() {
        money = 0;
        miniTower = 0;
        midTower = 0;
        massiveTower = 0;
    }
    public void setMoney(int inputMoney, String difficulty) {
        this.money = inputMoney;
        if (difficulty.equals("easy")) {
            towerCosts = new int[]{500, 1000, 2000};
        } else if (difficulty.equals("normal")) {
            towerCosts = new int[]{600, 1100, 2100};
        } else {
            towerCosts = new int[]{1000, 1500, 3000};
        }
    }
    public int buy(int towerType) {
        if (towerCosts[towerType] > money) {
            return -1;
        } else {
            if (towerType == 0) {
                miniTower += 1;
            } else if (towerType == 1) {
                midTower += 1;
            } else {
                massiveTower += 1;
            }
            money -= towerCosts[towerType];
            return money;
        }
    }
    public int[] getTowerCosts() {
        return towerCosts;
    }
    public int[] getTowerQuantities() {
        return  new int[] {miniTower, midTower, massiveTower};
    }

    // Helper methods for tests. Will be deleted.
    public int getMoney() {
        return money;
    }
}
