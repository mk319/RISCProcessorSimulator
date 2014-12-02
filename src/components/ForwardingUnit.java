package components;

import buffers.DecodeExecuteBuffer;
import buffers.ExecuteMemoryBuffer;
import buffers.MemoryWriteBackBuffer;

public class ForwardingUnit
{
//  public static void exeMemHazardCheck()
//  {
//    ExecuteMemoryBuffer executeMemoryBuffer = ExecuteMemoryBuffer.getInstance();
//    DecodeExecuteBuffer decodeExecuteBuffer = DecodeExecuteBuffer.getInstance();
//
//    //EX/MEM hazard
//    if (executeMemoryBuffer.readRegWrite()
//        && executeMemoryBuffer.readDestinationRegisterAddress() != 0
//        && executeMemoryBuffer.readDestinationRegisterAddress() == decodeExecuteBuffer.readRs())
//    {
//      decodeExecuteBuffer.writeRegReadValue1(executeMemoryBuffer.readAluResult());
//    }
//
//    if (executeMemoryBuffer.readRegWrite()
//        && executeMemoryBuffer.readDestinationRegisterAddress() != 0
//        && executeMemoryBuffer.readDestinationRegisterAddress() == decodeExecuteBuffer.readRt())
//    {
//      decodeExecuteBuffer.writeRegReadValue2(executeMemoryBuffer.readAluResult());
//    }
//  }

//  public static void memWbHazardCheck()
//  {
//    //MEM/WB hazard
//    DecodeExecuteBuffer decodeExecuteBuffer = DecodeExecuteBuffer.getInstance();
//    MemoryWriteBackBuffer memoryWriteBackBuffer = MemoryWriteBackBuffer.getInstance();
//    if(memoryWriteBackBuffer.readRegWrite()
//       && memoryWriteBackBuffer.readDestinationRegisterAddress() != 0
//       && memoryWriteBackBuffer.readDestinationRegisterAddress() == decodeExecuteBuffer.readRs())
//    {
//      decodeExecuteBuffer.writeRegReadValue1(memoryWriteBackBuffer.readDataReadFromMemory());
//    }
//    if(memoryWriteBackBuffer.readRegWrite()
//       && memoryWriteBackBuffer.readDestinationRegisterAddress() != 0
//       && memoryWriteBackBuffer.readDestinationRegisterAddress() == decodeExecuteBuffer.readRt())
//    {
//      decodeExecuteBuffer.writeRegReadValue2(memoryWriteBackBuffer.readDataReadFromMemory());
//    }
//  }
}
