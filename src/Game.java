import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Timer;

public class Game extends JPanel {

    public static int seconds = 0;
    public static int minutes = 0;

    public static String timer= "";

    public static double mapX = 0, mapY = 0;
    public static boolean reloading;
    private static boolean gameRunning = true;
    private static int lastFpsTime = 0;
    public static int fps, tps = 60;
    Weapon GUN = new Weapon();

    public static String[] path = new String[5];
    static Room[][] rooms = new Room[5][5];

    static Room room;
    static int[][] genRoom = new int[5][2];
    static int genCount = 0;

    int pathNum = 0;

    public Game()
    {

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                rooms[i][j] = new Room("null", i, j, 0);
            }
        }
        rooms[2][2] = new Room("start",2 ,2, pathNum);

        roomGeneration(4, 2, 2, 0, 0, pathNum);


        room = rooms[2][2];
        genRoom[genCount][0] = room.x;
        genRoom[genCount][1] = room.y;


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(rooms[i][j].type + " | ");
            }
            System.out.println();
            System.out.println("-------------------------");
        }

        gameLoop();
    }
    public void gameLoop() {
        Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!Main.pause) {
                    runTick();
                    repaint();

                }
                if (!gameRunning) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 0, 1000 / 60);
    }


    public synchronized void runTick() {
        fps++;

        if (fps % 60 == 0) {

            seconds++;
            if (seconds >= 60) {
                minutes++;
                seconds = 0;
            }

            timer = String.format("%02d:%02d", minutes, seconds);
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (rooms[i][j].check()) {
                    room = rooms[i][j];
                }
            }
        }



        GUN.weaponUpdate();
        Player.update();
        room.update(rooms);

    }

    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        setBackground(Color.black);


        if (Player.health <= 0) {
            gameRunning = false;
            return;
        }

        for (int h = 0; h < 5; h++) {
            for (int f = 0; f < 5; f++){
                Room temp = rooms[f][h];
                for (int i = 0; i < 25; i++) {
                    for (int j = 0; j < 25; j++) {
                        if (temp.tileArray[i][j].wall)
                            g2.setColor(Color.black);
                        else
                            g2.setColor(new Color(75, 0, 130));
                        temp.tileArray[i][j].x += (int) mapX;
                        temp.tileArray[i][j].y += (int) mapY;
                        g2.fillRect(temp.tileArray[i][j].x, temp.tileArray[i][j].y, temp.tileArray[i][j].width, temp.tileArray[i][j].height);
                    }
                }
            }
        }
    //    }


        Player.height = Player.sprite.getHeight() * 3;
        Player.width = Player.sprite.getWidth() * 3;

       
       double angle = (Math.atan2(Main.fHeight/2 - Main.mouseY, Main.fWidth/2 - Main.mouseX) - Math.PI);
       g.setColor(Color.red);
       g.drawLine(Main.fWidth/2 + (int)((Player.sprite.getWidth()+45)*Math.cos(angle)), Main.fHeight/2 + (int)((Player.sprite.getHeight()+45)*Math.sin(angle)), Main.mouseX, Main.mouseY);

   
        if (Player.range && GUN.ammo > 0) {
            if (!GUN.firing) {
                room.bullets.add(GUN.createNewBullet(Main.fWidth/2 - 15 + (int) ((Player.width+20) * Math.cos(angle-0.14)), Main.fHeight/2 - 5 + (int) ((Player.width+20)* Math.sin(angle-0.14)), 10, angle));
                GUN.ammo--;
            }
            GUN.firing = true;
        }

   
        for (int i = 0; i < room.monsters.size(); i++) {
            Mob temp = room.monsters.get(i);
            if (!temp.flip) {
                g2.drawImage(temp.sprite, temp.x, temp.y, temp.width, temp.height, null);
                if (temp.gun) {
                    g2.rotate(temp.shootAngle, temp.x + temp.width/2, temp.y + temp.height/2);
                    g2.drawImage(temp.weapon.sprite, temp.x + temp.width, temp.y + temp.height / 2 - 12, temp.weapon.width, temp.weapon.height, null);
                    g2.drawImage(temp.hand, temp.x + temp.width - 2, temp.y + temp.height / 2 + 2, temp.hand.getWidth() * 3, temp.hand.getWidth() * 3, null);
                    g2.rotate(-temp.shootAngle, temp.x + temp.width/2, temp.y + temp.height/2);
                }
            }
            else {
                g2.drawImage(temp.sprite, temp.x + temp.width, temp.y, -temp.width, temp.height, null);
                if (temp.gun) {
                    g2.rotate(temp.shootAngle, temp.x + temp.width / 2, temp.y + temp.height / 2);
                    g2.drawImage(temp.weapon.sprite, temp.x + temp.width, temp.y + temp.height / 2 + 12, temp.weapon.width, -temp.weapon.height, null);
                    g2.drawImage(temp.hand, temp.x + temp.width + 7, temp.y + temp.height / 2 - 2, temp.hand.getWidth() * 3, temp.hand.getWidth() * 3, null);
                    g2.rotate(-temp.shootAngle, temp.x + temp.width/2, temp.y + temp.height/2);
                }
            }
        }


        if ((Player.counter / 10) % 2 == 0) {

            if (!Player.flip) {
                g2.drawImage(Player.sprite, Player.x, Player.y, Player.width, Player.height, null);
                g2.rotate(angle, Main.fWidth / 2, Main.fHeight / 2);
                g2.drawImage(GUN.sprite, Player.x + Player.width, Player.y + Player.height / 2 - 12, GUN.width, GUN.height, null);
                g2.drawImage(Player.hand, Player.x + Player.width - 2, Player.y + Player.height / 2 + 2, Player.hand.getWidth() * 3, Player.hand.getWidth() * 3, null);
            } else {
                g2.drawImage(Player.sprite, Player.x + Player.width, Player.y, -Player.width, Player.height, null);
                g2.rotate(angle, Main.fWidth / 2, Main.fHeight / 2);
                g2.drawImage(GUN.sprite, Player.x + Player.width + 5, Player.y + Player.height / 2 + 12, GUN.width, -GUN.height, null);
                g2.drawImage(Player.hand, Player.x + Player.width + 7, Player.y + Player.height / 2 - 2, Player.hand.getWidth() * 3, -Player.hand.getWidth() * 3, null);
            }
            g2.rotate(-angle, Main.fWidth / 2, Main.fHeight / 2);


        }
        for (int i = 0; i < room.bullets.size();i++) {
            g2.rotate(room.bullets.get(i).angle,  room.bullets.get(i).x + room.bullets.get(i).width/2, room.bullets.get(i).y + room.bullets.get(i).height/2);
            g2.drawImage(room.bullets.get(i).sprite, room.bullets.get(i).x + room.bullets.get(i).width/2, room.bullets.get(i).y + room.bullets.get(i).height/2, room.bullets.get(i).width, room.bullets.get(i).height, null);
            g2.rotate(-room.bullets.get(i).angle,  room.bullets.get(i).x + room.bullets.get(i).width/2, room.bullets.get(i).y + room.bullets.get(i).height/2);
        }

        for (int i = 0; i < room.enemyBul.size(); i++) {
            g2.drawImage(room.enemyBul.get(i).sprite, room.enemyBul.get(i).x + room.enemyBul.get(i).width/2, room.enemyBul.get(i).y + room.enemyBul.get(i).height/2, room.enemyBul.get(i).width, room.enemyBul.get(i).height, null);
        }

        for (int i = 0; i < room.hits.size(); i ++) {
            int tempW = room.hits.get(i).hit[room.hits.get(i).hitFrame].getWidth()*3;
            int tempH = room.hits.get(i).hit[room.hits.get(i).hitFrame].getWidth()*3;
            g2.drawImage(room.hits.get(i).hit[room.hits.get(i).hitFrame], room.hits.get(i).x, room.hits.get(i).y, tempW, tempH, null);
            room.hits.get(i).hitFrame++;
            if (room.hits.get(i).hitFrame == 7) {
                room.hits.get(i).hitFrame = 0;
                room.hits.remove(i);
                i--;
            }

        }


        int heartX = 20;

        if (Player.health>= 2)
            g2.drawImage(ImageLoader.load("fullHeart.png"), heartX, 20, null);
        else
            g2.drawImage(ImageLoader.load("halfHeart2.png"), heartX, 20, null);
        heartX+=60;

        if (Player.health>= 4)
            g2.drawImage(ImageLoader.load("fullHeart.png"), heartX, 20, null);
        else if (Player.health >=3)
            g2.drawImage(ImageLoader.load("halfHeart2.png"), heartX, 20, null);
        heartX+=60;

        if (Player.health>= 6)
                g2.drawImage(ImageLoader.load("fullHeart.png"), heartX, 20, null);
        else if (Player.health >= 5)
            g2.drawImage(ImageLoader.load("halfHeart2.png"), heartX, 20, null);

        g2.setColor(Color.white);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 36));
        g2.drawString(timer, 50, 100);


        Game.mapX = 0;
        Game.mapY = 0;

    }

    public void roomGeneration (int roomNumber, int i, int j, int pathLoc, int roomNum, int pathNum) {
        if (roomNumber == 0) {
            path[pathLoc] = "final";
            return;
        }
        int iInit = i;
        int jInit = j;

        int randomNum = (int)(Math.random() * 4);

        System.out.println("randomNum = " + randomNum);
        System.out.println("roomNB = " + roomNumber);

        if (randomNum == 0 && i > 0) {
            i-= 1;
            path[pathLoc] = "up";
        }
        else if (randomNum == 1 && j < 4) {
            j+=1;
            path[pathLoc] = "right";
        }
        else if (randomNum == 2 && i < 4) {
            i+=1;
            path[pathLoc] = "down";
        }
        else if (randomNum == 3 && j > 0){
            j-=1;
            path[pathLoc] = "left";
        }



        if (!rooms[i][j].type.equals("null"))
            roomGeneration(roomNumber, iInit, jInit, pathLoc, roomNum, pathNum);
        else {

            pathLoc++;
            roomNum++;
            pathNum += 1;

            rooms[i][j] = new Room("room", i, j, pathNum);
            roomGeneration(roomNumber - 1, i, j, pathLoc, roomNum, pathNum);
        }
    }

}