package com.example.snakegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePageActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    //list of snake points/snake length
    private final List<SnakePoints> SnakePointsList = new ArrayList<>();
    private SurfaceView SurfaceView;
    private TextView ScoreTv;

    //surface holder to draw snake on surface's canvas
    private SurfaceHolder surfaceHolder;

    //snake moving position. values must be right, left, top, bottom.
    //by default snake move to right
    private String MovingPosition = "right";
    //score
    private int score = 0;
    //snake size/ points size
    //you can change this value to make bigger size snake
    private static final int PointsSize = 28;
    //default snake tale
    private static final int DefaultTalePoints =3;
    //snake color
    private static final int SnakeColor = Color.YELLOW;
    //snake moving speed. value must be lie between 1 - 1000
    private static final int SnakeMovingSpeed = 800;

    //random points position cordinates on the surfaceView
    private int PositionX, PositionY;

    //timer to move snake / change snake position after a specific time(SnakeMovingSpeed)
    private Timer timer;
    //canvas to draw snake and show on surface view
    private Canvas canvas = null;

    //point color / snake point color of a snake
    private Paint PointColor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        //getting surfaceview and score textview from xml file
        SurfaceView = findViewById(R.id.SurfaceView);
        ScoreTv = findViewById(R.id.ScoreTv);

        //getting imagebuttons from cml file
        final AppCompatImageButton topBtn = findViewById(R.id.TopBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.LeftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.RightBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.BottomBtn);

        //adding callback to surfaceview
        SurfaceView.getHolder().addCallback(this);
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if previous moving position in not bottom. snake can't move.
                //for example if snake moving to bottom then snake can't directly start moving to top
                //sanke must take right of left first then top
                if(!MovingPosition.equals("Bottom")){
                    MovingPosition = "Top";
                }

            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!MovingPosition.equals("Right")){
                    MovingPosition = "Left";
                }

            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!MovingPosition.equals("Left")){
                    MovingPosition = "Right";
                }

            }
        });

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!MovingPosition.equals("Top")){
                    MovingPosition = "Bottom";
                }

            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        //when surface is created then get surfaceHolder from it and assign to surfaceHolder
        this.surfaceHolder = surfaceHolder;

        //init data for snake/ surfaceView
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void init(){

        //clear snake points / snake length
        SnakePointsList.clear();

        //set default score as 0
        ScoreTv.setText("0");

        //make score 0
        score = 0;

        //setting default moving position
        MovingPosition = "Right";

        //default snake starting position on the screen
        int StartPositionX = (PointsSize)* DefaultTalePoints;

        //making snake's default length/ points
        for(int i=0; i< DefaultTalePoints; i++){

            //adding points to snake's tale
            SnakePoints snakepoints = new SnakePoints(StartPositionX, PointsSize);
            SnakePointsList.add(snakepoints);

            //increasing value for next points as snake's tale
            StartPositionX = StartPositionX - (PointsSize* 2 );

        }

        //add random point on the screen to be eaten by the snake
        addPoint();

        //start moving snake / start game
        moveSnake();
    }

    private void addPoint(){

        //getting surfaceview width and hight to add point on the surface to be eaten b the snake
        int SurfaceWidth = SurfaceView.getWidth() - (PointsSize* 2);
        int SurfaceHeight = SurfaceView.getHeight() - (PointsSize* 2);

        int randomXPosition = new Random().nextInt(SurfaceWidth / PointsSize);
        int randomYPosition = new Random().nextInt(SurfaceHeight / PointsSize);

        //check if randomXPosition is even or odd value. we need only even number
        if((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition+ 1;

        }

        if((randomYPosition % 2) != 0) {
            randomYPosition = randomYPosition+ 1;
        }

        PositionX = (PointsSize* randomXPosition) + PointsSize;
        PositionY = (PointsSize* randomYPosition) + PointsSize;
    }

    private void moveSnake(){

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //getting head position
                int HeadPositionX = SnakePointsList.get(0).getPositionX();
                int HeadPositionY = SnakePointsList.get(0).getPositionY();

                //check if snake eaten a points
                if(HeadPositionX == PositionX && PositionY == HeadPositionY){

                    //grow snake after eaten points
                    growSnake();

                    //add another random points on the screen
                    addPoint();
                }

                //check which side snake moving
                switch (MovingPosition){
                    case "Right":
                        //move snakes head to right
                        //other points follow snakes head points to move the snake
                        SnakePointsList.get(0).setPositionX(HeadPositionX + (PointsSize* 2));
                        SnakePointsList.get(0).setPositionY(HeadPositionY);
                        break;

                    case "Left":
                        //move snakes head to left
                        //other points follow snakes head points to move the snake
                        SnakePointsList.get(0).setPositionX(HeadPositionX - (PointsSize* 2));
                        SnakePointsList.get(0).setPositionY(HeadPositionY);
                        break;

                    case "Top":
                        //move snakes head to top
                        //other points follow snakes head points to move the snake
                        SnakePointsList.get(0).setPositionX(HeadPositionX);
                        SnakePointsList.get(0).setPositionY(HeadPositionY - (PointsSize* 2));
                        break;

                    case "Bottom":
                        //move snakes head to bottom
                        //other points follow snakes head points to move the snake
                        SnakePointsList.get(0).setPositionX(HeadPositionX);
                        SnakePointsList.get(0).setPositionY(HeadPositionY + (PointsSize* 2));
                        break;
                }

                //check if game over Weather snake touch edges or snake itself
                if(CheckGameOver(HeadPositionX,HeadPositionY)){

                    //stop timer/ stop moving snake
                    timer.purge();
                    timer.cancel();

                    //show game over dialog
                    AlertDialog .Builder builder = new AlertDialog.Builder(GamePageActivity.this);
                    builder.setMessage("Your Score = "+score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //restart game / re-init data
                            init();
                        }
                    });
                    //timer runs in background so we need to show dialog on main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }

                else {

                    //lock canvas on surfaceHolder to draw on it
                    canvas = surfaceHolder.lockCanvas();

                    //clear canvas with white color
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    //change snake's head position. other snake points will follow snake's head
                    canvas.drawCircle(SnakePointsList.get(0).getPositionX(),SnakePointsList.get(0).getPositionY(),PointsSize,CreatePointColor());

                    //draw random points circle on the surface to be eaten by the snake
                    canvas.drawCircle(PositionX,PositionY,PointsSize,CreatePointColor());

                    //other points is following snake's head position head of snake
                    for(int i=1;  i < SnakePointsList.size(); i++){

                        int getTempPositionX = SnakePointsList.get(i).getPositionX();
                        int getTempPositionY = SnakePointsList.get(i).getPositionY();

                        //move points scross the head
                        SnakePointsList.get(i).setPositionX(HeadPositionX);
                        SnakePointsList.get(i).setPositionY(HeadPositionY);
                        canvas.drawCircle(SnakePointsList.get(i).getPositionX(),SnakePointsList.get(i).getPositionY(),PointsSize,CreatePointColor());

                        //change head position
                        HeadPositionX = getTempPositionX;
                        HeadPositionY = getTempPositionY;
                    }

                    //unlock canvas to draw on surfaceview
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000- SnakeMovingSpeed, 1000-SnakeMovingSpeed);
    }

    private void growSnake(){

        //create new snake point
        SnakePoints snakePoints = new SnakePoints(0,0);

        //add point to the snake's tale
        SnakePointsList.add(snakePoints);

        //increase score
        score++;

        //setting score to Textviews
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ScoreTv.setText(String.valueOf(score));
            }
        });
    }
    private boolean CheckGameOver(int HeadPositionX, int HeadPositionY){
        boolean GameOver = false;

        //check if snake's head touches edges
        if(SnakePointsList.get(0).getPositionX()<0 ||
                SnakePointsList.get(0).getPositionY()<0 ||
                SnakePointsList.get(0).getPositionX() >= SurfaceView.getWidth() ||
                SnakePointsList.get(0).getPositionY() >= SurfaceView.getHeight())
        {

            GameOver = true;
        }
        else {

            //check if snake's head touches snake itself
            for(int i=1; i < SnakePointsList.size(); i++){

                if(HeadPositionX == SnakePointsList.get(i).getPositionX() &&
                        HeadPositionY == SnakePointsList.get(i).getPositionY()){
                    GameOver = true;
                    break;
                }
            }
        }


        return GameOver;
    }

    private Paint CreatePointColor(){

        //check if color not define before
        if(PointColor == null) {

            PointColor = new Paint();
            PointColor.setColor(SnakeColor);
            PointColor.setStyle(Paint.Style.FILL);
            PointColor.setAntiAlias(true); //smoothness
        }
            return PointColor;
    }
}