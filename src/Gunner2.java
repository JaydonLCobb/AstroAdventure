public class Gunner2 extends Gunner {

    public Gunner2 (int x, int y) {
        super(x, y);

        this.powerLvl = 7;

        weapon = new enemyRifle();

    }
    public void updateLocation() {
    	
    
        int dist = (int)Math.sqrt(Math.pow(Player.y-y,2) + Math.pow(Player.x-x,2));
     
        angle = Math.atan2(Player.y - y,Player.x - x);
    
        shootAngle = Math.atan2(Player.y + Player.height/2 - (y + height/2 + (int)(height/2*Math.sin(angle))),Player.x + Player.width/2 - (x + width/2 + (int)(width/2*Math.cos(angle))));
        
     
        if (Game.fps % 40 == 0)
            randDir = (int)(Math.random()*10);
        
     
        if (dist < 400) {
            if (randDir == 0)
                angle += Math.PI/2;
            else if (randDir == 1)
                angle -= Math.PI/2;
            deltaX = (int)(-speed*(Math.cos(angle)));
            deltaY = (int)(-speed*(Math.sin(angle)));

            if (Game.fps % 5 == 0 && randDir < 2) {
                Game.room.enemyBul.add(weapon.createNewBullet(x + width / 2 + (int) (width / 2 * Math.cos(angle)), y + height / 2 + (int) (height / 2 * Math.sin(angle)), 8, shootAngle));
                weapon.firing = true;
            }

            move = true;
        }
        
    
        else {
            deltaX = (int) (speed * (Math.cos(angle)));
            deltaY = (int) (speed * (Math.sin(angle)));
        }

        updateSprite();
        updateCollision();
        weapon.weaponUpdate();

    }
}
