package parser;
import org.apache.commons.cli.*;


public class App
{
    public static void main( String[] args ) throws Exception
    {
      Options options = new Options();
      options.addOption("i", "input", true, "Input file");
      options.addOption("o", "output", true, "Output file");
      options.addOption("h", "help", false, "Show help");
      CommandLineParser parser  = new DefaultParser();
      CommandLine cmd = parser.parse(options, args);
      boolean flag = false;
      if(!cmd.hasOption("i")){
        flag = true;
      }
      if(!cmd.hasOption("o")){
        flag = true;
      }
      if(cmd.hasOption("h") || flag){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("asm-parser", options);
        System.exit(0);
      }
        String path = cmd.getOptionValue("i");
        System.out.println("Processing: "+path);
        String pathWrite = cmd.getOptionValue("o");
        ParserFile parserF = new ParserFile(path);
        FileCompiler output = new FileCompiler(pathWrite);
        String line = parserF.readLine();
        byte[] buffer;
        while (line != null) {
          buffer = InstructionTradutor.in(line);
          if(buffer != null){
            output.writeInstruction(buffer);
          }
          line = parserF.readLine();

        }
        parserF.close();
    }
}
