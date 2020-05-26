<?php
  require 'other.php';

  function func1($a) {
    print( "Func 1: ".$a."\n" );
    func2($a);
  }

?>
