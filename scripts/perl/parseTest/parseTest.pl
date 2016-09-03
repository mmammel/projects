#!/usr/bin/perl -w

my $str = '<?xml version="1.0" encoding="UTF-8"?>\n'.
 '<StatusInfo>\n'.
 '<LogInfo DName="The date" DValue="02/01/2010 4:14:00 PM" IName="The IP address" IValue="75.144.176.233" AName="Activity" AValue="111" />\n'.
 '<LogInfo DName="The date" DValue="11/14/2011 6:36:53 PM" IName="The IP address" IValue="75.144.176.233" AName="Activity" AValue="221" />\n'.
 '<LogInfo DName="The date" DValue="11/14/2011 6:43:54 PM" IName="The IP address" IValue="75.144.176.233" AName="Activity" AValue="311" />\n'.
 '<LogInfo DName="The date" DValue="11/10/2011 2:11:02 PM" IName="The IP address" IValue="216.234.133.229" AName="Activity" AValue="311" />\n'.
 '<LogInfo DName="The date" DValue="11/15/2011 5:03:38 PM" IName="The IP address" IValue="70.167.109.138" AName="Activity" AValue="221" />\n'.
 '<LogInfo DName="The date" DValue="06/04/2010 5:33:40 PM" IName="The IP address" IValue="70.167.109.138" AName="Activity" AValue="221" />\n'.
 '<StatusCode Description="Check license Status" Code="421" />\n'.
 '</StatusInfo>';
my $logLine;
my $logdate;
my $logip;
my $logcode;
my $code;

  if( $str =~ m/Code=\"(.*?)\"/ ) {
    $code = $1;
  }

print "Code: ".$code."\n";

while( $str =~ m/(LogInfo.*? \/)/g ) {
  $logLine = $1;

  if( $logLine =~ m/DValue=\"(.*?)\"/ ) {
    $logdate = $1;
  }
  if( $logLine =~ m/IValue=\"(.*?)\"/ ) {
    $logip = $1;
  }
  if( $logLine =~ m/AValue=\"(.*?)\"/ ) {
    $logcode = $1;
  }

  print "[".$logdate."][".$logip."][".$logcode."]\n";
}


