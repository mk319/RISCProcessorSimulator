package components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    }
  }

  private static ArrayList<Integer> loadInstructions()
  {

    ArrayList<Integer> instructions = new ArrayList<Integer>();
    try (BufferedReader br = new BufferedReader(new FileReader("src\\Instructions.code")))
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
