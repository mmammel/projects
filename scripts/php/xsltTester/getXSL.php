<?php
require_once __DIR__.'/sdk/aws-autoloader.php';

use Aws\S3\S3Client;
use Aws\Credentials\CredentialProvider;
use Aws\Exception\AwsException;
use Aws\Result;

// Set response headers for JSON output
header('Content-Type: application/json');

$client = $_GET["c"];
$board = $_GET["b"];

$bucketName = 'skillcheck.dropbox'; // Replace with your S3 bucket name
$fileKey = 'smartpost/xslt/'.$client.'/'.$board.'/'.transform.xsl; // Optional key prefix (folder path)

try {

    $provider = CredentialProvider::instanceProfile();

    $s3Client = new S3Client([
      'version' => 'latest',
      'region' => 'us-west-2',
      'credentials' => $provider
    ]);

    // Prepare JSON response
    $response = [];

    // Retrieve file content from S3
    $fileContent = $s3Client->getObject([
      'Bucket' => $bucketName,
      'Key'    => $fileKey
    ]);

    // Read the XML content
    $xslData = (string) $fileContent['Body'];

    // Append to response array
    $response[] = [
        "file" => $fileKey,
        "last_modified" => $lastModified,
        "content" => $xslData
    ];

    // Return JSON response
    echo json_encode(["files" => $response], JSON_PRETTY_PRINT);

} catch (AwsException $e) {
    echo json_encode(["error" => $e->getMessage()]);
}
?>

