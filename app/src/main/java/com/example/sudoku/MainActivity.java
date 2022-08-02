package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public static StateOfGame stateOfGame = new StateOfGame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        start();

        //Получение настроек и параметров последней игры
        try{
            String json = "";
            @SuppressLint("SdCardPath") FileInputStream inputStream = openFileInput("GameState.json");
            Scanner in = new Scanner(inputStream);
            json = in.nextLine();
            stateOfGame = (new Gson()).fromJson(json, StateOfGame.class);
        }catch (Exception e){

        }
    }

    @Override
    public void onResume(){
       super.onResume();
        start();
    }

    //Это то, что выполняется и при первом запуске активности, и при возвращении к ней
    //Тупой способ исправить то, что при возвращении на тот же экран не отображается нажатие на ту же
    //радио-кнопку, которая была нажата в прошлый раз
    protected void start(){
        setContentView(R.layout.activity_main);

        //кнопка для продолжения предыдущей игры
        Button continue_game = findViewById(R.id.button7);
        //Если в объекте со всей информацией о состоянии приложения есть информация об игровом поле,
        //это значит, что есть незавершённая игра
        if(stateOfGame.cells != null){
            continue_game.setVisibility(View.VISIBLE);
            continue_game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            continue_game.setVisibility(View.GONE);
        }

        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);

        //Обработчик нажатий на радиокнопки
        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                stateOfGame.types = new int[6];
                switch (view.getId()){
                    case R.id.radioButton1:
                        stateOfGame.types[1] = 1;
                        break;
                    case R.id.radioButton2:
                        stateOfGame.types[3] = 1;
                        break;
                    case R.id.radioButton3:
                        stateOfGame.types[4] = 1;
                        break;
                    case R.id.radioButton4:
                        stateOfGame.types[0] = 1;
                        break;
                }
            }
        };

        //Все радиокнопки
        RadioButton rb = findViewById(R.id.radioButton4);
        rb.setOnClickListener(listener);
        RadioButton rb1 = findViewById(R.id.radioButton1);
        rb1.setOnClickListener(listener);
        RadioButton rb3 = findViewById(R.id.radioButton2);
        rb3.setOnClickListener(listener);
        RadioButton rb4 = findViewById(R.id.radioButton3);
        rb4.setOnClickListener(listener);

        //Кнопка для создания новой игры
        Button start = findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (stateOfGame.types[0] == 1 || stateOfGame.types[1] == 1 || stateOfGame.types[3] == 1 || stateOfGame.types[4] == 1) {
                    intent.putExtra("types", stateOfGame.types);

                    rb.setChecked(false);
                    rb1.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);

                    //Если ещё есть данные предыдущей игры, надо обнулить
                    stateOfGame.seconds = 0;
                    stateOfGame.hints = 0;
                    stateOfGame.mistakes = 0;

                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.nothing_chosen, Toast.LENGTH_LONG).show();
                }
            }
        });

        //Кнопка для перехода в настройки
        FloatingActionButton settings = findViewById(R.id.floatingActionButton3);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);
            }
        });

        //Кнопка для просмотра правил
        FloatingActionButton rules = findViewById(R.id.floatingActionButton9);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent1);
            }
        });
    }

    //Это сохранение всех настроек и параметров последней игры
    @Override
    public void onPause(){
        super.onPause();
        try{
            Gson gson = new Gson();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("GameState.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(gson.toJson(stateOfGame));
            outputStreamWriter.close();
        }catch (Exception e){

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            Gson gson = new Gson();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("GameState.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(gson.toJson(stateOfGame));
            outputStreamWriter.close();
        }catch (Exception e){

        }
    }
}