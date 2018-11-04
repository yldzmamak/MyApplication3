package com.myapp.yldzmamak.myapplication;

/**
 * Created by yldzmamak on 10.10.2018.
 */

public class emojiItem {
    public int drawable;
    public String color;

    public emojiItem(int drawable, String color ) {
        this.drawable = drawable;
        this.color = color;

    }


    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
