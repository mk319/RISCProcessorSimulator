package components;

import buffers.DecodeExecuteBuffer;
import buffers.FetchDecodeBuffer;

public class HazardDetectionUnit
{
   public static void decodeHazard()
   {
     DecodeExecuteBuffer decodeExecuteBuffer = DecodeExecuteBuffer.getInstance();
     FetchDecodeBuffer fetchDecodeBuffer = FetchDecodeBuffer.getInstance();
     if(decodeExecuteBuffer.readMemRead()
             && (decodeExecuteBuffer.readRt()
             == (fetchDecodeBuffer.readInstruction() & 0x0E00 >>> 9)
             || decodeExecuteBuffer.readRt()
             == (fetchDecodeBuffer.readInstruction() & 0x01C0 >>> 6)))
     {
       fetchDecodeBuffer.writeInstruction(0x1000);
       fetchDecodeBuffer.writeIncrementedPc(fetchDecodeBuffer.readIncrementedPc()-1);
     }
   }

}
