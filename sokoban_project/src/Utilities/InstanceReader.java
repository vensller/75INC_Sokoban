package Utilities;

import Model.Instance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InstanceReader {

    public static Instance readInstanceFromFile(String archive){
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

    public static List<Instance> readInstanceFromText(String text){
        List<Instance> list = new ArrayList<>();
        String[] arrayStates = text.split(";");

        for (int x = 0; x < arrayStates.length; x++){
            String[] rows = arrayStates[x].split("\n");
            int columnCount = rows[0].trim().split(",").length;

            if (columnCount > 1) {
                Instance instance = new Instance(rows.length, columnCount);

                for (int y = 0; y < rows.length; y++) {
                    String[] columns = rows[y].trim().split(",");

                    for (int z = 0; z < columns.length; z++)
                        instance.getMap()[y][z] = columns[z];
                }

                list.add(instance);
            }
        }

        return list;
    }

}
