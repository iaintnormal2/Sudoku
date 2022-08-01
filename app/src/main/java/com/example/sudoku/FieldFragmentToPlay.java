package com.example.sudoku;

//разметка с изначальной раскраской поля и текст на нём существуют отдельно,
// это нужно, чтобы для подсветки поля не приходилось рисовать отдельные фоны для синей и красной подсветки
// и её отсутствия, ещё и для кружочка/квадратика/чего-нибудь ещё на фоне (этих фонов и так много
// из-за тех, что нужны для головоломки, где границы между числами, которые отличаются на 1)
// ещё здесь пометки

import static com.example.sudoku.GameActivity.buttons;
import static com.example.sudoku.GameActivity.current_button;
import static com.example.sudoku.GameActivity.current_cell;
import static com.example.sudoku.MainActivity.stateOfGame;
import static com.example.sudoku.NoteFieldFragment.cell_size;
import static com.example.sudoku.NoteFieldFragment.text_size;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
                        //Размер ячейки
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
                                if(stateOfGame.settings[10]){
                                    if(current_button != null) {
                                        if (current_cell.getCurrentTextColor() == getResources().getColor(R.color.dark_blue)) {

                                            //Положение выбраной ячейки
                                            @SuppressLint("ResourceType") int row = (current_cell.getId() - 626) / max_num;
                                            @SuppressLint("ResourceType") int col = (current_cell.getId() - 626) % max_num;

                                            //Если режим заметок выключен
                                            if (!stateOfGame.note_mode) {

                                                //Цифр, которые такие же, как выбранная, становится больше
                                                stateOfGame.filled_numbers[Arrays.binarySearch(chars, current_button.getText().toString()) - 1]++;

                                                //Если в ячейке что-то было, этого становится меньше
                                                if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] > 0) {
                                                    stateOfGame.filled_numbers[stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] - 1]--;
                                                }

                                                //Собственно ставим цифру
                                                current_cell.setText(current_button.getText());

                                                stateOfGame.all_fields.add(new int[max_num][max_num]);
                                                for (int j = 0; j < max_num; j++) {
                                                    for (int k = 0; k < max_num; k++) {
                                                        stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[j][k] = stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 2)[j][k];
                                                    }
                                                }

                                                stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] = Arrays.binarySearch(chars, current_button.getText().toString());

                                                boolean is_mistake = false;
                                                boolean has_zero = false;
                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] <= 0) {
                                                            has_zero = true;
                                                            break;
                                                        }
                                                    }
                                                    if (has_zero) {
                                                        break;
                                                    }
                                                }
                                                //Если пользователь превысил допустимое количество ошибок, игра завершается его поражением
                                                for (int i = 0; i < max_num; i++) {
                                                    if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][col] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] && i != row) {
                                                        stateOfGame.mistakes++;
                                                        is_mistake = true;
                                                        break;
                                                    }
                                                    if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][i] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] && i != col) {
                                                        is_mistake = true;
                                                        stateOfGame.mistakes++;
                                                        break;
                                                    }
                                                }
                                                if (!is_mistake) {
                                                    for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                                                        for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                                                            if (i != row && j != col && stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col]) {
                                                                stateOfGame.mistakes++;
                                                                is_mistake = true;
                                                                break;
                                                            }
                                                            //А если заполнил поле по правилам, то выигрывает
                                                            if (i == (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2 - 1 && j == (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt - 1 && !has_zero) {
                                                                finish_game(getResources().getString(R.string.becauseofsuccess), getResources().getString(R.string.win), ((TextView) view.findViewById(R.id.textView3)).getText().toString());
                                                            }
                                                        }
                                                        if (is_mistake) {
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (stateOfGame.settings[7] && stateOfGame.mistakes >= stateOfGame.mistakes_limit) {
                                                    finish_game(getResources().getString(R.string.becauseofmistakes), getResources().getString(R.string.gameover),
                                                            ((TextView) view.findViewById(R.id.textView3)).getText().toString());
                                                }

                                                stateOfGame.all_notes.add(new int[max_num][max_num][max_num]);
                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        for (int k = 0; k < max_num; k++) {
                                                            stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k] = stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 2)[i][j][k];
                                                        }
                                                    }
                                                }

                                                stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col] = new int[max_num];

                                                if(stateOfGame.settings[9]) {
                                                    for (int i = 0; i < max_num; i++) {
                                                        stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][i][Arrays.binarySearch(chars, current_cell.getText().toString()) - 1] = 0;
                                                        stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][col][Arrays.binarySearch(chars, current_cell.getText().toString())-1] = 0;
                                                    }
                                                    for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                                                        for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                                                            stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][Arrays.binarySearch(chars, current_cell.getText().toString())-1] = 0;
                                                        }
                                                    }
                                                }
                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        String new_text = "";
                                                        for (int k = 0; k < max_num; k++) {
                                                            new_text += chars[stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k]] + "  ";
                                                            if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                                                new_text += "\n";
                                                            }
                                                        }
                                                        ((TextView) getActivity().findViewById(i * max_num + j + 1000)).setText(new_text);
                                                    }
                                                }
                                                //А это для режима заметок
                                            } else {
                                                //Текущее поле теперь предыдущее
                                                stateOfGame.all_notes.add(new int[max_num][max_num][max_num]);
                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        for (int k = 0; k < max_num; k++) {
                                                            stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k] = stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 2)[i][j][k];
                                                        }
                                                    }
                                                }
                                                stateOfGame.all_fields.add(new int[max_num][max_num]);
                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] = stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 2)[i][j];
                                                    }
                                                }
                                                stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] = -1;

                                                if (stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col][Arrays.binarySearch(chars, current_button.getText().toString())-1] == 0) {
                                                    stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col][Arrays.binarySearch(chars, current_button.getText().toString())-1] = Arrays.binarySearch(chars, current_button.getText().toString());
                                                } else {
                                                    stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col][Arrays.binarySearch(chars, current_button.getText().toString())-1] = 0;
                                                }

                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] >= 0) {
                                                            ((TextView) view.findViewById(i * max_num + j + 626)).setText(chars[stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j]]);
                                                        } else {
                                                            ((TextView) view.findViewById(i * max_num + j + 626)).setText("");
                                                        }
                                                    }
                                                }

                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        String new_text = "";
                                                        for (int k = 0; k < max_num; k++) {
                                                            new_text += chars[stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k]] + "  ";
                                                            if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                                                new_text += "\n";
                                                            }
                                                        }
                                                        ((TextView) getActivity().findViewById(R.id.fragmentContainerView3).findViewById(i * max_num + j + 1000)).setText(new_text);
                                                    }
                                                }
                                            }
                                        }//Если ячейка заполнена с начала игры, менять её нельзя
                                        else {
                                            Toast.makeText(getContext(), R.string.already_filled, Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        if (current_cell != null) {
                                            if (current_cell.getCurrentTextColor() == getResources().getColor(R.color.dark_blue)) {
                                                //Положение ячейки, которую надо стереть
                                                @SuppressLint("ResourceType") int row = (current_cell.getId() - 626) / max_num;
                                                @SuppressLint("ResourceType") int col = (current_cell.getId() - 626) % max_num;

                                                //Цифр, естественно, стало меньше
                                                if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] > 0) {
                                                    stateOfGame.filled_numbers[stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] - 1]--;
                                                }

                                                //Запоминаем состояние поля и меняем его
                                                stateOfGame.all_fields.add(new int[max_num][max_num]);
                                                for (int j = 0; j < max_num; j++) {
                                                    for (int k = 0; k < max_num; k++) {
                                                        stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[j][k] = stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 2)[j][k];
                                                    }
                                                }
                                                stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] = 0;
                                                ((TextView) (view.findViewById(row * max_num + col + 626))).setText("");

                                                //Если были заметки, то их тоже стираем
                                                //Поскольку после того, как поставили цифры, состояние заметок тоже поменяетя, надо
                                                //его запомнить
                                                stateOfGame.all_notes.add(new int[max_num][max_num][max_num]);
                                                for (int i = 0; i < max_num; i++) {
                                                    for (int j = 0; j < max_num; j++) {
                                                        for (int k = 0; k < max_num; k++) {
                                                            stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k] = stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 2)[i][j][k];
                                                        }
                                                    }
                                                }
                                                stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col] = new int[max_num];
                                                ((TextView) (getActivity().findViewById(R.id.fragmentContainerView3).findViewById(row * max_num + col + 1000))).setText("");
                                            }
                                        }
                                        //Опять же, стереть можно не все ячейки
                                        else {
                                            Toast.makeText(getContext(), R.string.already_filled, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    //Если пользователь правильно поставил все цифры, игра завершается победой
                                    if (Arrays.deepEquals(stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1), stateOfGame.cells.field)) {
                                        finish_game(getResources().getString(R.string.becauseofsuccess), getResources().getString(R.string.win),
                                                ((TextView) getActivity().findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                                    }
                                }
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

    public void finish_game(String reason, String result, String time){
        Intent intent = new Intent(getContext(), endActivity.class);
        //Передача информации о результатах игры
        intent.putExtra("time", getResources().getString(R.string.time) + time);
        intent.putExtra("mistakes", getResources().getString(R.string.mistakes) + stateOfGame.mistakes);
        intent.putExtra("hints", getResources().getString(R.string.hints) + stateOfGame.hints);
        intent.putExtra("reason", reason);
        intent.putExtra("result", result);
        startActivity(intent);
        getActivity().finish();
    }
}
