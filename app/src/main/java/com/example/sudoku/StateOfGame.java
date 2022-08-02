package com.example.sudoku;

import android.widget.TextView;

import java.util.ArrayList;

//Это класс в котором содержатся все переменные, использующиеся в программе
//Он был сделан, чтобы было проще их было записать в файл и достать оттуда
public class StateOfGame {

    //Размер игрового поля
    public int max_num = 9;
    //Какая часть от общего количества ячеек изначально заполнена
    public double level = 0.5;
    //Объект, с помощью которого генерируется и потом хранится правильное заполнение поля и то,
    // что пользователь видит в начале игры
    public Table cells;

    //Количества ошибок и подсказок, время, прошедшее с начала игры
    public int mistakes = 0;
    public int hints = 0;
    public long seconds = 0;

    //Запущен ли таймер
    public boolean running = true;

    //Тип игры (каким образом раскрашивать поле)
    public int[] types = new int[6];

    //Установленные пользователем значения настроек
    //Они расставлены в том же порядке, в каком кнопки на экране настроек
    public boolean[] settings = {true, true, true, true, true, true, false, false, false, true, false};

    //Ограничения на ошибки, подсказки и время игры
    public long time_limit = 0;
    public int hints_limit = 0;
    public int mistakes_limit = 0;

    //Режим заметок или обычный
    public boolean note_mode = false;

    //Сколько каких цифр есть на поле
    //Нужно, чтобы убирать те, которые больше не понадобится ставить и возвращать их при необходимости
    public int[] filled_numbers;

    //Все состояния поля
    ArrayList<int[][]> all_fields = new ArrayList<>();

    //Все состояния заметок
    ArrayList<int[][][]> all_notes = new ArrayList<>();

    //Чем заполнять поле
    int chars_mode = 0;
}
