package sie.amplifier_conctroller.DataStruct;

public class DataStruct_Output
{
  public DataStruct_EQ[] EQ = new DataStruct_EQ[31];
  public int IN1_Vol;
  public int IN2_Vol;
  public int IN3_Vol;
  public int IN4_Vol;
  public int IN_polar;
  public int cliplim;
  public int delay;
  public int eq_mode;
  public int gain;
  public int h_filter;
  public int h_freq;
  public int h_level;
  public int l_filter;
  public int l_freq;
  public int l_level;
  public int lim_a;
  public int lim_mode;
  public int lim_r;
  public int lim_rate;
  public int lim_t;
  public int linkgroup_num;
  public int mute;
  public byte[] name = new byte[8];
  public int none1;
  public int none2;
  public int none3;
  public int none4;
  public int none5;
  public int none6;
  public int none7;
  public int none8;
  public int none9;
  public int polar;
  public int source;
  public int spk_type;
  
  public DataStruct_Output()
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


/* Location:              C:\Users\Administrator\Documents\Tencent\QQPhoneManager\Application\新建文件夹\classes-dex2jar.jar!\leon\android\DataStruct\DataStruct_Output.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */