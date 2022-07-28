package com.example.sudoku;

import java.util.ArrayList;

public class Table {
    public int[][] field;
    public int[][] question;
    public int sqrt;
    public int sqrt_2;
    public static int[][] transportation(int[][] matrix, int max_num, int sqrt, int sqrt_2){
        int[][] clone = new int[max_num][max_num];
        if(sqrt_2 == sqrt) {
            for (int i = 0; i < max_num; i++) {
                for (int j = 0; j < max_num; j++) {
                    clone[j][i] = matrix[i][j];
                }
            }
        }else{
            clone = swap_rows(matrix, sqrt, sqrt_2);
        }
        return clone;
    }

    public static int[][] swap_rows(int[][] matrix, int sqrt, int sqrt_2){
        if(sqrt != 1) {
            for (int j = 0; j < sqrt_2; j++) {
                int square = j;
                ArrayList<Integer> available_rows = new ArrayList<>();
                for (int i = square * sqrt_2; i <= sqrt_2 * square + (sqrt_2 - 1); i++) {
                    available_rows.add(i);
                }
                int index_a = available_rows.remove((int) (Math.random() * available_rows.size()));
                int index_b = available_rows.remove((int) (Math.random() * available_rows.size()));
                int[] row_A = new int[matrix.length];
                System.arraycopy(matrix[index_a], 0, row_A, 0, matrix.length);
                int[] row_B = new int[matrix.length];
                System.arraycopy(matrix[index_b], 0, row_B, 0, matrix.length);
                matrix[index_a] = row_B;
                matrix[index_b] = row_A;
            }
        }
        return matrix;
    }

    public static int[][] swap_cols(int[][] matrix, int sqrt, int sqrt_2){
        for(int j = 0; j < sqrt_2; j++) {
            ArrayList<Integer> available_cols = new ArrayList<>();
            if(sqrt_2 != 1 && sqrt != 1) {
                int square = j;
                for (int i = square * sqrt; i <= sqrt * square + (sqrt - 1); i++) {
                    available_cols.add(i);
                }
            }else{
                for(int i = 0; i < matrix.length; i++){
                    available_cols.add(i);
                }
            }
            int index_a = available_cols.remove((int) (Math.random() * available_cols.size()));
            int index_b = available_cols.remove((int) (Math.random() * available_cols.size()));
            int[] col_A = new int[matrix.length];
            for(int i = 0; i < matrix.length; i++){
                col_A[i] = matrix[i][index_a];
            }
            int[] col_B = new int[matrix.length];
            for(int i = 0; i < matrix.length; i++){
                col_B[i] = matrix[i][index_b];
            }
            for(int i = 0; i < matrix.length; i++){
                matrix[i][index_a] = col_B[i];
            }
            for(int i = 0; i < matrix.length; i++){
                matrix[i][index_b] = col_A[i];
            }
        }
        return matrix;
    }

    public static int[][] swap_areas_horizontal(int[][] matrix, int sqrt, int sqrt_2){
        if(sqrt_2 !=1 && sqrt != 1) {
            ArrayList<Integer> available_areas = new ArrayList<>();
            for (int i = 0; i < sqrt; i++) {
                available_areas.add(i);
            }
            int index_a = available_areas.remove((int) (Math.random() * available_areas.size()));
            int index_b = available_areas.remove((int) (Math.random() * available_areas.size()));
            int[][] area_A = new int[sqrt_2][matrix.length];
            int[][] area_B = new int[sqrt_2][matrix.length];
            for (int i = index_a * sqrt_2; i < index_a * sqrt_2 + sqrt_2; i++) {
                System.arraycopy(matrix[i], 0, area_A[i - index_a * sqrt_2], 0, matrix[i].length);
            }
            for (int i = index_b * sqrt_2; i < index_b * sqrt_2 + sqrt_2; i++) {
                System.arraycopy(matrix[i], 0, area_B[i - index_b * sqrt_2], 0, matrix[i].length);
            }
            for (int i = index_b * sqrt_2; i < index_b * sqrt_2 + sqrt_2; i++) {
                System.arraycopy(area_A[i - index_b * sqrt_2], 0, matrix[i], 0, matrix[i].length);
            }
            for (int i = index_a * sqrt_2; i < index_a * sqrt_2 + sqrt_2; i++) {
                System.arraycopy(area_B[i - index_a * sqrt_2], 0, matrix[i], 0, matrix[i].length);
            }
        }else{
            matrix = swap_rows(matrix, sqrt, sqrt_2);
        }
        return matrix;
    }

    public static int[][] swap_areas_vertical(int[][] matrix, int sqrt, int sqrt_2){
        if(sqrt_2 !=1 && sqrt != 1) {
            ArrayList<Integer> available_areas = new ArrayList<>();
            for (int i = 0; i < sqrt_2; i++) {
                available_areas.add(i);
            }
            int index_a = available_areas.remove((int) (Math.random() * available_areas.size()));
            int index_b = available_areas.remove((int) (Math.random() * available_areas.size()));
            int[][] area_A = new int[sqrt][matrix.length];
            int[][] area_B = new int[sqrt][matrix.length];
            for(int i = index_a * sqrt; i < index_a * sqrt + sqrt; i++){
                for(int j = 0; j < matrix.length; j++){
                    area_A[i-index_a * sqrt][j] = matrix[j][i];
                }
            }
            for(int i = index_b * sqrt; i < index_b * sqrt + sqrt; i++){
                for(int j = 0; j < matrix.length; j++){
                    area_B[i-index_b * sqrt][j] = matrix[j][i];
                }
            }
            for(int i = index_a * sqrt; i < index_a * sqrt + sqrt; i++){
                for(int j = 0; j < matrix.length; j++){
                    matrix[j][i] = area_B[i-index_a * sqrt][j];
                }
            }
            for(int i = index_b * sqrt; i < index_b * sqrt + sqrt; i++){
                for(int j = 0; j < matrix.length; j++){
                    matrix[j][i] = area_A[i-index_b * sqrt][j];
                }
            }
        }else{
            matrix = swap_cols(matrix, sqrt, sqrt_2);
        }
        return matrix;
    }

    public void fill_table(int max_num) {
        field = new int[max_num][max_num];
        sqrt_2 = (int) Math.sqrt(max_num);
        sqrt = max_num / sqrt_2;
        if (!(sqrt * sqrt_2 == max_num)) {
            if (max_num % 3 == 0) {
                sqrt_2 = 3;
            } else if (max_num % 4 == 0) {
                sqrt_2 = 4;
            } else if (max_num % 2 == 0) {
                sqrt_2 = 2;
            } else if (max_num % 5 == 0) {
                sqrt_2 = 5;
            } else {
                sqrt_2 = max_num;
            }
            sqrt = max_num / sqrt_2;
        }
        for (int i = 0; i < max_num; i++) {
            for (int j = 0; j < max_num; j++) {
                if(i == 0){
                    field[i][j] = j+1;
                }else{
                    field[i][j] = field[i - 1][(j + sqrt)%max_num];
                    if(i%sqrt_2==0){
                        field[i][j] = (field[i][j]+1)%max_num;
                        if(field[i][j] == 0){
                            field[i][j] = max_num;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < max_num*5; i++){
            int action = (int) (Math.random()*4);
            switch (action){
                case 0:
                    field = transportation(field, max_num, sqrt, sqrt_2);
                    break;
                case 1:
                    field = swap_areas_horizontal(field, sqrt, sqrt_2);
                    break;
                case 2:
                    field = swap_areas_vertical(field, sqrt, sqrt_2);
                    break;
                case 3:
                    field = swap_rows(field, sqrt, sqrt_2);
                    break;
                default:
                    field = swap_cols(field, sqrt, sqrt_2);
                    break;
            }
        }
    }

    public int[][] choose_cells(int[][] field, double level){
        int[][] res = new int[field.length][field.length];
        ArrayList<Integer> available_cells = new ArrayList<>();
        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field.length; j++){
                available_cells.add(i*MainActivity.stateOfGame.max_num+j);
            }
        }
        for(int i = 0; i < field.length* field.length*level; i++){
            int newCell = available_cells.remove((int) (Math.random()*available_cells.size()));
            res[newCell/ field.length][newCell% field.length] = field[newCell/ field.length][newCell% field.length];
        }
        return res;
    }

    public Table(int max_num, double level) {
        field = new int[max_num][max_num];
        fill_table(max_num);
        question = choose_cells(field, level);
        }
    }