package parser;
import java.io.*;
import java.util.*;

/*
Function name space for binary tradution 
*/
 public class ParserFile{
     private String filename;
     private Scanner reader;
     public ParserFile(String filename){
       this.filename = filename;
       String line;
       try {
         File file = new File(this.filename);
         this.reader = new Scanner(file);
       } catch(Exception e) {
         e.printStackTrace();
       }
     }
     public String readLine(){
        if(this.reader.hasNextLine()){
          return reader.nextLine();
        }
        return null;
     }
     public void close(){
       this.reader.close();
     }
 }
