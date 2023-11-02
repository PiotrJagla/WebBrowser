package com.mygdx.game.browser;

import java.util.Random;

public class Utils {
    public static int getRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(min, max - min) + min;
    }
}
