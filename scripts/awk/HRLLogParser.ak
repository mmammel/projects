BEGIN {
  FS=" "
  printTime=0;
  packetLoss="";
}
/packet loss/ {
  tempPacketLoss = $6;
  if( tempPacketLoss != packetLoss ) {
    printTime = 1;
    packetLoss = tempPacketLoss;
  } else {
    printTime = 0;
  }
}
/E[SD]T/ {
  if( printTime ) {
    printf( "%s %s\n", $0, packetLoss);
  }
}
