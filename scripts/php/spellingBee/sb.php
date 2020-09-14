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

}



?>
