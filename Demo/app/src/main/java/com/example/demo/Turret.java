package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

public class Turret extends AppCompatActivity {
    private TurretType type;
    private int level; // 0, 1, or 2
    private int damage;
    private int range;
    private int attackSpeed;
    private int viewId;
    private int projectileViewId;

    public Turret(TurretType type, int viewId, int projectileViewId) {
        this.type = type;
        this.level = 0;
        this.viewId = viewId;
        this.projectileViewId = projectileViewId;
        if (type == TurretType.ARCHER) {
            damage = 5;
            range = 1;
            attackSpeed = 4;
        } else if (type == TurretType.CANNON) {
            damage = 20;
            range = 3;
            attackSpeed = 2;
        } else /* ICE TYPE */ {
            damage = 0;
            range = 1;
            attackSpeed = 1;
        }
    }

    public Turret(TurretType type, int level, int damage, int range, int attackSpeed, int viewId,
                  int projectileViewId) {
        this.type = type;
        this.level = level;
        this.damage = damage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.viewId = viewId;
        this.projectileViewId = projectileViewId;
    }

    public TurretType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level < 3 ? level : 2;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getViewId() {
        return viewId;
    }

    public int getProjectileViewId() {
        return projectileViewId;
    }

    public void upgrade() {
        if (type == TurretType.ARCHER) {
            if (level < 2) {
                damage += 2;
                range += 1;
                attackSpeed += 1;
            }
        } else if (type == TurretType.CANNON) {
            if (level < 2) {
                damage += 5;
                range += 1;
                attackSpeed += 1;
            }
        } else /* ICE TYPE */ {
            if (level < 2) {
                range += 1;
                attackSpeed += 1;
            }
        }
        setLevel(++level);
    }
}
