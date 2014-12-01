import java.util.concurrent.ExecutionException;

import components.Cpu;
import components.Memory;
import components.RegisterFile;

public class Main
{

  public static void main(String[] args)
  {
    Cpu.init();

    try
    {
      System.out.println("===Start of Execution===");
      printState();
      Cpu.execute();
      System.out.println("===End of Execution===");
      printState();
    } catch (ExecutionException | InterruptedException e)
    {
      e.printStackTrace();
    }
  }


  public static void printState()
  {
    //this method assumes memory is always larger than the register file
    Memory memory = Memory.getInstance();
    RegisterFile registerFile = RegisterFile.getInstance();
     System.out.print("--------------------\t\t\t"); System.out.println("--------------------");
     System.out.print("| Memory Locations |\t\t\t"); System.out.println("|   Register File  |");
     System.out.print("--------------------\t\t\t"); System.out.println("--------------------");
     System.out.print("| Location | Value |\t\t\t"); System.out.println("| Location | Value |");
    for(int i=0; i< memory.getMemorySize();++i)
    {
      System.out.format("|     [%2x] | 0x%-4x|\t\t\t",i,memory.getMemory(i));
      if(i<registerFile.getRegisterFileSize())
      {
        System.out.format("|     [%2x] | 0x%-4x|%n",i,registerFile.getRegister(i));
      }
      else
      {
        System.out.format("%n");
      }
    }
  }

}