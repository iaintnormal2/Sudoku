package com.example.sudoku;

import static com.example.sudoku.MainActivity.stateOfGame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    EditText time_limit_seconds;
    EditText time_limit_minutes;
    EditText hints_limit;
    EditText mistakes_limit;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        int[] switches = {R.id.switch1, R.id.switch2, R.id.switch3, R.id.switch4, R.id.switch5,
        R.id.switch6, R.id.switch7, R.id.switch8, R.id.switch9};

        for(int i = 0; i < 9; i++){
            findViewById(switches[i]).setId(i*10000000);
            ((Switch) findViewById(i*10000000)).setChecked(stateOfGame.settings[i]);
            findViewById(i*10000000).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    stateOfGame.settings[view.getId()/10000000] = !stateOfGame.settings[view.getId()/10000000];
                }
            });
        }

        if(stateOfGame.settings[6]) {
            time_limit_seconds = findViewById(R.id.editTextNumber2);
            time_limit_seconds.setText(stateOfGame.time_limit % 60 + "");
            time_limit_minutes = findViewById(R.id.editTextNumber3);
            time_limit_minutes.setText(stateOfGame.time_limit / 60 + "");
        }
        if(stateOfGame.settings[7]) {
            mistakes_limit = findViewById(R.id.editTextNumber4);
            mistakes_limit.setText(stateOfGame.mistakes_limit + "");
        }
        if(stateOfGame.settings[8]) {
            hints_limit = findViewById(R.id.editTextNumber5);
            hints_limit.setText(stateOfGame.hints_limit + "");
        }

        Button save = findViewById(R.id.button11);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < switches.length; i++){
                    if(((Switch) findViewById(i*10000000)).isChecked()){
                        stateOfGame.settings[i] = true;
                    }
                }
                //Если какое-нибудь из полей пустое, то поствим в него 0
                if(stateOfGame.settings[6]){
                    if((((TextView) findViewById(R.id.editTextNumber3)).getText().toString()).equals("")){
                        ((TextView) findViewById(R.id.editTextNumber3)).setText("0");
                    }
                    if((((TextView) findViewById(R.id.editTextNumber2)).getText().toString()).equals("")){
                        ((TextView) findViewById(R.id.editTextNumber2)).setText("0");
                    }
                }
                try{
                    long minutes = Long.parseLong(((TextView) findViewById(R.id.editTextNumber3)).getText().toString());
                    long seconds = Long.parseLong(((TextView) findViewById(R.id.editTextNumber2)).getText().toString());
                    stateOfGame.time_limit = minutes*60 + seconds;
                    ((TextView) findViewById(R.id.editTextNumber3)).setText(minutes+"");
                    ((TextView) findViewById(R.id.editTextNumber2)).setText(seconds+"");
                    if(stateOfGame.time_limit == 0){
                        stateOfGame.settings[6] = false;
                        Toast.makeText(getApplicationContext(), R.string.error4, Toast.LENGTH_SHORT).show();
                    }
                }catch(NumberFormatException e){
                    if(stateOfGame.settings[6]){
                        Toast.makeText(getApplicationContext(), R.string.error3, Toast.LENGTH_SHORT).show();
                    }
                }
                try{
                    int mistakes = Integer.parseInt(((TextView) findViewById(R.id.editTextNumber4)).getText().toString());
                    stateOfGame.mistakes = mistakes;
                }catch(NumberFormatException e){
                    if(stateOfGame.settings[7]){
                        Toast.makeText(getApplicationContext(), R.string.error3, Toast.LENGTH_SHORT).show();
                    }
                }
                try{
                    stateOfGame.hints = Integer.parseInt(((TextView) findViewById(R.id.editTextNumber5)).getText().toString());
                }catch(NumberFormatException e){
                    if(stateOfGame.settings[8]){
                        Toast.makeText(getApplicationContext(), R.string.error3, Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        });
    }
}
