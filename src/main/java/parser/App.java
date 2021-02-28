package parser;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        String path = "/home/poney/script/java/info4B/asm-parser/src/test/files/";
        String pathRead = path+"test.txt";
        String pathWrite = path + "test.cor";
        ParserFile parser = new ParserFile(pathRead);
        FileCompiler output = new FileCompiler(pathWrite);
        String line = parser.readLine();
        byte[] buffer;
        while (line != null) {
          buffer = InstructionTradutor.in(line);
          if(buffer != null){
            output.writeInstruction(buffer);
            System.out.println(line);
          }
          line = parser.readLine();

        }
        parser.close();
    }
}
