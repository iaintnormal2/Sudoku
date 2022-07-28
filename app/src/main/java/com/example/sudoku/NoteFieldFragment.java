package com.example.sudoku;

import static com.example.sudoku.MainActivity.stateOfGame;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NoteFieldFragment extends Fragment {

    public static String[] chars= {"  ", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};
    public static int cell_size;
    public static float text_size;
    int max_num = stateOfGame.max_num;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            cell_size = (int) (((getResources().getConfiguration().screenWidthDp - 16)*getResources().getDisplayMetrics().density)/ (max_num+1));
            text_size = (float) (getResources().getConfiguration().screenWidthDp/(max_num+3));
        }else{
            cell_size = (int) (((getResources().getConfiguration().screenHeightDp - 8)*getResources().getDisplayMetrics().density)/ (max_num+1));
            text_size = (float) (getResources().getConfiguration().screenHeightDp/(max_num+3));
        }

        TableLayout view = new TableLayout(getContext());
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        view.setGravity(View.TEXT_ALIGNMENT_GRAVITY);

        if(stateOfGame.table_notes == null){
            stateOfGame.table_notes= new int[max_num][max_num][max_num];
        }
        if(stateOfGame.previous_table_notes == null){
            stateOfGame.previous_table_notes= new int[max_num][max_num][max_num];
        }

        if(savedInstanceState != null) {
            for (int i = 0; i < max_num; i++) {
                for (int j = 0; j < max_num; j++) {
                    stateOfGame.table_notes[i][j] = savedInstanceState.getIntArray("table_notes"+i+j);
                    stateOfGame.previous_table_notes[i][j] = savedInstanceState.getIntArray("previous_table_notes"+i+j);
                }
            }
        }

        for (int i = 0; i < stateOfGame.cells.sqrt; i++) {
            TableRow newRow = new TableRow(getContext());

            for (int j = 0; j < stateOfGame.cells.sqrt_2; j++) {
                TableLayout square = new TableLayout(getContext());
                square.setBackground(getResources().getDrawable(R.drawable.wide_border));
                square.setPadding(1, 1, 1, 1);

                for(int k = 0; k < stateOfGame.cells.sqrt_2; k++){
                    TableRow little_row = new TableRow(getContext());

                    for(int l = 0; l < stateOfGame.cells.sqrt; l++){
                        TextView little_square = new TextView(getContext());
                        little_square.setHeight(cell_size);
                        little_square.setWidth(cell_size);
                        little_square.setId((k + stateOfGame.cells.sqrt_2 * i) * max_num + l + stateOfGame.cells.sqrt * j + 1000);
                        little_square.setTextSize(text_size/stateOfGame.cells.sqrt_2);
                        little_square.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        little_square.setTextColor(getResources().getColor(R.color.gray));

                        int row = k + stateOfGame.cells.sqrt_2 * i;
                        int col = l + stateOfGame.cells.sqrt * j;

                        String text = "";
                        for(int m = 0; m < max_num; m++){
                            text += chars[stateOfGame.table_notes[row][col][m]]+"  ";
                            if((m+1)% stateOfGame.cells.sqrt_2 == 0){
                                text+="\n";
                            }
                        }
                        little_square.setText(text);

                        if(stateOfGame.types[1] == 1) {
                            if (stateOfGame.cells.field[row][col] % 2 == 0) {
                                little_square.setBackground(getResources().getDrawable(R.drawable.even));
                            } else {
                                little_square.setBackground(getResources().getDrawable(R.drawable.border));
                            }
                        }else if(stateOfGame.types[3]==1){
                            int[] delta = new int[4];
                            if(row > 0) {
                                delta[0] = Math.abs(stateOfGame.cells.field[row - 1][col] - stateOfGame.cells.field[row][col]);
                            }
                            if(row < max_num-1){
                                delta[2] = Math.abs(stateOfGame.cells.field[row + 1][col] - stateOfGame.cells.field[row][col]);
                            }
                            if(col < max_num-1){
                                delta[1] = Math.abs(stateOfGame.cells.field[row][col+1] - stateOfGame.cells.field[row][col]);
                            }
                            if(col > 0){
                                delta[3] = Math.abs(stateOfGame.cells.field[row][col-1] - stateOfGame.cells.field[row][col]);
                            }
                            if(delta[0] == 1 && delta[1] == 1 && delta[2] == 1 && delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.udrl));
                            }
                            else if(delta[0] == 1 && delta[1] == 1 && delta[2] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.udr));
                            }
                            else if(delta[0] == 1 && delta[2] == 1 && delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.udl));
                            }
                            else if(delta[0] == 1 && delta[1] == 1 && delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.url));
                            }
                            else if(delta[1] == 1 && delta[2] == 1 && delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.lrd));
                            }
                            else if(delta[0] == 1 && delta[1] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.ur));
                            }
                            else if(delta[0] == 1 && delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.ul));
                            }
                            else if(delta[2] == 1 && delta[1] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.dr));
                            }
                            else if(delta[2] == 1 && delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.dl));
                            }
                            else if(delta[0] == 1 && delta[2] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.ud));
                            }
                            else if(delta[1] == 1 && delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.rl));
                            }
                            else if(delta[0] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.u));
                            }
                            else if(delta[1] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.r));
                            }
                            else if(delta[2] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.d));
                            }
                            else if(delta[3] == 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.l));
                            }else{
                                little_square.setBackground(getResources().getDrawable(R.drawable.border));
                            }
                        }else if(stateOfGame.types[4] == 1){
                            if(stateOfGame.cells.field[row][col] < max_num/3 + 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.from1to3));
                            }else if(stateOfGame.cells.field[row][col] < (max_num/3)*2 + 1){
                                little_square.setBackground(getResources().getDrawable(R.drawable.from4to6));
                            }else{
                                little_square.setBackground(getResources().getDrawable(R.drawable.border));
                            }
                        }else{
                            little_square.setBackground(getResources().getDrawable(R.drawable.border));
                        }
                        little_row.addView(little_square);
                    }
                    square.addView(little_row);
                }
                newRow.addView(square);
            }

            view.addView(newRow);
        }
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        for(int i = 0; i < max_num; i++){
            for(int j = 0; j < max_num; j++){
                savedInstanceState.putIntArray("table_notes"+i+j, stateOfGame.table_notes[i][j]);
                savedInstanceState.putIntArray("previous_table_notes"+i+j, stateOfGame.previous_table_notes[i][j]);
            }
        }
    }
}