package ServerService;
//@ author:YajingZhang yajingzhang00@gmail.com



/*A program implments Java.lang.Runnable interface allows concurrent client requests and create a new thread
for each socket connection*/

import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MultithreadedServer implements Runnable {
    protected Service service;
    protected Socket socket;
    public static String datafield;


    public void setService(Service service) {
        this.service = service;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        int port = 8888;

        if(args.length==2){

                port = Integer.parseInt(args[0]);
                datafield= args[1];


        }

        ServerGUI gui = ServerGUI.getInstance();
        gui.initalGui();
        gui.refresh();

        //Create a server socket and waiting for client's connection
        ServerSocket serverSocket= new ServerSocket(port);
        System.out.println("The service is running on port " +port);


        while(true){
            // waiting for client connection
            Socket socket= serverSocket.accept();
            socket.setSoTimeout(14000);
            MultithreadedServer server = new MultithreadedServer();
            server.setService(new Service());
            server.setSocket(socket);
            // Start a new server thread..
            System.out.println("A new connection");
            new Thread(server).start();
            System.out.println("A new thread started");


        }
    }

    public void execute(){
        try {
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            //Read Message from client and parse the execution
            String ClientMessage= reader.readUTF();
            System.out.println("2..Message received by server is "+ClientMessage);
            Service service= new Service();

            //Send replay back to Client
            JSONObject toClient =service.RequestHandling(ClientMessage);
            writer.writeUTF(toClient.toString());
            System.out.println("3..Message send to Client by server is "+toClient);
            writer.flush();


            //Close the stream
            reader.close();
            writer.close();
        }
        catch(Exception e ){
            e.printStackTrace();
        }
    }





    @Override
    public void run() {
        execute();
    }

    public Object[][] loadDictionary() throws IOException {
        Map<String, String > dictionary = new HashMap<>();
        String delimiter = ":";


       Stream<String> lines = Files.lines(Paths.get(datafield));
            lines.filter(line -> line.contains(delimiter)).forEach(
                    line -> dictionary.putIfAbsent(line.split(delimiter)[0], line.split(delimiter)[1])
            );

        Integer row = dictionary.size();
       Object [] [] data=  new String [row] [2];

        Integer index = 0;
        for (Map.Entry<String,String> entry: dictionary.entrySet()){
            data[index][0]=entry.getKey();
            data[index][1]=entry.getValue();

            index++;

        }


        return data;

    }




}