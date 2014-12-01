package buffers;


/**
 * The decode stage writes data to the DecodeExecuteBuffer to be read by the execute stage. This
 * class has been made a singleton to allow for forwarding.
 */
public class DecodeExecuteBuffer
{
  private int incrementedPc;
  private int signExtendedBytes;

  //values that are read from registers
  private int regReadValue1;
  private int regReadValue2;

  //instruction [6-3]
  private int rd;
  //instruction [6-9]
  private int rt;

  //CONTROL SIGNALS
  //EX
  private boolean aluSrc;
  private boolean aluOp0;
  private boolean aluOp1;
  private boolean aluOp2;
  private boolean regDst;

  //MEM
  private boolean branch;
  private boolean jump;
  private boolean memWrite;
  private boolean memRead;
  private int jumpAddress;

  //WB
  private boolean memToReg;
  private boolean regWrite;

  private static DecodeExecuteBuffer instance;

  public static DecodeExecuteBuffer getInstance()
  {
    if(instance == null)
    {
      instance = new DecodeExecuteBuffer();
    }
    return instance;
  }

  public int readIncrementedPc()
  {
    return incrementedPc;
  }

  public void writeIncrementedPc(int incrementedPc)
  {
    this.incrementedPc = incrementedPc;
  }

  public int readSignExtendedBytes()
  {
    return signExtendedBytes;
  }

  public void writeSignExtendedBytes(int signExtendedBytes)
  {
    this.signExtendedBytes = signExtendedBytes;
  }

  public int readRegReadValue1()
  {
    return regReadValue1;
  }

  public void writeRegReadValue1(int regReadValue1)
  {
    this.regReadValue1 = regReadValue1;
  }

  public int readRegReadValue2()
  {
    return regReadValue2;
  }

  public void writeRegReadValue2(int regReadValue2)
  {
    this.regReadValue2 = regReadValue2;
  }

  public int readRd()
  {
    return rd;
  }

  public void writeRd(int rd)
  {
    this.rd = rd;
  }

  public int readRt()
  {
    return rt;
  }

  public void writeRt(int rt)
  {
    this.rt = rt;
  }

  public boolean readAluSrc()
  {
    return aluSrc;
  }

  public void writeAluSrc(boolean aluSrc)
  {
    this.aluSrc = aluSrc;
  }

  public boolean readAluOp0()
  {
    return aluOp0;
  }

  public void writeAluOp0(boolean aluOp0)
  {
    this.aluOp0 = aluOp0;
  }

  public boolean readAluOp1()
  {
    return aluOp1;
  }

  public void writeAluOp1(boolean aluOp1)
  {
    this.aluOp1 = aluOp1;
  }

  public boolean readAluOp2()
  {
    return aluOp2;
  }

  public void writeAluOp2(boolean aluOp2)
  {
    this.aluOp2 = aluOp2;
  }

  public boolean readRegDst()
  {
    return regDst;
  }

  public void writeRegDst(boolean regDst)
  {
    this.regDst = regDst;
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
