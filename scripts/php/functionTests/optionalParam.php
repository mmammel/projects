<?php

function foobar( $arr ) {
  print( $arr[0]."\n" );
  if( isset( $arr[1] ) ) {
    print( $arr[1]."\n" );
  }
}

$array = array( "foo", "bar" );

foobar($array);

?>
