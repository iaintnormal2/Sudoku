package com.example.sudoku;

import static com.example.sudoku.MainActivity.stateOfGame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChooseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle SavedInstanceState) {

        super.onCreate(SavedInstanceState);

        setContentView(R.layout.activity_choose);

        //Из предыдущей активности получаем тип игры
        Intent i = getIntent();
        int[] types = i.getExtras().getIntArray("types");

        //И подготавливаем его для передачи следующей активности
        Intent j = new Intent(ChooseActivity.this, GameActivity.class);
        j.putExtra("types", types);

        //обработка нажатий на любую из кнопок выбора уровня
        View.OnClickListener listener1 = new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                EditText input_max_num = findViewById(R.id.editTextNumber);
                try {
                    //Получение стороны поля (она же максимальное число на поле)
                    int max_num = Integer.parseInt(input_max_num.getText().toString());
                    if (max_num > 3 && max_num < 26) {
                        stateOfGame.max_num = max_num;
                        //В зависимости от кнопки выбирается то, какая часть ячеек будет заполнена
                        switch (view.getId()) {
                            case R.id.button2:
                                stateOfGame.level = 0.5;
                                break;
                            case R.id.button3:
                                stateOfGame.level = 0.4;
                                break;
                            case R.id.button4:
                                stateOfGame.level = 0.3;
                                break;
                            case R.id.button5:
                                stateOfGame.level = 0.2;
                                break;
                            default:
                                stateOfGame.level = 0.1;
                        }
                        //Проверяем, будут ли ячейки окрашены тем или иным образом
                        //Как только это становится понятно, уровень можно передавать следующей активности
                        for (int i = 0; i < types.length; i++) {
                            if (types[i] == 1) {
                                j.putExtra("level", stateOfGame.level - 0.1);
                                j.putExtra("max_num", max_num);
                                break;
                            }
                            //Если да, то заполнить надо меньше ячеек
                            else if (i > 0) {
                                j.putExtra("level", stateOfGame.level);
                                j.putExtra("max_num", max_num);
                                break;
                            }
                        }
                        //Теперь можно решить, где какие цифры ставить, и запустить игру
                        stateOfGame.cells = new Table(max_num, stateOfGame.level);

                        stateOfGame.all_fields = new ArrayList<>();

                        stateOfGame.filled_numbers = new int[max_num];

                        stateOfGame.all_fields.add(new int[max_num][max_num]);
                        stateOfGame.all_fields.add(new int[max_num][max_num]);

                        for(int i = 0; i < max_num; i++){
                            for(int j = 0; j < max_num; j++){
                                stateOfGame.all_fields.get(0)[i][j] = stateOfGame.cells.question[i][j];
                                stateOfGame.all_fields.get(1)[i][j] = stateOfGame.cells.question[i][j];
                            }
                        }

                        stateOfGame.all_notes = new ArrayList<>();
                        stateOfGame.all_notes.add(new int[max_num][max_num][max_num]);
                        stateOfGame.all_notes.add(new int[max_num][max_num][max_num]);

                        for (int i = 0; i < max_num; i++) {
                            for (int j = 0; j < max_num; j++) {
                                if (stateOfGame.cells.question[i][j] > 0) {
                                    stateOfGame.filled_numbers[stateOfGame.cells.question[i][j] - 1]++;
                                }
                            }
                        }

                        startActivity(j);
                        finish();
                    }
                    //Поле создастся и при больших значениях, но это может занимать много времени,
                    //И количество символов Юникода ограничено, а двузначные и более числа выглядят не очень
                    else if (max_num > 25) {
                        Toast.makeText(getApplicationContext(), R.string.error1, Toast.LENGTH_LONG).show();
                        input_max_num.setText("25");
                    }
                    //Если пользователь введёт 1, то получается, что тупо надо поставить цифру 1
                    // или ничего не ставить,
                    //а 2 и 3 убраны, потому что при выборе максимальной сложности поле будет пустым
                    else {
                        Toast.makeText(getApplicationContext(), R.string.error2, Toast.LENGTH_LONG).show();
                        input_max_num.setText("4");
                    }
                }
                //Если пользователь оставил поле ввода пустым
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error3, Toast.LENGTH_LONG).show();
                    input_max_num.setText("9");
                }
            }
        };


        Button l1 = findViewById(R.id.button2);
        l1.setOnClickListener(listener1);
        Button l2 = findViewById(R.id.button3);
        l2.setOnClickListener(listener1);
        Button l3 = findViewById(R.id.button4);
        l3.setOnClickListener(listener1);
        Button l4 = findViewById(R.id.button5);
        l4.setOnClickListener(listener1);
        Button l5 = findViewById(R.id.button6);
        l5.setOnClickListener(listener1);

        //Поле ввода для размера таблицы
        EditText size_input = findViewById(R.id.editTextNumber);

        //Обработка нажатия на кнопку для увеличения числа
        View.OnClickListener listener_plus = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = Integer.parseInt(size_input.getText().toString());
                if (size + 1 > 25) {
                    Toast.makeText(getApplicationContext(), R.string.error1, Toast.LENGTH_LONG).show();
                } else {
                    size++;
                    size_input.setText(size + "");
                }
            }
        };

        //Обработка нажатия на кнопку для уменьшения числа
        View.OnClickListener listener_minus = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = Integer.parseInt(size_input.getText().toString());
                if (size - 1 <= 3) {
                    Toast.makeText(getApplicationContext(), R.string.error2, Toast.LENGTH_LONG).show();
                } else {
                    size--;
                    size_input.setText(size + "");
                }
            }
        };


        Button minus = findViewById(R.id.button8);
        minus.setOnClickListener(listener_minus);
        Button plus = findViewById(R.id.button9);
        plus.setOnClickListener(listener_plus);
    }
}
