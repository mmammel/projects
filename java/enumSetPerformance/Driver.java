import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Driver
{
  public static void main( String [] args )
  {
    List<EnumSet<MyEnum>> setList = new ArrayList<EnumSet<MyEnum>>();

    for( int i = 0; i < 18000; i++ )
    {
      setList.add( EnumSet.of( MyEnum.E245, MyEnum.E42, MyEnum.E8 ) );
    }

    int hits = 0;
    MyEnum val = MyEnum.E0;

    for( int j = 0; j < 18000; j++ )
    {
      switch( j%370 )
      {
        case 0:
         val = MyEnum.E0;
         break;

        case 1:
         val = MyEnum.E1;
         break;

        case 2:
         val = MyEnum.E2;
         break;

        case 3:
         val = MyEnum.E3;
         break;

        case 4:
         val = MyEnum.E4;
         break;

        case 5:
         val = MyEnum.E5;
         break;

        case 6:
         val = MyEnum.E6;
         break;

        case 7:
         val = MyEnum.E7;
         break;

        case 8:
         val = MyEnum.E8;
         break;

        case 9:
         val = MyEnum.E9;
         break;

        case 10:
         val = MyEnum.E10;
         break;

        case 11:
         val = MyEnum.E11;
         break;

        case 12:
         val = MyEnum.E12;
         break;

        case 13:
         val = MyEnum.E13;
         break;

        case 14:
         val = MyEnum.E14;
         break;

        case 15:
         val = MyEnum.E15;
         break;

        case 16:
         val = MyEnum.E16;
         break;

        case 17:
         val = MyEnum.E17;
         break;

        case 18:
         val = MyEnum.E18;
         break;

        case 19:
         val = MyEnum.E19;
         break;

        case 20:
         val = MyEnum.E20;
         break;

        case 21:
         val = MyEnum.E21;
         break;

        case 22:
         val = MyEnum.E22;
         break;

        case 23:
         val = MyEnum.E23;
         break;

        case 24:
         val = MyEnum.E24;
         break;

        case 25:
         val = MyEnum.E25;
         break;

        case 26:
         val = MyEnum.E26;
         break;

        case 27:
         val = MyEnum.E27;
         break;

        case 28:
         val = MyEnum.E28;
         break;

        case 29:
         val = MyEnum.E29;
         break;

        case 30:
         val = MyEnum.E30;
         break;

        case 31:
         val = MyEnum.E31;
         break;

        case 32:
         val = MyEnum.E32;
         break;

        case 33:
         val = MyEnum.E33;
         break;

        case 34:
         val = MyEnum.E34;
         break;

        case 35:
         val = MyEnum.E35;
         break;

        case 36:
         val = MyEnum.E36;
         break;

        case 37:
         val = MyEnum.E37;
         break;

        case 38:
         val = MyEnum.E38;
         break;

        case 39:
         val = MyEnum.E39;
         break;

        case 40:
         val = MyEnum.E40;
         break;

        case 41:
         val = MyEnum.E41;
         break;

        case 42:
         val = MyEnum.E42;
         break;

        case 43:
         val = MyEnum.E43;
         break;

        case 44:
         val = MyEnum.E44;
         break;

        case 45:
         val = MyEnum.E45;
         break;

        case 46:
         val = MyEnum.E46;
         break;

        case 47:
         val = MyEnum.E47;
         break;

        case 48:
         val = MyEnum.E48;
         break;

        case 49:
         val = MyEnum.E49;
         break;

        case 50:
         val = MyEnum.E50;
         break;

        case 51:
         val = MyEnum.E51;
         break;

        case 52:
         val = MyEnum.E52;
         break;

        case 53:
         val = MyEnum.E53;
         break;

        case 54:
         val = MyEnum.E54;
         break;

        case 55:
         val = MyEnum.E55;
         break;

        case 56:
         val = MyEnum.E56;
         break;

        case 57:
         val = MyEnum.E57;
         break;

        case 58:
         val = MyEnum.E58;
         break;

        case 59:
         val = MyEnum.E59;
         break;

        case 60:
         val = MyEnum.E60;
         break;

        case 61:
         val = MyEnum.E61;
         break;

        case 62:
         val = MyEnum.E62;
         break;

        case 63:
         val = MyEnum.E63;
         break;

        case 64:
         val = MyEnum.E64;
         break;

        case 65:
         val = MyEnum.E65;
         break;

        case 66:
         val = MyEnum.E66;
         break;

        case 67:
         val = MyEnum.E67;
         break;

        case 68:
         val = MyEnum.E68;
         break;

        case 69:
         val = MyEnum.E69;
         break;

        case 70:
         val = MyEnum.E70;
         break;

        case 71:
         val = MyEnum.E71;
         break;

        case 72:
         val = MyEnum.E72;
         break;

        case 73:
         val = MyEnum.E73;
         break;

        case 74:
         val = MyEnum.E74;
         break;

        case 75:
         val = MyEnum.E75;
         break;

        case 76:
         val = MyEnum.E76;
         break;

        case 77:
         val = MyEnum.E77;
         break;

        case 78:
         val = MyEnum.E78;
         break;

        case 79:
         val = MyEnum.E79;
         break;

        case 80:
         val = MyEnum.E80;
         break;

        case 81:
         val = MyEnum.E81;
         break;

        case 82:
         val = MyEnum.E82;
         break;

        case 83:
         val = MyEnum.E83;
         break;

        case 84:
         val = MyEnum.E84;
         break;

        case 85:
         val = MyEnum.E85;
         break;

        case 86:
         val = MyEnum.E86;
         break;

        case 87:
         val = MyEnum.E87;
         break;

        case 88:
         val = MyEnum.E88;
         break;

        case 89:
         val = MyEnum.E89;
         break;

        case 90:
         val = MyEnum.E90;
         break;

        case 91:
         val = MyEnum.E91;
         break;

        case 92:
         val = MyEnum.E92;
         break;

        case 93:
         val = MyEnum.E93;
         break;

        case 94:
         val = MyEnum.E94;
         break;

        case 95:
         val = MyEnum.E95;
         break;

        case 96:
         val = MyEnum.E96;
         break;

        case 97:
         val = MyEnum.E97;
         break;

        case 98:
         val = MyEnum.E98;
         break;

        case 99:
         val = MyEnum.E99;
         break;

        case 100:
         val = MyEnum.E100;
         break;

        case 101:
         val = MyEnum.E101;
         break;

        case 102:
         val = MyEnum.E102;
         break;

        case 103:
         val = MyEnum.E103;
         break;

        case 104:
         val = MyEnum.E104;
         break;

        case 105:
         val = MyEnum.E105;
         break;

        case 106:
         val = MyEnum.E106;
         break;

        case 107:
         val = MyEnum.E107;
         break;

        case 108:
         val = MyEnum.E108;
         break;

        case 109:
         val = MyEnum.E109;
         break;

        case 110:
         val = MyEnum.E110;
         break;

        case 111:
         val = MyEnum.E111;
         break;

        case 112:
         val = MyEnum.E112;
         break;

        case 113:
         val = MyEnum.E113;
         break;

        case 114:
         val = MyEnum.E114;
         break;

        case 115:
         val = MyEnum.E115;
         break;

        case 116:
         val = MyEnum.E116;
         break;

        case 117:
         val = MyEnum.E117;
         break;

        case 118:
         val = MyEnum.E118;
         break;

        case 119:
         val = MyEnum.E119;
         break;

        case 120:
         val = MyEnum.E120;
         break;

        case 121:
         val = MyEnum.E121;
         break;

        case 122:
         val = MyEnum.E122;
         break;

        case 123:
         val = MyEnum.E123;
         break;

        case 124:
         val = MyEnum.E124;
         break;

        case 125:
         val = MyEnum.E125;
         break;

        case 126:
         val = MyEnum.E126;
         break;

        case 127:
         val = MyEnum.E127;
         break;

        case 128:
         val = MyEnum.E128;
         break;

        case 129:
         val = MyEnum.E129;
         break;

        case 130:
         val = MyEnum.E130;
         break;

        case 131:
         val = MyEnum.E131;
         break;

        case 132:
         val = MyEnum.E132;
         break;

        case 133:
         val = MyEnum.E133;
         break;

        case 134:
         val = MyEnum.E134;
         break;

        case 135:
         val = MyEnum.E135;
         break;

        case 136:
         val = MyEnum.E136;
         break;

        case 137:
         val = MyEnum.E137;
         break;

        case 138:
         val = MyEnum.E138;
         break;

        case 139:
         val = MyEnum.E139;
         break;

        case 140:
         val = MyEnum.E140;
         break;

        case 141:
         val = MyEnum.E141;
         break;

        case 142:
         val = MyEnum.E142;
         break;

        case 143:
         val = MyEnum.E143;
         break;

        case 144:
         val = MyEnum.E144;
         break;

        case 145:
         val = MyEnum.E145;
         break;

        case 146:
         val = MyEnum.E146;
         break;

        case 147:
         val = MyEnum.E147;
         break;

        case 148:
         val = MyEnum.E148;
         break;

        case 149:
         val = MyEnum.E149;
         break;

        case 150:
         val = MyEnum.E150;
         break;

        case 151:
         val = MyEnum.E151;
         break;

        case 152:
         val = MyEnum.E152;
         break;

        case 153:
         val = MyEnum.E153;
         break;

        case 154:
         val = MyEnum.E154;
         break;

        case 155:
         val = MyEnum.E155;
         break;

        case 156:
         val = MyEnum.E156;
         break;

        case 157:
         val = MyEnum.E157;
         break;

        case 158:
         val = MyEnum.E158;
         break;

        case 159:
         val = MyEnum.E159;
         break;

        case 160:
         val = MyEnum.E160;
         break;

        case 161:
         val = MyEnum.E161;
         break;

        case 162:
         val = MyEnum.E162;
         break;

        case 163:
         val = MyEnum.E163;
         break;

        case 164:
         val = MyEnum.E164;
         break;

        case 165:
         val = MyEnum.E165;
         break;

        case 166:
         val = MyEnum.E166;
         break;

        case 167:
         val = MyEnum.E167;
         break;

        case 168:
         val = MyEnum.E168;
         break;

        case 169:
         val = MyEnum.E169;
         break;

        case 170:
         val = MyEnum.E170;
         break;

        case 171:
         val = MyEnum.E171;
         break;

        case 172:
         val = MyEnum.E172;
         break;

        case 173:
         val = MyEnum.E173;
         break;

        case 174:
         val = MyEnum.E174;
         break;

        case 175:
         val = MyEnum.E175;
         break;

        case 176:
         val = MyEnum.E176;
         break;

        case 177:
         val = MyEnum.E177;
         break;

        case 178:
         val = MyEnum.E178;
         break;

        case 179:
         val = MyEnum.E179;
         break;

        case 180:
         val = MyEnum.E180;
         break;

        case 181:
         val = MyEnum.E181;
         break;

        case 182:
         val = MyEnum.E182;
         break;

        case 183:
         val = MyEnum.E183;
         break;

        case 184:
         val = MyEnum.E184;
         break;

        case 185:
         val = MyEnum.E185;
         break;

        case 186:
         val = MyEnum.E186;
         break;

        case 187:
         val = MyEnum.E187;
         break;

        case 188:
         val = MyEnum.E188;
         break;

        case 189:
         val = MyEnum.E189;
         break;

        case 190:
         val = MyEnum.E190;
         break;

        case 191:
         val = MyEnum.E191;
         break;

        case 192:
         val = MyEnum.E192;
         break;

        case 193:
         val = MyEnum.E193;
         break;

        case 194:
         val = MyEnum.E194;
         break;

        case 195:
         val = MyEnum.E195;
         break;

        case 196:
         val = MyEnum.E196;
         break;

        case 197:
         val = MyEnum.E197;
         break;

        case 198:
         val = MyEnum.E198;
         break;

        case 199:
         val = MyEnum.E199;
         break;

        case 200:
         val = MyEnum.E200;
         break;

        case 201:
         val = MyEnum.E201;
         break;

        case 202:
         val = MyEnum.E202;
         break;

        case 203:
         val = MyEnum.E203;
         break;

        case 204:
         val = MyEnum.E204;
         break;

        case 205:
         val = MyEnum.E205;
         break;

        case 206:
         val = MyEnum.E206;
         break;

        case 207:
         val = MyEnum.E207;
         break;

        case 208:
         val = MyEnum.E208;
         break;

        case 209:
         val = MyEnum.E209;
         break;

        case 210:
         val = MyEnum.E210;
         break;

        case 211:
         val = MyEnum.E211;
         break;

        case 212:
         val = MyEnum.E212;
         break;

        case 213:
         val = MyEnum.E213;
         break;

        case 214:
         val = MyEnum.E214;
         break;

        case 215:
         val = MyEnum.E215;
         break;

        case 216:
         val = MyEnum.E216;
         break;

        case 217:
         val = MyEnum.E217;
         break;

        case 218:
         val = MyEnum.E218;
         break;

        case 219:
         val = MyEnum.E219;
         break;

        case 220:
         val = MyEnum.E220;
         break;

        case 221:
         val = MyEnum.E221;
         break;

        case 222:
         val = MyEnum.E222;
         break;

        case 223:
         val = MyEnum.E223;
         break;

        case 224:
         val = MyEnum.E224;
         break;

        case 225:
         val = MyEnum.E225;
         break;

        case 226:
         val = MyEnum.E226;
         break;

        case 227:
         val = MyEnum.E227;
         break;

        case 228:
         val = MyEnum.E228;
         break;

        case 229:
         val = MyEnum.E229;
         break;

        case 230:
         val = MyEnum.E230;
         break;

        case 231:
         val = MyEnum.E231;
         break;

        case 232:
         val = MyEnum.E232;
         break;

        case 233:
         val = MyEnum.E233;
         break;

        case 234:
         val = MyEnum.E234;
         break;

        case 235:
         val = MyEnum.E235;
         break;

        case 236:
         val = MyEnum.E236;
         break;

        case 237:
         val = MyEnum.E237;
         break;

        case 238:
         val = MyEnum.E238;
         break;

        case 239:
         val = MyEnum.E239;
         break;

        case 240:
         val = MyEnum.E240;
         break;

        case 241:
         val = MyEnum.E241;
         break;

        case 242:
         val = MyEnum.E242;
         break;

        case 243:
         val = MyEnum.E243;
         break;

        case 244:
         val = MyEnum.E244;
         break;

        case 245:
         val = MyEnum.E245;
         break;

        case 246:
         val = MyEnum.E246;
         break;

        case 247:
         val = MyEnum.E247;
         break;

        case 248:
         val = MyEnum.E248;
         break;

        case 249:
         val = MyEnum.E249;
         break;

        case 250:
         val = MyEnum.E250;
         break;

        case 251:
         val = MyEnum.E251;
         break;

        case 252:
         val = MyEnum.E252;
         break;

        case 253:
         val = MyEnum.E253;
         break;

        case 254:
         val = MyEnum.E254;
         break;

        case 255:
         val = MyEnum.E255;
         break;

        case 256:
         val = MyEnum.E256;
         break;

        case 257:
         val = MyEnum.E257;
         break;

        case 258:
         val = MyEnum.E258;
         break;

        case 259:
         val = MyEnum.E259;
         break;

        case 260:
         val = MyEnum.E260;
         break;

        case 261:
         val = MyEnum.E261;
         break;

        case 262:
         val = MyEnum.E262;
         break;

        case 263:
         val = MyEnum.E263;
         break;

        case 264:
         val = MyEnum.E264;
         break;

        case 265:
         val = MyEnum.E265;
         break;

        case 266:
         val = MyEnum.E266;
         break;

        case 267:
         val = MyEnum.E267;
         break;

        case 268:
         val = MyEnum.E268;
         break;

        case 269:
         val = MyEnum.E269;
         break;

        case 270:
         val = MyEnum.E270;
         break;

        case 271:
         val = MyEnum.E271;
         break;

        case 272:
         val = MyEnum.E272;
         break;

        case 273:
         val = MyEnum.E273;
         break;

        case 274:
         val = MyEnum.E274;
         break;

        case 275:
         val = MyEnum.E275;
         break;

        case 276:
         val = MyEnum.E276;
         break;

        case 277:
         val = MyEnum.E277;
         break;

        case 278:
         val = MyEnum.E278;
         break;

        case 279:
         val = MyEnum.E279;
         break;

        case 280:
         val = MyEnum.E280;
         break;

        case 281:
         val = MyEnum.E281;
         break;

        case 282:
         val = MyEnum.E282;
         break;

        case 283:
         val = MyEnum.E283;
         break;

        case 284:
         val = MyEnum.E284;
         break;

        case 285:
         val = MyEnum.E285;
         break;

        case 286:
         val = MyEnum.E286;
         break;

        case 287:
         val = MyEnum.E287;
         break;

        case 288:
         val = MyEnum.E288;
         break;

        case 289:
         val = MyEnum.E289;
         break;

        case 290:
         val = MyEnum.E290;
         break;

        case 291:
         val = MyEnum.E291;
         break;

        case 292:
         val = MyEnum.E292;
         break;

        case 293:
         val = MyEnum.E293;
         break;

        case 294:
         val = MyEnum.E294;
         break;

        case 295:
         val = MyEnum.E295;
         break;

        case 296:
         val = MyEnum.E296;
         break;

        case 297:
         val = MyEnum.E297;
         break;

        case 298:
         val = MyEnum.E298;
         break;

        case 299:
         val = MyEnum.E299;
         break;

        case 300:
         val = MyEnum.E300;
         break;

        case 301:
         val = MyEnum.E301;
         break;

        case 302:
         val = MyEnum.E302;
         break;

        case 303:
         val = MyEnum.E303;
         break;

        case 304:
         val = MyEnum.E304;
         break;

        case 305:
         val = MyEnum.E305;
         break;

        case 306:
         val = MyEnum.E306;
         break;

        case 307:
         val = MyEnum.E307;
         break;

        case 308:
         val = MyEnum.E308;
         break;

        case 309:
         val = MyEnum.E309;
         break;

        case 310:
         val = MyEnum.E310;
         break;

        case 311:
         val = MyEnum.E311;
         break;

        case 312:
         val = MyEnum.E312;
         break;

        case 313:
         val = MyEnum.E313;
         break;

        case 314:
         val = MyEnum.E314;
         break;

        case 315:
         val = MyEnum.E315;
         break;

        case 316:
         val = MyEnum.E316;
         break;

        case 317:
         val = MyEnum.E317;
         break;

        case 318:
         val = MyEnum.E318;
         break;

        case 319:
         val = MyEnum.E319;
         break;

        case 320:
         val = MyEnum.E320;
         break;

        case 321:
         val = MyEnum.E321;
         break;

        case 322:
         val = MyEnum.E322;
         break;

        case 323:
         val = MyEnum.E323;
         break;

        case 324:
         val = MyEnum.E324;
         break;

        case 325:
         val = MyEnum.E325;
         break;

        case 326:
         val = MyEnum.E326;
         break;

        case 327:
         val = MyEnum.E327;
         break;

        case 328:
         val = MyEnum.E328;
         break;

        case 329:
         val = MyEnum.E329;
         break;

        case 330:
         val = MyEnum.E330;
         break;

        case 331:
         val = MyEnum.E331;
         break;

        case 332:
         val = MyEnum.E332;
         break;

        case 333:
         val = MyEnum.E333;
         break;

        case 334:
         val = MyEnum.E334;
         break;

        case 335:
         val = MyEnum.E335;
         break;

        case 336:
         val = MyEnum.E336;
         break;

        case 337:
         val = MyEnum.E337;
         break;

        case 338:
         val = MyEnum.E338;
         break;

        case 339:
         val = MyEnum.E339;
         break;

        case 340:
         val = MyEnum.E340;
         break;

        case 341:
         val = MyEnum.E341;
         break;

        case 342:
         val = MyEnum.E342;
         break;

        case 343:
         val = MyEnum.E343;
         break;

        case 344:
         val = MyEnum.E344;
         break;

        case 345:
         val = MyEnum.E345;
         break;

        case 346:
         val = MyEnum.E346;
         break;

        case 347:
         val = MyEnum.E347;
         break;

        case 348:
         val = MyEnum.E348;
         break;

        case 349:
         val = MyEnum.E349;
         break;

        case 350:
         val = MyEnum.E350;
         break;

        case 351:
         val = MyEnum.E351;
         break;

        case 352:
         val = MyEnum.E352;
         break;

        case 353:
         val = MyEnum.E353;
         break;

        case 354:
         val = MyEnum.E354;
         break;

        case 355:
         val = MyEnum.E355;
         break;

        case 356:
         val = MyEnum.E356;
         break;

        case 357:
         val = MyEnum.E357;
         break;

        case 358:
         val = MyEnum.E358;
         break;

        case 359:
         val = MyEnum.E359;
         break;

        case 360:
         val = MyEnum.E360;
         break;

        case 361:
         val = MyEnum.E361;
         break;

        case 362:
         val = MyEnum.E362;
         break;

        case 363:
         val = MyEnum.E363;
         break;

        case 364:
         val = MyEnum.E364;
         break;

        case 365:
         val = MyEnum.E365;
         break;

        case 366:
         val = MyEnum.E366;
         break;

        case 367:
         val = MyEnum.E367;
         break;

        case 368:
         val = MyEnum.E368;
         break;

        case 369:
         val = MyEnum.E369;
         break;

        case 370:
         val = MyEnum.E370;
         break;
      }

      if( setList.get(j).contains( val ) ) hits++;
    }

    System.out.println( "Hits: " + hits );

        InputStreamReader input_reader = null;
        int input = 0;

        try
        {
          input_reader = new InputStreamReader( System.in );

          System.out.print("Press <enter>...");

          while( (input = input_reader.read()) != 0  )
          {

            break;

          }

          System.out.println( "Seeya" );

        }
        catch( Exception e )
        {
          System.out.println( "Caught an exception: " + e.toString() );
          e.printStackTrace();
    }

  }


}
