package sie.amplifier_conctroller.DataStruct;

public class DataStruct_EQ_Output
{
  public DataStruct_EQ[] EQ = new DataStruct_EQ[31];
  
  public DataStruct_EQ_Output()
  {
    int i = 0;
    for (;;)
    {
      if (i >= 31) {
        return;
      }
      this.EQ[i] = new DataStruct_EQ();
      i += 1;
    }
  }
}


/* Location:              C:\Users\Administrator\Documents\Tencent\QQPhoneManager\Application\新建文件夹\classes-dex2jar.jar!\leon\android\DataStruct\DataStruct_EQ_Output.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */