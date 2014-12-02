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
    ExecuteMemoryBuffer outBuffer = ExecuteMemoryBuffer.getInstance();

    //for testing show which instruction is being executed
   // System.out.println("EXE");

    //PC: Need to do offset
    int incrementedPc = decodeExecuteBuffer.readIncrementedPc();

    int signExtended = decodeExecuteBuffer.readSignExtendedBytes();

    outBuffer.writeIncrementedPcWithOffset(incrementedPc + signExtended);

    //ALU Control
    int functionCode = decodeExecuteBuffer.readSignExtendedBytes() & 0x0007;

    int aluControlInput = Alu.getAluControl(decodeExecuteBuffer.readAluOp2(),
                                            decodeExecuteBuffer.readAluOp1(),
                                            decodeExecuteBuffer.readAluOp0(),
                                            functionCode);
    if (decodeExecuteBuffer.readAluSrc())
    {
      outBuffer.writeAluResult(Alu.performALU(aluControlInput,
                                              decodeExecuteBuffer.readRegReadValue1(),
                                              decodeExecuteBuffer.readSignExtendedBytes(),
                                              decodeExecuteBuffer.readRt()));
    }
    else
    {
      outBuffer.writeAluResult(Alu.performALU(aluControlInput,
                                              decodeExecuteBuffer.readRegReadValue1(),
                                              decodeExecuteBuffer.readRegReadValue2(),
                                              decodeExecuteBuffer.readRt()));
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

    outBuffer.writeAluZeroResult(outBuffer.readAluResult() == 0);
    outBuffer.writeRegReadValue2(decodeExecuteBuffer.readRegReadValue2());
    outBuffer.writeBranch(decodeExecuteBuffer.readBranch());
    outBuffer.writeJump(decodeExecuteBuffer.readJump());
    outBuffer.writeMemRead(decodeExecuteBuffer.readMemRead());
    outBuffer.writeMemWrite(decodeExecuteBuffer.readMemWrite());

    outBuffer.writeMemToReg(decodeExecuteBuffer.readMemToReg());
    outBuffer.writeRegWrite(decodeExecuteBuffer.readRegWrite());
    outBuffer.writeJumpAddress(decodeExecuteBuffer.readJumpAddress());

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
