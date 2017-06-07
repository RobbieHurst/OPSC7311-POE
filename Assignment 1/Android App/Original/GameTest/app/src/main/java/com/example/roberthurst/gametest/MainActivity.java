package com.example.roberthurst.gametest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends Activity {

    //Declaration of variables that is used for shared preferences and the Creation of the Grid and Animation objects.
    public static final String SP_KEY_BEST_SCORE = "bestScore";
    public static final String SP_KEY_Game = "Game";
    private static MainActivity mainActivity = null;

    private int score = 0;
    private TextView currentScore,bestScore;
    private LinearLayout linlayout = null;
    private Button btnNewGame;
    private Button btnHelp;
    private Game game;
    private GridAnimation animLayer = null;


    public MainActivity() {
        mainActivity = this;
    }


    //On create method that will create and check for saved states of the game.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creation of shared preferences object.
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();     //GSON object that is used to serialize and create a JSON file that will save the objects of the grid.

        String json = preferences.getString(SP_KEY_Game,"");

        if(json == ""){     //If the JSON is empty then create a new Game.

            game = (Game) findViewById(R.id.gameView);          //Getting the Game Object from the activity Layout. That
            game.setBackgroundColor(Color.parseColor("#80158c"));

        }
        else{

            game = gson.fromJson(json, Game.class);
            json = "";
            //game.setBackgroundColor(Color.parseColor("#80158c"));

        }

        //Looking for the Linear Layout and gaining the object.
        linlayout = (LinearLayout) findViewById(R.id.container);
        linlayout.setBackgroundColor(Color.parseColor("#80158c"));

        //Getting the various objects from the XML.
        currentScore = (TextView) findViewById(R.id.tvScore);
        bestScore = (TextView) findViewById(R.id.tvBestScore);

        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            game.startGame();
        }});

        btnHelp = (Button) findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Creation of the help Dialog button.
                new AlertDialog.Builder(mainActivity).setTitle("Help").setMessage("Swipe in four directions. Up, Down, Left, Right. To shift the colors and merge the same color into itself. The aim of the game, is to get to the last color. Which is a very dark purple. Enjoy Purple Nurple!").setPositiveButton("OK!!!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        //Gaining the animiation layer from the XML.
        animLayer = (GridAnimation) findViewById(R.id.gridAnim);
    }

    @Override
    public void onBackPressed(){

        saveGame(game); //Saving the game in Preferences.

    }
    public void saveGame(Game tempGame){        //Saving method.


        Editor editor = getPreferences(MODE_PRIVATE).edit();    //Creation of editor that will save the JSON to a specific key names SP_KEY_GAME.
        Gson gson = new Gson();                                 //This will be used to restore the game.
        String json = gson.toJson(tempGame);
        editor.putString(SP_KEY_Game,json);
        editor.commit();

    }
    public void clearScore(){       //Method that wil clear the score.
        score = 0;
        showScore();
    }

    public void showScore(){
        currentScore.setText(score+"");    //Method that will show the score.
    }


    public void addScore(int s){        //Method that will add the current score to the score.
        score+=s;
        showScore();

        int maxScore = Math.max(score, getBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }

    public void saveBestScore(int s){
        Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putInt(SP_KEY_BEST_SCORE, s);
        editor.commit();
    }

    public int getBestScore(){
        return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
    }

    public void showBestScore(int s){
        bestScore.setText(s+"");
    }

    public GridAnimation getAnimLayer() {
        return animLayer;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

}
