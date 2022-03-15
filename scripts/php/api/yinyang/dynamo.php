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

  $tableName = 'yinyang';

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
          'ProjectionExpression' => '#nm, numrows, numcols, #desc',
          'KeyConditionExpression' => '#nm = :name',
	  'ExpressionAttributeNames'=> [ '#nm' => 'name', '#desc' => 'descriptor' ],
          'ExpressionAttributeValues'=> $eav
         ];

       try {
         $result = $dynamodb->query($params);

         foreach ($result['Items'] as $i) {
           $puzzle = $marshaler->unmarshalItem($i);
	   $yinyang[] = array( 'name' => $puzzle['name'], 'rows' => $puzzle['numrows'], 'cols' => $puzzle['numcols'], 'descriptor' => $puzzle['descriptor'] );
         }
        $output = array( 'status' => 'success', 'results' => $yinyang );

       } catch (DynamoDbException $e) {
        $output = array( 'status' => 'failure', 'message' => $e->getMessage() );
       }
      } else {
        $params = [
          'TableName' => $tableName,
          'ProjectionExpression' => '#nm, numrows, numcols, #desc',
	  'ExpressionAttributeNames'=> [ '#nm' => 'name', '#desc' => 'descriptor' ]
        ];

       try {
        while (true) {
          $result = $dynamodb->scan($params);

          foreach ($result['Items'] as $i) {
            $puzzle = $marshaler->unmarshalItem($i);
            $yinyang[] = array( 'name' => $puzzle['name'], 'rows' => $puzzle['numrows'], 'cols' => $puzzle['numcols'], 'descriptor' => $puzzle['descriptor'] );
          }

          if (isset($result['LastEvaluatedKey'])) {
            $params['ExclusiveStartKey'] = $result['LastEvaluatedKey'];
          } else {
            break;
          }
        }

        $output = array( 'status' => 'success', 'results' => $yinyang );
       } catch (DynamoDbException $e) {
        $output = array( 'status' => 'failure', 'message' => $e->getMessage() );
       }
      }

 }

  header("Content-Type: text/json");
  print json_encode($output);
?>

