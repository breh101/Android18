package com.example.demo;

public enum TurretType {
    ARCHER("archer"), CANNON("cannon"), ICE("ice");
    private String turretType;
    TurretType(String difficulty) {
        this.turretType = turretType;
    }
    public String getDifficulty() {
        return this.turretType;
    }
}