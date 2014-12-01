package components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import buffers.DecodeExecuteBuffer;
import buffers.ExecuteMemoryBuffer;
import buffers.FetchDecodeBuffer;
import buffers.MemoryWriteBackBuffer;
import stages.DecodeStage;
import stages.ExecutionStage;
import stages.FetchStage;
import stages.MemoryStage;
import stages.WriteBackStage;

public class Cpu
{
  private static Memory       mem;
  private static RegisterFile reg;

  private static final String INSTRUCTIONS_FILE = "src\\Instructions.code";
  private static final String DEBUG_INSTRUCTIONS_FILE = "src\\Test.code";

  private static boolean PCSrc = false;

  /**
   * get the CPU ready to begin executing instructions
   */
  public static void init()
  {
    mem = Memory.getInstance();

    //load instructions into the instruction memory
    mem.setInstructionMemory(loadInstructions());

    reg = RegisterFile.getInstance();
  }

  /**
   * Start processing instructions
   *
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @SuppressWarnings("StatementWithEmptyBody")
  public static void execute() throws ExecutionException, InterruptedException
  {

    ExecutorService pool = Executors.newFixedThreadPool(5);
    Future<FetchDecodeBuffer> fetchFuture = null;
    Future<DecodeExecuteBuffer> decodeFuture = null;
    Future<ExecuteMemoryBuffer> executeFuture = null;
    Future<MemoryWriteBackBuffer> memoryFuture = null;
    Future<Void> writeBackFuture = null;

    //execution is complete when the PC is larger than the number of instructions

    while (reg.getPc() != mem.getInstructionMemorySize())
    {
      //Each stage is submitted to the thread pool. The future is then saved and passed to the next
      //thread. This means that the next thread will now wait for the result of previous thread
      // before it executes.
      //The empty while loop is to make sure that thread is not currently executing before
      // executing the next instruction

      //wait for fetch stage to be free
      while (FetchStage.isRunning())
      {
      }
      //start new fetch stage
      fetchFuture = pool.submit(new FetchStage());

      //wait for decode stage to be free
      while (DecodeStage.isRunning())
      {
      }
      decodeFuture = pool.submit(new DecodeStage(fetchFuture.get()));

      //wait for execution stage to be free
      while (ExecutionStage.isRunning())
      {
      }
      //start new execution stage
      executeFuture = pool.submit(new ExecutionStage(decodeFuture.get()));

      //wait for memory stage to be free
      while (MemoryStage.isRunning())
      {
      }
      //start new memory stage
      memoryFuture = pool.submit(new MemoryStage(executeFuture.get()));

      //wait for write back stage to be free
      while (WriteBackStage.isRunning())
      {
      }
      //start new write back stage
      writeBackFuture = pool.submit(new WriteBackStage(memoryFuture.get()));
      writeBackFuture.get();
      printState();
    }
    pool.shutdown();
    try
    {
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }catch(InterruptedException e)
    {
      e.printStackTrace();
    }

  }

  public static void printState()
  {
    //this method assumes memory is always larger than the register file
    Memory memory = Memory.getInstance();
    RegisterFile registerFile = RegisterFile.getInstance();
    PrintWriter writer = null;
    try
    {
      writer = new PrintWriter("debug.log", "UTF-8");
    writer.println("PC = " + reg.getPc());
    writer.print("--------------------\t\t\t"); writer.println("--------------------");
    writer.print("| Memory Locations |\t\t\t"); writer.println("|   Register File  |");
    writer.print("--------------------\t\t\t"); writer.println("--------------------");
    writer.print("| Location | Value |\t\t\t"); writer.println("| Location | Value |");
    for(int i=0; i<mem.getMemorySize();++i)
    {
      writer.format("|     [%2x] | 0x%-4x|\t\t\t",i,memory.getMemory(i));
      if(i<registerFile.getRegisterFileSize())
      {
        writer.format("|     [%2x] | 0x%-4x|%n",i,registerFile.getRegister(i));
      }
      else
      {
        writer.format("%n");
      }
    }

    } catch (FileNotFoundException | UnsupportedEncodingException e)
    {
      e.printStackTrace();
    } finally
    {
      if(writer != null)
      writer.close();
    }
  }

  private static ArrayList<Integer> loadInstructions()
  {

    ArrayList<Integer> instructions = new ArrayList<Integer>();
    try (BufferedReader br = new BufferedReader(new FileReader(INSTRUCTIONS_FILE)))
    {
      String line;
      while ((line = br.readLine()) != null)
      {
        instructions.add(Integer.parseInt(line, 2));
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    return instructions;
  }

  public static boolean isPCSrc()
  {
    return PCSrc;
  }

  public static void setPCSrc(boolean PCSrc)
  {
    Cpu.PCSrc = PCSrc;
  }
}
