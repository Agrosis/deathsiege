package com.jantox.siege;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {

    private static int mx = 0;
    private static int my = 0;

    public static int curnum = 1;

    public static boolean r = false;

    public static boolean w = false, a = false, s = false, d = false;
    public static boolean space = false, shift = false;

    public static boolean in = true;
    public static boolean rmouse, lmouse;

    public static boolean down;
    public static boolean up;
    public static boolean enter;

    public static void update() {
        mx = Mouse.getX();
        my = Mouse.getY();

        lmouse = Mouse.isButtonDown(0);
        rmouse = Mouse.isButtonDown(1);

        if(lmouse) {
            if(in == false) {
                in = true;
                Mouse.setGrabbed(true);
                GameInstance.paused = false;
            }
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            up = true;
        } else {
            up = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            down = true;
        } else {
            down = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
            enter = true;
        } else {
            enter = false;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            w = true;
        } else {
            w = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            a = true;
        } else {
            a = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            s = true;
        } else {
            s = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            d = true;
        } else {
            d = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            space = true;
        } else {
            space = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
            curnum = 1;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
            curnum = 2;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_3)) {
            curnum = 3;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_4)) {
            curnum = 4;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_5)) {
            curnum = 5;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_6)) {
            curnum = 6;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_7)) {
            curnum = 7;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_8)) {
            curnum = 8;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_9)) {
            curnum = 9;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            shift = true;
        } else {
            shift = false;
        }

        int dwheel = Mouse.getDWheel();
        if(dwheel < 0) {
            curnum ++;
            if(curnum > 4)
                curnum = 4;
        } else if(dwheel > 0) {
            curnum--;
            if(curnum < 1)
                curnum = 1;
        }

        while(Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                    Mouse.setGrabbed(false);
                    in = false;
                    GameInstance.paused = true;
                } else if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
                    curnum--;
                    if(curnum < 1)
                        curnum = 1;
                } else if (Keyboard.getEventKey() == Keyboard.KEY_E) {
                    curnum++;
                    if(curnum > 4)
                        curnum = 4;
                } else if (Keyboard.getEventKey() == Keyboard.KEY_R) {
                    r = !r;
                } else if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
                    GameInstance.debug = !GameInstance.debug;
                }
            }
        }
    }

    public static int getMouseX() {
        return mx;
    }

    public static int getMouseY() {
        return my;
    }

    public static void setMouse(int x, int y) {
        Mouse.setCursorPosition(x, y);
    }
}
