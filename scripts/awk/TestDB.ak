function toJSONArray(arr) {
  retVal = "[";
  count = 0;
  for( ele in arr ) {
    if( count++ > 0 ) {
      retVal = retVal ",";
    }
    retVal = retVal "\"" arr[ele] "\"";
  }
  retVal = retVal "]";
  return retVal;
}

BEGIN { FS="\t" }
{
  count=0;
  delete industries;
  delete categories;
  for( i = 12; i <= 20; i++ ) {
    if( length($i) > 0 ) {
      industries[count++] = $i;
    }
  }
  count = 0;
  for( i = 21; i <= 40; i++ ) {
    if( length($i) > 0 ) {
      categories[count++] = $i;
    }
  }
  desc = $2;
  gsub(/'/,"'\"'\"'",desc);
  print "curl -XPUT localhost:9200/assessments/test/" $5 " -d '{ \
         \"testKey\" : \"" $5 "\",\
         \"name\" : \"" $3 "\",\
         \"description\" : \"" desc "\",\
         \"productgroup\" : \"" $4 "\",\
         \"tokenvalue\" : \"" $1 "\",\
         \"level\" : \"" $6 "\",\
         \"version\" : \"" $7 "\",\
         \"language\" : \"" $8 "\",\
         \"type\" : \"" $9 "\",\
         \"items\" : \"" $10 "\",\
         \"duration\" : \"" $11 "\",\
         \"industry\" : " toJSONArray(industries) ",\
         \"category\" : " toJSONArray(categories) "\
     }';" 
}
END {
}
