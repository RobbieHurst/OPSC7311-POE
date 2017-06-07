package com.example.roberthurst.gametest;

/**
 * Created by roberthurst on 3/25/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Rob on 3/25/2017.
 */

public class Tile extends FrameLayout{

    private TextView label;
    private View background;
    private int num = 0;
    private String pathRound = "drawable/roundOne";

    public Tile(Context context){
        super(context);

        LayoutParams lp = null;

        background = new View(getContext());
        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        background.setBackgroundColor(Color.parseColor("#ffffff"));
        addView(background, lp);

        label = new TextView(getContext());
        label.setTextSize(28);
        label.setGravity(Gravity.CENTER);
        //label.setHapticFeedbackEnabled(true);

        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);

        setNum(0);

    }
    public int getNum(){
        return num;
    }
    public void setNum(int num) {
        this.num = num;

        /*if (num<=0) {
            label.setText("");
        }else{
            label.setText(num+"");
        }*/

        //Switch statement that will compare the number of the tile and assign a color.
        switch (num) {
            case 0:
                label.setBackgroundColor(Color.parseColor("#fdebff"));
                break;
            case 2:
                label.setBackgroundColor(Color.parseColor("#9b59b6"));
                break;
            case 4:
                label.setBackgroundColor(Color.parseColor("#2980b9"));
                break;
            case 8:
                label.setBackgroundColor(Color.parseColor("#1abc9c"));
                break;
            case 16:
                label.setBackgroundColor(Color.parseColor("#27ae60"));
                break;
            case 32:
                label.setBackgroundColor(Color.parseColor("#f1c40f"));
                break;
            case 64:
                label.setBackgroundColor(Color.parseColor("#e67e22"));
                break;
            case 128:
                label.setBackgroundColor(Color.parseColor("#d35400"));
                break;
            case 256:
                label.setBackgroundColor(Color.parseColor("#283747"));
                break;
            case 512:
                label.setBackgroundColor(Color.parseColor("#7f8c8d"));
                break;
            case 1024:
                label.setBackgroundColor(Color.parseColor("#34495e"));
                break;
            case 2048:
                label.setBackgroundColor(Color.parseColor("#5b2c6f"));
                break;
            default:
                label.setBackgroundColor(Color.parseColor("#ffffff"));
                break;

        }
    }
    //Checking if a tile equals another.
    public boolean equals(Tile t){

        int num = getNum();


        return num == t.getNum();
    }
    protected Tile clone(){
        Tile tile= new Tile(getContext());
        tile.setNum(getNum());
        return tile;
    }

    public TextView getLabel() {
        return label;
    }


}

