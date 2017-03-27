package sie.amplifier_conctroller.DataStruct;

public class DataStruct_EFFect
{
  public DataStruct_EQ[] EQ = new DataStruct_EQ[8];
  public int Echo_Delay;
  public int Echo_LoRatio;
  public int Echo_Repeat;
  public int Echo_Space;
  public int Echo_level;
  public int L_preDelay;
  public int R_preDelay;
  public int Ratio;
  public int Rev_LoRatio;
  public int Rev_level;
  public int Rev_preDelay;
  public int Rev_time;
  public int gain;
  public int h_filter;
  public int h_freq;
  public int h_level;
  public int l_filter;
  public int l_freq;
  public int l_level;
  public byte[] name = new byte[8];
  public int none3;
  public int none7;
  public int none8;
  
  public DataStruct_EFFect()
  {
    int i = 0;
    for (;;)
    {
      if (i >= 8) {
        return;
      }
      this.EQ[i] = new DataStruct_EQ();
      i += 1;
    }
  }
}


/* Location:              C:\Users\Administrator\Documents\Tencent\QQPhoneManager\Application\新建文件夹\classes-dex2jar.jar!\leon\android\DataStruct\DataStruct_EFFect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */