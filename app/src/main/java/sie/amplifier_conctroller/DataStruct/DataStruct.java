package sie.amplifier_conctroller.DataStruct;

import java.lang.reflect.Array;

public class DataStruct
{
  public static final String App_versions = "ADKP-NV1.01";
  public static final int BACKLIGHT_INFO = 20;
  public static String BT_ConnectedID;
  public static String BT_ConnectedID_Old;
  public static String BT_ConnectedName;
  public static String BT_GetID;
  public static String BT_GetName;
  public static String BT_Paired_Name;
  public static boolean B_LinkDAll = false;
  public static boolean B_SETOUT_NAME = false;
  public static final String BoardCast_FlashEQUI = "BoardCast_FlashEQUI";

  public static boolean CHS_BT_CONNECTED = false;

  public static final int Ch6_LPF_F_INFO = 51;
  public static final String Copyright = "KP 2016";
  public static int CurProID = 0;

  public static int DELAY_SENDREC_COS = 0;
  public static final int DELAY_SETTINGS_TIMES = 384;
  public static final int DEVICE_ID_INFO = 21;
  public static final String DSP_MACHINE_NAME = "B8";
  public static int curEqstyle = 0;
  public static DataStruct_EQ_List EQList = new DataStruct_EQ_List();

  public static int DecipheringFlag = 0;

  public static final float[] EQ_BW;
  public static final int EQ_BW_MAX = 295;
  public static final int[] EQ_FREQ;
  public static final int EQ_Freq_MAX = 240;
  public static final int EQ_Gain_MAX = 240;
  public static final int EQ_LEVEL_MAX = 720;
  public static final int EQ_LEVEL_MIN = 480;
  public static final int EQ_LEVEL_ZERO = 600;
  public static final int ERROR_ACK = 82;
  public static int Encrypt_DATA = 0;
  public static int EncryptionFlag = 0;
  public static final int FEEDBACK = 10;
  public static final int FRAME_END = 170;
  public static final int FRAME_STA = 238;
  public static final float[] FREQ241;
  public static final int GROUP_NAME = 0;
  public static int HEAD_DATA = 0xde;
  public static boolean HW_MUTE = false;
  public static final int DATA_START_POS = 0x03;
  public static final int IAP_DSP_END_INFO = 60;
  public static final int IAP_DSP_PROGAM_INFO = 59;
  public static final int IAP_DSP_REQUEST_INFO = 58;
  public static final int IAP_END_INFO = 57;
  public static final int IAP_PROGAM_INFO = 56;
  public static final int IAP_REQUEST_INFO = 54;
  public static final int IAP_START_INFO = 55;
  public static final int INPUT_CH_MAX = 4;
  public static final int IN_CH_EQ_MAX = 9;
  public static final int IN_CH_MAX = 1;
  public static final int IN_ID_MAX = 13;
  public static final int IN_LEN = 112;
  public static final int IN_LIMIT_ID = 12;
  public static final int IN_MISC_ID = 9;
  public static final int IN_NAME_ID = 13;
  public static final int IN_NOISEGATE_ID = 11;
  public static final int IN_XOVER_ID = 10;
  public static int[] Input_Sbuf;
  public static final int LED_DATA = 3;
  public static final int LOGO_INFO = 16;
  public static int LinkMODE = 0;
  public static final int MAX_GROUP = 21;
  public static final int MAX_INPUT_VALUME = 100;
  public static final int MAX_MainVALUME = 66;
  public static final int MAX_MainVBased = 0;
  public static final int MAX_OUTPUT_VALUME = 600;
  public static final int MAX_OV_S = 10;
  public static final int MAX_USE_GROUP = 6;
  public static final int MAX_XOVER_OCT = 5;
  public static final int MCU_BUSY = 204;
  public static String MCU_Versions = "MDKP-NV1.00";
  public static final int MICRO = 1;
  public static final int MICSET = 5;
  public static final int MIC_MAX_VAL_INFO = 34;
  public static final int MIC_MIN_EN_INFO = 41;
  public static final int MIC_MIN_VAL_INFO = 35;
  public static final int MIC_MUSIC_EFF_VOL = 8;
  public static final int MUSIC = 3;
  public static final int MUSICSET = 6;
  public static final int MUS_MAX_VAL_INFO = 38;
  public static final int MUS_MIN_EN_INFO = 49;
  public static final int MUS_MIN_VAL_INFO = 39;
  public static boolean MUTE_IN_SYSTEM = false;
  public static boolean MainVlaumeFromSystem = false;
  public static final int OUTPUT = 4;
  public static final int OUT_CH_EQ_MAX = 31;
  public static final int OUT_CH_EQ_MAX_USE = 10;
  public static final int OUT_CH_MAX = 8;
  public static final int OUT_ID_MAX = 36;
  public static final int OUT_LEN = 296;
  public static final int OUT_LIMIT_ID = 35;
  public static final int OUT_MAX_VAL_INFO = 32;
  public static final int OUT_MIN_EN_INFO = 40;
  public static final int OUT_MIN_VAL_INFO = 33;
  public static final int OUT_MISC_ID = 31;
  public static final int OUT_MIX_ID = 34;
  public static final int OUT_NAME_ID = 36;
  public static final int OUT_Valume_ID = 33;
  public static final int OUT_XOVER_ID = 32;
  public static int[][] Output_Sbuf;
  public static final int PAGEVIEW_About = 245;
  public static final int PAGEVIEW_EQ = 246;
  public static final int PAGEVIEW_HOME = 240;
  public static final int PAGEVIEW_Main = 241;
  public static final int PAGEVIEW_Output = 244;
  public static final int PAGEVIEW_SetDelay = 242;
  public static final int PAGEVIEW_Weight = 247;
  public static final int PAGEVIEW_XOver = 243;
  public static final int PC_SOURCE_SET = 2;
  public static final int READ_CMD = 162;
  public static final int RIGHT_ACK = 81;
  public static int[] SDF_sbuf;
  public static final int SENDBUFMAX = 100;
  public static final int SOFTWARE_VERSION = 4;
  public static final int SOUND_FIELD_INFO = 64;
  public static final int STARTUP_DATA_INFO = 53;
  public static final int SUPER_PASSWORD_DATA = 19;
  public static final int SYSTEM = 9;
  public static final int SYSTEM_DATA = 5;
  public static final int SYSTEM_INFO = 17;
  public static final int SYSTEM_LEN = 24;
  public static final int SYSTEM_SPK_TYPE = 6;
  public static final int TEMPERATURE_INFO = 22;
  public static final int U0DataLen = 400;
  public static final int UI_EQ_ALL = 17;
  public static final int UI_EQ_BW = 13;
  public static final int UI_EQ_Freq = 14;
  public static final int UI_EQ_G_P_MODE_EQ = 16;
  public static final int UI_EQ_Level = 15;
  public static final int UI_HFilter = 1;
  public static final int UI_HFreq = 3;
  public static final int UI_HOct = 2;
  public static final int UI_LFilter = 4;
  public static final int UI_LFreq = 6;
  public static final int UI_LOct = 5;
  public static final int UI_OutEQ100 = 10;
  public static final int UI_OutEQ10K = 12;
  public static final int UI_OutEQ1K = 11;
  public static final int UI_OutMute = 8;
  public static final int UI_OutPolar = 9;
  public static final int UI_OutVal = 7;
  public static int UI_Type = 0;
  public static final int WIFI_RESET_INFO = 65;
  public static final int WIFI_SETSSID_INFO = 66;
  public static final int WRITE_CMD = 161;
  public static final int XOVER_OCT_MAX = 7;
  public static final int[] XOver_FREQ;
  public static int input_sourcetemp = 0;
  public static final String welcome_logo = "B2-NV1.00";
  public int AutoSource = 0;
  public int AutoSourcedB = 0;
  public int[] Buf = new int['Ɛ'];
  public int ChannelID;
  public int CheckSum;
  public int[] DataBuf = new int['Ơ'];
  public int DataID;
  public int DataLen;
  public int DataType;
  public int DeviceID;
  public int FrameEnd;
  public int FrameStar;
  public int FrameType;
  public int INC1;
  public int INC2;
  public int INC3;
  public DataStruct_Input[] IN_CH = new DataStruct_Input[1];
  public static int MainvolMuteFlg = 1;
  public DataStruct_Output[] OUT_CH = new DataStruct_Output[8];
  public int PCFadeInFadeOutFlg;
  public int PcCustom;
  public char[][] UserGroup = (char[][])Array.newInstance(Character.TYPE, new int[] { 21, 16 });
  public int UserID;
  public int alldelay = 20;
  public int aux_gain = 0;
  public int aux_mode = 0;
  public int blue_gain = 0;
  public int device_mode = 3;
  public int hi_mode = 0;
  public static int  input_source = 3;
  public static int main_vol = 30;
  public int noisegate_t = 0;
  public int none4 = 0;
  public int none5 = 0;
  public int none6 = 0;
  public int out1_spk_type;
  public int out2_spk_type;
  public int out3_spk_type;
  public int out4_spk_type;
  public int out5_spk_type;
  public int out6_spk_type;
  public int out7_spk_type;
  public int out8_spk_type;
  public int[] out_led = new int[15];



  public static DataStruct_sieProtocol sieProtocol = new DataStruct_sieProtocol();
  public static byte otherCMD = 0;
  //channel
  public static final byte max_channel = 8;
  //public static boolean [] chanelMute = new boolean[] { true,true,true,true,true,true ,true,true};
  public static boolean [] chanelMute = new boolean[] { false,false,false,false,false,false ,false,false};
  public static byte [] chanelLastVolume = new byte[] { 10,10,10,10,10,10 ,10,10};
  public static byte polar[] = new byte[]{0,0,0,0,0,0,0,0};

  //eq
  public static byte [] cur_eq = new  byte[31];
  public static final byte  eqMax = 31;
  public static int curQValue = 150;
  public static final int  QMax = 415;
  public static final int  QMin = 25;
  public static final int userEqMax = 8;
  public static int curUserEqData = 0;

  //delay
  public static int [] delay = new int[max_channel];
  public static final int delayMax = 10000;

  //fre div
  public static int curFreDiv = 0;
  public static int [] freDivHight = new int[max_channel];
  public static int [] freDivLow = new int[max_channel];

  //out put mode
  public static int Output_mode = 0;

  
  static
  {
    BT_Paired_Name = "DSP HD";
    BT_ConnectedID = "NULL";
    BT_ConnectedID_Old = "NULL";
    BT_ConnectedName = "NULL";
    BT_GetName = "NULL";
    BT_GetID = "NULL";
    CHS_BT_CONNECTED = false;
    B_SETOUT_NAME = true;
    Encrypt_DATA = 131;
    EncryptionFlag = 33;
    DecipheringFlag = 32;
    DELAY_SENDREC_COS = 0;
    HW_MUTE = false;
    MUTE_IN_SYSTEM = true;
    MainVlaumeFromSystem = true;
    LinkMODE = 1;
    B_LinkDAll = false;
    EQ_FREQ = new int[] { 20, 25, 32, 40, 50, 63, 80, 100, 125, 160, 200, 250, 315, 400, 500, 630, 800, 1000, 1250, 1600, 2000, 2500, 3150, 4000, 5000, 6300, 8000, 10000, 12500, 16000, 20000 };
    XOver_FREQ = new int[] { 20, 23, 27, 32, 37, 42, 49, 57, 66, 76, 88, 102, 118, 140, 162, 187, 216, 250, 289, 334, 375, 420, 486, 561, 648, 749, 866, 1000, 1123, 1297, 1498, 1731, 2000, 2245, 2594, 2997, 3462, 4000, 4757, 5496, 6350, 7127, 8000, 9243, 10679, 12338, 13849, 15102, 16000, 17959, 20000 };
    FREQ241 = new float[] { 20.0F, 20.3F, 20.9F, 21.5F, 22.1F, 22.7F, 23.4F, 24.1F, 24.8F, 25.5F, 26.3F, 27.0F, 27.8F, 28.7F, 29.5F, 30.4F, 31.3F, 32.2F, 33.1F, 34.1F, 35.1F, 36.1F, 37.2F, 38.3F, 39.4F, 40.5F, 41.7F, 42.9F, 44.2F, 45.5F, 46.8F, 48.2F, 49.6F, 51.1F, 52.6F, 54.1F, 55.7F, 57.3F, 59.0F, 60.7F, 62.5F, 64.3F, 66.2F, 68.2F, 70.2F, 72.2F, 74.3F, 76.5F, 78.7F, 81.1F, 83.4F, 85.9F, 88.4F, 91.0F, 93.6F, 96.4F, 99.2F, 102.0F, 105.0F, 108.0F, 111.0F, 115.0F, 118.0F, 121.0F, 125.0F, 129.0F, 132.0F, 136.0F, 140.0F, 144.0F, 149.0F, 153.0F, 158.0F, 162.0F, 167.0F, 172.0F, 179.0F, 182.0F, 187.0F, 193.0F, 198.0F, 204.0F, 210.0F, 216.0F, 223.0F, 229.0F, 236.0F, 243.0F, 250.0F, 257.0F, 265.0F, 273.0F, 281.0F, 289.0F, 297.0F, 306.0F, 315.0F, 324.0F, 334.0F, 344.0F, 354.0F, 364.0F, 375.0F, 386.0F, 397.0F, 409.0F, 420.0F, 433.0F, 445.0F, 459.0F, 472.0F, 486.0F, 500.0F, 515.0F, 530.0F, 545.0F, 561.0F, 578.0F, 595.0F, 612.0F, 630.0F, 648.0F, 667.0F, 687.0F, 707.0F, 728.0F, 749.0F, 771.0F, 794.0F, 817.0F, 841.0F, 866.0F, 891.0F, 917.0F, 944.0F, 972.0F, 1000.0F, 1030.0F, 1060.0F, 1090.0F, 1123.0F, 1155.0F, 1190.0F, 1224.0F, 1260.0F, 1297.0F, 1335.0F, 1374.0F, 1414.0F, 1456.0F, 1498.0F, 1542.0F, 1587.0F, 1634.0F, 1682.0F, 1731.0F, 1782.0F, 1834.0F, 1888.0F, 1943.0F, 2000.0F, 2059.0F, 2119.0F, 2181.0F, 2245.0F, 2311.0F, 2378.0F, 2448.0F, 2520.0F, 2594.0F, 2670.0F, 2748.0F, 2828.0F, 2911.0F, 2997.0F, 3084.0F, 3175.0F, 3268.0F, 3364.0F, 3462.0F, 3564.0F, 3668.0F, 3776.0F, 3886.0F, 4000.0F, 4117.0F, 4238.0F, 4362.0F, 4490.0F, 4621.0F, 4757.0F, 4896.0F, 5000.0F, 5187.0F, 5339.0F, 5496.0F, 5657.0F, 5823.0F, 5993.0F, 6169.0F, 6350.0F, 6536.0F, 6727.0F, 6924.0F, 7127.0F, 7336.0F, 7551.0F, 7772.0F, 8000.0F, 8234.0F, 8476.0F, 8724.0F, 8980.0F, 9243.0F, 9514.0F, 9792.0F, 10079.0F, 10374.0F, 10679.0F, 10992.0F, 11314.0F, 11645.0F, 11987.0F, 12338.0F, 12699.0F, 13071.0F, 13454.0F, 13849.0F, 14254.0F, 14672.0F, 15102.0F, 15545.0F, 16000.0F, 16469.0F, 16951.0F, 17448.0F, 17959.0F, 18486.0F, 19027.0F, 19585.0F, 20000.0F };
    EQ_BW = new float[] { 28.852F, 24.043F, 20.608F, 18.031F, 16.027F, 14.424F, 13.112F, 12.019F, 11.094F, 10.301F, 9.614F, 9.012F, 8.482F, 8.01F, 7.588F, 7.208F, 6.864F, 6.551F, 6.266F, 6.004F, 5.764F, 5.541F, 5.336F, 5.144F, 4.966F, 4.8F, 4.645F, 4.499F, 4.362F, 4.233F, 4.112F, 3.997F, 3.889F, 3.786F, 3.688F, 3.595F, 3.507F, 3.423F, 3.343F, 3.266F, 3.193F, 3.123F, 3.056F, 2.992F, 2.93F, 2.871F, 2.814F, 2.759F, 2.707F, 2.656F, 2.607F, 2.56F, 2.515F, 2.471F, 2.428F, 2.387F, 2.348F, 2.309F, 2.272F, 2.236F, 2.201F, 2.167F, 2.134F, 2.102F, 2.071F, 2.041F, 2.012F, 1.983F, 1.955F, 1.928F, 1.902F, 1.877F, 1.852F, 1.827F, 1.804F, 1.78F, 1.758F, 1.736F, 1.714F, 1.693F, 1.673F, 1.653F, 1.633F, 1.614F, 1.596F, 1.577F, 1.559F, 1.542F, 1.525F, 1.508F, 1.492F, 1.475F, 1.46F, 1.444F, 1.429F, 1.414F, 1.4F, 1.385F, 1.371F, 1.358F, 1.344F, 1.331F, 1.318F, 1.305F, 1.293F, 1.28F, 1.268F, 1.256F, 1.245F, 1.233F, 1.222F, 1.211F, 1.2F, 1.189F, 1.179F, 1.168F, 1.158F, 1.148F, 1.138F, 1.128F, 1.119F, 1.109F, 1.1F, 1.091F, 1.082F, 1.073F, 1.064F, 1.056F, 1.047F, 1.039F, 1.031F, 1.023F, 1.015F, 1.007F, 0.999F, 0.991F, 0.984F, 0.976F, 0.969F, 0.961F, 0.954F, 0.947F, 0.94F, 0.933F, 0.927F, 0.92F, 0.913F, 0.907F, 0.9F, 0.894F, 0.887F, 0.881F, 0.875F, 0.869F, 0.863F, 0.857F, 0.851F, 0.845F, 0.84F, 0.834F, 0.828F, 0.823F, 0.817F, 0.812F, 0.807F, 0.801F, 0.796F, 0.791F, 0.786F, 0.781F, 0.776F, 0.771F, 0.766F, 0.761F, 0.757F, 0.752F, 0.747F, 0.742F, 0.738F, 0.733F, 0.729F, 0.724F, 0.72F, 0.716F, 0.711F, 0.707F, 0.703F, 0.699F, 0.695F, 0.69F, 0.686F, 0.682F, 0.678F, 0.674F, 0.671F, 0.667F, 0.663F, 0.659F, 0.655F, 0.652F, 0.648F, 0.644F, 0.641F, 0.637F, 0.633F, 0.63F, 0.626F, 0.623F, 0.62F, 0.616F, 0.613F, 0.609F, 0.606F, 0.603F, 0.6F, 0.596F, 0.593F, 0.59F, 0.587F, 0.584F, 0.581F, 0.577F, 0.574F, 0.571F, 0.568F, 0.565F, 0.563F, 0.56F, 0.557F, 0.554F, 0.551F, 0.548F, 0.545F, 0.543F, 0.54F, 0.537F, 0.534F, 0.532F, 0.529F, 0.526F, 0.524F, 0.521F, 0.518F, 0.516F, 0.513F, 0.511F, 0.508F, 0.506F, 0.503F, 0.501F, 0.498F, 0.496F, 0.493F, 0.491F, 0.489F, 0.486F, 0.484F, 0.482F, 0.479F, 0.477F, 0.475F, 0.473F, 0.47F, 0.468F, 0.466F, 0.464F, 0.461F, 0.459F, 0.457F, 0.455F, 0.453F, 0.451F, 0.449F, 0.447F, 0.445F, 0.442F, 0.44F, 0.438F, 0.436F, 0.434F, 0.432F, 0.43F, 0.428F, 0.427F, 0.425F, 0.423F, 0.421F, 0.419F, 0.417F, 0.415F, 0.413F, 0.411F, 0.41F, 0.408F, 0.406F, 0.404F };
    UI_Type = 0;
    input_sourcetemp = 3;
    CurProID = 0;
    Input_Sbuf = new int[112];
    Output_Sbuf = (int[][])Array.newInstance(Integer.TYPE, new int[] { 8, 296 });
    SDF_sbuf = new int[50];
  }
  
  public DataStruct()
  {

  }
}


/* Location:              C:\Users\Administrator\Documents\Tencent\QQPhoneManager\Application\新建文件夹\classes-dex2jar.jar!\leon\android\DataStruct\DataStruct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */