package sie.amplifier_conctroller.DataStruct;

public class DataStruct_EQ_Output_All
{
  public DataStruct_EQ_Output[] OutPut_EQ = new DataStruct_EQ_Output[8];
  
  public DataStruct_EQ_Output_All()
  {
    int i = 0;
    for (;;)
    {
      if (i >= 8) {
        return;
      }
      this.OutPut_EQ[i] = new DataStruct_EQ_Output();
      i += 1;
    }
  }
}


/* Location:              C:\Users\Administrator\Documents\Tencent\QQPhoneManager\Application\新建文件夹\classes-dex2jar.jar!\leon\android\DataStruct\DataStruct_EQ_Output_All.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */