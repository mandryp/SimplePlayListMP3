package testfileandarrays;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jl.decoder.JavaLayerException;

public class TestFileAndArrays extends JFrame {

    private final JPanel btnPanel;
    private final DefaultListModel myListModel;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        TestFileAndArrays myPlayList = new TestFileAndArrays("Playlist");
        myPlayList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myPlayList.setSize(600, 400);
        myPlayList.setVisible(true);

    }

    public TestFileAndArrays(String title) {
//        создаем фрейм
        super(title);

//      Создаем панели
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        btnPanel = new JPanel(new GridLayout(4, 1));
        myListModel = new DefaultListModel();

        JList myTrackList = new JList(myListModel);
        myTrackList.addMouseListener(new MyMouseListener());
        JScrollPane scrlPanel = new JScrollPane(myTrackList);

//      Создаем кнопки
        ButtonListener bl = new ButtonListener();
        addButton("Open list...", "open", bl);
        addButton("Save list...", "save", bl);
        addButton("Add track...", "add", bl);
        addButton("Remove track...", "remove", bl);

        mainPanel.add(scrlPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.EAST);
        getContentPane().add(mainPanel);
    }

    private void addButton(String name, String nameAction, ButtonListener bl) {

        JButton btn = new JButton(name);
        btn.setActionCommand(nameAction);
        btn.addActionListener(bl);
        btnPanel.add(btn);

    }

    private class ButtonListener implements ActionListener {

        private File file;
        private ArrayList<String> list;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!((Object) e.getSource() instanceof JButton)) {
                System.out.println("Команда не найдена");
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            switch (e.getActionCommand()) {
                case "open": {
                    fileChooser.setFileFilter(new FileNameExtensionFilter("List m3u", "m3u"));
                    int showOpenDialog = fileChooser.showOpenDialog(null);
                    if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();
                    }
                    try {
                        list = FileOperations.getListFromFile(file);
                        for (String list1 : list) {
                            myListModel.addElement(list1);
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TestFileAndArrays.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "save": {
                    fileChooser.setFileFilter(new FileNameExtensionFilter("List m3u", "m3u"));
                    int showSaveDialog = fileChooser.showSaveDialog(null);
                    if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();
                        try {
                            FileOperations.writeListToFile(file, myListModel);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TestFileAndArrays.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
                case "add": {
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Tracks mp3", "mp3"));
                    int showOpenDialog = fileChooser.showOpenDialog(null);
                    if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();
                    }
                    myListModel.addElement(file.getPath());
                    break;
                }

            }

        }
    }

    private class MyMouseListener extends MouseAdapter {

        private File file;
        private MyPlayer player;
        private String prevName;
        private BufferedInputStream bis;

        @Override
        public void mouseClicked(MouseEvent e) {

//          запуск по двойному клику
            JList list = (JList) e.getSource();
            if (e.getClickCount() == 2) {
                if (player != null) {
                    player.close();
                    player = null;
                }
                int i = list.locationToIndex(e.getPoint());

                if (i >= 0) {
                    String name = (String) list.getModel().getElementAt(i);
                    if (name.equals(prevName)) {
                        prevName = "";
                        return;
                    }
                    file = new File(name);

                    try {
                        FileInputStream fis = new FileInputStream(file);
                        bis = new BufferedInputStream(fis);
                        player = new MyPlayer(bis);
                        player.play();
                        prevName = name;

                    } catch (FileNotFoundException | JavaLayerException ex) {
                        Logger.getLogger(TestFileAndArrays.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
}
