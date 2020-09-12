<?php

if( isset($_GET["s"]) ) {
  $letters = $_GET["s"];
  $keyletter = substr($letters,-1);

  $result = shell_exec('cat ./scrabble_words.txt | egrep "^['.$letters.']{4,}$" | grep '.$keyletter);

  echo $result;

}



?>
