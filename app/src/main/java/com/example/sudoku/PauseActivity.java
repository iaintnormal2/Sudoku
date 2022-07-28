package com.example.sudoku;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PauseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.pause);

        FloatingActionButton start = findViewById(R.id.floatingActionButton8);
        //Собственно возвращение в игру
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //Чтобы в игру можно было вернуться только через специальную кнопку
    //При нажатии на кнопку "назад" ничего не происходит
    @Override
    public void onBackPressed(){

    }
}
