<?php
  $servername = "localhost";
  $username = "mjmtools_api";
  $password = "_#BHAPI0mama1729";
  $dbname = "mjmtools_yinyang";

  $method = $_SERVER["REQUEST_METHOD"];
  if( $method == "POST" ) {
    $conn = new mysqli($servername, $username, $password, $dbname);
    $data = json_decode(file_get_contents('php://input'), true);
    if( $data["id"] != NULL ) {
      $sql = $conn->prepare("UPDATE yinyang SET name = ?, numrows = ?, numcols = ?, descriptor = ? WHERE id = ?");
      $sql->bind_param('sddsd', $data["name"],$data["rows"],$data["cols"],$data["descriptor"],$data["id"]);
    } else {
      $sql = $conn->prepare("INSERT INTO yinyang (name, numrows, numcols, descriptor, created_date) VALUES (?,?,?,?,CURRENT_TIMESTAMP)");
      $sql->bind_param('sdds', $data["name"],$data["rows"],$data["cols"],$data["descriptor"]);
    }

    if(!$sql->execute()) {
        trigger_error('Error executing MySQL query: ' . $sql->error);
    }
    $sql->close();
    $conn->close();

    $output = array( 'status' => 'success' );

  } else if( $method == "GET" ) {
    $conn = new mysqli($servername, $username, $password, $dbname);

    if ($conn->connect_errno) {
      $output = array( 'status' => 'failure', 'results' => 'Error ['.$conn->connect_errno.']: '.$conn->connect_error );
    } else {
      if( isset($_GET["id"]) ) {
        $sql = $conn->prepare("SELECT id, name, numrows, numcols, descriptor FROM yinyang WHERE id = ? ORDER BY name");
        $sql->bind_param( "d", intval( $_GET["id"] ) );
      } else {
        $sql = $conn->prepare("SELECT id, name, numrows, numcols, descriptor FROM yinyang ORDER BY name");
      }
  
      $sql->execute();
      $sql->bind_result($c0,$c1,$c2,$c3,$c4);
  
      while( $sql->fetch() ) {
        $yinyang[] = array( 'id' => $c0, 'name' => $c1, 'rows' => $c2, 'cols' => $c3, 'descriptor' => $c4 );
      }
  
      $sql->close();
      $conn->close();
  
      $output = array( 'status' => 'success', 'results' => $yinyang );
    }
  }

  header("Content-Type: text/json");
  print json_encode($output);
?>

