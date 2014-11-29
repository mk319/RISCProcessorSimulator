package stages;

import java.util.concurrent.Callable;

import buffers.DecodeExecuteBuffer;
import buffers.ExecuteMemoryBuffer;
import components.Alu;

public class ExecutionStage implements Callable<ExecuteMemoryBuffer>
{
  private static boolean running = false;

  private final DecodeExecuteBuffer decodeExecuteBuffer;

  public ExecutionStage(DecodeExecuteBuffer decodeExecuteBuffer)
  {
    this.decodeExecuteBuffer = decodeExecuteBuffer;
  }

  @Override
  public ExecuteMemoryBuffer call() throws Exception
  {
    running = true;
    //TODO: Implement ExecutionStage
    ExecuteMemoryBuffer outBuffer = ExecuteMemoryBuffer.getInstance();

    //for testing show which instruction is being executed
    System.out.println("EXE");

    //PC: Need to do offset
    int incrementedPc = decodeExecuteBuffer.readIncrementedPc();

    int signExtended = decodeExecuteBuffer.readSignExtendedBytes();
    incrementedPc = incrementedPc + signExtended;

    outBuffer.writeIncrementedPcWithOffwrite(incrementedPc);
    //ALU Control
    int functionCode = decodeExecuteBuffer.readSignExtendedBytes() & 0x0003;

    int aluControlInput = Alu.getAluControl(decodeExecuteBuffer.readAluOp2(),
                                            decodeExecuteBuffer.readAluOp1(),
                                            decodeExecuteBuffer.readAluOp0(),
                                            functionCode);
    if (decodeExecuteBuffer.readAluSrc())
    {
      outBuffer.writeAluResult(Alu.performALU(aluControlInput,
                                              decodeExecuteBuffer.readRegReadValue1(),
                                              decodeExecuteBuffer.readSignExtendedBytes()));
    }
    else
    {
      outBuffer.writeAluResult(Alu.performALU(aluControlInput,
                                              decodeExecuteBuffer.readRegReadValue1(),
                                              decodeExecuteBuffer.readRegReadValue2()));
    }

    //Rt or Rd
    if (decodeExecuteBuffer.readRegDst())
    {
      outBuffer.writeDestinationRegisterAddress(decodeExecuteBuffer.readRd());
    }
    else
    {
      outBuffer.writeDestinationRegisterAddress(decodeExecuteBuffer.readRt());
    }

    outBuffer.writeJump(decodeExecuteBuffer.readJump());
    outBuffer.writeMemRead(decodeExecuteBuffer.readMemRead());
    outBuffer.writeMemWrite(decodeExecuteBuffer.readMemWrite());

    outBuffer.writeMemToReg(decodeExecuteBuffer.readMemToReg());
    outBuffer.writeRegWrite(decodeExecuteBuffer.readRegWrite());

    running = false;
    return outBuffer;
  }

  public static boolean isRunning()
  {
    return running;
  }

  public static void setRunning(boolean running)
  {
    ExecutionStage.running = running;
  }
}
