package com.example.roberthurst.gametest;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by roberthurst on 3/6/17.
 */
public class Game extends LinearLayout {


    //Declaration of arrays to map and produce the tiles on the front end.
    private Tile[][] tileMapping = new Tile[GridConfig.ROWS][GridConfig.ROWS]; //Tile Array that will be used to drawo out the tiles in the Front end. It makes use of the constant variables in a seperate class.
    private List<Point> emptyPoints = new ArrayList<Point>(); // List of points, which are used to track x and y coordintes that will sim our 2d array.

    //Constructor that takes the state of the objects that are currently on screen.
    public Game(Context context) {
        super(context);

        initGameView();
    }


    //Constructor
    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);

        initGameView();
    }

    //init of creating the view that will populate the layout.
    private void initGameView(){


        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xffbbada0);


        //Looking for touch
        setOnTouchListener(new View.OnTouchListener() {

            //Declaration of variables used for comparison of touch on and off.
            private float xValue, yValue, endX, endY;

            @Override
            //Overiding the OnTouch method.
            public boolean onTouch(View v, MotionEvent event) {


                //Looking for the different
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        xValue = event.getX();
                        yValue = event.getY();      //Getting the X and Y values when the user pressed down on the Phone.

                        break;
                    case MotionEvent.ACTION_UP:

                        endX = event.getX()- xValue;        //Getting the end possition of the X AND Y values.
                        endY = event.getY()- yValue;

                        if (Math.abs(endX)>Math.abs(endY)) {
                            if (endX < -5) {
                                swipeLeft();
                            }else if ( endX > 5 ) {
                                swipeRight();
                            }
                        }else{
                            if (endY < -5) {
                                swipeUp();
                            }else if (endY > 5) {
                                swipeDown();
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);

        GridConfig.Tile_WIDTH = (Math.min(width, height)-10)/GridConfig.ROWS;

        addTile(GridConfig.Tile_WIDTH,GridConfig.Tile_WIDTH);

        startGame();
    }

    private void addTile(int tileWidth,int tileHeight){     //Adding a tile to the Layout.

        Tile tile;

        LinearLayout line;              //Creation of Tile Object, Layout, and The layout parameters.
        LinearLayout.LayoutParams lineLp;

        for (int y = 0; y < GridConfig.ROWS; y++) { //Looping through the Linear Layout.


            line = new LinearLayout(getContext());          //Getting the current state of the Layout.
            lineLp = new LinearLayout.LayoutParams(-1, tileHeight);
            addView(line, lineLp);

            for (int x = 0; x < GridConfig.ROWS; x++) {

                tile = new Tile(getContext());
                line.addView(tile, tileWidth, tileHeight);

                tileMapping[x][y] = tile; //Adding the tile.
            }
        }
    }

    public void startGame(){

        MainActivity aty = MainActivity.getMainActivity();      //Creation of Main activity.
        aty.clearScore();   //Clearing the Score for current label.
        aty.showBestScore(aty.getBestScore());  //Getting the bestScore from the share preferences.

        for (int y = 0; y < GridConfig.ROWS; y++) {
            for (int x = 0; x < GridConfig.ROWS; x++) { //Looping through the tile Mapping array and setting the number to 0.
                tileMapping[x][y].setNum(0);
            }
        }

        addRandomNum();     //Calling the addRandomNum method twice :) :D
        addRandomNum();
    }

    private void addRandomNum(){

        emptyPoints.clear();        //Clearing the array of points. (x,y)

        for (int y = 0; y < GridConfig.ROWS; y++) {

            for (int x = 0; x < GridConfig.ROWS; x++) {

                if (tileMapping[x][y].getNum()<=0) {    //Checking to see if the point in the tile array is empty.
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size()>0) {

            //Removing the empty point at the random generated number.
            Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
            tileMapping[p.x][p.y].setNum(Math.random()>0.1?2:4); //Generating the random number.

            //Animation layer.
            MainActivity.getMainActivity().getAnimLayer().createScaleTo1(tileMapping[p.x][p.y]);
        }
    }


    private void swipeLeft(){

        boolean merge = false; //Boolean that is used for if there is a merge.

        for (int j = 0; j < GridConfig.ROWS; j++) {     //Loops that are used to run through the tile array.

            for (int x = 0; x < GridConfig.ROWS; x++) {

                for (int x1 = x+1; x1 < GridConfig.ROWS; x1++) {

                    if (tileMapping[x1][j].getNum()>0) {        //Checking if the number in the array is greater than 0 ie empty.

                        if (tileMapping[x][j].getNum()<=0) {    //if the number in the next line is 0 then move the number to the next one.

                            //animation layer that will get the current tile, and translate it to the next position.
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x1][j],tileMapping[x][j], x1, x, j, j);

                            //Setting the next tile as the current tile.
                            tileMapping[x][j].setNum(tileMapping[x1][j].getNum());
                            tileMapping[x1][j].setNum(0); //Setting the current tile as 0.

                            x--;
                            merge = true; //Merging hasn't has occured.

                        }else if (tileMapping[x][j].equals(tileMapping[x1][j])) { //if the two tiles are equal then start merging

                            //Animation layer that will create the new Grid.
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x1][j], tileMapping[x][j],x1, x, j, j);
                            tileMapping[x][j].setNum(tileMapping[x][j].getNum()*2); //Creating the new number.
                            tileMapping[x1][j].setNum(0);

                            MainActivity.getMainActivity().addScore(tileMapping[x][j].getNum());
                            merge = true; //Merging has occured.
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            //if statement that checks and creates a new number.
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeRight(){      //See above Comments from swipeLeft.

        boolean merge = false;

        for (int y = 0; y < GridConfig.ROWS; y++) {
            for (int x = GridConfig.ROWS-1; x >=0; x--) {

                for (int x1 = x-1; x1 >=0; x1--) {
                    if (tileMapping[x1][y].getNum()>0) {

                        if (tileMapping[x][y].getNum()<=0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x1][y], tileMapping[x][y],x1, x, y, y);
                            tileMapping[x][y].setNum(tileMapping[x1][y].getNum());
                            tileMapping[x1][y].setNum(0);

                            x++;
                            merge = true;
                        }else if (tileMapping[x][y].equals(tileMapping[x1][y])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x1][y], tileMapping[x][y],x1, x, y, y);
                            tileMapping[x][y].setNum(tileMapping[x][y].getNum()*2);
                            tileMapping[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(tileMapping[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeUp(){  //See above Comments from swipeLeft.

        boolean merge = false;

        for (int x = 0; x < GridConfig.ROWS; x++) {
            for (int y = 0; y < GridConfig.ROWS; y++) {

                for (int y1 = y+1; y1 < GridConfig.ROWS; y1++) {
                    if (tileMapping[x][y1].getNum()>0) {

                        if (tileMapping[x][y].getNum()<=0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x][y1],tileMapping[x][y], x, x, y1, y);
                            tileMapping[x][y].setNum(tileMapping[x][y1].getNum());
                            tileMapping[x][y1].setNum(0);

                            y--;

                            merge = true;
                        }else if (tileMapping[x][y].equals(tileMapping[x][y1])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x][y1],tileMapping[x][y], x, x, y1, y);
                            tileMapping[x][y].setNum(tileMapping[x][y].getNum()*2);
                            tileMapping[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(tileMapping[x][y].getNum());
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeDown(){  //See above Comments from swipeLeft.

        boolean merge = false;

        for (int x = 0; x < GridConfig.ROWS; x++) {
            for (int y = GridConfig.ROWS-1; y >=0; y--) {

                for (int y1 = y-1; y1 >=0; y1--) {
                    if (tileMapping[x][y1].getNum()>0) {

                        if (tileMapping[x][y].getNum()<=0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x][y1],tileMapping[x][y], x, x, y1, y);
                            tileMapping[x][y].setNum(tileMapping[x][y1].getNum());
                            tileMapping[x][y1].setNum(0);

                            y++;
                            merge = true;
                        }else if (tileMapping[x][y].equals(tileMapping[x][y1])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(tileMapping[x][y1],tileMapping[x][y], x, x, y1, y);
                            tileMapping[x][y].setNum(tileMapping[x][y].getNum()*2);
                            tileMapping[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(tileMapping[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete(){

        boolean complete = true;

        CONDITIONS:
        for (int y = 0; y < GridConfig.ROWS; y++) { //For loops that run through the x and y variables in tile array.

            for (int x = 0; x < GridConfig.ROWS; x++) {

                if (tileMapping[x][y].getNum()==0||
                        (x>0&&tileMapping[x][y].equals(tileMapping[x-1][y]))||
                        (x<GridConfig.ROWS-1&&tileMapping[x][y].equals(tileMapping[x+1][y]))||
                        (y>0&&tileMapping[x][y].equals(tileMapping[x][y-1]))||
                        (y<GridConfig.ROWS-1&&tileMapping[x][y].equals(tileMapping[x][y+1]))) //Checking conditions throughout the array. These conditions are essentially if the user can't move at all.
                {

                    complete = false;
                    break CONDITIONS;
                }
            }
        }

        if (complete) {

            boolean finalCheck = false;

            for(int i =0; i<GridConfig.ROWS; i++){      //Checking though the array for the key tile 2048 that will create the winning case.

                for(int j= 0 ; j<GridConfig.ROWS; j++){

                    if(tileMapping[i][j].getNum() == 2048) {

                        finalCheck = true;

                    }
                }
            }

            if(finalCheck == true){

                //Dialogs that will show if you have won or lost.

                new AlertDialog.Builder(getContext()).setTitle("Purple Nurple").setMessage("You Win!!").setPositiveButton("Restart", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame();
                    }
                }).show();

            }else{

                new AlertDialog.Builder(getContext()).setTitle("Purple Nurple").setMessage("Game Over!").setPositiveButton("Restart", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame();
                    }
                }).show();

            }

        }

    }
}
