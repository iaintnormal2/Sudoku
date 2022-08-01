package com.example.sudoku;

//разметка с изначальной раскраской поля и текст на нём существуют отдельно,
// это нужно, чтобы для подсветки поля не приходилось рисовать отдельные фоны для синей и красной подсветки
// и её отсутствия, ещё и для кружочка/квадратика/чего-нибудь ещё на фоне (этих фонов и так много
// из-за тех, что нужны для головоломки, где границы между числами, которые отличаются на 1)
// ещё здесь пометки

import static com.example.sudoku.GameActivity.buttons;
import static com.example.sudoku.GameActivity.current_cell;
import static com.example.sudoku.MainActivity.stateOfGame;
import static com.example.sudoku.NoteFieldFragment.cell_size;
import static com.example.sudoku.NoteFieldFragment.text_size;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class FieldFragmentToPlay extends Fragment {

    public static int[][] table_text;

    //Если возможных цифр больше 9, двузначные надо заменить на буквы, а нули заменяются на пустое место
    public static String[] chars= {"  ", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};

    int max_num = stateOfGame.max_num;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        //Массив с Id всех ячеек таблицы
        table_text = new int[max_num][max_num];

        if(savedInstanceState != null){
            stateOfGame.all_fields = new ArrayList<>();
            for(int i = 0; i < savedInstanceState.getInt("changes"); i++) {
                stateOfGame.all_fields.add(new int[max_num][max_num]);
                for (int j = 0; j < max_num; j++) {
                    stateOfGame.all_fields.get(i)[j] = savedInstanceState.getIntArray("current_field" + i + j);
                }
            }
            for(int i = 0; i < max_num; i++){
                table_text[i] = savedInstanceState.getIntArray("cells"+i);
                stateOfGame.cells.question[i] = savedInstanceState.getIntArray("question"+i);
                stateOfGame.cells.field[i] = savedInstanceState.getIntArray("answer"+i);
            }
            buttons = savedInstanceState.getIntArray("buttons");
        }

        //Это рисование таблицы
        //Вложенных цикла не два, потому что надо было разбить поле ещё и на квадраты меньшего размера
        TableLayout view = new TableLayout(getContext());

        for (int i = 0; i < stateOfGame.cells.sqrt; i++) {
            TableRow newRow = new TableRow(getContext());

            for (int j = 0; j < stateOfGame.cells.sqrt_2; j++) {
                TableLayout square = new TableLayout(getContext());
                square.setPadding(1, 1, 1, 1);

                for(int k = 0; k < stateOfGame.cells.sqrt_2; k++){
                    TableRow little_row = new TableRow(getContext());

                    for(int l = 0; l < stateOfGame.cells.sqrt; l++){
                        //Создание ячейки
                        TextView cell = new TextView(getContext());
                        //Установка Id
                        int row = k + stateOfGame.cells.sqrt_2 * i;
                        int col = l + stateOfGame.cells.sqrt * j;
                        cell.setId(row * max_num + col+626);
                        //Добавление в специальный массив
                        table_text[row][col] = cell.getId();
                        //Размерв ячейки
                        cell.setWidth(cell_size);
                        cell.setHeight(cell_size);
                        //Размер и выравнивание ткста
                        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        cell.setTextSize(text_size);
                        //Цвет в зависмости от того, заполняется пользователем или программой
                        if(stateOfGame.cells.question[row][col] != 0){
                            cell.setTextColor(getResources().getColor(R.color.black));
                        }else{
                            cell.setTextColor(getResources().getColor(R.color.dark_blue));
                        }
                        //Установка текста
                        if(stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] >= 0) {
                            cell.setText(chars[stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col]]);
                        }
                        little_row.addView(cell);

                        //обработка нажатия
                        cell.setClickable(true);
                        cell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View cell) {
                                current_cell = (TextView) cell;
                                //Убираем окраску всех ячеек
                                for(int i = 0; i < max_num; i++){
                                    for(int j = 0; j < max_num; j++){
                                        view.findViewById(table_text[i][j]).setBackgroundColor(getResources().getColor(R.color.transparent));
                                    }
                                }
                                //Подсвечиваем серым строку, столбец и квадрат (сли пользователь согласен)
                                if(stateOfGame.settings[0]) {
                                    for (int i = 0; i < max_num; i++) {
                                        view.findViewById(table_text[row][i]).setBackgroundColor(getResources().getColor(R.color.transparent_grey));
                                        view.findViewById(table_text[i][col]).setBackgroundColor(getResources().getColor(R.color.transparent_grey));
                                    }
                                    for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                                        for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                                            view.findViewById(table_text[i][j]).setBackgroundColor(getResources().getColor(R.color.transparent_grey));
                                        }
                                    }
                                }
                                //Посветка одинаковых цифр
                                if(stateOfGame.settings[1]) {
                                    for (int i = 0; i < max_num; i++) {
                                        for (int j = 0; j < max_num; j++) {
                                            if (stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] > 0 && stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[i][j] == stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col]) {
                                                view.findViewById(table_text[i][j]).setBackgroundColor(getResources().getColor(R.color.transparent_light_blue));
                                            }
                                        }
                                    }
                                }
                                //если в ячейке правильная цифра, она пустая или подсвечивать ошибки не нужно, подсвечиваем синим
                                if (Arrays.binarySearch(chars, current_cell.getText().toString()) == stateOfGame.cells.field[row][col] || !stateOfGame.settings[2]
                                        || stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] <= 0) {
                                    cell.setBackgroundColor(getResources().getColor(R.color.transparent_light_blue));
                                }
                                // если цифра неправильная, то подсвечиваем красным
                                else {
                                    cell.setBackgroundColor(getResources().getColor(R.color.transparent_red));
                                }
                            }
                        });
                    }
                    square.addView(little_row);
                }
                newRow.addView(square);
            }

            view.addView(newRow);
        }

        if(savedInstanceState != null){
            current_cell = view.findViewById(savedInstanceState.getInt("current_cell"));
        }

        return view;
    }


    //Сохранение значений всех переменных, чтобы они не обнулились
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        for(int i = 0; i < max_num; i++){
            savedInstanceState.putIntArray(("question"+i), stateOfGame.cells.question[i]);
            savedInstanceState.putIntArray(("answer"+i), stateOfGame.cells.field[i]);
            savedInstanceState.putIntArray(("cells"+i), table_text[i]);
            savedInstanceState.putInt("changes", stateOfGame.all_fields.size());
        }
        for(int i = 0; i < stateOfGame.all_fields.size(); i++) {
            for (int j = 0; j < max_num; j++) {
                savedInstanceState.putIntArray(("current_field" + i + j), stateOfGame.all_fields.get(i)[j]);
            }
        }
        savedInstanceState.putIntArray("buttons", buttons);
    }
}
