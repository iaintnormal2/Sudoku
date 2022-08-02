package com.example.sudoku;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PageFragment extends Fragment {
    private int pageNumber;

    public int[] headers = {R.string.general, R.string.option1, R.string.option2, R.string.option3};
    public int[] rules = {R.string.general_rule, R.string.oddandeven, R.string.neighbours, R.string.trio};
    public int[] images = {R.drawable.usual, R.drawable.oddandeven, R.drawable.neighbours, R.drawable.trio};

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.page, container, false);
        ((TextView) result.findViewById(R.id.textView15)).setText(headers[pageNumber]);
        ((TextView) result.findViewById(R.id.textView16)).setText(rules[pageNumber]);
        ((ImageView) result.findViewById(R.id.imageView)).setImageResource(images[pageNumber]);

        return result;
    }
}
