package client;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientGUI  {
    JTextField word;
    JTextArea meaning;
    Alterbox alterbox;

    private String getWord(){
        String reply = word.getText();
        return reply;
    }
    private String getMeaning(){
        String reply= meaning.getText();
        return reply;
    }



    public void preparedGUI(){
        //Creating the Frame
        JFrame frame = new JFrame("Dictionary Service");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);

        //Creating the panel at bottom to query/add/delete a word
        JPanel buttom = new JPanel();
        JButton query = new JButton("query");
        query.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    JSONObject response = ClientLogic.getInstance().clientSocket("query",getWord(),"");



                    if(response.get("status").toString().equals("1")) {
                        //Fetch the result/meaning
                        meaning.setText(response.get("meaning").toString());

                    }else{
                        //pop error message
                        alterbox(response.get("message").toString(),"Error");

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JButton add = new JButton("add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JSONObject response= ClientLogic.getInstance().clientSocket("add",getWord(),getMeaning());
                    if(response.get("status").toString().equals("1")){
                        //The word has been added to server successful
                       alterbox(response.get("message").toString(),"Successful");
                    }
                    else{
                        //Pop up error information
                        alterbox(response.get("message").toString(),"Error");
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JButton delete = new JButton("delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JSONObject response= ClientLogic.getInstance().clientSocket("delete",getWord(),"");
                    if(response.get("status").toString().equals("1")){
                        //Pop up successful message
                        alterbox(response.get("message").toString(),"Successful");
                        word.setText("");
                        meaning.setText("");

                    }else{
                        //pop up error message
                        alterbox(response.get("message").toString(),"Error");
                        meaning.setText("");

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttom.add(query);
        buttom.add(add);
        buttom.add(delete);

        //Creating the text field for word
        JPanel top = new JPanel();
        JLabel headerlabel = new JLabel("Dictionary Service");
        top.add(headerlabel);

        //Creating the Text Area for input
        JPanel center = new JPanel();
        JLabel meaningLabel = new JLabel("Meaning ");
        meaning = new JTextArea(3,15);
        word = new JTextField(15); //restricted the input field to 10 characters
        JLabel wordLabel = new JLabel("Word     ");
        center.add(wordLabel);
        center.add(word);
        center.add(meaningLabel);
        center.add(meaning);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH,top);
        frame.getContentPane().add(BorderLayout.SOUTH, buttom);
        frame.getContentPane().add(BorderLayout.CENTER, center);
        frame.setVisible(true);

    }
    private void alterbox(String Message, String title){
        if(alterbox==null){
            alterbox= new Alterbox();
        }
        alterbox.infoBox(Message, title);

    }

}

class Alterbox{
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}