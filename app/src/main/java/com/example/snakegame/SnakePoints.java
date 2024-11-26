package com.example.snakegame;

public class SnakePoints {

    private int PositionX, PositionY;

    public SnakePoints(int positionX, int positionY) {
        PositionX = positionX;
        PositionY = positionY;
    }

    public int getPositionX() {
        return PositionX;
    }

    public void setPositionX(int positionX) {
        PositionX = positionX;
    }

    public int getPositionY() {
        return PositionY;
    }

    public void setPositionY(int positionY) {
        PositionY = positionY;
    }
}
