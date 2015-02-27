package testfileandarrays;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jl.decoder.JavaLayerException;

public class SimplePlayListMP3 extends JFrame {

    private final JPanel btnPanel;
    private final DefaultListModel myListModel;
    private final JList myTrackList;
    private File file;
    private MyPlayer player;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        SimplePlayListMP3 myPlayList = new SimplePlayListMP3("Playlist");
        myPlayList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myPlayList.setSize(600, 300);
        myPlayList.setVisible(true);

    }

    public SimplePlayListMP3(String title) {
//      Создаем фрейм
        super(title);

//      Создаем контекстное меню
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem play = new JMenuItem("play");
        JMenuItem stop = new JMenuItem("stop");
        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (player != null) {
                    player.stop();
                    player = null;
                }
                try {
                    player = new MyPlayer(file);
                    player.playTrack();
                } catch (FileNotFoundException | JavaLayerException ex) {
                    Logger.getLogger(SimplePlayListMP3.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        stop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (player != null) {
                    player.stop();
                    player = null;
                }
            }
        });
        
        popupMenu.add(play);
        popupMenu.add(stop);

//      Создаем панели
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        btnPanel = new JPanel(new GridLayout(4, 1));
        btnPanel.setPreferredSize(new Dimension(50, 100));
        myListModel = new DefaultListModel();

        myTrackList = new JList(myListModel);
        myTrackList.setComponentPopupMenu(popupMenu);
        myTrackList.setToolTipText("Double click for play/stop");
        myTrackList.addMouseListener(new MyMouseListener());
        JScrollPane scrlPanel = new JScrollPane(myTrackList);

//      Создаем кнопки
        ButtonListener bl = new ButtonListener();
        ImageIcon iconOpen = new javax.swing.ImageIcon(getClass().getResource("/icons/open.png"));
        ImageIcon iconSave = new javax.swing.ImageIcon(getClass().getResource("/icons/save.png"));
        ImageIcon iconAdd = new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"));
        ImageIcon iconDel = new javax.swing.ImageIcon(getClass().getResource("/icons/del.png"));

        addButton("Open list...", "open", bl, iconOpen);
        addButton("Save list...", "save", bl, iconSave);
        addButton("Add track...", "add", bl, iconAdd);
        addButton("Remove track...", "remove", bl, iconDel);

        mainPanel.add(scrlPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.EAST);
        getContentPane().add(mainPanel);
    }

    private void addButton(String name, String nameAction, ButtonListener bl, ImageIcon icon) {

        JButton btn = new JButton();
        btn.setActionCommand(nameAction);
        btn.setToolTipText(name);
        btn.setIcon(icon);
        btn.addActionListener(bl);
        btnPanel.add(btn);

    }

    private class ButtonListener implements ActionListener {

        private ArrayList<String> list;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!((Object) e.getSource() instanceof JButton)) {
                System.out.println(e.getSource().toString());
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
                        Logger.getLogger(SimplePlayListMP3.class.getName()).log(Level.SEVERE, null, ex);
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
                            Logger.getLogger(SimplePlayListMP3.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
                case "add": {
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Tracks mp3", "mp3");

                    fileChooser.setFileFilter(filter);
                    fileChooser.setMultiSelectionEnabled(true);
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    int showOpenDialog = fileChooser.showOpenDialog(null);
                    if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                        File[] files = fileChooser.getSelectedFiles();
                        for (int i = 0; i < files.length; i++) {
                            file = files[i];
                            if (file.isDirectory()) {
                                File[] folderFiles = file.listFiles();
                                for (int j = 0; j < folderFiles.length; j++) {
                                    File folderFile = folderFiles[j];
                                    FileOperations.addFileToList(folderFile, myListModel);
                                }

                            }
                        }
                    } else {
                        FileOperations.addFileToList(file, myListModel);
                    }
                    break;
                }
                case "remove": {
                    int[] selInds = myTrackList.getSelectedIndices();
                    for (int i = 0; i < selInds.length; i++) {
                        int selInd = selInds[i];
                        myListModel.remove(selInd - i);
                    }
                    break;
                }

            }

        }

    }

    private class MyMouseListener extends MouseAdapter {

        private String prevName;
        private BufferedInputStream bis;

        @Override
        public void mouseReleased(MouseEvent e) {
            getFileFromList(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            getFileFromList(e);
        }

        public void getFileFromList(MouseEvent e) {

            int i = myTrackList.locationToIndex(e.getPoint());
            myTrackList.setSelectedIndex(i);
            if (i >= 0) {
                String name = (String) myTrackList.getModel().getElementAt(i);
                file = new File(name);
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {

//          запуск по двойному клику
            if (e.getClickCount() == 2) {
                if (player != null) {
                    player.stop();
                    player = null;
                }
                int i = myTrackList.locationToIndex(e.getPoint());

                if (i >= 0) {
                    String name = (String) myTrackList.getModel().getElementAt(i);
                    if (name.equals(prevName)) {
                        prevName = "";
                        return;
                    }
                    file = new File(name);
                    try {
                        player = new MyPlayer(file);
                        player.playTrack();
                    } catch (FileNotFoundException | JavaLayerException ex) {
                        Logger.getLogger(SimplePlayListMP3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
}
