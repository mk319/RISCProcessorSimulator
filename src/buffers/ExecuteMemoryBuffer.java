package buffers;


/**
 * The execute stage writes data to the ExecuteMemoryBuffer to be read by the memory stage.
 * This class has been made a singleton to allow for forwarding.
 */
public class ExecuteMemoryBuffer
{
  private int incrementedPcWithOffset;
  
  private int aluResult;
  private boolean aluZeroResult;

  private int regReadValue2;
  
  private int destinationRegisterAddress;
  
  //CONTROL SIGNALS
  //MEM
  private boolean branch;
  private boolean jump;
  private boolean memWrite;
  private boolean memRead;
  private int jumpAddress;
  //WB
  private boolean memToReg;
  private boolean regWrite;

  private static ExecuteMemoryBuffer instance;

  public static ExecuteMemoryBuffer getInstance()
  {
    if(instance == null)
    {
      instance = new ExecuteMemoryBuffer();
    }
    return instance;
  }

  public int readIncrementedPcWithOffset()
  {
    return incrementedPcWithOffset;
  }

  public void writeIncrementedPcWithOffset(int incrementedPcWithOffset)
  {
    this.incrementedPcWithOffset = incrementedPcWithOffset;
  }

  public int readAluResult()
  {
    return aluResult;
  }

  public void writeAluResult(int aluResult)
  {
    this.aluResult = aluResult;
  }

  public boolean readAluZeroResult()
  {
    return aluZeroResult;
  }

  public void writeAluZeroResult(boolean aluZeroResult)
  {
    this.aluZeroResult = aluZeroResult;
  }

  public int readRegReadValue2()
  {
    return regReadValue2;
  }

  public void writeRegReadValue2(int regReadValue2)
  {
    this.regReadValue2 = regReadValue2;
  }

  public int readDestinationRegisterAddress()
  {
    return destinationRegisterAddress;
  }

  public void writeDestinationRegisterAddress(int destinationRegisterAddress)
  {
    this.destinationRegisterAddress = destinationRegisterAddress;
  }

  public boolean readBranch()
  {
    return branch;
  }

  public void writeBranch(boolean branch)
  {
    this.branch = branch;
  }

  public boolean readMemWrite()
  {
    return memWrite;
  }

  public void writeMemWrite(boolean memWrite)
  {
    this.memWrite = memWrite;
  }

  public boolean readMemRead()
  {
    return memRead;
  }

  public void writeMemRead(boolean memRead)
  {
    this.memRead = memRead;
  }

  public boolean readMemToReg()
  {
    return memToReg;
  }

  public void writeMemToReg(boolean memToReg)
  {
    this.memToReg = memToReg;
  }

  public boolean readRegWrite()
  {
    return regWrite;
  }

  public void writeRegWrite(boolean regWrite)
  {
    this.regWrite = regWrite;
  }

  public boolean readJump()
  {
    return jump;
  }

  public void writeJump(boolean jump)
  {
    this.jump = jump;
  }

  public int readJumpAddress()
  {
    return jumpAddress;
  }

  public void writeJumpAddress(int jumpAddress)
  {
    this.jumpAddress = jumpAddress;
  }
}
