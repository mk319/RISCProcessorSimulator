package stages;

import java.util.concurrent.Callable;

import buffers.MemoryWriteBackBuffer;
import components.RegisterFile;

public class WriteBackStage implements Callable<Void>
{
  private static boolean running = false;

  private final MemoryWriteBackBuffer memoryWriteBackBuffer;

  public WriteBackStage(MemoryWriteBackBuffer memoryWriteBackBuffer)
  {
    this.memoryWriteBackBuffer = memoryWriteBackBuffer;
  }

  @Override
  public Void call() throws Exception
  {
    running = true;
    //System.out.println("WB");
    if(memoryWriteBackBuffer.readRegWrite())
    {
      RegisterFile registerFile = RegisterFile.getInstance();
      if(memoryWriteBackBuffer.readMemToReg())
      {
        //write from memory to register
        registerFile.setRegister(memoryWriteBackBuffer.readDestinationRegisterAddress(),
                                 memoryWriteBackBuffer.readDataReadFromMemory());
      }
      else
      {
        //write from ALU to register
        registerFile.setRegister(memoryWriteBackBuffer.readDestinationRegisterAddress(),
                                 memoryWriteBackBuffer.readALUResult());
      }
    }
    running = false;
    return null;
  }

  public static boolean isRunning()
  {
    return running;
  }

  public static void setRunning(boolean running)
  {
    WriteBackStage.running = running;
  }
}
