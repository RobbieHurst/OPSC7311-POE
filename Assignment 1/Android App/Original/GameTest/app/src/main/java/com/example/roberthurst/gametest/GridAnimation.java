package com.example.roberthurst.gametest;

/**
 * Created by roberthurst on 3/25/17.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class GridAnimation extends FrameLayout {

    //Declaration of List of the Tiles that are sitting on the front end.

    private List<Tile> tiles = new ArrayList<Tile>();

    public GridAnimation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayer();
    }

    public GridAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayer();
    }

    public GridAnimation(Context context) {
        super(context);
        initLayer();
    }

    private void initLayer(){
    }


    //Move animation method. That compares where the tile was, where it will go, and what the tile will become.
    public void createMoveAnim(final Tile initTile,final Tile finatlTile,int fromX,int toX,int fromY,int toY){

        final Tile tile = getTile(initTile.getNum());


        //Layout parameters for the grid on the front end.
        LayoutParams lp = new LayoutParams(GridConfig.Tile_WIDTH, GridConfig.Tile_WIDTH);
        lp.leftMargin = fromX*GridConfig.Tile_WIDTH;
        lp.topMargin = fromY*GridConfig.Tile_WIDTH;
        tile.setLayoutParams(lp);

        //Checking to see if the final tile is not bigger than 0.

        if (finatlTile.getNum()<=0) {
            finatlTile.getLabel().setVisibility(View.INVISIBLE); //Setting the tile to invisible until you need to finish the animation.
        }
        TranslateAnimation ta = new TranslateAnimation(0, GridConfig.Tile_WIDTH*(toX-fromX), 0, GridConfig.Tile_WIDTH*(toY-fromY));
        ta.setDuration(100);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                finatlTile.getLabel().setVisibility(View.VISIBLE); //Showing the tile.
                recycleTile(tile);
            }
        });
        tile.startAnimation(ta);
    }

    //getting the tile method.
    private Tile getTile(int num){
        Tile tile;
        if (tiles.size()>0) {
            tile = tiles.remove(0);
        }else{
            tile = new Tile(getContext());
            addView(tile);
        }
        tile.setVisibility(View.VISIBLE);
        tile.setNum(num);
        return tile;
    }
    private void recycleTile(Tile tile){
        tile.setVisibility(View.INVISIBLE);
        tile.setAnimation(null);
        tiles.add(tile);
    }

    //Creation of tile that will scale the tile as it is generated on the Front end Grid.
    public void createScaleTo1(Tile target){
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(100);
        target.setAnimation(null);
        target.getLabel().startAnimation(sa);
    }

}

