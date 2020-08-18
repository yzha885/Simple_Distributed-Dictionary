package client;

/*The Client side program allows query/add/delete requests*/
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


public class ClientLogic {
    private static int port=8888;
    private static String host;
    public static ClientLogic client;
    public static ClientGUI gui;
    public DataInputStream reader;
    public DataOutputStream writer;


    public static void main(String[] args) throws IOException {


        if(args.length==2){
            port = Integer.parseInt(args[1]);
            host= args[0];
            System.out.println("Connecting to "+host+" "+"port");
        }

        //invoke gui
        gui = new ClientGUI();
        gui.preparedGUI();

    }



    public JSONObject guiRequest(String type, String word, String meaning){
        JSONObject mess = new JSONObject();
        mess.put("Type", type);
        mess.put("Word",word);
        mess.put("Meaning",meaning);
        return mess;
    }


    private static JSONObject StringtoJSON(String res){
        JSONObject resJSON = null;
        try {
            JSONParser parser = new JSONParser();
            resJSON = (JSONObject) parser.parse(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resJSON;
    }
    public  JSONObject clientSocket(String type,String word, String meaning) throws IOException {
        JSONObject response;

        Socket socket = null;
        try {
            socket = new Socket(host,port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Server is offline GG :( ", "Server Error",
                    JOptionPane.ERROR_MESSAGE);
        }


        //Input and output Stream
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        //Send request to server
        JSONObject request = guiRequest(type, word, meaning);
        System.out.println("1..Message send from Client is "+request);
        output.writeUTF(request.toString());
        output.flush();

        //Receive data from server
        String serverReply = input.readUTF();
        response= StringtoJSON(serverReply);
        System.out.println("4..Message Received by Client is "+response);


        //Disconnecting
        input.close();
        output.close();
        socket.close();

        return response;
    }
    public static ClientLogic getInstance(){
        if(client==null){
            client = new ClientLogic();
        }
        return client;
    }







}