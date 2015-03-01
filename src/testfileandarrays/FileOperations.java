package testfileandarrays;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class FileOperations {

    public static void writeListToFile(File file, DefaultListModel lm) {

        try (PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < lm.size(); i++) {
                pw.println(lm.get(i));
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static ArrayList<String> getListFromFile(File file) {

        ArrayList<String> list = new ArrayList<>();
        Scanner s;
        int i = 0;
        try {
            s = new Scanner(file);
            while (s.hasNext()) {
                list.add(i, s.nextLine());
                i++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void addFolderToList(File folder, DefaultListModel dlm) {
        
        File[] folderFiles = folder.listFiles();
        for (File folderFile : folderFiles) {
            FileOperations.addFileToList(folderFile, dlm);
        }
        
    }

    public static void addFileToList(File file, DefaultListModel dlm) {

        if (file.isDirectory()) {
            FileOperations.addFolderToList(file, dlm);
        } else if (file.getName().endsWith(".mp3") && !dlm.contains(file.getPath())) {
            dlm.addElement(file.getPath());
        }

    }
}
