package com.example.leo_yang.othello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;

public class Main2Activity extends Activity {

    final static int cmNone  = 0;
    final static int cmBlack = -1;
    final static int cmWhite = 1;
    MediaPlayer mediaplayer = new MediaPlayer();

    class ChessTable {

        public int[][] table;
        public int[][][] backup;
        public int[][] hintstable;
        int step =0;

        public final int Rows = 8;
        public final int Cols = 8;

        public boolean hints = false;
        public int side = -1;
        int chessblack = 2;
        int blackback[] = new int[64];
        int whiteback[] = new int[64];
        int chesswhite = 2;
        int ChessOrNot = 0;
        public ChessTable() {

            table = new int[8][8];
            backup = new int[64][8][8];
            hintstable = new int[8][8];
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {

                    table[i][j] = cmNone;
                    hintstable[i][j] = cmNone;

                }
            table[3][3] = cmWhite;
            table[3][4] = cmBlack;
            table[4][3] = cmBlack;
            table[4][4] = cmWhite;
        }


        public void backup() {

            for (int i = 0; i < 8; i++) {
                System.arraycopy(table[i], 0, backup[step][i], 0, 8);
                whiteback[step] = chesswhite;
                blackback[step] = chessblack;

            }


            System.out.println("backupstep:"+step);
            step++;

            if (step == 1){
                Button button;
                button = (Button)findViewById(R.id.undo);
                button.setEnabled(true);
                button.setBackgroundColor(0Xff444444);
            }

        }
        public void undo() {
            if(step == 0){
                return;
            }
            step--;

            for (int i = 0; i < 8; i++) {
                System.arraycopy(backup[step][i], 0, table[i], 0, 8);

            }
            int k,j;
            for (k = 0;k < 8; k++){
                for (j = 0; j < 8; j++){
                    backup[step][j][k] = 0;
                }
            }

            side = side * (-1);
            chessblack = blackback[step];
            chesswhite = whiteback[step];

            if (step == 0){

                Button button;
                button = (Button)findViewById(R.id.undo);
                button.setEnabled(false);
                button.setBackgroundColor(0Xff999999);

            }
        }





    }
    ChessTable board = new ChessTable();
    long time ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button newGame;
        Button undo;
        Button Hints;
        mediaplayer = MediaPlayer.create(this,R.raw.back_music);
        mediaplayer.start();
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaplayer.release();
            }
        });
        ChessTable board = new ChessTable();
        newGame = (Button)findViewById(R.id.newGame);
        undo = (Button)findViewById(R.id.undo);
        Hints = (Button)findViewById(R.id.hints);

        time = System.currentTimeMillis();








    }



    public void onClickForButton(final View v){
        TableLayout table ;
        table = (TableLayout)findViewById(R.id.table);
        ImageButton img_hint;


        mediaplayer = MediaPlayer.create(this,R.raw.button_click);
        mediaplayer.start();
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaplayer.release();
            }
        });





        Resources res = v.getResources();
        String button_name = res.getResourceName(v.getId());
        int place = button_name.lastIndexOf('/');
        String position = button_name.substring(place + 12, place + 14);
        int int_position = Integer.parseInt(position);
        int row = (int_position - 1) / 8;
        int column = (int_position - 1) % 8;
        int side = board.side;
        int PutorNot = PutChess(board,row,column,board.side);
        ImageButton imagebutton ;
        TextView numblack;
        TextView numwhite;
        numblack = (TextView)findViewById(R.id.number_black);
        numwhite = (TextView)findViewById(R.id.number_white);
        ImageView image1;
        image1 = (ImageView)findViewById(R.id.turn);



        if (side != board.side){

            if (side == -1) {
                board.chessblack++;

                image1.setImageResource(R.drawable.white_chess);

            }
            else {
                board.chesswhite++;
                image1.setImageResource(R.drawable.black_chess);
            }

        }


        int a = board.chessblack;
        int b = board.chesswhite;
        String aString = Integer.toString(a);
        String bString = Integer.toString(b);
        numblack.setText(":"+aString);
        numwhite.setText(":"+bString);




        int i=0;
        int j=0;
        for(i=0;i<board.Rows;i++){
            for (j=0;j<board.Cols;j++){

                if (board.table[i][j] == 1){
                    int chess = i * 8 + j;
                    chess ++;
                    String chessString = Integer.toString(chess);
                    imagebutton = (ImageButton)table.findViewWithTag(chessString);
                    imagebutton.setImageResource(R.drawable.white_chess);
                }
                else if (board.table[i][j] == -1){
                    int chess = i * 8 + j;
                    chess ++;
                    String chessString = Integer.toString(chess);
                    imagebutton = (ImageButton)table.findViewWithTag(chessString);
                    imagebutton.setImageResource(R.drawable.black_chess);
                }

            }
        }

        if (board.hints == true){

            for(i=0;i<board.Rows;i++){
                for (j=0;j<board.Cols;j++){
                    if (i == row && j ==column){

                    }
                    else if (board.hintstable[i][j] == 1 ){
                        int chess = i * 8 + j;
                        chess ++;
                        String chessString = Integer.toString(chess);
                        imagebutton = (ImageButton)table.findViewWithTag(chessString);
                        imagebutton.setImageResource(R.drawable.transparent);
                    }


                }
            }



            for (i = 0; i < 8; i++)
                for (j = 0; j < 8; j++) {

                    board.hintstable[i][j] = cmNone;


                }
            for(i=0;i<board.Rows;i++) {
                for (j = 0; j < board.Cols; j++) {
                    if(board.table[i][j] != cmNone)
                        continue;
                    else {
                        int result = CanPutChess(board,i,j,board.side);
                        if (result != 1){
                            continue;
                        }
                        else{
                            board.hintstable[i][j] = 1;

                        }
                    }

                }
            }
            ImageButton imagebutton1;
            if (board.side == 1){
                for(i=0;i<board.Rows;i++){
                    for (j=0;j<board.Cols;j++){
                        if (board.hintstable[i][j] == 1){
                            int chess = i * 8 + j;
                            chess ++;
                            String chessString = Integer.toString(chess);
                            imagebutton1 = (ImageButton)table.findViewWithTag(chessString);
                            imagebutton1.setImageResource(R.drawable.white_chess_t);
                        }


                    }
                }
            }
            else{
                for(i=0;i<board.Rows;i++){
                    for (j=0;j<board.Cols;j++){
                        if (board.hintstable[i][j] == 1){
                            int chess = i * 8 + j;
                            chess ++;
                            String chessString = Integer.toString(chess);
                            imagebutton1 = (ImageButton)table.findViewWithTag(chessString);
                            imagebutton1.setImageResource(R.drawable.black_chess_t);
                        }


                    }
                }
            }



        }


        int putornot = PutorNot(board.side);
        if (putornot == 0){
            Toast toast  ;
            Dialog dialog;
            board.side = board.side * (-1);
            int putornot1 = PutorNot(board.side);
            if (putornot1 == 0){
                board.side = board.side * (-1);

                mediaplayer = MediaPlayer.create(this,R.raw.game_over);
                mediaplayer.start();
                mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaplayer.release();
                    }
                });

                TextView textView;
                textView = (TextView)findViewById(R.id.TurnString);
                textView.setText("Win:");
                time = System.currentTimeMillis() - time;
                SimpleDateFormat sDateFormat    =   new    SimpleDateFormat("mm:ss");
                String str = sDateFormat.format(time);
                if (board.chesswhite > board.chessblack) {


                    image1.setImageResource(R.drawable.white_chess);
                    dialog = new AlertDialog.Builder(this).setIcon(R.drawable.white_chess).setTitle("  Wins").setMessage("whiteChess:" + board.chesswhite + " VS blackChess:" + board.chessblack + "\nTime : " + str).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OnClickForNewGame(v);
                        }
                    }).setNegativeButton("Cancel",null).show();

                }
                else if (board.chesswhite < board.chessblack){
                    dialog = new AlertDialog.Builder(this).setIcon(R.drawable.black_chess).setTitle("  Wins").setMessage("Black Chess:" + board.chessblack + " VS  White Chess:" + board.chesswhite + "\nTime : " + str).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OnClickForNewGame(v);
                        }
                    }).setNegativeButton("Cancel",null).show();
                    image1.setImageResource(R.drawable.black_chess);
                }
                else{
                    dialog = new AlertDialog.Builder(this).setIcon(R.drawable.transparent).setTitle("No One Wins").setMessage("white Chess:" + board.chesswhite + " VS  black Chess:" + board.chessblack + "\nTime : " + str).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OnClickForNewGame(v);
                        }
                    }).setNegativeButton("Cancel",null).show();
                    textView.setText("DRAW:");
                    image1.setImageResource(R.drawable.transparent);
                }

            }



            else if (board.side == -1) {


                image1.setImageResource(R.drawable.black_chess);
                toast = Toast.makeText(getApplicationContext(),
                        "white can't put any chess,Turn to black", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
            else {

                image1.setImageResource(R.drawable.white_chess);
                toast = Toast.makeText(getApplicationContext(),
                        "black can't put any chess,Turn to white", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


        }

    }

    //Listener for Undo Button
    public  void onClickForUndo(View v){
        TableLayout table ;
        table = (TableLayout)findViewById(R.id.table);

        board.undo();





        ImageButton imagebutton;
        ImageView imageview;
        imageview  = (ImageView)findViewById(R.id.turn);
        TextView numblack;
        TextView numwhite;
        numblack = (TextView)findViewById(R.id.number_black);
        numwhite = (TextView)findViewById(R.id.number_white);
        int a = board.chessblack;
        int b = board.chesswhite;
        String aString = Integer.toString(a);
        String bString = Integer.toString(b);
        numblack.setText(":" + aString);
        numwhite.setText(":" + bString);

        //System.out.println("hh" + Arrays.toString(board.table[0]));
       // System.out.println("hh" + Arrays.toString(board.table[1]));
       // System.out.println("hh" + Arrays.toString(board.table[2]));
       // System.out.println("hh" + Arrays.toString(board.table[3]));
       // System.out.println("hh" + Arrays.toString(board.table[4]));
      //  System.out.println("hh" + Arrays.toString(board.table[5]));
       // System.out.println("hh" + Arrays.toString(board.table[6]));
       // System.out.println("hh" + Arrays.toString(board.table[7]));






        int i=0;
        int j=0;
        for(i=0;i<board.Rows;i++){
            for (j=0;j<board.Cols;j++){

                if (board.table[i][j] == 1){
                    int chess = i * 8 + j;
                    chess ++;
                    String chessString = Integer.toString(chess);
                    imagebutton = (ImageButton)table.findViewWithTag(chessString);
                    imagebutton.setImageResource(R.drawable.white_chess);
                }
                else if (board.table[i][j] == -1){
                    int chess = i * 8 + j;
                    chess ++;
                    String chessString = Integer.toString(chess);
                    imagebutton = (ImageButton)table.findViewWithTag(chessString);
                    imagebutton.setImageResource(R.drawable.black_chess);
                }
                else{
                    int chess = i * 8 + j;
                    chess ++;
                    String chessString = Integer.toString(chess);
                    imagebutton = (ImageButton)table.findViewWithTag(chessString);
                    imagebutton.setImageResource(R.drawable.transparent);
                }

            }
        }
        if (board.side == -1) {
            imageview.setImageResource(R.drawable.black_chess);

        }
        else {
            imageview.setImageResource(R.drawable.white_chess);
        }

        if (board.hints == true){
            board.hints = false;
            onClickForHints(v);

        }

    }


    //Listener for Hints Button
    public void  onClickForHints(View v){
        TableLayout table ;
        table = (TableLayout)findViewById(R.id.table);
        Button buttonhints;
        buttonhints = (Button)findViewById(R.id.hints);



        if (!board.hints ){

            buttonhints.setText("HINTS OFF");

            board.hints = true;
            int i=0;
            int j=0;
            for (i = 0; i < 8; i++)
                for (j = 0; j < 8; j++) {

                    board.hintstable[i][j] = cmNone;


                }
            for(i=0;i<board.Rows;i++) {
                for (j = 0; j < board.Cols; j++) {
                    if(board.table[i][j] != cmNone)
                        continue;
                    else {
                        int result = CanPutChess(board,i,j,board.side);
                        if (result != 1){
                            continue;
                        }
                        else{
                            board.hintstable[i][j] = 1;

                        }
                    }

                }
            }
            ImageButton imagebutton;
            if (board.side == 1){
                for(i=0;i<board.Rows;i++){
                    for (j=0;j<board.Cols;j++){
                        if (board.hintstable[i][j] == 1){
                            int chess = i * 8 + j;
                            chess ++;
                            String chessString = Integer.toString(chess);
                            imagebutton = (ImageButton)table.findViewWithTag(chessString);
                            imagebutton.setImageResource(R.drawable.white_chess_t);
                        }


                    }
                }
            }
            else{
                for(i=0;i<board.Rows;i++){
                    for (j=0;j<board.Cols;j++){
                        if (board.hintstable[i][j] == 1){
                            int chess = i * 8 + j;
                            chess ++;
                            String chessString = Integer.toString(chess);
                            imagebutton = (ImageButton)table.findViewWithTag(chessString);
                            imagebutton.setImageResource(R.drawable.black_chess_t);
                        }


                    }
                }
            }



        }

        else{

            buttonhints.setText("HINTS ON");
            board.hints = false;
            int i,j;
            ImageButton imagebutton;

            for(i=0;i<board.Rows;i++){
                for (j=0;j<board.Cols;j++){
                    if (board.hintstable[i][j] == 1){
                            int chess = i * 8 + j;
                            chess ++;
                            String chessString = Integer.toString(chess);
                            imagebutton = (ImageButton)table.findViewWithTag(chessString);
                            imagebutton.setImageResource(R.drawable.transparent);
                    }


                }
            }




        }
    }


    public void OnClickForNewGame(View v){
        mediaplayer = MediaPlayer.create(this,R.raw.back_music);
        mediaplayer.start();
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaplayer.release();
            }
        });


        board = new ChessTable();
        ImageButton imagebutton;

        TableLayout table ;
        table = (TableLayout)findViewById(R.id.table);

        //initial chess board
        int i,j;
        for (i=0;i < board.Rows; i++){
            for (j = 0;j<board.Cols; j++){
                int chess = i * 8 + j;
                chess ++;
                String chessString = Integer.toString(chess);
                imagebutton = (ImageButton)table.findViewWithTag(chessString);
                imagebutton.setImageResource(R.drawable.transparent);

            }
        }

        imagebutton = (ImageButton)findViewById(R.id.imageButton28);
        imagebutton.setImageResource(R.drawable.white_chess);

        imagebutton = (ImageButton)findViewById(R.id.imageButton29);
        imagebutton.setImageResource(R.drawable.black_chess);

        imagebutton = (ImageButton)findViewById(R.id.imageButton36);
        imagebutton.setImageResource(R.drawable.black_chess);

        imagebutton = (ImageButton)findViewById(R.id.imageButton37);
        imagebutton.setImageResource(R.drawable.white_chess);


        //initial game result
        TextView textView;
        textView = (TextView)findViewById(R.id.number_white);
        textView.setText(":2");

        textView = (TextView)findViewById(R.id.number_black);
        textView.setText(":2");

        textView = (TextView)findViewById(R.id.TurnString);
        textView.setText("Turn:");

        ImageView imageView;
        imageView = (ImageView)findViewById(R.id.turn);
        imageView.setImageResource(R.drawable.black_chess);

        Button button;
        button = (Button)findViewById(R.id.undo);
        button.setEnabled(false);
        button.setBackgroundColor(0xff999999);

        board.hints = true;
        onClickForHints(v);



    }



    //decide whether there are positions for putting
    int PutorNot(int side){
        int i=0;
        int j=0;
        for(i=0;i<board.Rows;i++) {
            for (j = 0; j < board.Cols; j++) {
                if(board.table[i][j] != cmNone)
                    continue;
                else {
                    int result = CanPutChess(board,i,j,side);
                    if (result != 1){
                        continue;
                    }
                    else{
                        return 1;
                    }
                }

            }
        }
        return  0;

    }


    //decide whether can player put this chess
    int CanPutChess(ChessTable board,int row,int column,int side) {
        if (board == null)
            return 0;

        int i, j;
        int dirx = -1;
        int diry = -1;
        int k,m;
        int change = 0;
        int count = 0;
        for (dirx = -1; dirx < 2; dirx++) {
            for (diry = -1; diry < 2; diry++) {
                count = 0;
                if (dirx == 0 && diry == 0) continue;
                if (row + diry >= 0 && row + diry < board.Rows && column + dirx >= 0 && column + dirx < board.Cols && board.table[row + diry][column + dirx] == (-1) * side) {
                    for (i = row + diry , j = column + dirx ; i >= 0 && j >= 0 && i < board.Rows && j < board.Cols; i = i + diry, j = j + dirx) {

                        if (board.table[i][j] == (-1) * side) {
                            count++;
                            continue;
                        } else if (board.table[i][j] == side) {
                            return 1;
                        }
                        else break;
                    }
                }
            }
        }
        return 0;
    }

    int PutChess(ChessTable board,int row,int column,int side) {
        if (board == null)
            return 0;
        if (board.table[row][column] != cmNone)
            return 0;

        int tag = 0;
        int i, j;
        int dirx = -1;
        int diry = -1;
        int k,m;
        int change = 0;
        int count = 0;
        for (dirx = -1; dirx < 2; dirx++) {
            for (diry = -1; diry < 2; diry++) {
                count = 0;
                if (dirx == 0 && diry == 0) continue;
                if (row + diry >= 0 && row + diry < board.Rows && column + dirx >= 0 && column + dirx < board.Cols && board.table[row + diry][column + dirx] == (-1) * side) {
                    for (i = row + diry , j = column + dirx ; i >= 0 && j >= 0 && i < board.Rows && j < board.Cols; i = i + diry, j = j + dirx) {

                        if (board.table[i][j] == (-1) * side) {
                            count++;
                            continue;
                        } else if (board.table[i][j] == side) {
                            if (tag == 0){
                                board.backup();
                                tag = 1;
                            }
                            board.table[row][column] = side;
                            board.side = (-1)*side;
                            if(side == -1){
                                board.chessblack += count;
                                board.chesswhite -= count;
                            }
                            else{
                                board.chesswhite += count;
                                board.chessblack -= count;
                            }
                            for(k = row + diry, m = column + dirx; k <= row + count && k >= row - count && m <= column + count && m >= column - count; k += diry,m += dirx)
                            {
                                board.table[k][m] = side;
                            }
                            break;
                        }
                        else break;
                    }
                }
            }


        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        if(mediaplayer != null)
            mediaplayer.release();
        super.onDestroy();
    }





}
