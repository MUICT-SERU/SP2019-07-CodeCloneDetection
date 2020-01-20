package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        ArrayList<String> list = new ArrayList<>();
        File file = new File("falseclones.csv");
        Scanner sc = new Scanner(file);

        Random generator = new Random(9);

        while (sc.hasNextLine()) {
            double num = generator.nextDouble() * (0.5);
            list.add(num+","+sc.next());
        }

        Collections.sort(list);

        FileWriter f = new FileWriter("SelectedFalseClones1.csv");
        for (int i = 0; i <= 22663; i++) {
//            System.out.println(list.get(i));
//            f.write(list.get(i)+"\n");

            String[] del = list.get(i).split(",");
            String value = del[1];
        for (int j=2; j<del.length; j++){
                value += ","+del[j];
            }
            System.out.println(value);
            f.write(value+"\n");
        }

    }
//    static double randomGenerator(long seed) {
//        Random generator = new Random(seed);
////        for (int i = 0; i < 100; i++) {
//            double num = generator.nextDouble() * (0.5);
////            System.out.println(num);
//            return num;
//        }

}
