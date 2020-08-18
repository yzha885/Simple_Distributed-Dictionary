package ServerService;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/*A implmentation of the service Interface*/
public class Service implements Iservice {
    private String filePath= null;
    private static Scanner fileScanner;


    @Override
    public synchronized JSONObject query(String Word) {
        Map<Boolean,String> pair = checkexist(Word);
        Boolean exist = checkexist(pair);

        JSONObject returnMess;

        if (exist){
          //Find the meaning of the word
          String meaning = (String)pair.get(true);
          returnMess =StringToJson(1,Word,meaning,meaning);

        }else{
          //return the information with lack of word in the data field
           returnMess=StringToJson(0,Word,"",Word+" is not existed");
        }
        return returnMess;

    }

    @Override
    public synchronized JSONObject add(String Word, String Meaning) throws IOException {
        Map<Boolean,String> pair = checkexist(Word);
        Boolean exist = checkexist(pair);
        JSONObject replay = new JSONObject() ;

        if(exist){
            replay= StringToJson(0,Word,null,Word+" already exist");
        }else{
            replay= addToFile(Word, Meaning);
        }
        return replay;
    }

    @Override
    public synchronized JSONObject delete(String Word) throws IOException {
        Map<Boolean,String> pair = checkexist(Word);
        Boolean exist = checkexist(pair);
        JSONObject reply ;
        System.out.println("!!");


        if(exist){
            //delete the word from data field

            reply = deleteFromFile(Word);
        }else{
            //error message back to client
            reply= StringToJson(0,Word,"",Word+" is not exited");
        }



        return reply;
    }

    private JSONObject StringToJson (Integer status, String word, String meaning,String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",status);
        jsonObject.put("word",word);
        jsonObject.put("meaning",meaning);
        jsonObject.put("message",message);

        return jsonObject;
    }

    private JSONObject ParseRequest(String string)throws  ParseException{
        JSONObject jsonObject  = null;
        JSONParser parser= new JSONParser();
        jsonObject = (JSONObject) parser.parse(string);

        return jsonObject;
    }

    //Check a word is existed in dictionary or not
    private Map<Boolean, String> checkexist(String queryword){
        //The word is existed  if at lease one true is existed as key and it's corresponding meaning has been stored in its values
        HashMap<Boolean, String> result = new HashMap<Boolean, String>();
        result.put(false,"");
        boolean scanNext = true;
        String meaning;

        //Fetch the data file
        try {
            fileScanner = new Scanner(new File(getFilePath()));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Server side error ", "Server Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        //Search a word
        fileScanner.useDelimiter("[:\n]");
        while (fileScanner.hasNext()&& scanNext ) {
            String word = fileScanner.next();
            try {
                meaning = fileScanner.next();}
            catch(Exception e ) {
                meaning = "";
            }
            if(word.equalsIgnoreCase(queryword))
            {
                scanNext = false;
               result.put(true,meaning);
            }
        }

        return result;
    }

    private String getFilePath(){
        if (filePath==null){
            this.filePath = MultithreadedServer.datafield;
        }
        return filePath;
    }

    //Add to file class perform actual write back to file  and return status info
    private JSONObject addToFile(String word, String meaning) throws IOException {

            FileWriter fw = new FileWriter(getFilePath(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(word+":"+meaning);
            pw.flush();
            pw.close();

            return StringToJson(1,word,meaning,word+" has been added");

    }
    private JSONObject deleteFromFile(String wordtoremoved)throws IOException{
        String removeResult = "";
        String tempFile = "temp.txt";
        File oldFile = new File(filePath);
        File newFile = new File(tempFile);

        String word ;
        String meaning ;

        FileWriter fw = new FileWriter(tempFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        fileScanner = new Scanner(new File(filePath));
        fileScanner.useDelimiter("[:\n]");

        while(fileScanner.hasNext())
        {
            word = fileScanner.next();
            meaning = fileScanner.next();

            if(!word.equalsIgnoreCase(wordtoremoved))
            {
                pw.println(word+":"+meaning);
            }
        }
        fileScanner.close();
        pw.flush();
        pw.close();
        oldFile.delete();
        File rename = new File(filePath);
        newFile.renameTo(rename);
        
        JSONObject reply = StringToJson(1,wordtoremoved,"",wordtoremoved+" has been removed");

        return reply;

    }
    public JSONObject RequestHandling(String fromClient) throws ParseException, IOException {
        JSONObject ToClient=  new JSONObject();
        JSONObject ClientRequest = ParseRequest(fromClient);

        String RequestType= ClientRequest.get("Type").toString();
        String RequestWord= ClientRequest.get("Word").toString();
        String RequestMeaning = ClientRequest.get("Meaning").toString();


        switch(RequestType){
            case "add":
                ToClient = add(RequestWord,RequestMeaning);
                break;
            case "delete":
                ToClient=delete(RequestWord);break;
            case "query":
                ToClient=query(RequestWord);break;

        }
        return ToClient;

    }

    private boolean checkexist(Map<Boolean, String> pair){
        boolean result = false;
       if( pair.containsKey(true)){
           result= true;
       }
        return result;
    }





}