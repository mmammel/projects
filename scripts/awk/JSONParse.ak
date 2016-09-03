BEGIN {
  FS="\""
  recordCount = 0;
  print "IF OBJECT_ID('PostAssessmentAnswer','U') IS NOT NULL"
  print "DROP TABLE PostAssessmentAnswer;"
  print ""
  print "CREATE TABLE PostAssessmentAnswer ( \
      id INT IDENTITY NOT NULL, \
      date DATETIME NOT NULL, \
      candidateId NVARCHAR(255) NOT NULL, \
      question NVARCHAR(255) NOT NULL, \
      answerId INT, \
      answerText NVARCHAR(255) \
   );"
}
/\{/ {
  if( recordCount % 100 == 0 ) {
    if( recordCount > 0 ) print ";";
    printf( "INSERT INTO PostAssessmentAnswer (date,candidateId,question,answerId,answerText) VALUES (" )
  } else {
    printf( ",(" );
  }
}
/\"Date\"/ {
  dateString = $4;
  gsub( /T/," ",dateString );
  gsub( /\..*$/,"",dateString );
  printf( "'%s',", dateString );
}
/\"AssesseeId\"/ {
  printf( "'%s',",$4 );
}
/\"QuestionTitle\"/ {
  printf( "'%s',",$4 );
}
/\"AnswerId\"/ {
  string = $3;
  gsub(/[^0-9]/,"",string);
  if( length(string) == 0 ) {
    string = "NULL";
  }
  printf( "%s,",string );
}
/\"AnswerText\"/ {
  printf( "'%s'",$4 );
}
/\}/ {
  printf ")"
  recordCount++;
}
END {
  print ";";
}

