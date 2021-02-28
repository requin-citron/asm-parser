package parser;

import java.io.*;
import java.util.*;

/*
la taille max d'un opcode est 40bits soit 9 bytes
la conversion text "binaire" ce passe comme suit:
4 bit d'instruction:
4 bits de mode d'addressage A;
4 bits de mode d'addressage B:
14 bits d'argument A:
14 bits d'argument B:

total: 40 bits

*/

//final class car il n'y a pas de raison que l'on crée une class fils
class InstructionTradutor{
  //constant déclaration
  private static Character SEPARATOR_TOKEN = ' '; // space pourquoi faire compliquer quand on peut faire simple;
  private static String[] instructionSet = new String[] {
    "MOV", // 1: transfere le contenue de l'addresse A a l'addresse B
    "ADD", // 2: ajoute le contenue de l'addresse  A a l'addresse B
    "SUB", // 3: soustrai le contenue de l'addresse A a l'addresse B
    "JMP", // 4: transfere l'execution a l'addresse A
    "JMZ", // 5: transfere l'execution  a l'addresse A si le contenue de l'addresse B == 0
    "JMG", // 6: transfere l'execution a l'addresse A si le contenue de l'addresse B > 0
    "DJZ", // 7: retranche 1 du contenue de l'addresse B et saute a A si le result ==0
    "CMP", // 8: compare le contenue de l'addresse A et le contenue de l'addresse B si ils sont different sauté a l'instruction suivante
    "DAT" // 0: valeur non executable B est la valeur de la donnée
  };
  //private const int instructionLength = 9;
  //input sanitization
  public static  String sanitize(String input){
    if(input.equals("")) return "";
    int indexStart = 0;
    int indexEnd = input.length()-1;
    while(indexStart<indexEnd && input.charAt(indexStart) == InstructionTradutor.SEPARATOR_TOKEN){
       indexStart++;
    }
    while(indexEnd>0 && input.charAt(indexEnd) == InstructionTradutor.SEPARATOR_TOKEN){
      indexEnd--;
    }
    if(indexEnd-indexStart < 0) return "";
    return input.substring(indexStart, indexEnd+1);
  }
  //strchr comme en c (je ne connais pas bien java)
  public static int strchr(String input, Character aim){
    int i = 0;
    int length = input.length();
    while(i < (length-1) && input.charAt(i) != aim){
      i++;
    }
    if(input.charAt(i)==aim) return i;
    return -1;
  }
  //process MOV
  public static byte[] mov(String movline){
    byte[] array = new byte[] {(byte)0x90};
    return array;
  }
  //process ADD
  public static byte[] add(String addline){
    byte[] array = new byte[] {(byte)0x91};
    return array;
  }
  //process SUB
  public static byte[] sub(String subline){
    byte[] array = new byte[] {(byte)0x92};
    return array;
  }
  //process JMP
  public static byte[] jmp(String subline){
    byte[] array = new byte[] {(byte)0x93};
    return array;
  }
  //process JMZ
  public static byte[] jmz(String subline){
    byte[] array = new byte[] {(byte)0x94};
    return array;
  }
  //process JMG
  public static byte[] jmg(String subline){
    byte[] array = new byte[] {(byte)0x95};
    return array;
  }
  //process DJZ
  public static byte[] djz(String subline){
    byte[] array = new byte[] {(byte)0x96};
    return array;
  }
  //process CMP
  public static byte[] cmp(String subline){
    byte[] array = new byte[] {(byte)0x97};
    return array;
  }
  //process DAT
  public static byte[] dat(String subline){
    byte[] array = new byte[] {(byte)0x98};
    return array;
  }
  //fonction principale de la traduction
  public static byte[] in(String line){
    if(line.equals("")) return null;
    String sanitizeLine = InstructionTradutor.sanitize(line);
    int opCodeDelimiter = InstructionTradutor.strchr(sanitizeLine, InstructionTradutor.SEPARATOR_TOKEN);
    if(opCodeDelimiter == -1){//catch error
      System.err.println("Syntax Error: "+ sanitizeLine);
      System.exit(1);// return error
    }
    String opCode =  sanitizeLine.substring(0, opCodeDelimiter).toUpperCase();
    if(opCode.equals(InstructionTradutor.instructionSet[0])){       // MOV
        return mov(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[1])){ // ADD
        return add(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[2])){ //SUB
        return sub(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[3])){ // JMP
        return jmp(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[4])){ // JMZ
        return jmz(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[5])){ // JMG
        return jmg(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[6])){ // DJZ
        return djz(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[7])){ // CMP
        return cmp(sanitizeLine);
    }else if(opCode.equals(InstructionTradutor.instructionSet[8])){ // DAT
        return dat(sanitizeLine);
    }else{
      System.err.println("Unknown op code: "+ opCode);
      System.exit(1);
    }
    assert 1==0; // never reach
    return null;
  }
}
