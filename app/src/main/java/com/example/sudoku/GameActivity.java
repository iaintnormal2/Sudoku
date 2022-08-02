//Самый здоровенный и старшный кусок кода

package com.example.sudoku;

import static com.example.sudoku.FieldFragmentToPlay.chars;
import static com.example.sudoku.FieldFragmentToPlay.table_text;
import static com.example.sudoku.MainActivity.stateOfGame;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Arrays;

public class GameActivity extends FragmentActivity {
    //кнопка, которая была нажата последней
    public static Button current_button;
    public static TextView current_cell;
    //все кнопки, которые могут быть на экране
    public static int[] buttons;

    int max_num = stateOfGame.max_num;

    @SuppressLint("ResourceType")
    //выделение всех нужных ячеек
    private void paint(int[] buttons) {
        try {
            // столбец и строка выделенной ячейки
            int row = (current_cell.getId() - 626) / max_num;
            int col = (current_cell.getId() - 626) % max_num;
            //убираем подсветку у всех ячеек
            for (int i = 0; i < max_num; i++) {
                for (int j = 0; j < max_num; j++) {
                    (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]).setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
            if (stateOfGame.settings[0]) {
                //подсвечиваем серым строку и столбец
                for (int i = 0; i < max_num; i++) {
                    (findViewById(R.id.fragmentContainerView)).findViewById(table_text[row][i]).setBackgroundColor(getResources().getColor(R.color.transparent_grey));
                    (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][col]).setBackgroundColor(getResources().getColor(R.color.transparent_grey));
                }
                //подсвечиваем серым малый квадрат
                for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                    for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                        (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]).setBackgroundColor(getResources().getColor(R.color.transparent_grey));
                    }
                }
            }
            //подсвечиваем синим ячейки с такой же цифрой, как на выбранной, если она не пустая
            if (stateOfGame.settings[1]) {
                for (int i = 0; i < max_num; i++) {
                    for (int j = 0; j < max_num; j++) {
                        if (stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] > 0 && stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[i][j] == stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col]) {
                            (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]).setBackgroundColor(getResources().getColor(R.color.transparent_light_blue));
                        }
                    }
                }
            }
            boolean is_mistake = false;

            //если в ячейке правильная цифра, она пустая или подсвечивать ошибки не нужно, подсвечиваем синим
            for (int i = 0; i < max_num; i++) {
                if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][col] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] && i != row) {
                    is_mistake = true;
                    break;
                }
                if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][i] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] && i != col) {
                    is_mistake = true;
                    break;
                }
            }
            if (!is_mistake) {
                for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                    for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                        if (i != row && j != col && stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col]) {
                            is_mistake = true;
                            break;
                        }
                    }
                    if (is_mistake) {
                        break;
                    }
                }
            }
            if (!is_mistake || !stateOfGame.settings[2]
                    || stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] <= 0) {
                current_cell.setBackgroundColor(getResources().getColor(R.color.transparent_light_blue));
            }
            // если цифра неправильная, то подсвечиваем красным
            else {
                current_cell.setBackgroundColor(getResources().getColor(R.color.transparent_red));
                ((TextView) (findViewById(R.id.header)).findViewById(R.id.textView4)).setText(stateOfGame.mistakes + "");
            }
            //если какую-то цифру больше не потребуется ставить, то кнопку, на которой она написана, нужно убрать,
            //а если она сначала закончилась, а теперь снова нужна, тогда наоборот
            for (int i = 0; i < stateOfGame.filled_numbers.length; i++) {
                if (stateOfGame.filled_numbers[i] >= max_num) {
                    findViewById(buttons[i]).setClickable(false);
                    findViewById(buttons[i]).setVisibility(View.INVISIBLE);
                } else {
                    findViewById(buttons[i]).setClickable(true);
                    findViewById(buttons[i]).setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onCreate(Bundle SavedInstanceState) {

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_game);

        current_cell = new TextView(this);

        //все кнопки, которые могут быть на экране
        buttons = new int[]{R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5,
                R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_A, R.id.button_B,
                R.id.button_C, R.id.button_D, R.id.button_E, R.id.button_F, R.id.button_G, R.id.button_H,
                R.id.button_I, R.id.button_J, R.id.button_K, R.id.button_L, R.id.button_M, R.id.button_N,
                R.id.button_O, R.id.button_P};

        //нужное количество кнопок становится видимым и устанавливается функция, вызываемая при нажатии
        for (int i = 0; i < max_num; i++) {
            ((Button) findViewById(buttons[i])).setText(chars[stateOfGame.chars_mode][i+1]);
            (findViewById(buttons[i])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    current_button = (Button) view;
                    if(!stateOfGame.settings[10]) {
                        if (current_cell != null) {
                            //Если это не одна из тех ячеек, которые изначально заполнены
                            if (current_cell.getCurrentTextColor() == getResources().getColor(R.color.dark_blue)) {

                                //Положение выбраной ячейки
                                @SuppressLint("ResourceType") int row = (current_cell.getId() - 626) / max_num;
                                @SuppressLint("ResourceType") int col = (current_cell.getId() - 626) % max_num;

                                //Если режим заметок выключен
                                if (!stateOfGame.note_mode) {

                                    //Цифр, которые такие же, как выбранная, становится больше
                                    stateOfGame.filled_numbers[Arrays.binarySearch(chars[stateOfGame.chars_mode], current_button.getText().toString())-1]++;

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

                                    stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] = Arrays.binarySearch(chars[stateOfGame.chars_mode], current_button.getText().toString());

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
                                                    finish_game(getResources().getString(R.string.becauseofsuccess), getResources().getString(R.string.win),
                                                            ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                                                }
                                            }
                                            if (is_mistake) {
                                                break;
                                            }
                                        }
                                    }
                                    if (stateOfGame.settings[7] && stateOfGame.mistakes >= stateOfGame.mistakes_limit) {
                                        finish_game(getResources().getString(R.string.becauseofmistakes), getResources().getString(R.string.gameover),
                                                ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
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
                                            stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][i][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_cell.getText().toString())-1] = 0;
                                            stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][col][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_cell.getText().toString())-1] = 0;
                                        }
                                        for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                                            for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                                                stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_cell.getText().toString())-1] = 0;
                                            }
                                        }
                                    }

                                    for (int i = 0; i < max_num; i++) {
                                        for (int j = 0; j < max_num; j++) {
                                            String new_text = "";
                                            for (int k = 0; k < max_num; k++) {
                                                new_text += chars[stateOfGame.chars_mode][stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k]] + "  ";
                                                if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                                    new_text += "\n";
                                                }
                                            }
                                            ((TextView) findViewById(R.id.fragmentContainerView3).findViewById(i * max_num + j + 1000)).setText(new_text);
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

                                    if (stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_button.getText().toString())-1] == 0) {
                                        stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_button.getText().toString())-1] = Arrays.binarySearch(chars[stateOfGame.chars_mode], current_button.getText().toString());
                                    } else {
                                        stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][col][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_button.getText().toString())-1] = 0;
                                    }

                                    for (int i = 0; i < max_num; i++) {
                                        for (int j = 0; j < max_num; j++) {
                                            if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] >= 0) {
                                                ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).setText(chars[stateOfGame.chars_mode][stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j]]);
                                            } else {
                                                ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).setText("");
                                            }
                                        }
                                    }

                                    for (int i = 0; i < max_num; i++) {
                                        for (int j = 0; j < max_num; j++) {
                                            String new_text = "";
                                            for (int k = 0; k < max_num; k++) {
                                                new_text += chars[stateOfGame.chars_mode][stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k]] + "  ";
                                                if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                                    new_text += "\n";
                                                }
                                            }
                                            ((TextView) findViewById(R.id.fragmentContainerView3).findViewById(i * max_num + j + 1000)).setText(new_text);
                                        }
                                    }
                                }
                            }//Если ячейка заполнена с начала игры, менять её нельзя
                            else {
                                Toast.makeText(getApplicationContext(), R.string.already_filled, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        for(int i = 0; i < max_num; i++){
                            ((TextView) findViewById(buttons[i])).setTextColor(getResources().getColor(R.color.white));
                        }
                        current_button.setTextColor(getResources().getColor(R.color.dark_blue));
                    }
                    //посветка всех нужных ячеек
                    paint(buttons);
                }
            });
            //Делаем кнопку видимой
            findViewById(buttons[i]).setVisibility(View.VISIBLE);
        }

        current_button = findViewById(buttons[0]);
        current_button.setTextColor(getResources().getColor(R.color.dark_blue));

        //Кнопка для отмены действия
        FloatingActionButton undo = findViewById(R.id.floatingActionButton4);
        undo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                if(stateOfGame.all_fields.size() > 1) {

                    stateOfGame.all_fields.remove(stateOfGame.all_fields.size() - 1);

                    for (int i = 0; i < max_num; i++) {
                        for (int j = 0; j < max_num; j++) {
                            if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] >= 0) {
                                try {
                                    if(Arrays.binarySearch(chars[stateOfGame.chars_mode], ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).getText().toString()) > 0){
                                        stateOfGame.filled_numbers[Arrays.binarySearch(chars[stateOfGame.chars_mode], ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).getText().toString())-1]--;
                                    }
                                }catch(NumberFormatException e){

                                }
                                ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).setText(chars[stateOfGame.chars_mode][stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j]]);
                                try{
                                    if(Arrays.binarySearch(chars[stateOfGame.chars_mode], ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).getText().toString()) > 0) {
                                        stateOfGame.filled_numbers[Arrays.binarySearch(chars[stateOfGame.chars_mode], ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).getText().toString()) - 1]++;
                                    }
                                }catch(NumberFormatException e){

                                }
                            } else {
                                ((TextView) findViewById(R.id.fragmentContainerView).findViewById(i * max_num + j + 626)).setText("");
                            }
                        }
                    }
                }

                if(stateOfGame.all_notes.size() > 1){
                    stateOfGame.all_notes.remove(stateOfGame.all_notes.size()-1);
                    for(int i = 0; i < max_num; i++){
                        for(int j = 0; j < max_num; j++){
                            String new_text = "";
                            for(int k = 0; k < max_num; k++){
                                new_text += chars[stateOfGame.chars_mode][stateOfGame.all_notes.get(stateOfGame.all_notes.size()-1)[i][j][k]] + "  ";
                                if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                    new_text += "\n";
                                }
                            }
                            ((TextView) findViewById(R.id.fragmentContainerView3).findViewById(i * max_num + j + 1000)).setText(new_text);
                        }
                    }
                }

                //Красим поле
                paint(buttons);
            }
        });

        //Ластик
        FloatingActionButton reset = findViewById(R.id.floatingActionButton5);
        reset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                for(int i = 0; i < max_num; i++){
                    ((TextView) findViewById(buttons[i])).setTextColor(getResources().getColor(R.color.white));
                }
                if(!stateOfGame.settings[10]) {
                    if (current_cell != null) {
                        if (current_cell.getCurrentTextColor() == getResources().getColor(R.color.dark_blue)) {
                            //Положение ячейки, которую надо стереть
                            int row = (current_cell.getId() - 626) / max_num;
                            int col = (current_cell.getId() - 626) % max_num;

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
                            ((TextView) (findViewById(R.id.fragmentContainerView).findViewById(row * max_num + col + 626))).setText("");


                            paint(buttons);
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
                            ((TextView) (findViewById(R.id.fragmentContainerView3).findViewById(row * max_num + col + 1000))).setText("");
                        }
                    }
                    //Опять же, стереть можно не все ячейки
                    else {
                        Toast.makeText(getApplicationContext(), R.string.already_filled, Toast.LENGTH_LONG).show();
                    }
                }else {
                    current_button = null;
                }
            }
        });

        //Кнопка для подсказки
        //И последняя страшная функцция в этом файле
        FloatingActionButton hint = findViewById(R.id.floatingActionButton7);
        hint.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                stateOfGame.hints++;
                ((TextView) findViewById(R.id.header).findViewById(R.id.textView6)).setText(stateOfGame.hints+"");

                //Если подсказок больше, чем можно, игра завершается
                if (stateOfGame.settings[8] && stateOfGame.hints >= stateOfGame.hints_limit) {
                    finish_game(getResources().getString(R.string.becauseofhints), getResources().getString(R.string.gameover),
                            ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                }
                //Для того, чтобы дать подсказку, можно выбрать пустую ячейку или ячейку с ошибкой
                ArrayList<TextView> empty_cells = new ArrayList<>();
                ArrayList<TextView> wrong_cells = new ArrayList<>();
                for (int i = 0; i < max_num; i++) {
                    for (int j = 0; j < max_num; j++) {
                        if (stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[i][j] == 0) {
                            empty_cells.add((findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]));
                        } else if (stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[i][j] != stateOfGame.cells.field[i][j]) {
                            wrong_cells.add((findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]));
                        }
                    }
                }
                TextView cell_for_hint;
                //Если есть ошибки, в первую очередь исправляем их
                if (wrong_cells.size() != 0) {
                    cell_for_hint = wrong_cells.get((int) (Math.random() * wrong_cells.size()));
                } else {
                    cell_for_hint = empty_cells.get((int) (Math.random() * empty_cells.size()));
                }
                //Тепрь выбранная ячейка та, где подсказка
                current_cell = cell_for_hint;

                //Дальше делается всё то же самое, что при вводе
                int row = (cell_for_hint.getId() - 626) / max_num;
                int col = (cell_for_hint.getId() - 626) % max_num;

                //Цифр, которые такие же, как выбранная, становится больше
                stateOfGame.filled_numbers[stateOfGame.cells.field[row][col] - 1]++;

                //Если в ячейке что-то было, этого становится меньше
                if (stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] > 0) {
                    stateOfGame.filled_numbers[stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] - 1]--;
                }

                //Собственно ставим цифру
                current_cell.setText(chars[stateOfGame.chars_mode][stateOfGame.cells.field[row][col]]);

                stateOfGame.all_fields.add(new int[max_num][max_num]);
                for(int j = 0; j < max_num; j++){
                    for(int k = 0; k < max_num; k++){
                        stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[j][k] = stateOfGame.all_fields.get(stateOfGame.all_fields.size()-2)[j][k];
                    }
                }

                stateOfGame.all_fields.get(stateOfGame.all_fields.size()-1)[row][col] = stateOfGame.cells.field[row][col];

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
                        stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[row][i][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_cell.getText().toString()) - 1] = 0;
                        stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][col][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_cell.getText().toString()) - 1] = 0;
                    }
                    for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                        for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                            stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][Arrays.binarySearch(chars[stateOfGame.chars_mode], current_cell.getText().toString()) - 1] = 0;
                        }
                    }
                }

                stateOfGame.all_notes.get(stateOfGame.all_notes.size()-1)[row][col] = new int[max_num];

                for(int i = 0; i < max_num; i++){
                    for(int j = 0; j < max_num; j++){
                        String new_text = "";
                        for(int k = 0; k < max_num; k++){
                            new_text += chars[stateOfGame.chars_mode][stateOfGame.all_notes.get(stateOfGame.all_notes.size() - 1)[i][j][k]]+"  ";
                            if((k+1) % stateOfGame.cells.sqrt_2 == 0){
                                new_text+="\n";
                            }
                        }
                        ((TextView) findViewById(R.id.fragmentContainerView3).findViewById(i*max_num + j + 1000)).setText(new_text);
                    }
                }
                boolean is_mistake = false;
                boolean has_zero = false;
                for(int i = 0; i < max_num; i++){
                    for(int j = 0; j < max_num; j++){
                        if(stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] <= 0){
                            has_zero = true;
                            break;
                        }
                    }
                    if(has_zero){
                        break;
                    }
                }
                for(int i = 0; i < max_num; i++) {
                    if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][col] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] && i != row) {
                        is_mistake = true;
                        break;
                    }
                    if (stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][i] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col] && i != col) {
                        is_mistake = true;
                        break;
                    }
                }
                if(!is_mistake) {
                    for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                        for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                            if (i != row && j != col && stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[i][j] == stateOfGame.all_fields.get(stateOfGame.all_fields.size() - 1)[row][col]) {
                                is_mistake = true;
                                break;
                            }
                            //А если заполнил поле по правилам, то выигрывает
                            if (i == (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2-1 && j == (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt-1 && !has_zero){
                                finish_game(getResources().getString(R.string.becauseofsuccess), getResources().getString(R.string.win),
                                        ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                            }
                        }
                        if(is_mistake){
                            break;
                        }
                    }
                }
                paint(buttons);
            }
        });


        //Включение и выключение режима заметок
        FloatingActionButton note = findViewById(R.id.floatingActionButton6);
        if (stateOfGame.note_mode) {
            note.setImageResource(R.drawable.pencil_on);
        } else {
            note.setImageResource(R.drawable.pencil);
        }
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateOfGame.note_mode = !stateOfGame.note_mode;
                if (stateOfGame.note_mode) {
                    ((FloatingActionButton) view).setImageResource(R.drawable.pencil_on);
                } else {
                    ((FloatingActionButton) view).setImageResource(R.drawable.pencil);
                }
            }
        });
    }

    //функция для завершения игры
    public void finish_game(String reason, String result, String time){
        Intent intent = new Intent(GameActivity.this, endActivity.class);
        //Передача информации о результатах игры
        intent.putExtra("time", getResources().getString(R.string.time) + time);
        intent.putExtra("mistakes", getResources().getString(R.string.mistakes) + stateOfGame.mistakes);
        intent.putExtra("hints", getResources().getString(R.string.hints) + stateOfGame.hints);
        intent.putExtra("reason", reason);
        intent.putExtra("result", result);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!stateOfGame.settings[10]) {
            for(int i = 0; i < max_num; i++){
                ((TextView) findViewById(buttons[i])).setTextColor(getResources().getColor(R.color.white));
            }
        }
    }
}
