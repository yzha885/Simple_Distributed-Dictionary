package ServerService;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class ServerGUI implements ActionListener{
    //Static variable single instance of type ServerGUI
    private static ServerGUI serverGUI = null;
    String [] columnNames ={"Word","Meaning"};
    MultithreadedServer server ;

    //GUI components
    //GUI components
    private JFrame frame;
    private JTable table;
    private JPanel panel;



    //Initial pop up of server GUI
    public void initalGui(){
        frame = new JFrame();
        panel = new JPanel();
        JButton button = new JButton("Refresh");
        JLabel headerLable = new JLabel("Dictionary Content");

        //Load initial data


        Object[][] data = {{"Kibe","Ulcerated chilblains"},{"Supercilium","eyebrow or ridge or pride"}};

        table = new JTable(data, columnNames);
        frame.setSize(400, 400);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));

        panel.add(headerLable);
        panel.add(this.table);

        panel.add(button);
        button.addActionListener(this);


        frame.add(panel,BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Dictionary Server");
        frame.pack();
        frame.setVisible(true);

    }

    public static ServerGUI getInstance(){
        if (serverGUI==null){
            serverGUI= new ServerGUI();
        }
        return serverGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        refresh();
    }

    public void refresh(){
        //Capture content in the database and replace the existing table
        MultithreadedServer server = getServer();
        Object[][] data = new Object[0][];
        try {
            data = server.loadDictionary();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        DefaultTableModel newModel= new DefaultTableModel(data, columnNames);
        table.setModel(newModel);



        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }



    private MultithreadedServer getServer(){
        if (server==null){
            server = new MultithreadedServer();
        }
        return server;
    }






}
