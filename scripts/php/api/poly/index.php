<?php
  $servername = "localhost";
  $username = "mjmtools_api";
  $password = "_#BHAPI0mama1729";
  $dbname = "mjmtools_polyhedra";

  $method = $_SERVER["REQUEST_METHOD"];
  if( $method == "POST" ) {
    $conn = new mysqli($servername, $username, $password, $dbname);
    $data = json_decode(file_get_contents('php://input'), true);
    if( $data["id"] != NULL ) {
      $sql = $conn->prepare("UPDATE polyhedron SET name = ?, vertices = ?, description = ? WHERE id = ?");
      $sql->bind_param('sssd', $data["name"],$data["vertices"],$data["description"],$data["id"]);
    } else {
      $sql = $conn->prepare("INSERT INTO polyhedron (name, vertices, description, created_date) VALUES (?,?,?,NOW())");
      $sql->bind_param('sss', $data["name"],$data["vertices"],$data["description"]);
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
      $sql = $conn->prepare("SELECT id, name, vertices, description FROM polyhedron WHERE id = ? ORDER BY name");
      $sql->bind_param( "d", intval( $_GET["id"] ) );
    } else {
      $sql = $conn->prepare("SELECT id, name, vertices, description FROM polyhedron ORDER BY name");
    }

    $sql->execute();
    $sql->bind_result($c0,$c1,$c2,$c3);

    while( $sql->fetch() ) {
      $polys[] = array( 'id' => $c0, 'name' => $c1, 'vertices' => $c2, 'description' => $c3 );
    }

    $sql->close();
    $conn->close();

    $output = array( 'status' => 'success', 'results' => $polys );
  }

  header("Content-Type: text/json");
  print json_encode($output);
?>

