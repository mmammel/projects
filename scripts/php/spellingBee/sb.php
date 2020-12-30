<?php

if( isset($_GET["s"]) ) {
  $letters = $_GET["s"];

  if( preg_match( "/^[a-z]+$/", $letters) == 1 ) {
    $keyletter = substr($letters,-1);

    $result = shell_exec('cat ./scrabble_words.txt | egrep "^['.$letters.']{4,}$" | grep '.$keyletter);

    echo $result;
  } else {
    echo "error";
  }
} else if( isset($_GET["t"]) ) {
  $result = shell_exec('curl -s https://www.nytimes.com/puzzles/spelling-bee | awk '.escapeshellarg('{ idx=index($0,"window.gameData"); if(idx>0) { obj=substr($0,idx); idx=index(obj, "{"); obj=substr(obj, idx); idx=index(obj,"</script>"); obj=substr(obj,0,idx-1); printf("%s\n", obj); } }') );
  echo $result;
}



?>
