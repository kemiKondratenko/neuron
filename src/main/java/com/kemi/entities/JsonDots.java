package com.kemi.entities;

/**
 * Created by Eugene on 30.03.2016.
 */
public class JsonDots {

    private Double x_axis;
    private Double y_axis;
    private int radius;
    private String color;
    private String word;

    public JsonDots(Double x_axis, Double y_axis, String word) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.radius = 5;
        this.color = "black";
        this.word = word;
    }

    public Double getX_axis() {
        return x_axis;
    }

    public void setX_axis(Double x_axis) {
        this.x_axis = x_axis;
    }

    public Double getY_axis() {
        return y_axis;
    }

    public void setY_axis(Double y_axis) {
        this.y_axis = y_axis;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
