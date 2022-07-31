package com.example.sudoku;

import static com.example.sudoku.MainActivity.stateOfGame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.OutputStreamWriter;
import java.util.Locale;

public class HeaderFragment extends Fragment {

    ImageButton start;

    boolean shutup = false;

    //Это, по сути, сам фрагмент, то, как он выглядит
    View view;

    public static final Handler handler = new Handler();

    //Обработка нажатия на кнопку паузы
    View.OnClickListener listener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            stateOfGame.running = false;
            Intent intent = new Intent(getContext(), PauseActivity.class);
            startActivity(intent);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.header_fragment, container, false);

        start = view.findViewById(R.id.floatingActionButton);

        //Установка значений времени, ошибок и подсказок предыдущей игры (если таковая имеется)
        long hours = stateOfGame.seconds / 3600 ;
        long minutes = (stateOfGame.seconds % 3600 ) / 60 ;
        long secs = stateOfGame.seconds % 60 ;

        //Запуск таймера
        stateOfGame.running = true;
        runTimer();

        start.setOnClickListener(listener2);

        //Если телефон, допустим, повернули, то все переменные должны обнулиться, поэтому дальше есть функция,
        // которая сохраняет их значения, а это их получение
        if (savedInstanceState != null ) {
            stateOfGame.seconds = savedInstanceState.getLong( "seconds");
            stateOfGame.running = savedInstanceState.getBoolean( "running" );
        }

        String time = String.format(Locale.getDefault(), "%d:%02d:%02d" , hours, minutes, secs);
        ((TextView) view.findViewById(R.id.textView3)).setText(time);
        ((TextView) view.findViewById(R.id.textView4)).setText(stateOfGame.mistakes+"");
        ((TextView) view.findViewById(R.id.textView6)).setText(stateOfGame.hints+"");

        //Переход в настройки
        FloatingActionButton settings = view.findViewById(R.id.floatingActionButton2);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent1);
            }
        });

        //Если пользватель решил не смотреть на количество своих ошибок,
        //текстовое поле для их вывода становится невидимым
        TextView mistakes_output = view.findViewById(R.id.textView4);
        if(!stateOfGame.settings[4]){
            mistakes_output.setVisibility(View.INVISIBLE);
        }

        //А если ему не нужно количество подсказок, то
        //поле для их вывода тоже нужно скрыть
        TextView hints_output = view.findViewById(R.id.textView6);
        if(!stateOfGame.settings[5]){
            hints_output.setVisibility(View.INVISIBLE);
        }

        //Если пользователь совсем обнаглел, и ему не нужен таймер,
        //то и его убираем
        TextView time_output = view.findViewById(R.id.textView3);
        if(!stateOfGame.settings[3]){
            time_output.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    //Собственно сохранение значений времени и состояния таймера
    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState)
    {
        savedInstanceState.putLong( "seconds" , stateOfGame.seconds);
        savedInstanceState.putBoolean( "running" , stateOfGame.running);
    }

    //Функция для того, чтобы ставить таймер на паузу
    @Override
    public void onPause()
    {
        super .onPause();
        stateOfGame.running = false;
        System.out.println("ok");
    }

    //Продолжение осчёта времени
    @Override
    public void onResume()
    {
        super .onResume();
        stateOfGame.running = true ;
        //Если пользватель решил не смотреть на количество своих ошибок,
        //текстовое поле для их вывода становится невидимым
        TextView mistakes_output = view.findViewById(R.id.textView4);
        if(!stateOfGame.settings[4]){
            mistakes_output.setVisibility(View.INVISIBLE);
        }else{
            mistakes_output.setVisibility(View.VISIBLE);
        }

        //А если ему не нужно количество подсказок, то
        //поле для их вывода тоже нужно скрыть
        TextView hints_output = view.findViewById(R.id.textView6);
        if(!stateOfGame.settings[5]){
            hints_output.setVisibility(View.INVISIBLE);
        }else{
            hints_output.setVisibility(View.VISIBLE);
        }

        //Если пользователь совсем обнаглел, и ему не нужен таймер,
        //то и его убираем
        TextView time_output = view.findViewById(R.id.textView3);
        if(!stateOfGame.settings[3]){
            time_output.setVisibility(View.INVISIBLE);
        }else{
            time_output.setVisibility(View.VISIBLE);
        }
    }

    //Сам отсчёт времени
    private void runTimer()
    {
        final TextView timeView = (TextView) view.findViewById(R.id.textView3);

        //Runnable - это отдельный поток
        handler.post( new Runnable() {
            @Override
            public void run()
            {
                //Если игра не завершена, то есть таймер не вырублен
                if(!shutup) {

                    //Перевод только секунд в часы, миинуты и секунды
                    long hours = stateOfGame.seconds / 3600;
                    long minutes = (stateOfGame.seconds % 3600) / 60;
                    long secs = stateOfGame.seconds % 60;

                    //Приведение этого всего в удобоваримый формат
                    String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                    timeView.setText(time);

                    //Если таймер запущен, увеличивается количество секунд
                    if (stateOfGame.running) {
                        stateOfGame.seconds++;
                        //Если превышен лимит времени, то игра завершается
                        if (stateOfGame.settings[6] && stateOfGame.seconds > stateOfGame.time_limit) {
                            Intent intent = new Intent(getActivity(), endActivity.class);
                            intent.putExtra("reason", getResources().getString(R.string.becauseoftime));
                            intent.putExtra("time", getResources().getString(R.string.time) + ((TextView) view.findViewById(R.id.textView3)).getText().toString());
                            intent.putExtra("mistakes", getResources().getString(R.string.mistakes) + stateOfGame.mistakes);
                            intent.putExtra("hints", getResources().getString(R.string.hints) + stateOfGame.hints);
                            intent.putExtra("result", getResources().getString(R.string.gameover));
                            startActivity(intent);
                            getActivity().finish();
                            Thread.currentThread().interrupt();
                        }
                    }
                    //Ждём секунду
                    handler.postDelayed(this, 1000);
                }else{
                    //А если игру завершают, то вырубаем таймер
                    //Иначе при запуске следующей время прибавляют одновременно несколько потоков (жалкое зрелище)
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Чтобы таймер выключился
        shutup = true;
    }
}
