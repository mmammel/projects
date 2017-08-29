BEGIN {
  FS=":";
  city="N/A";
  country="N/A";
  region="N/A";
}
{
  if( $2 != " " ) {
    gsub("^ ", "", $2);
    if( $1 == "country" ) {
      country = $2;
    } else if ( $1 == "region" ) {
      region = $2;
    } else if ( $1 == "city" ) {
      city = $2;
    }
  }
}
END {
  printf("%s\t%s\t%s",city, region, country);
}
