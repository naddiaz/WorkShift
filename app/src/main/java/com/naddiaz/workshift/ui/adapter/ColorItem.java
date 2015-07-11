package com.naddiaz.workshift.ui.adapter;

/**
 * Created by NESTOR on 11/07/2015.
 */
public class ColorItem {
    private int turn;
    private String text;
    private String colorStart;
    private String colorEnd;

    public ColorItem(int turn, String text, String colorStart, String colorEnd) {
        this.turn = turn;
        this.text = text;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
    }

    @Override
    public String toString() {
        return "ColorItem{" +
                "turn=" + turn +
                ", text='" + text + '\'' +
                ", colorStart='" + colorStart + '\'' +
                ", colorEnd='" + colorEnd + '\'' +
                '}';
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColorStart() {
        return colorStart;
    }

    public void setColorStart(String colorStart) {
        this.colorStart = colorStart;
    }

    public String getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(String colorEnd) {
        this.colorEnd = colorEnd;
    }
}
