package com.example.demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GamePage extends AppCompatActivity {
    private final int maxNumTurrets = 10;
    private final int maxNumEnemies = 10;
    private int difficulty; // 0: easy, 1: normal, 2: hard
    private Stage stage = new Stage();
    private Enemy enemy = new Enemy(3, 20, R.id.enemy);
    private Turret[] currentTurrets = new Turret[maxNumTurrets];
    private int[] turretPrices = new int[3];
    private int health;
    private int enemyHealth;
    private int money;
    private boolean gameInProgress = false;
    private boolean showTurretMenu = false;

    // for stats
    private int totalDamage = 0;
    private int numTurrets = 0;
    private int numUpgrades = 0;

    private ObjectAnimator moveY;

    // MediaPlayers
    private MediaPlayer mediaPlayer;
    private MediaPlayer win;
    private MediaPlayer lose;
    private MediaPlayer coinWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        getInfo();
        configureGameInfo();
        configureBackButton();
        configureStartCombatButton();
        configureNextStageButton();
        placeTurrets();
    }

    // Gets difficulty info from the last view.
    private void getInfo() {
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", 0);
    }

    // Configure game info based on the difficulty.
    private void configureGameInfo() {
        ImageView healthBar = findViewById(R.id.healthbar);
        ImageView enemyView = findViewById(enemy.getViewId());
        if (difficulty == 0) { // easy
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.easy);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            healthBar.setImageResource(R.drawable.healthbar5);
            enemyView.setImageResource(R.drawable.kirby);
            stage.setDifficulty(0);
            health = 5;
            money = 10000;
            enemyHealth = 30;
            enemy.setHealth(enemyHealth);
            enemy.setSpeed(20);
        } else if (difficulty == 1) { // normal
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.normal);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            healthBar.setImageResource(R.drawable.healthbar4);
            enemyView.setImageResource(R.drawable.waluigi);
            stage.setDifficulty(1);
            health = 4;
            money = 7000;
            enemyHealth = 40;
            enemy.setHealth(enemyHealth);
            enemy.setSpeed(15);
        } else { // hard
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hard);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            healthBar.setImageResource(R.drawable.healthbar3);
            enemyView.setImageResource(R.drawable.wario);
            stage.setDifficulty(2);
            health = 3;
            money = 5000;
            enemyHealth = 50;
            enemy.setHealth(enemyHealth);
            enemy.setSpeed(10);
        }
        setTurretPrices();
        setMoney(money);
    }

    // Configure Back button behavior.
    private void configureBackButton() {
        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamePage.this, NameDifficultyScreen.class));
            }
        });
    }

    // Configure Turret Buttons.
    private void configureTurretButtons(int index) {
        int[] turrets = {R.id.turretLeft1, R.id.turretLeft2, R.id.turretLeft3, R.id.turretLeft4,
            R.id.turretLeft5, R.id.turretRight1, R.id.turretRight2, R.id.turretRight3,
            R.id.turretRight4, R.id.turretRight5};
        int[] projectiles = {R.id.turretLeft1Projectile, R.id.turretLeft2Projectile,
            R.id.turretLeft3Projectile, R.id.turretLeft4Projectile,
            R.id.turretLeft5Projectile, R.id.turretRight1Projectile,
            R.id.turretRight2Projectile, R.id.turretRight3Projectile,
            R.id.turretRight4Projectile, R.id.turretRight5Projectile};
        Button archer = findViewById(R.id.archerButton);
        archer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTurretsMenu();
                if (money < turretPrices[0]) {
                    alertMoney();
                    return;
                }
                Turret turret = new Turret(TurretType.ARCHER, turrets[index], projectiles[index]);
                ImageView turretImage = findViewById(turret.getViewId());
                ImageView projectile = findViewById(turret.getProjectileViewId());
                if (index < 5) { // left turrets
                    turretImage.setImageResource(R.drawable.archer_1_left);
                    projectile.setImageResource(R.drawable.arrow_left);
                } else { // right turrets
                    turretImage.setImageResource(R.drawable.archer_1_right);
                    projectile.setImageResource(R.drawable.arrow_right);
                }
                numTurrets++;
                currentTurrets[index] = turret;
                money -= turretPrices[0];
                setMoney(money);
                turretImage.setVisibility(View.VISIBLE);
                turretAction(index);
                turretSelectionReset();
            }
        });
        Button cannon = findViewById(R.id.cannonButton);
        cannon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTurretsMenu();
                if (money < turretPrices[1]) {
                    alertMoney();
                    return;
                }
                Turret turret = new Turret(TurretType.CANNON, turrets[index], projectiles[index]);
                ImageView turretImage = findViewById(turret.getViewId());
                ImageView projectile = findViewById(turret.getProjectileViewId());
                projectile.setImageResource(R.drawable.cannonball);
                if (index < 5) {
                    turretImage.setImageResource(R.drawable.cannon_1_left);
                } else {
                    turretImage.setImageResource(R.drawable.cannon_1_right);
                }
                numTurrets++;
                currentTurrets[index] = turret;
                money -= turretPrices[1];
                setMoney(money);
                turretImage.setVisibility(View.VISIBLE);
                turretAction(index);
                turretSelectionReset();
            }
        });
        Button ice = findViewById(R.id.iceButton);
        ice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTurretsMenu();
                if (money < turretPrices[2]) {
                    alertMoney();
                    return;
                }
                Turret turret = new Turret(TurretType.ICE, turrets[index], projectiles[index]);
                ImageView turretImage = findViewById(turret.getViewId());
                if (index < 5) {
                    turretImage.setImageResource(R.drawable.ice_1_left);
                } else {
                    turretImage.setImageResource(R.drawable.ice_1_right);
                }
                numTurrets++;
                currentTurrets[index] = turret;
                money -= turretPrices[2];
                setMoney(money);
                turretImage.setVisibility(View.VISIBLE);
                iceTurretAction(index);
                turretSelectionReset();
            }
        });
    }

    private void configureUpgradeButton(int index) {
        Button upgrade = findViewById(R.id.upgrade);
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Turret turret = currentTurrets[index];
                ImageView turretImage = findViewById(turret.getViewId());
                if (index < 5) {
                    if (turret.getType() == TurretType.ARCHER) {
                        if (money < turretPrices[0]) {
                            alertMoney();
                            return;
                        }
                        if (turret.getLevel() == 0) {
                            turretImage.setImageResource(R.drawable.archer_2_left);
                            money -= turretPrices[0];
                            turret.upgrade();
                        } else if (turret.getLevel() == 1) {
                            turretImage.setImageResource(R.drawable.archer_3_left);
                            money -= turretPrices[0];
                            turret.upgrade();
                        }
                    } else if (turret.getType() == TurretType.CANNON) {
                        if (money < turretPrices[1]) {
                            alertMoney();
                            return;
                        }
                        if (turret.getLevel() == 0) {
                            turretImage.setImageResource(R.drawable.cannon_2_left);
                            money -= turretPrices[1];
                            turret.upgrade();
                        } else if (turret.getLevel() == 1) {
                            turretImage.setImageResource(R.drawable.cannon_3_left);
                            money -= turretPrices[1];
                            turret.upgrade();
                        }
                    } else /* ICE TYPE */ {
                        if (money < turretPrices[2]) {
                            alertMoney();
                            return;
                        }
                        if (turret.getLevel() == 0) {
                            turretImage.setImageResource(R.drawable.ice_2_left);
                            money -= turretPrices[2];
                            turret.upgrade();
                        } else if (turret.getLevel() == 1) {
                            turretImage.setImageResource(R.drawable.ice_3_left);
                            money -= turretPrices[2];
                            turret.upgrade();
                        }
                    }
                } else {
                    if (turret.getType() == TurretType.ARCHER) {
                        if (money < turretPrices[0]) {
                            alertMoney();
                            return;
                        }
                        if (turret.getLevel() == 0) {
                            turretImage.setImageResource(R.drawable.archer_2_right);
                            money -= turretPrices[0];
                            turret.upgrade();
                        } else if (turret.getLevel() == 1) {
                            turretImage.setImageResource(R.drawable.archer_3_right);
                            money -= turretPrices[0];
                            turret.upgrade();
                        }
                    } else if (turret.getType() == TurretType.CANNON) {
                        if (money < turretPrices[1]) {
                            alertMoney();
                            return;
                        }
                        if (turret.getLevel() == 0) {
                            turretImage.setImageResource(R.drawable.cannon_2_right);
                            money -= turretPrices[1];
                            turret.upgrade();
                        } else if (turret.getLevel() == 1) {
                            turretImage.setImageResource(R.drawable.cannon_3_right);
                            money -= turretPrices[1];
                            turret.upgrade();
                        }
                    } else /* ICE TYPE */ {
                        if (money < turretPrices[2]) {
                            alertMoney();
                            return;
                        }
                        if (turret.getLevel() == 0) {
                            turretImage.setImageResource(R.drawable.ice_2_right);
                            money -= turretPrices[2];
                            turret.upgrade();
                        } else if (turret.getLevel() == 1) {
                            turretImage.setImageResource(R.drawable.ice_3_right);
                            money -= turretPrices[2];
                            turret.upgrade();
                        }
                    }
                }
                ++numUpgrades;
                setMoney(money);
                turretSelectionReset();
                upgrade.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Configure the start combat button.
    private void configureStartCombatButton() {
        Button startCombatButton = findViewById(R.id.startcombat);
        startCombatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameInProgress = true;
                ImageView enemyView = findViewById(enemy.getViewId());
                enemyView.setVisibility(View.VISIBLE);
                enemy.setPosition(0);
                for (int i = 0; i < currentTurrets.length; i++) {
                    if (currentTurrets[i] != null) {
                        if (currentTurrets[i].getType() == TurretType.ICE) {
                            iceTurretAction(i);
                        } else {
                            turretAction(i);
                        }
                    }
                }
                moveEnemy();
                startCombatButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void configureNextStageButton() {
        Button startCombatButton = findViewById(R.id.startcombat);
        Button nextStageButton = findViewById(R.id.nextStage);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveY.cancel();
                stage.setNumStage(stage.getNumStage() + 1);
                TextView stageNum = findViewById(R.id.stageNum);
                if (stage.isBossStage()) {
                    ImageView background = findViewById(R.id.imageView);
                    ImageView enemyView = findViewById(enemy.getViewId());
                    enemyView.setImageResource(R.drawable.mario);
                    background.setBackgroundResource(R.drawable.mustafar);
                    stageNum.setText("BOSS");
                    enemyHealth *= 4;
                } else {
                    stageNum.setText(String.valueOf(stage.getNumStage()));
                    enemyHealth += 20;
                }
                enemy.setHealth(enemyHealth);
                enemy.setPosition(0);
                resetEnemyHealthBar();
                nextStageButton.setVisibility(View.INVISIBLE);
                startCombatButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void placeTurrets() {
        int[] mappings = {R.id.turret1, R.id.turret2, R.id.turret3, R.id.turret4, R.id.turret5,
            R.id.turret6, R.id.turret7, R.id.turret8, R.id.turret9, R.id.turret10};
        for (int i = 0; i < mappings.length; i++) {
            int finalI = i;
            ImageView turretPlace = findViewById(mappings[i]);
            turretPlace.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    turretSelectionReset();
                    Button upgrade = findViewById(R.id.upgrade);
                    // Check if there is already a turret.
                    if (currentTurrets[finalI] != null) {
                        closeTurretsMenu();
                        if (upgrade.getVisibility() == View.VISIBLE) {
                            turretPlace.setAlpha(1f);
                            upgrade.setVisibility(View.INVISIBLE);
                            return false;
                        }
                        turretPlace.setAlpha(0.5f);
                        upgrade.setVisibility(View.VISIBLE);
                        configureUpgradeButton(finalI);
                        return false;
                    }

                    // There is no turret. Then choose what to construct.
                    upgrade.setVisibility(View.INVISIBLE);
                    if (!showTurretMenu) {
                        turretPlace.setAlpha(0.5f);
                        openTurretsMenu();
                    } else {
                        turretPlace.setAlpha(1f);
                        closeTurretsMenu();
                    }
                    configureTurretButtons(finalI);
                    return false;
                }
            });
        }
    }

    private void moveEnemy() {
        ImageView enemyView = findViewById(enemy.getViewId());
        Button startCombatButton = findViewById(R.id.startcombat);
        moveY = ObjectAnimator.ofFloat(enemyView, "translationY", 0f, -1400f);
        moveY.setRepeatCount(Animation.ABSOLUTE);
        moveY.setInterpolator(new LinearInterpolator());
        moveY.setDuration(enemy.getSpeed() * 1000);
        moveY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (gameInProgress) {
                    decreaseHealth();
                }
                if (health <= 0) {
                    lose = MediaPlayer.create(getApplicationContext(), R.raw.lose);
                    lose.start();
                    Intent intent = new Intent(getApplicationContext(), GameOverScreen.class);
                    intent.putExtra("totalDamage", totalDamage);
                    intent.putExtra("numTurrets", numTurrets);
                    intent.putExtra("numUpgrades", numUpgrades);
                    startActivity(intent);
                }
                if (enemy.getHealth() > 0) {
                    startCombatButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {

            }
            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        moveY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                enemy.setPosition((Float) valueAnimator.getAnimatedValue());
            }
        });
        moveY.start();
    }

    // make three different turret Action
    private void iceTurretAction(int index) {
        Turret turret = currentTurrets[index];
        ImageView turretView = findViewById(turret.getViewId());
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0);
        ta.setDuration(2_000 * turret.getAttackSpeed());
        ta.setRepeatCount(0);
        ta.setFillAfter(true);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (moveY != null) {
                    moveY.pause();
                    turretView.setAlpha(1f);
                }
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (moveY != null) {
                    moveY.resume();
                    turretView.setAlpha(0.5f);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        turretView.startAnimation(ta);
    }

    private void turretAction(int index) {
        // determine the turret type
        // choose what to use: ball / arrow
        // rotate projectiles accordingly
        Turret turret = currentTurrets[index];
        ImageView projectile = findViewById(turret.getProjectileViewId());
        // Change enemy to Enemy Object, and use array here.
        ImageView enemyView = findViewById(R.id.enemy);
        // Choose which enemy to attack here.
        float dx = xDeltaCalculator(projectile, enemyView);
        float dy = yDeltaCalculator(enemy, turret, enemyView);
        TranslateAnimation ta = new TranslateAnimation(0, dx, 0, dy);
        projectile.setRotation((float) Math.toDegrees(Math.atan(yDeltaCalculator(enemy, turret,
            enemyView) / xDeltaCalculator(projectile, enemyView))));
        ta.setDuration(5_000 / turret.getAttackSpeed());
        ta.setRepeatCount(0);
        ta.setInterpolator(new LinearInterpolator());
        ta.setFillAfter(true);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                projectile.setAlpha(1f);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                // decrease enemy health (only when cannon and archery)
                // make cannon ball invisible
                projectile.setAlpha(0f);
                ImageView enemyView = findViewById(enemy.getViewId());
                coinWin = MediaPlayer.create(getApplicationContext(), R.raw.coinwin);
                if (enemyView.getVisibility() == View.VISIBLE) {
                    decreaseEnemyHealth(turret.getDamage());
                    coinWin.start();
                    if (difficulty == 0) {
                        money += 300;
                    } else if (difficulty == 1) {
                        money += 200;
                    } else {
                        money += 100;
                    }
                }
                setMoney(money);
                if (enemy.getHealth() <= 0) {
                    gameInProgress = false;
                    Button nextStageButton = findViewById(R.id.nextStage);
                    Button startCombatButton = findViewById(R.id.startcombat);
                    enemyView.setVisibility(View.INVISIBLE);
                    if (!stage.isBossStage()) {
                        nextStageButton.setVisibility(View.VISIBLE);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), WinScreen.class);
                        intent.putExtra("totalDamage", totalDamage);
                        intent.putExtra("numTurrets", numTurrets);
                        intent.putExtra("numUpgrades", numUpgrades);
                        startActivity(intent);
                    }
                    startCombatButton.setVisibility(View.INVISIBLE);
                    win = MediaPlayer.create(getApplicationContext(), R.raw.win);
                    win.start();
                }
                if (true /* there is an enemy in range */) {
                    turretAction(index);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (isInRange(turret, enemy) && gameInProgress/* enemy in range && game in progress*/) {
            projectile.startAnimation(ta);
        }
    }

    private void openTurretsMenu() {
        if (!showTurretMenu) {
            LinearLayout buttons = findViewById(R.id.linearLayout);
            TranslateAnimation ta = new TranslateAnimation(0, 0, 700, 0);
            ta.setDuration(500);
            ta.setRepeatCount(0);
            ta.setInterpolator(new AccelerateDecelerateInterpolator());
            ta.setFillAfter(true);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            buttons.startAnimation(ta);
            showTurretMenu = true;
        }
    }

    private void closeTurretsMenu() {
        if (showTurretMenu) {
            LinearLayout buttons = findViewById(R.id.linearLayout);
            TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 700);
            ta.setDuration(500);
            ta.setRepeatCount(0);
            ta.setInterpolator(new AccelerateDecelerateInterpolator());
            ta.setFillAfter(true);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            buttons.startAnimation(ta);
            showTurretMenu = false;
        }
    }

    private void decreaseHealth() {
        ImageView healthBar = findViewById(R.id.healthbar);
        if (health == 5) {
            healthBar.setImageResource(R.drawable.healthbar4);
        } else if (health == 4) {
            healthBar.setImageResource(R.drawable.healthbar3);
        } else if (health == 3) {
            healthBar.setImageResource(R.drawable.healthbar2);
        } else if (health == 2) {
            healthBar.setImageResource(R.drawable.healthbar1);
        } else if (health == 1) {
            healthBar.setImageResource(R.drawable.healthbar0);
        }
        --health;
    }

    private void decreaseEnemyHealth(int by) {
        View origin = findViewById(R.id.enemyHealthBack);
        View enemyHealthBar = findViewById(R.id.enemyHealth);
        ViewGroup.LayoutParams layoutParams = enemyHealthBar.getLayoutParams();
        layoutParams.width = origin.getWidth() * (enemy.getHealth() - by) / enemyHealth;
        enemyHealthBar.setLayoutParams(layoutParams);
        enemy.decreaseHealth(by);
        totalDamage += by;
    }
    private void resetEnemyHealthBar() {
        View origin = findViewById(R.id.enemyHealthBack);
        View enemyHealthBar = findViewById(R.id.enemyHealth);
        ViewGroup.LayoutParams layoutParams = enemyHealthBar.getLayoutParams();
        layoutParams.width = origin.getWidth();
        enemyHealthBar.setLayoutParams(layoutParams);
    }

    private void setTurretPrices() {
        if (difficulty == 0) { // easy
            turretPrices[0] = 400; // archer
            turretPrices[1] = 600; // cannon
            turretPrices[2] = 800; // ice
        } else if (difficulty == 1) { // normal
            turretPrices[0] = 450;
            turretPrices[1] = 700;
            turretPrices[2] = 950;
        } else { // hard
            turretPrices[0] = 500;
            turretPrices[1] = 800;
            turretPrices[2] = 1100;
        }
        TextView archerPrice = findViewById(R.id.archerPrice);
        TextView cannonPrice = findViewById(R.id.cannonPrice);
        TextView icePrice = findViewById(R.id.icePrice);
        archerPrice.setText(String.valueOf(turretPrices[0]));
        cannonPrice.setText(String.valueOf(turretPrices[1]));
        icePrice.setText(String.valueOf(turretPrices[2]));
    }

    private void setMoney(int money) {
        TextView moneySet = findViewById(R.id.moneyView);
        moneySet.setText(String.valueOf(money));
    }

    private void alertMoney() {
        AlertDialog.Builder insufficientMoney = new AlertDialog.Builder(this)
                .setTitle("Money Insufficient")
                .setMessage("You do not have enough money to buy the tower.")
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        turretSelectionReset();
        insufficientMoney.show();
    }

    private void turretSelectionReset() {
        int[] turretList = {R.id.turret1, R.id.turret2, R.id.turret3, R.id.turret4, R.id.turret5,
            R.id.turret6, R.id.turret7, R.id.turret8, R.id.turret9, R.id.turret10};
        for (int id : turretList) {
            ImageView turretPlace = findViewById(id);
            turretPlace.setAlpha(1f);
        }
    }

    private float xDeltaCalculator(View from, View to) {
        return to.getX() + to.getWidth() / 2 - from.getX() - from.getWidth() / 2;
    }

    private float yDeltaCalculator(Enemy enemy, Turret turret, View to) {
        View from = findViewById(turret.getViewId());
        float result = enemy.getPosition() - to.getHeight() / 2
            + ((View) from.getParent()).getHeight() - from.getY();
        if (result > 1400f) {
            return 1400f;
        }
        float adjustment = 400 - 15 * turret.getAttackSpeed();
        return result - adjustment;
    }

    public boolean isInRange(Turret turret, Enemy enemy) {
        // find the distance from the turret to given object
        ImageView turretView = findViewById(turret.getViewId());
        if (-1 * enemy.getPosition() + 400 >= ((View) turretView.getParent()).getHeight()
            - turretView.getY()) {
            return false;
        }
        return true;
    }

    // For Media
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }
}