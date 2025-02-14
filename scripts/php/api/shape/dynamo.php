<?php
  require_once(__DIR__.'/../../aws/aws-autoloader.php');

  date_default_timezone_set('UTC');

  use Aws\DynamoDb\Exception\DynamoDbException;
  use Aws\DynamoDb\Marshaler;
  use Aws\Credentials\CredentialProvider;

  $provider = CredentialProvider::instanceProfile();

  $sdk = new Aws\Sdk([
    'region'   => 'us-east-1',
    'version'  => 'latest',
    'credentials' => $provider
  ]);

  $dynamodb = $sdk->createDynamoDb();
  $marshaler = new Marshaler();

  $tableName = 'shape';

  $method = $_SERVER["REQUEST_METHOD"];
  if( $method == "POST" ) {
    $inputJson = file_get_contents('php://input');
    $item = $marshaler->marshalJson( $inputJson );

    $params = [
        'TableName' => $tableName,
        'Item' => $item
    ];

    try {
        $result = $dynamodb->putItem($params);
        $output = array( 'status' => 'success' );
    } catch (DynamoDbException $e) {
        $output = array( 'status' => 'failure', 'message' => $e->getMessage() );
    }

  } else if( $method == "GET" ) {

      if( isset($_GET["name"]) ) {
        $eav = $marshaler->marshalJson('
        {
            ":name":"'.$_GET["name"].'"
        }
        ');

        $params = [
          'TableName' => $tableName,
          'ProjectionExpression' => '#nm, faces, #desc',
          'KeyConditionExpression' => '#nm = :name',
	  'ExpressionAttributeNames'=> [ '#nm' => 'name', '#desc' => 'description' ],
          'ExpressionAttributeValues'=> $eav
         ];

       try {
         $result = $dynamodb->query($params);

         foreach ($result['Items'] as $i) {
           $shape = $marshaler->unmarshalItem($i);
	   $shapes[] = array( 'name' => $shape['name'], 'faces' => $shape['faces'], 'description' => $shape['description'] );
         }
        $output = array( 'status' => 'success', 'results' => $shapes );

       } catch (DynamoDbException $e) {
        $output = array( 'status' => 'failure', 'message' => $e->getMessage() );
       }
      } else {
        $params = [
          'TableName' => $tableName,
          'ProjectionExpression' => '#nm, faces, #desc',
	  'ExpressionAttributeNames'=> [ '#nm' => 'name', '#desc' => 'description' ]
        ];

       try {
        while (true) {
          $result = $dynamodb->scan($params);

          foreach ($result['Items'] as $i) {
            $shape = $marshaler->unmarshalItem($i);
            $shapes[] = array( 'name' => $shape['name'], 'faces' => $shape['faces'], 'description' => $shape['description'] );
          }

          if (isset($result['LastEvaluatedKey'])) {
            $params['ExclusiveStartKey'] = $result['LastEvaluatedKey'];
          } else {
            break;
          }
        }

        $output = array( 'status' => 'success', 'results' => $shapes );
       } catch (DynamoDbException $e) {
        $output = array( 'status' => 'failure', 'message' => $e->getMessage() );
       }
      }

 }

  header("Content-Type: text/json");
  print json_encode($output);
?>

