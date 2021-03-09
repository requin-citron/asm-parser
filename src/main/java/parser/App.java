package parser;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        if(args.length < 1){
          System.out.println("Usage: file_name.jar file.red");
          System.exit(1);
        }

        String path = args[0];
        System.out.println("Processing: "+path);
        String pathWrite = "./" + "out.cor";
        ParserFile parser = new ParserFile(path);
        FileCompiler output = new FileCompiler(pathWrite);
        String line = parser.readLine();
        byte[] buffer;
        while (line != null) {
          buffer = InstructionTradutor.in(line);
          if(buffer != null){
            output.writeInstruction(buffer);
          }
          line = parser.readLine();

        }
        parser.close();
    }
}
