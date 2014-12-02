package stages;

import java.util.concurrent.Callable;

import buffers.ExecuteMemoryBuffer;
import buffers.MemoryWriteBackBuffer;
import components.Memory;
import components.RegisterFile;

public class MemoryStage implements Callable<MemoryWriteBackBuffer>
{
  private static boolean running = false;

  private final ExecuteMemoryBuffer executeMemoryBuffer;

  public MemoryStage(ExecuteMemoryBuffer executeMemoryBuffer)
  {
    this.executeMemoryBuffer = executeMemoryBuffer;
  }

  @Override
  public MemoryWriteBackBuffer call() throws Exception
  {
    running = true;
    MemoryWriteBackBuffer outBuffer = MemoryWriteBackBuffer.getInstance();

    //System.out.println("MEM");

    Memory memory = Memory.getInstance();
    if (executeMemoryBuffer.readMemRead())
    {
      //read from the memory location calculated by the ALU
      outBuffer.writeDataReadFromMemory(memory.getMemory(executeMemoryBuffer.readAluResult()));
    }
    else
    {
      //read from "random" memory location
      outBuffer.writeDataReadFromMemory(0);
    }

    if (executeMemoryBuffer.readMemWrite())
    {
      //write value from register to memory
      memory.setMemory(executeMemoryBuffer.readAluResult(),
                       executeMemoryBuffer.readRegReadValue2());
    }

    //set PCSrc: Branch & ALUZero
    RegisterFile registerFile = RegisterFile.getInstance();
    if(executeMemoryBuffer.readBranch() && executeMemoryBuffer.readAluZeroResult())
    {
      registerFile.writePc(executeMemoryBuffer.readIncrementedPcWithOffset());
    }
    if(executeMemoryBuffer.readJump())
    {
      registerFile.writePc(executeMemoryBuffer.readJumpAddress());
    }

    //pass write back stage signals on
    outBuffer.writeMemToReg(executeMemoryBuffer.readMemToReg());
    outBuffer.writeRegWrite(executeMemoryBuffer.readRegWrite());
    outBuffer.writeAluResult(executeMemoryBuffer.readAluResult());
    outBuffer.writeDestinationRegisterAddress(executeMemoryBuffer.readDestinationRegisterAddress());

    running = false;
    return outBuffer;
  }

  public static boolean isRunning()
  {
    return running;
  }

  public static void setRunning(boolean running)
  {
    MemoryStage.running = running;
  }
}
