package components;

import com.sun.javaws.exceptions.InvalidArgumentException;

public class Alu
{
  private static final int ALU_ADD = 0b0010;
  private static final int ALU_SUB = 0b0110;
  private static final int ALU_AND = 0b0000;
  private static final int ALU_OR  = 0b0001;
  private static final int ALU_XOR = 0b0011;
  private static final int ALU_SLL = 0b1011;
  private static final int ALU_SRA = 0b1000;
  private static final int ALU_SRL = 0b1001;
  private static final int ALU_SLT = 0b0111;

  public static int performALU(int aluControlCode, int operand1, int operand2)
  {
    switch (aluControlCode)
    {
      case ALU_SLL: //sll
        return operand1 << 4;

      case ALU_SRA: //sra
        return operand1 >> operand2;

      case ALU_SRL: //srl
        return operand1 >>> operand2;

      case ALU_AND: //and
        return operand1 & operand2;

      case ALU_XOR: //xor
        return operand1 ^ operand2;

      case ALU_OR: //or
        return operand1 | operand2;

      case ALU_SLT: //slt
        return operand1 < operand2 ? 1 : 0;

      case ALU_ADD:
        return operand1 + operand2;

      case ALU_SUB:
        return operand1 - operand2;
      default:
        throw new IllegalArgumentException("ALU operation does not exists.");
    }
  }

  public static int getAluControl(boolean aluOp2, boolean aluOp1, boolean aluOp0, int functionCode)
  {
    //ALUOp = 1XX
    if (aluOp2)
    {
      //use function code
      switch (functionCode)
      {
        case 0b000:
          //ALUOp = 101
          if (!aluOp1 && aluOp0)
          {
            return ALU_ADD;
          }
          //ALUOp = 110
          else if (aluOp1 && !aluOp0)
          {
            return ALU_SLL;
          }
        case 0b001:
          //ALUOp = 101
          if (!aluOp1 && aluOp0)
          {
            return ALU_SUB;
          }
          //ALUOp = 110
          else if (aluOp1 && !aluOp0)
          {
            return ALU_SRA;
          }
        case 0b010:
          return ALU_SRL;
        case 0b011:
          return ALU_AND;
        case 0b110:
          return ALU_OR;
        case 0b100:
          return ALU_XOR;
        case 0b101:
          return ALU_SLT;
        default:
          throw new IllegalArgumentException("Invalid function code.");
      }
    }
    //ALUOp = 0XX
    else
    {
      //ALUOp = 000
      if (!aluOp1 && !aluOp0)
      {
        //load, store, or addi
        return ALU_ADD;
      }
      //ALUOp = 010
      else if (aluOp1 && !aluOp0)
      {
        //SLTI
        return ALU_SLT;
      }
      //ALUOp = 001
      else if (!aluOp1)
      {
        //beq
        return ALU_SUB;
      }
      //ALUOp = 111
      else
      {
        //don't care
        return ALU_ADD;
      }
    }
  }
}
