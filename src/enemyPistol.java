public class enemyPistol extends Weapon{

    enemyPistol() {
        idle = ImageLoader.load("Monsters/enemyPistol/colt_1851_idle_001.png");
        fire = ImageLoader.arrayLoad("Monsters/enemyPistol/colt_1851_shoot_00", 4);
        bulletImg = ImageLoader.load("Monsters/5x5_enemy_projectile_001_dark.png");


        sprite = idle;
        width = sprite.getWidth() * 3;
        height = sprite.getHeight() * 3;
    }

    public void weaponUpdate() {
        if (firing) {
            sprite = fire[fireFrame];
            if (Game.fps % 5 == 0)
                fireFrame++;
            if (fireFrame == 4) {
                fireFrame = 0;
                firing = false;
            }
        }
    }
}
