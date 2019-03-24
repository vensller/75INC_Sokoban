package Utilities;

import Model.Instance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InstanceReader {

    public Instance readInstance(String archive){
        Instance instance = null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(archive));
            String str = reader.readLine();
            String[] arrayStr = str.split(" ");
            instance = new Instance(Integer.parseInt(arrayStr[0]), Integer.parseInt(arrayStr[1]));

            int line = 0;
            while ((str = reader.readLine()) != null){
                arrayStr = str.split("");

                if (arrayStr.length != instance.getColumns())
                    throw new IOException();

                for (int x = 0; x < arrayStr.length; x++)
                    instance.getMap()[line][x] = arrayStr[x];

                line++;
            }
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            instance = null;
        } catch (IOException e) {
            e.printStackTrace();
            instance = null;
        }

        return instance;
    }

}
