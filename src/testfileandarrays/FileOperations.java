package testfileandarrays;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.DefaultListModel;

public class FileOperations{

    public static void writeListToFile(File file, DefaultListModel lm) throws FileNotFoundException {

        try (PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < lm.size(); i++) {
                pw.println(lm.get(i));
            }
            pw.close();
        }

    }

    public static ArrayList<String> getListFromFile(File file) throws FileNotFoundException {

        ArrayList<String> list = new ArrayList<>();
        Scanner s = new Scanner(file);
        int i = 0;
        while (s.hasNext()) {
            list.add(i, s.nextLine());
            i++;
        }
        return list;
    }

    public static void addFileToList(File file, DefaultListModel dlm) {

        if (file.isFile() && file.getName().endsWith(".mp3") && !dlm.contains(file.getPath())) {
            dlm.addElement(file.getPath());
        }

    }
}
