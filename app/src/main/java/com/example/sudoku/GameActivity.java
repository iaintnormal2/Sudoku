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
    Button current_button;
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
                        if (stateOfGame.current_field[row][col] > 0 && stateOfGame.current_field[i][j] == stateOfGame.current_field[row][col]) {
                            (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]).setBackgroundColor(getResources().getColor(R.color.transparent_light_blue));
                        }
                    }
                }
            }
            //если в ячейке правильная цифра, она пустая или подсвечивать ошибки не нужно, подсвечиваем синим
            if (Arrays.binarySearch(chars, current_cell.getText().toString()) == stateOfGame.cells.field[row][col] || !stateOfGame.settings[2]
                    || stateOfGame.current_field[row][col] <= 0) {
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
                if (stateOfGame.filled_numbers[i] == max_num) {
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
            (findViewById(buttons[i])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (current_cell != null) {
                        //Если это не одна из тех ячеек, которые изначально заполнены
                        if (current_cell.getCurrentTextColor() == getResources().getColor(R.color.dark_blue)) {

                            current_button = (Button) view;

                            //Положение выбраной ячейки
                            @SuppressLint("ResourceType") int row = (current_cell.getId() - 626) / max_num;
                            @SuppressLint("ResourceType") int col = (current_cell.getId() - 626) % max_num;

                            //Если режим заметок выключен
                            if (!stateOfGame.note_mode) {

                                //Цифр, которые такие же, как выбранная, становится больше
                                stateOfGame.filled_numbers[Integer.parseInt(current_button.getText().toString()) - 1]++;

                                //Если в ячейке что-то было, этого становится меньше
                                if (stateOfGame.current_field[row][col] > 0) {
                                    stateOfGame.filled_numbers[stateOfGame.current_field[row][col] - 1]--;
                                }

                                //Собственно ставим цифру
                                current_cell.setText(current_button.getText());

                                //Замена значений предыдущего состояния поля
                                //Только текущую ячейку менять не выйдет, потому что тогда
                                //не сохранятся предыдущие изменения, и вместо отмены одного действия получится отмена всего
                                for (int i = 0; i < max_num; i++) {
                                    for (int j = 0; j < max_num; j++) {
                                        stateOfGame.previous_field[i][j] = stateOfGame.current_field[i][j];
                                    }
                                }

                                //Если пользователь превысил допустимое количество ошибок, игра завершается его поражением
                                stateOfGame.current_field[row][col] = Integer.parseInt(current_button.getText().toString());
                                if (stateOfGame.current_field[row][col] != stateOfGame.cells.field[row][col]) {
                                    stateOfGame.mistakes++;
                                    if (stateOfGame.settings[7] && stateOfGame.mistakes >= stateOfGame.mistakes_limit) {
                                        finish_game(getResources().getString(R.string.becauseofmistakes), getResources().getString(R.string.gameover),
                                                ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                                    }
                                }

                                //Поскольку после того, как поставили цифры, состояние заметок тоже поменяетя, надо
                                //его запомнить
                                for (int i = 0; i < max_num; i++) {
                                    for (int j = 0; j < max_num; j++) {
                                        for (int k = 0; k < max_num; k++) {
                                            stateOfGame.previous_table_notes[i][j][k] = stateOfGame.table_notes[i][j][k];
                                        }
                                    }
                                }

                                //А в ячейке с нормальной цифрой никаких заметок быть не должно
                                stateOfGame.table_notes[row][col] = new int[max_num];
                                ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(row * max_num + col + 1000)).setText("");

                                //Если в соседних ячейках была такая цифра в виде заметки, она оттуда убирается
                                //Тут по порядку для строки, столбца и квадрата, но принцип один
                                for (int j = 0; j < max_num; j++) {
                                    //Если заметка с такой цифрой есть
                                    if (stateOfGame.table_notes[row][j][Arrays.binarySearch(chars, current_button.getText()) - 1] != 0) {

                                        //Теперь то, что раньше было в ячейке, то есть цифра, которую нажали - это её предыдущее значение
                                        stateOfGame.previous_table_notes[row][j][Arrays.binarySearch(chars, current_button.getText()) - 1] = Arrays.binarySearch(chars, current_button.getText().toString());
                                        //А текущее - 0
                                        stateOfGame.table_notes[row][j][Arrays.binarySearch(chars, current_button.getText()) - 1] = 0;

                                        //Теперь в ячейку ставятся цифры, которые должны быть в качестве заметок
                                        String new_text = "";
                                        for (int k = 0; k < max_num; k++) {
                                            new_text += chars[stateOfGame.table_notes[row][j][k]] + "  ";
                                            if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                                new_text += "\n";
                                            }
                                        }
                                        ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(row * max_num + j + 1000)).setText(new_text);
                                    }

                                    if (stateOfGame.table_notes[j][col][Arrays.binarySearch(chars, current_button.getText()) - 1] != 0) {
                                        stateOfGame.previous_table_notes[j][col][Arrays.binarySearch(chars, current_button.getText()) - 1] = Arrays.binarySearch(chars, current_button.getText().toString());
                                        stateOfGame.table_notes[j][col][Arrays.binarySearch(chars, current_button.getText()) - 1] = 0;
                                        String new_text = "";
                                        for (int k = 0; k < max_num; k++) {
                                            new_text += chars[stateOfGame.table_notes[j][col][k]] + "  ";
                                            if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                                new_text += "\n";
                                            }
                                        }
                                        ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(j * max_num + col + 1000)).setText(new_text);
                                    }
                                }

                                for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                                    for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                                        if (stateOfGame.table_notes[i][j][Arrays.binarySearch(chars, current_button.getText()) - 1] != 0) {
                                            stateOfGame.previous_table_notes[i][j][Arrays.binarySearch(chars, current_button.getText()) - 1] = Arrays.binarySearch(chars, current_button.getText().toString());
                                            stateOfGame.table_notes[i][j][Arrays.binarySearch(chars, current_button.getText()) - 1] = 0;
                                            String new_text = "";
                                            for (int k = 0; k < max_num; k++) {
                                                new_text += chars[stateOfGame.table_notes[i][j][k]] + "  ";
                                                if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                                    new_text += "\n";
                                                }
                                            }
                                            ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(i * max_num + j + 1000)).setText(new_text);
                                        }
                                    }
                                }
                            }
                            //А это для режима заметок
                            else {
                                //Текущее поле теперь предыдущее
                                for (int i = 0; i < max_num; i++) {
                                    for (int j = 0; j < max_num; j++) {
                                        for (int k = 0; k < max_num; k++) {
                                            stateOfGame.previous_table_notes[i][j][k] = stateOfGame.table_notes[i][j][k];
                                        }
                                    }
                                }
                                if (stateOfGame.current_field[row][col] > 0) {
                                    //если в ячейке была цифра, то она становится одной из заметок
                                    stateOfGame.table_notes[row][col][stateOfGame.current_field[row][col] - 1] = stateOfGame.current_field[row][col];
                                }
                                for (int i = 0; i < max_num; i++) {
                                    for (int j = 0; j < max_num; j++) {
                                        stateOfGame.previous_field[i][j] = stateOfGame.current_field[i][j];
                                    }
                                }
                                //чтобы обозначить, что в ячейке не пусто, а написаны заметки
                                stateOfGame.current_field[row][col] = -1;

                                if (stateOfGame.table_notes[row][col][Integer.parseInt(current_button.getText().toString()) - 1] == 0) {
                                    stateOfGame.table_notes[row][col][Integer.parseInt(current_button.getText().toString()) - 1] = Integer.parseInt(current_button.getText().toString());
                                }
                                //если цифра уже была в ячейке, она удаляется
                                else {
                                    stateOfGame.table_notes[row][col][Integer.parseInt(current_button.getText().toString()) - 1] = 0;
                                }
                                String new_text = "";
                                for (int j = 0; j < max_num; j++) {
                                    new_text += chars[stateOfGame.table_notes[row][col][j]] + "  ";
                                    if ((j + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                        new_text += "\n";
                                    }
                                }
                                ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(row * max_num + col + 1000)).setText(new_text);
                                ((TextView) (findViewById(R.id.fragmentContainerView)).findViewById(table_text[row][col])).setText("");
                            }
                            //посветка всех нужных ячеек
                            paint(buttons);
                        }
                        //Если пользователь правильно поставил все цифры, игра завершается победой
                        if (Arrays.deepEquals(stateOfGame.current_field, stateOfGame.cells.field)) {
                            finish_game(getResources().getString(R.string.becauseofsuccess), getResources().getString(R.string.win),
                                    ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                        }
                    }
                    //Если ячейка заполнена с начала игры, менять её нельзя
                    else {
                        Toast.makeText(getApplicationContext(), R.string.already_filled, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //Делаем кнопку видимой
            findViewById(buttons[i]).setVisibility(View.VISIBLE);
        }

        //Кнопка для отмены действия
        FloatingActionButton undo = findViewById(R.id.floatingActionButton4);
        undo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                for (int i = 0; i < max_num; i++) {
                    for (int j = 0; j < max_num; j++) {
                        //Если в ячейке что-то было, а теперь убираем, то надо зафиксировать, что количество цифр уменьшилось
                        if (stateOfGame.current_field[i][j] > 0) {
                            stateOfGame.filled_numbers[stateOfGame.current_field[i][j] - 1]--;
                        }
                        //А если после отмены действия что-то появляется, то этого становится больше
                        if (stateOfGame.previous_field[i][j] > 0) {
                            stateOfGame.filled_numbers[stateOfGame.previous_field[i][j] - 1]++;
                        }

                        //Пользователь мог ткнуть после того, как что-то написал, в другое рандомное место
                        //Или могли измениться заметки сразу в нескольких ячейках, так что всю таблицку надо проверить на изменения
                        if (stateOfGame.current_field[i][j] != stateOfGame.previous_field[i][j]) {

                            stateOfGame.current_field[i][j] = stateOfGame.previous_field[i][j];
                            //Завершение игры при превышении лимита на ошибки
                            if (stateOfGame.current_field[i][j] != stateOfGame.cells.field[i][j] && stateOfGame.current_field[i][j] > 0) {
                                stateOfGame.mistakes++;
                                if (stateOfGame.settings[7] && stateOfGame.mistakes >= stateOfGame.mistakes_limit) {
                                    finish_game(getResources().getString(R.string.becauseofmistakes), getResources().getString(R.string.gameover),
                                            ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                                }
                            }

                            //Если в новой ячейке не заметки, то надо поставить какую-то цифру, а если заметки, оставить пустой
                            if (stateOfGame.previous_field[i][j] >= 0) {
                                ((TextView) (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j])).setText(chars[stateOfGame.previous_field[i][j]] + "");
                            } else {
                                ((TextView) (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j])).setText("");
                            }
                            //Выбранная ячейка теперь та, которая изменилась
                            current_cell = (findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]);
                        }
                        //Запоминаем состояние заметок
                        for (int k = 0; k < max_num; k++) {
                            if (stateOfGame.table_notes[i][j][k] != stateOfGame.previous_table_notes[i][j][k]) {
                                stateOfGame.table_notes[i][j][k] = stateOfGame.previous_table_notes[i][j][k];
                            }
                        }

                        //На ячейку для заметок ставится новый текст
                        String new_text = "";
                        for (int l = 0; l < max_num; l++) {
                            new_text += chars[stateOfGame.table_notes[i][j][l]] + "  ";
                            if ((l + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                new_text += "\n";
                            }
                        }
                        ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(i * max_num + j + 1000)).setText(new_text);
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
                if (current_cell != null) {
                    if (current_cell.getCurrentTextColor() == getResources().getColor(R.color.dark_blue)) {
                        //Положение ячейки, которую надо стереть
                        int row = (current_cell.getId() - 626) / max_num;
                        int col = (current_cell.getId() - 626) % max_num;

                        //Цифр, естественно, стало меньше
                        if (stateOfGame.current_field[row][col] > 0) {
                            stateOfGame.filled_numbers[stateOfGame.current_field[row][col] - 1]--;
                        }

                        //Запоминаем состояние поля и меняем его
                        stateOfGame.previous_field[row][col] = stateOfGame.current_field[row][col];
                        if (stateOfGame.previous_field[row][col] > 0) {
                            for (int i = 0; i < max_num; i++) {
                                stateOfGame.previous_table_notes[row][col][i] = stateOfGame.table_notes[row][col][i];
                            }
                            stateOfGame.table_notes[row][col] = new int[max_num];
                        }
                        stateOfGame.current_field[row][col] = 0;
                        current_cell.setText("");

                        paint(buttons);
                        //Если были заметки, то их тоже стираем
                        ((TextView) (findViewById(R.id.fragmentContainerView3).findViewById(row * max_num + col + 1000))).setText("");
                    }
                }
                //Опять же, стереть можно не все ячейки
                else {
                    Toast.makeText(getApplicationContext(), R.string.already_filled, Toast.LENGTH_LONG).show();
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
                //Если подсказок больше, чем можно, игра завершается
                if (stateOfGame.settings[8] && stateOfGame.hints >= stateOfGame.hints_limit) {
                    finish_game(getResources().getString(R.string.becauseofhints), getResources().getString(R.string.gameover),
                            ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                }
                //Для того, чтобы дать подсказку, можео выбрать пустую яейку или ячейку с ошибкой
                ArrayList<TextView> empty_cells = new ArrayList<>();
                ArrayList<TextView> wrong_cells = new ArrayList<>();
                for (int i = 0; i < max_num; i++) {
                    for (int j = 0; j < max_num; j++) {
                        if (stateOfGame.current_field[i][j] == 0) {
                            empty_cells.add((findViewById(R.id.fragmentContainerView)).findViewById(table_text[i][j]));
                        } else if (stateOfGame.current_field[i][j] != stateOfGame.cells.field[i][j]) {
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
                stateOfGame.previous_field[row][col] = stateOfGame.current_field[row][col];
                stateOfGame.current_field[row][col] = stateOfGame.cells.field[row][col];
                cell_for_hint.setText(stateOfGame.cells.field[row][col] + "");
                paint(buttons);
                ((TextView) (findViewById(R.id.header)).findViewById(R.id.textView6)).setText("" + stateOfGame.hints);
                if (!(Arrays.equals(stateOfGame.table_notes[row][col], new int[max_num]))) {
                    for (int k = 0; k < max_num; k++) {
                        if (stateOfGame.table_notes[row][col][k] != stateOfGame.previous_table_notes[row][col][k]) {
                            stateOfGame.table_notes[row][col][k] = stateOfGame.previous_table_notes[row][col][k];
                        }
                    }
                    stateOfGame.table_notes[row][col] = new int[max_num];
                    ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(row * max_num + col + 1000)).setText("");
                }
                for (int j = 0; j < max_num; j++) {
                    if (stateOfGame.table_notes[row][j][stateOfGame.cells.field[row][col] - 1] != 0) {
                        stateOfGame.previous_table_notes[row][j][stateOfGame.cells.field[row][col] - 1] = stateOfGame.cells.field[row][col];
                        stateOfGame.table_notes[row][j][stateOfGame.cells.field[row][col] - 1] = 0;
                        String new_text = "";
                        for (int k = 0; k < max_num; k++) {
                            new_text += chars[stateOfGame.table_notes[row][j][k]] + "  ";
                            if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                new_text += "\n";
                            }
                        }
                        ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(row * max_num + j + 1000)).setText(new_text);
                    }
                    if (stateOfGame.table_notes[j][col][stateOfGame.cells.field[row][col] - 1] != 0) {
                        stateOfGame.previous_table_notes[j][col][stateOfGame.cells.field[row][col] - 1] = stateOfGame.cells.field[row][col];
                        stateOfGame.table_notes[j][col][stateOfGame.cells.field[row][col] - 1] = 0;
                        String new_text = "";
                        for (int k = 0; k < max_num; k++) {
                            new_text += chars[stateOfGame.table_notes[j][col][k]] + "  ";
                            if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                new_text += "\n";
                            }
                        }
                        ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(j * max_num + col + 1000)).setText(new_text);
                    }
                }
                for (int i = (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2; i < (row / stateOfGame.cells.sqrt_2) * stateOfGame.cells.sqrt_2 + stateOfGame.cells.sqrt_2; i++) {
                    for (int j = (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt; j < (col / stateOfGame.cells.sqrt) * stateOfGame.cells.sqrt + stateOfGame.cells.sqrt; j++) {
                        if (stateOfGame.table_notes[i][j][stateOfGame.cells.field[row][col] - 1] != 0) {
                            stateOfGame.previous_table_notes[i][j][stateOfGame.cells.field[row][col] - 1] = stateOfGame.cells.field[row][col];

                            stateOfGame.table_notes[i][j][stateOfGame.cells.field[row][col] - 1] = 0;
                            String new_text = "";
                            for (int k = 0; k < max_num; k++) {
                                new_text += chars[stateOfGame.table_notes[i][j][k]] + "  ";
                                if ((k + 1) % stateOfGame.cells.sqrt_2 == 0) {
                                    new_text += "\n";
                                }
                            }
                            ((TextView) (findViewById(R.id.fragmentContainerView3)).findViewById(i * max_num + j + 1000)).setText(new_text);
                        }
                    }
                }
                if (Arrays.deepEquals(stateOfGame.current_field, stateOfGame.cells.field)) {
                    finish_game(getResources().getString(R.string.becauseofsuccess), getResources().getString(R.string.win),
                            ((TextView) findViewById(R.id.header).findViewById(R.id.textView3)).getText().toString());
                }
            }
        });


        //Включение и выключение режима заметок
        FloatingActionButton note = findViewById(R.id.floatingActionButton6);
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
}
