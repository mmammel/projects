<?php
  $servername = "localhost";
  $username = "mjmtools_api";
  $password = "_#BHAPI0mama1729";
  $dbname = "mjmtools_shapes";

  $method = $_SERVER["REQUEST_METHOD"];
  if( $method == "POST" ) {
    $conn = new mysqli($servername, $username, $password, $dbname);
    $data = json_decode(file_get_contents('php://input'), true);
    if( $data["id"] != NULL ) {
      $sql = $conn->prepare("UPDATE shape SET name = ?, faces = ?, description = ? WHERE id = ?");
      $sql->bind_param('sssd', $data["name"],$data["faces"],$data["description"],$data["id"]);
    } else {
      $sql = $conn->prepare("INSERT INTO shape (name, faces, description, created_date) VALUES (?,?,?,NOW())");
      $sql->bind_param('sss', $data["name"],$data["faces"],$data["description"]);
    }

    if(!$sql->execute()) {
        trigger_error('Error executing MySQL query: ' . $sql->error);
    }
    $sql->close();
    $conn->close();

    $output = array( 'status' => 'success' );

  } else if( $method == "GET" ) {
    $conn = new mysqli($servername, $username, $password, $dbname);
    if( isset($_GET["id"]) ) {
      $sql = $conn->prepare("SELECT id, name, faces, description FROM shape WHERE id = ? ORDER BY name");
      $sql->bind_param( "d", intval( $_GET["id"] ) );
    } else {
      $sql = $conn->prepare("SELECT id, name, faces, description FROM shape ORDER BY name");
    }

    $sql->execute();
    $sql->bind_result($c0,$c1,$c2,$c3);

    while( $sql->fetch() ) {
      $shapes[] = array( 'id' => $c0, 'name' => $c1, 'faces' => $c2, 'description' => $c3 );
    }

    $sql->close();
    $conn->close();

    $output = array( 'status' => 'success', 'results' => $shapes );
  }

  header("Content-Type: text/json");
  print json_encode($output);
?>

