package stages;

import java.util.concurrent.Callable;

import buffers.DecodeExecuteBuffer;
import buffers.FetchDecodeBuffer;
import components.RegisterFile;

public class DecodeStage implements Callable<DecodeExecuteBuffer>
{
  private static boolean running = false;

  //masks to allow to get specific parts of the instruction word
  private static final int OPCODE_MASK      = 0xF000;
  private static final int SIGN_EXTEND_MASK = 0x003F;
  private static final int RS_MASK          = 0x0E00;
  private static final int RT_MASK          = 0x01C0;
  private static final int RD_MASK          = 0x0038;

  //opcodes for each instruction type
  private static final int OPCODE_R_FORMAT_0 = 0b0000;
  private static final int OPCODE_R_FORMAT_1 = 0b0010;
  private static final int OPCODE_SW         = 0b1011;
  private static final int OPCODE_LW         = 0b1100;
  private static final int OPCODE_BEQ        = 0b1001;
  private static final int OPCODE_ADDI       = 0b0001;
  private static final int OPCODE_SLTI       = 0b0011;
  private static final int OPCODE_JMP        = 0b1111;


  private final FetchDecodeBuffer   fetchDecodeBuffer;
  private       DecodeExecuteBuffer outBuffer;

  public DecodeStage(FetchDecodeBuffer fetchDecodeBuffer)
  {
    this.fetchDecodeBuffer = fetchDecodeBuffer;
  }

  @Override
  public DecodeExecuteBuffer call() throws Exception
  {
    running = true;

    //get the instruction word from the buffer
    int instruction = fetchDecodeBuffer.readInstruction();

    //initialize new buffer for Execute stage
    outBuffer = DecodeExecuteBuffer.getInstance();

    //get the OPCODE from the instruction
    int opCode = (OPCODE_MASK & instruction) >> 12;

    //for testing show which instruction is being executed
    System.out.println("DEC: " + Integer.toHexString(opCode) + "\t\t\t" + String
            .format("%16s", Integer.toBinaryString(instruction)).replace(' ', '0'));

    //set control signals based on instruction type
    switch (opCode)
    {
      //R - Format
      case OPCODE_R_FORMAT_0:
        setRFormatControlSignals();
        outBuffer.writeAluOp1(false);
        outBuffer.writeAluOp0(true);
        break;
      case OPCODE_R_FORMAT_1:
        setRFormatControlSignals();
        outBuffer.writeAluOp1(true);
        outBuffer.writeAluOp0(false);
        break;
      //SW
      case OPCODE_SW:
        setSwControlSignals();
        break;
      //LW
      case OPCODE_LW:
        setLwControlSignals();
        break;
      //BEQ
      case OPCODE_BEQ:
        setBeqControlSignals();
        break;
      //ADDI
      case OPCODE_ADDI:
        setAddiControlSignals();
        break;
      //SLTI
      case OPCODE_SLTI:
        setSltiControlSignals();
        break;
      //JMP
      case OPCODE_JMP:
        setJmpControlSignals();
        break;
      //opcode did not match the opcode of a valid instruction
      default:
        throw new Exception("Invalid instruction!");
    }

    outBuffer.writeIncrementedPc(fetchDecodeBuffer.readIncrementedPc());
    outBuffer.writeSignExtendedBytes(instruction & SIGN_EXTEND_MASK);

    //Read from register file
    RegisterFile registerFile = RegisterFile.getInstance();
    int rs = (instruction & RS_MASK) >>> 9;
    outBuffer.writeRegReadValue1(registerFile.getRegister(rs));
    int rt = (instruction & RT_MASK) >>> 6;
    outBuffer.writeRegReadValue2(registerFile.getRegister(rt));
    outBuffer.writeRt(rt);

    outBuffer.writeJumpAddress(instruction & 0x0FFF);

    int rd = (instruction & RD_MASK) >>> 3;
    outBuffer.writeRd(rd);

    running = false;
    return outBuffer;
  }

  private void setAddiControlSignals()
  {
    //RegDst = 0
    outBuffer.writeRegDst(false);
    //ALUOp2 = 0
    outBuffer.writeAluOp2(false);
    //ALUOp1 = 0
    outBuffer.writeAluOp1(false);
    //ALUOp0 = 0
    outBuffer.writeAluOp0(false);
    //ALUSrc = 1
    outBuffer.writeAluSrc(true);
    //Branch = 0
    outBuffer.writeBranch(false);
    //MemRead = 0
    outBuffer.writeMemRead(false);
    //MemWrite = 0
    outBuffer.writeMemWrite(false);
    //RegWrite = 1
    outBuffer.writeRegWrite(true);
    //MemToReg = 0
    outBuffer.writeMemToReg(false);
    //Jump = 0
    outBuffer.writeJump(false);
  }

  private void setSltiControlSignals()
  {
    //RegDst = 0
    outBuffer.writeRegDst(false);
    //ALUOp2 = 0
    outBuffer.writeAluOp2(false);
    //ALUOp1 = 1
    outBuffer.writeAluOp1(false);
    //ALUOp0 = 0
    outBuffer.writeAluOp0(false);
    //ALUSrc = 1
    outBuffer.writeAluSrc(true);
    //Branch = X
    outBuffer.writeBranch(false);
    //MemRead = 0
    outBuffer.writeMemRead(false);
    //MemWrite = 0
    outBuffer.writeMemWrite(false);
    //RegWrite = 1
    outBuffer.writeRegWrite(true);
    //MemToReg = X
    outBuffer.writeMemToReg(false);
    //Jump = 0
    outBuffer.writeJump(false);
  }

  private void setJmpControlSignals()
  {
    //RegDst = X
    outBuffer.writeRegDst(false);
    //ALUOp1 = X
    outBuffer.writeAluOp2(true);
    //ALUOp1 = X
    outBuffer.writeAluOp1(true);
    //ALUOp0 = X
    outBuffer.writeAluOp0(true);
    //ALUSrc = X
    outBuffer.writeAluSrc(false);
    //Branch = X
    outBuffer.writeBranch(false);
    //MemRead = 0
    outBuffer.writeMemRead(false);
    //MemWrite = 0
    outBuffer.writeMemWrite(false);
    //RegWrite = 0
    outBuffer.writeRegWrite(false);
    //MemToReg = X
    outBuffer.writeMemToReg(false);
    //Jump = 1
    outBuffer.writeJump(true);
  }

  private void setBeqControlSignals()
  {
    //RegDst = X
    outBuffer.writeRegDst(false);
    //ALUOp2 = 0
    outBuffer.writeAluOp2(false);
    //ALUOp1 = 0
    outBuffer.writeAluOp1(false);
    //ALUOp0 = 1
    outBuffer.writeAluOp0(true);
    //ALUSrc = 0
    outBuffer.writeAluSrc(false);
    //Branch = 1
    outBuffer.writeBranch(true);
    //MemRead = 0
    outBuffer.writeMemRead(false);
    //MemWrite = 0
    outBuffer.writeMemWrite(false);
    //RegWrite = 0
    outBuffer.writeRegWrite(false);
    //MemToReg = X
    outBuffer.writeMemToReg(false);
    //Jump = 0
    outBuffer.writeJump(false);
  }

  private void setLwControlSignals()
  {
    //RegDst = 0
    outBuffer.writeRegDst(false);
    //ALUOp2 = 0
    outBuffer.writeAluOp1(false);
    //ALUOp1 = 0
    outBuffer.writeAluOp1(false);
    //ALUOp0 = 0
    outBuffer.writeAluOp0(false);
    //ALUSrc = 1
    outBuffer.writeAluSrc(true);
    //Branch = 0
    outBuffer.writeBranch(false);
    //MemRead = 1
    outBuffer.writeMemRead(true);
    //MemWrite = 0
    outBuffer.writeMemWrite(false);
    //RegWrite = 1
    outBuffer.writeRegWrite(true);
    //MemToReg = 1
    outBuffer.writeMemToReg(true);
    //Jump = 0
    outBuffer.writeJump(false);
  }

  private void setSwControlSignals()
  {
    //RegDst = X
    outBuffer.writeRegDst(false);
    //ALUOp2 = 0
    outBuffer.writeAluOp2(false);
    //ALUOp1 = 0
    outBuffer.writeAluOp1(false);
    //ALUOp0 = 0
    outBuffer.writeAluOp0(false);
    //ALUSrc = 1
    outBuffer.writeAluSrc(true);
    //Branch = 0
    outBuffer.writeBranch(false);
    //MemRead = 0
    outBuffer.writeMemRead(false);
    //MemWrite = 1
    outBuffer.writeMemWrite(true);
    //RegWrite = 0
    outBuffer.writeRegWrite(false);
    //MemToReg = X
    outBuffer.writeMemToReg(false);
    //Jump = 0
    outBuffer.writeJump(false);
  }

  private void setRFormatControlSignals()
  {
    //RegDst = 1
    outBuffer.writeRegDst(true);
    //ALUSrc = 0
    outBuffer.writeAluSrc(false);
    //MemToReg = 1
    outBuffer.writeMemToReg(false);
    //RegWrite = 1
    outBuffer.writeRegWrite(true);
    //MemWrite = 0
    outBuffer.writeMemWrite(false);
    //MemRead = 0
    outBuffer.writeMemRead(false);
    
    outBuffer.writeMemWrite(false);
    //Branch = 0
    outBuffer.writeBranch(false);
    //ALUOp2 = 1
    outBuffer.writeAluOp2(true);
    //Jump = 0
    outBuffer.writeJump(false);
  }

  public static boolean isRunning()
  {
    return running;
  }

  public static void setRunning(boolean running)
  {
    DecodeStage.running = running;
  }
}
