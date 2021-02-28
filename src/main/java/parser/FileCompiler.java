package parser;
import java.io.*;


public class FileCompiler{
    private String filename;
    private OutputStream self;
    public FileCompiler(String filename){
        this.filename = filename;
        try {
          this.self = new FileOutputStream(this.filename);
        } catch(Exception e) {
          e.printStackTrace();
        }

    }
    public void writeInstruction(byte[] buffer){
      try {
        this.self.write(buffer);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
}
