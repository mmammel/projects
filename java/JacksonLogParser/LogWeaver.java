import java.util.Date;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Set;
import java.util.HashSet;

public class LogWeaver
{
  private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS");

  private File log1 = null;
  private File log2 = null;
  private Set<String> batchIds = null;
  private BufferedWriter writer = null;

  private static final String OUTPUT_FILE = "result.txt";
  private static final int ONE = 1;
  private static final int TWO = 2;
  private static final String ONE_PREFIX = "ONE ";
  private static final String TWO_PREFIX = "TWO ";

  private static final String [] BATCH_IDS = {  "402881be05552e8a0105554c20473e10",
                                                "402881be075a20e501075b5b48780004",
                                                "402881be057eb22f01057eb26c4e0014",
                                                "402881be057eb22f01057eb8ab3e095b",
                                                "402881be05a27e580105a28c62180023",
                                                "402881be05a783ac0105a7aea60a002d",
                                                "402881be05bb0d090105bc7553d30b60",
                                                "402881be05c6dc170105c6f1a9070032",
                                                "402881be05c6dc170105c7297e4a45a7",
                                                "402881be05c6dc170105c7297eab45ab",
                                                "402881be05c6dc170105c729832e45cf",
                                                "402881be0f97019f010f971b253100ad",
                                                "402881be05c6dc170105c72984c445db",
                                                "402881be05c6dc170105c72988ac45f7",
                                                "402881be05c6dc170105c7298ad34607",
                                                "402881be05c6dc170105c7298e41461f",
                                                "402881be05c6dc170105c7299448464f",
                                                "402881be05c6dc170105c72995df465b",
                                                "402881be05c6dc170105c72997e5466b",
                                                "402881be05c6dc170105c729a58b46d7",
                                                "402881be05c6dc170105c729a80946eb",
                                                "402881be05c6dc170105c729aa6246ff",
                                                "402881be05c6dc170105c729ac4b470b",
                                                "402881be05c6dc170105c729ad104713",
                                                "402881be05c6dc170105c729af144723",
                                                "402881be05c6dc170105c729b13c4733",
                                                "402881be05c6dc170105c729b3924743",
                                                "402881be0afa14c2010afb567acc0010",
                                                "402881be05c6dc170105c729b6d2475b",
                                                "402881be05c6dc170105c7298ca44613",
                                                "402881be05d9f6340105d9f80de10009",
                                                "402881be05d9e0130105d9e534c60004",
                                                "402881be05d9e0130105d9e5a8500007",
                                                "402881be05d9e0130105d9e6971b000d",
                                                "402881be05d9e0130105d9e6d8500010",
                                                "402881be05d9e0130105d9e71fbe0013",
                                                "402881be05d9e0130105d9e7722f0016",
                                                "402881be05d9e0130105d9e7c8e20019",
                                                "402881be05d9e0130105d9e84407001c",
                                                "402881be05d9e0130105d9e8a09c001f",
                                                "402881be05d9e0130105d9e95fb20025",
                                                "402881be05d9e0130105d9e9a55f0028",
                                                "402881be05d9e0130105d9ea345d002b",
                                                "402881be05d9e0130105d9f07812002e",
                                                "402881be05d9e0130105d9f0fa480031",
                                                "402881be05d9e0130105d9f18ed30034",
                                                "402881be05d9f6340105d9f71cee0003",
                                                "402881be05d9f6340105d9f785780006",
                                                "402881be05d9f6340105d9f86a0d000c",
                                                "402881be05d9f6340105d9f9c6e1000f",
                                                "402881be05d9f6340105d9fa21100012",
                                                "402881be05d9f6340105d9fa8b9c0015",
                                                "402881be05d9f6340105d9faf2310018",
                                                "402881be05d9f6340105d9fbe287001b",
                                                "402881be05d9f6340105d9fc5df5001e",
                                                "402881be05d9f6340105d9fca4620021",
                                                "402881be05d9f6340105d9fe6a520024",
                                                "402881be05d9f6340105d9ff1c360027",
                                                "402881be05d9f6340105d9ffc001002a",
                                                "402881be05d9f6340105da0044e0002d",
                                                "402881be05d9f6340105da00a20d0030",
                                                "402881be05d9f6340105da01523c0036",
                                                "402881be05d9f6340105da0227ee0039",
                                                "402881be05d9f6340105da028f22003c",
                                                "402881be05d9f6340105da041a430048",
                                                "402881be05d9f6340105da046931004b",
                                                "402881be05d9f6340105da04b843004e",
                                                "402881be05d9f6340105da052be90051",
                                                "402881be05d9f6340105da059a270054",
                                                "402881be05d9f6340105da0602d40057",
                                                "402881be05d9f6340105da06558c005a",
                                                "402881be05d9f6340105da086caf005d",
                                                "402881be05d9f6340105da0a36e40060",
                                                "402881be05d9f6340105da0a94c70063",
                                                "402881be05d9f6340105da0affe70066",
                                                "402881be05d9f6340105da0b4d380069",
                                                "402881be05d9f6340105da0badaf006c",
                                                "402881be05d9f6340105da0c37fd006f",
                                                "402881be05d9f6340105da0c98a50072",
                                                "402881be05d9f6340105da0d64af0078",
                                                "402881be05d9f6340105da0dc20f007b",
                                                "402881be05e456e60105e56ec5d00036",
                                                "402881be126a539e01126bd814eb01e8",
                                                "402881be060d6d6501060e02c41216a8",
                                                "402881be05e91df00105e9da15cf0603",
                                                "402881be060320aa0106047549a102e3",
                                                "402881be060320aa0106047822e009b5",
                                                "402881be060320aa01060478242309c2",
                                                "402881be060d6d6501060d6f323c007e",
                                                "402881be060e8f7f01060eb9383c00a8",
                                                "402881be0611f7e7010613013f260530",
                                                "402881be0611f7e7010613013f770535",
                                                "402881be0611f7e7010613013fc9053a",
                                                "402881be062dc40201062dea8eb450dc",
                                                "402881be0630feb00106318acd01019a",
                                                "402881be0630feb00106318acdb701a5",
                                                "402881be092627510109265ae2b20017",
                                                "402881be0630feb00106318aceaf01ad",
                                                "402881be0630feb00106318ad29201d5",
                                                "402881be0630feb00106318ad3f501df",
                                                "402881be09209e8801092276d96f0185",
                                                "402881be0925ba65010925c69739000d",
                                                "402881be0630feb00106318ad54d01e9",
                                                "402881be092ade0001092afa5a9d000c",
                                                "402881be076abef901076b19407a000e",
                                                "402881be064a270e01064a38d1f803d8",
                                                "402881be064a270e01064a38cc4603aa",
                                                "402881be0650e33b010656b757713ebc",
                                                "402881be065ab78001065be78de40ce6",
                                                "402881be089fd58e01089fedc0b2000a",
                                                "402881be0f37b766010f3a177a8610c4",
                                                "402881be065ab78001065be78ff20d02",
                                                "402881be065ab78001065be791ba0d1a",
                                                "402881be065ab78001065be793250d27",
                                                "402881be065ab78001065be795510d44",
                                                "402881be065ab78001065be7963b0d50",
                                                "402881be065ab78001065be798000d5c",
                                                "402881be065ab78001065be799340d68",
                                                "402881be065ab78001065be79a170d74",
                                                "402881be065ab78001065be79b100d80",
                                                "402881be065ab78001065be79c280d8c",
                                                "402881be065ab78001065be79d0a0d98",
                                                "402881be065ab78001065be79e060da4",
                                                "402881be065ab78001065c18b0d62716",
                                                "402881be0660bc7101066f1a4b55249a",
                                                "402881be0e886840010e8af90c412b43",
                                                "402881be067b686001067b687ab20019",
                                                "402881be1370f7f10113731b4afb1810",
                                                "402881be13731ec20113733443ee0029",
                                                "402881be090bed0b01090c2fbe63000f",
                                                "402881be13731ec201137339f1190043",
                                                "402881be13731ec201137354b85b017a",
                                                "402881be13731ec201137350ca4c015d",
                                                "402881be13731ec20113732ded860013",
                                                "402881be13731ec201137353341e0168",
                                                "402881be13731ec201137357f4610187",
                                                "402881be13731ec20113735a5c9d0192",
                                                "402881be13731ec20113733730320034",
                                                "402881be13731ec20113734eda4a0152",
                                                "402881be067f0ed7010693efb56d2628",
                                                "402881be067f0ed7010693efb5ad262d",
                                                "402881be0697ed030106993e8cbf1fd7",
                                                "402881be0697ed030106993e8d771fe1",
                                                "402881be0697ed030106993e8e331feb",
                                                "402881be0697ed030106993e8f0b1ff5",
                                                "402881be0697ed030106993e8fc61fff",
                                                "402881be0697ed030106993e908e2009",
                                                "402881be0697ed030106993e91492013",
                                                "402881be0697ed030106993f1b8c202c",
                                                "402881be0e5a0f01010e5d206bff33e5",
                                                "402881be104b729101104b796abf000c",
                                                "402881be08110a7f0108205e30450006",
                                                "402881be072904530107290d3dac05c1",
                                                "402881be072904530107290d3eb705d0",
                                                "402881be072904530107290d401905df",
                                                "402881be072904530107290d410405ee",
                                                "402881be072904530107290d41f305fd",
                                                "402881be072904530107290d42fa060c",
                                                "402881be072904530107290d441a061b",
                                                "402881be072904530107290d450f062a",
                                                "402881be072904530107290d461f0639",
                                                "402881be072904530107290d47200648",
                                                "402881be072904530107290d48750657",
                                                "402881be072904530107290d49da0666",
                                                "402881be072904530107290d4a930671",
                                                "402881be0c3eaa45010c40a0dbdc0027",
                                                "402881be0c695d64010c69a00768000c",
                                                "402881be08298575010835af966d010a",
                                                "402881be08298575010835af695a0107",
                                                "402881be08905e3f0108918d02540010",
                                                "402881be09209e8801092211c6c30166",
                                                "402881be08e804030108e99f2ed60049",
                                                "402881be08e804030108e99f6393004c",
                                                "402881be08e804030108e99f9c7a004f",
                                                "402881be0aa7c53c010aa85139ef0043",
                                                "402881be08fc970f0108fccdf7f90009",
                                                "402881be09209e88010921e05c29011a",
                                                "402881be09209e88010921e086d50125",
                                                "402881be09300bb2010930b2e07b01cb",
                                                "402881be09300bb2010930b2a01b01c0",
                                                "402881be09300bb2010930b3cc8001f7",
                                                "402881be09300bb2010930b31ace01d6",
                                                "402881be09300bb2010930b35a8f01e1",
                                                "402881be09300bb2010930b588ef0217",
                                                "402881be09300bb2010930b40ac80202",
                                                "402881be10ed73f40110ef8d263c116d",
                                                "402881be09300bb2010930b3913501ec",
                                                "402881be09300bb2010930b5c1a50222",
                                                "402881be0ec66b7f010ec982382a3b0b",
                                                "402881be1048a87301104a9897d00838",
                                                "402881be099cc84601099d99168f0029",
                                                "402881be099cc84601099d9b64ce0035",
                                                "402881be0a69c3af010a69fb657e000c",
                                                "402881be0a50a432010a50e3168d0030",
                                                "402881be0a4b89f4010a4c93f8a4003e",
                                                "402881be0a4b89f4010a4c932234000b",
                                                "402881be0a411d16010a415714de0067",
                                                "402881be0a5fa975010a5feaa08c005f",
                                                "402881be0a83c5dc010a8471c05d008f",
                                                "402881be0a83c5dc010a84715c870065",
                                                "402881be0acbc2ce010acc90e0190030",
                                                "402881be090bed0b01090cab45cd00ca",
                                                "402881be090bed0b01090cb34c4601a4",
                                                "402881be0c1a9017010c1b1f78710048",
                                                "402881be0d680421010d6ae11541344f",
                                                "402881be0c156597010c176056be022a",
                                                "402881be0c1a9017010c1b258240006c",
                                                "402881be0c156597010c1768b065024e",
                                                "402881be0c1a9017010c1abcbc8e000c",
                                                "402881be0eaa1988010eaa3b846d01c5",
                                                "402881be0c1a9017010c1b33f8dc00cc",
                                                "402881be0fe490bd010fe4b8b40a0012",
                                                "402881be0c1a9017010c1b2c5bf9009c",
                                                "402881be0c1a9017010c1b2770340078",
                                                "402881be0c1a9017010c1b31f3b700c0",
                                                "402881be0c1a9017010c1b2e221c00a8",
                                                "402881be0c156597010c1766bf880242",
                                                "402881be0c1a9017010c1b2af2b00090",
                                                "402881be0c1a9017010c1ac09f2f0024",
                                                "402881be0c1a9017010c1abe7d360018",
                                                "402881be0c1a9017010c1acdf34f0030",
                                                "402881be0c1a9017010c1b2927ab0084",
                                                "402881be0c1a9017010c1b21a4480054",
                                                "402881be0c156597010c1751c213021e",
                                                "402881be0c1a9017010c1b23e09c0060",
                                                "402881be0c1a9017010c1b0cbb7d003c",
                                                "402881be0c156597010c176327fe0236",
                                                "402881be13cf03c20113cf086431000b",
                                                "402881be0c5e6c37010c5e9ef2a0000c",
                                                "402881be0c9f2c1a010ca0aeb84e0086",
                                                "402881be0d1d996c010d1db42e08002c",
                                                "402881be0d40b368010d41af1abf1f18",
                                                "402881be0d40b368010d41b22d391f57",
                                                "402881be0cf2d278010cf386d742002b",
                                                "402881be0cf2d278010cf38711080036",
                                                "402881be0d159e61010d17199114057b",
                                                "402881be0d30afbc010d32443742000e",
                                                "402881be0d30afbc010d3247791e001a",
                                                "402881be0d43f79c010d45939bed01c7",
                                                "402881be0d43f79c010d45804d160189",
                                                "402881be0d62ddc4010d64dfeb96124b",
                                                "402881be0db16532010db212aca65a99",
                                                "402881be117da6040111804cad7e3037",
                                                "402881be0d7c9d91010d7f1b82210ca1",
                                                "402881be0d7c9d91010d7f1b4af70c96",
                                                "402881be0d7c9d91010d7f1b15ab0c8b",
                                                "402881be0d7c9d91010d7f1ae4710c80",
                                                "402881be0d7c9d91010d7f1bbc190cac",
                                                "402881be0d81c3ed010d83aed2d80cfc",
                                                "402881be0d81c3ed010d8456852727bf",
                                                "402881be0d81c3ed010d8464129e27ea",
                                                "402881be0dac48af010dad7f750e1f6f",
                                                "402881be0e0ccf9f010e0f4b323724bf",
                                                "402881be0dd574bd010dd62d76b3000d",
                                                "402881be0dede978010defbc33031143",
                                                "402881be0e07a944010e0ae2d02035a6",
                                                "402881be0e07a944010e0ae82b6c35d7",
                                                "402881be0d81c3ed010d83beda420e07",
                                                "402881be0d81c3ed010d849007c72afc",
                                                "402881be0ecb91dc010ecdfd4c2b1df9",
                                                "402881be13731ec201137343c25c0059",
                                                "402881be0e7f759f010e8016a4371031",
                                                "402881be0e7f759f010e806be3861071",
                                                "402881be0ed0b837010ed358578938ef",
                                                "402881be0e7f759f010e806c5bff107c",
                                                "402881be0e7f759f010e807b7f341087",
                                                "402881be0e886840010e8b092aff2b63",
                                                "402881be0e886840010e8b0b97fe2b6e",
                                                "402881be0e886840010e8b0c15de2b79",
                                                "402881be0e886840010e8b0c7d3e2b84",
                                                "402881be0e886840010e8b0d1beb2b8f",
                                                "402881be0e886840010e8b0d87712b9a",
                                                "402881be0e886840010e8b0e1c462ba5",
                                                "402881be0e886840010e8b0e735e2bb0",
                                                "402881be0e886840010e8b0ebb582bbb",
                                                "402881be0e886840010e8b110fe42bcc",
                                                "402881be0e886840010e8b1179cb2bd7",
                                                "402881be0e886840010e8b11dc712be2",
                                                "402881be0e886840010e8b1241272bed",
                                                "402881be0e9a8c03010e9a9a55e70527",
                                                "402881be0e886840010e8afa075a2b4f",
                                                "402881be0e9a8c03010e9aabd7f21a39",
                                                "402881be0e9a8c03010e9aac64d91a44",
                                                "402881be0ecb91dc010ecd53fbb1149f",
                                                "402881be0ecb91dc010ecd533c831494",
                                                "402881be0f99903d010f9b20afdc0126",
                                                "402881be0f9469e3010f965d6b78093c",
                                                "402881be117da60401117eca124b016f",
                                                "402881be0f9469e3010f965df8f50947",
                                                "402881be0f9469e3010f965e1e050952",
                                                "402881be0f9469e3010f965e643c095d",
                                                "402881be0f9469e3010f965e87590968",
                                                "402881be0f9469e3010f965eab9f0973",
                                                "402881be0f9469e3010f965eef710989",
                                                "402881be0fe490bd010fe4c09e03001f",
                                                "402881be104dcecc01104fca9d130abe",
                                                "402881be105d41e401105eb91b1100f4",
                                                "402881be104dcecc01104f660c630283",
                                                "402881be1078d1c6011079c07bb5163d",
                                                "402881be0f974783010f9759a42c0052",
                                                "402881be1223b8a6011223bf2b53000c",
                                                "402881be106cb4f701106f7d021e1a36",
                                                "402881be107c280a01107d95170201cb",
                                                "402881be10ed73f40110f006ee6f2668",
                                                "402881be114f4cc701115122fa090e42",
                                                "402881be1193806801119407b5e5136f",
                                                "402881be117da60401117f4e53ad01ee",
                                                "402881be11020d6201110368b82e064e",
                                                "402881be11c061c60111c36d03c43ffa",
                                                "402881be11ff80730111ff8d77fb000c",
                                                "402881be11ff80730111ffd2cdf30043",
                                                "402881be12db9f850112dcf325a50195",
                                                "402881be1229d95b01122a715fd017a0",
                                                "402881be122c874c01122df7f5bc01ba",
                                                "402881be122c874c01122df826f401c5",
                                                "402881be122c874c01122df896aa01db",
                                                "402881be122c874c01122df8562f01d0",
                                                "402881be125093d3011253a841a4231f",
                                                "402881be1229d95b01122aa99f2f19af",
                                                "402881be12c1dfba0112c44180351bfd",
                                                "402881be12c1dfba0112c4c3eef6351e",
                                                "402881be12e2119e0112e2e158050d8a",
                                                "402881be12e2119e0112e2e2b09d0d95",
                                                "402881be132a32ae01132b6627aa1385",
                                                "402881be131e9233011320788a990d3a",
                                                "402881be131e92330113209550c00d96",
                                                "402881be131e9233011320a45c550dad",
                                                "402881be131e9233011320a7861b0db9",
                                                "402881be131e923301132073f2280d2e",
                                                "402881be131e923301132073b4100d23",
                                                "402881be131e92330113207cb4c70d46",
                                                "402881be1323b88b01132505f54d0204",
                                                "402881be136bd19601136d47c57d01e3",
                                                "402881be13aaf5e00113ab7b0e7a6a2c",
                                                "402881be14441cad011446799a8910cd",
                                                "402881be14441cad01144681662110e3",
                                                "402881be14441cad01144681fdbc10f9",
                                                "402881be13be37570113c13d0a5e2178",
                                                "402881be13f6dd470113f841653804e4",
                                                "402881be13f6dd470113f8b5f2941000",
                                                "402881be14d196610114d20c64f31722",
                                                "402881be14d196610114d202f79216cf",
                                                "402881be14d196610114d208ca49170c",
                                                "402881be14d196610114d20ac9571717",
                                                "402881be14cf28600114d08b53b10097",
                                                "402880b7161ffc5601161ffd9455000b"  };

  public LogWeaver( String file1, String file2 ) throws IOException
  {
    this.log1 = new File( file1 );
    this.log2 = new File( file2 );
    writer = new BufferedWriter( new FileWriter( OUTPUT_FILE ) );

    this.batchIds = new HashSet<String>();

    for( int i = 0; i < BATCH_IDS.length; i++ )
    {
      batchIds.add( BATCH_IDS[i] );
    }
  }

  public void weave() throws IOException
  {
    BufferedReader reader1 = new BufferedReader( new FileReader( this.log1 ) );
    BufferedReader reader2 = new BufferedReader( new FileReader( this.log2 ) );

    String read1 = null, read2 = null;
    Date date1 = null, date2 = null, last = null;

    read1 = reader1.readLine();
    read2 = reader2.readLine();

    while( true )
    {
      if( read1 == null && read2 != null )
      {
        //read1 done, finish 2 and continue
        this.writeToResult( TWO, read2 );

        while( (read2 = reader2.readLine()) != null )
        {
          this.writeToResult( TWO, read2 );
        }
      }
      else if( read1 != null && read2 == null )
      {
        //read2 done, finish 1 and continue
        this.writeToResult( ONE, read1 );

        while( (read1 = reader1.readLine()) != null )
        {
          this.writeToResult( ONE, read1 );
        }
      }
      else if( read1 == null && read2 == null )
      {
        //done, exit
        break;
      }
      else
      {
        date1 = this.getDateFromLine( read1 );

        /**
         * subtract 1m 43s from jackson's timestamps
         */
        date1.setTime( date1.getTime() - 103000L );
        date2 = this.getDateFromLine( read2 );

        if( date1.before( date2 ) )
        {
          this.writeToResult( ONE, read1 );
          read1 = reader1.readLine();
        }
        else if( date1.after( date2 ) )
        {
          this.writeToResult( TWO, read2 );
          read2 = reader2.readLine();
        }
        else
        {
          this.writeToResult( ONE, read1 );
          read1 = reader1.readLine();
        }
      }

    }

    this.writer.flush();
    this.writer.close();
    reader1.close();
    reader2.close();
  }

  private Date getDateFromLine( String line )
  {
    Date retVal = null;
    try
    {
      retVal = DATEFORMAT.parse( line );
    }
    catch( Exception pe )
    {
      System.out.println( "Caught an exception parsing date from : " + line + ": " + pe.toString() );
    }

    return retVal;
  }

  private void writeToResult( int input, String line ) throws IOException
  {
    String toWrite = null;

    if( this.batchIds.contains( line.substring( line.length() - 32 ) ) )
    {
      if( input == TWO )
      {
        toWrite = TWO_PREFIX + line;
      }
      else if( input == ONE )
      {
        toWrite = ONE_PREFIX + line;
      }

      this.writer.write( toWrite, 0, toWrite.length() );
      this.writer.newLine();
    }
  }

  public static void main( String [] args )
  {
    LogWeaver LW = null;

    try
    {
      LW = new LogWeaver( args[0], args[1] );
      LW.weave();
    }
    catch( IOException ioe )
    {
      System.out.println( "Caught an exception: " + ioe.toString() );
    }
  }
}
