package components;

public class RegisterFile
{
  private static RegisterFile instance           = null;
  private final  int          REGISTER_FILE_SIZE = 8;
  private int[] registers;

  private int pc;

  private RegisterFile()
  {
    registers = new int[REGISTER_FILE_SIZE];
    for (int i = 0; i < REGISTER_FILE_SIZE; i++)
    {
      registers[i] = 0x00;
    }
  }

  //Singleton to maintain only one RegisterFile
  public static RegisterFile getInstance()
  {
    if (instance == null)
    {
      instance = new RegisterFile();
    }
    return instance;
  }

  public void setRegister(int index, int value)
  {

    if (index >= REGISTER_FILE_SIZE || index < 0)
    {
      throw new ArrayIndexOutOfBoundsException("Attempting to write to register that does not " +
                                               "exist" +
                                               ".");
    }
    if (index != 0)
    {
      registers[index] = value;
    }
  }

  public int getRegister(int index)
  {
    if (index >= REGISTER_FILE_SIZE || index < 0)
    {
      throw new ArrayIndexOutOfBoundsException("Attempting to read from register that does not " +
                                               "exist" +
                                               ".");
    }
    return registers[index];
  }

  public int getRegisterFileSize()
  {
    return REGISTER_FILE_SIZE;
  }

  public void writePc(int pc)
  {
    this.pc = pc;
  }

  public int getPc()
  {
    return pc;
  }

  public void setPc(int pc)
  {
    this.pc = pc;
  }
}
