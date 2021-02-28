package parser;

import java.io.*;
import java.util.*;

/*
la taille max d'un opcode est 40bits soit 5 bytes
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
  //supprimer tout les Character d'une chaine
  public static String eraseChar(String input, Character aim){
    String output = new String("");
    for(int i=0; i<input.length(); i++){
      if(input.charAt(i) != aim) output += input.charAt(i);
    }
    return output;

  }
  //retourn le mode d'addressage
  public static int getAddrMode(String input){
    assert(input.length() > 0);
    switch (input.charAt(0)) {
          case '#':
            return 0x0;
          case '@':
            return 0x2;
          default: // cas ou il n'y a pas de symbol
            return 0x1;
    }
  }
  //cas ou on a un entier négatif
  public static int processNegatif(int input){
    if(input < 0){
      int ret = input*-1;
      if(8191 < ret){
        System.err.println("Value is to big: "+String.valueOf(input));
        System.exit(1);
      }
      ret = ((~ret)&0x3fff)+1 ;
      return ret;
    }
    if(8191 < input){
      System.err.println("Value is to big: "+String.valueOf(input));
      System.exit(1);
    }
    return input;
  }
  //make instruction with A and B
  public static byte[] makeInstruction(int opcode, String line){
    String[] args = eraseChar(line, SEPARATOR_TOKEN).split(",");
    if(args.length != 2){ // catch error
        System.err.println("Syntax Error: "+ line);
        System.exit(1);
    }
    String tokenA = args[0];
    String tokenB = args[1];
    int tokenAaddr = InstructionTradutor.getAddrMode(tokenA);
    if(tokenAaddr != 0x1){ //trim symbol
        tokenA = tokenA.substring(1,tokenA.length());
    }

    int tokenBaddr = InstructionTradutor.getAddrMode(tokenB);
    if(tokenBaddr != 0x1){ //trim symbol
        tokenB = tokenB.substring(1,tokenB.length());
    }

    int a = Integer.parseInt(tokenA);
    int b = Integer.parseInt(tokenB);
    //check pour le moins
    if(tokenAaddr != 0x1){
      a = InstructionTradutor.processNegatif(a);
    }
    if(tokenBaddr != 0x1){
      b = InstructionTradutor.processNegatif(b);
    }
    byte[] array = new byte[5];
    array[0] = (byte)((opcode<<4) | tokenAaddr);
    array[1] = (byte)((tokenBaddr << 4) | (a&0x3c00)>>10);
    array[2] = (byte)((a&0x3fc) >> 2);
    array[3] = (byte)(((a&0x3) << 6) | ((b&0x3f00)>>8));
    array[4] = (byte)(b&0xff);
    return array;
  }
  //make instruction just with A
  public static byte[] makeInstructionOne(int opcode, String line){
    String tokenA = eraseChar(line, SEPARATOR_TOKEN);
    int tokenAaddr = InstructionTradutor.getAddrMode(tokenA);
    if(tokenAaddr != 0x1){ //trim symbol
        tokenA = tokenA.substring(1,tokenA.length());
    }
    int a = Integer.parseInt(tokenA);
    //check pour le moins
    if(tokenAaddr != 0x1){
      a = InstructionTradutor.processNegatif(a);
    }
    byte[] array = new byte[3];
    array[0] = (byte)((opcode<<4) | tokenAaddr);
    array[1] = (byte)(a&0x3fc0);
    array[2] = (byte)(a&0x3f);
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
    String body = sanitizeLine.substring(opCodeDelimiter+1, sanitizeLine.length());
    if(opCode.equals(InstructionTradutor.instructionSet[0])){       // MOV
        return makeInstruction(0x1, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[1])){ // ADD
        return makeInstruction(0x2, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[2])){ //SUB
        return makeInstruction(0x3, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[3])){ // JMP
        return makeInstructionOne(0x4, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[4])){ // JMZ
        return makeInstruction(0x5, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[5])){ // JMG
        return makeInstruction(0x6, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[6])){ // DJZ
        return makeInstruction(0x7, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[7])){ // CMP
        return makeInstruction(0x8, body);
    }else if(opCode.equals(InstructionTradutor.instructionSet[8])){ // DAT
        return makeInstructionOne(0x0, body);
    }else{
      System.err.println("Unknown op code: "+ opCode);
      System.exit(1);
    }
    assert 1==0; // never reach
    return null;
  }
}
