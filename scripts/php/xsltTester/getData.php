<?php
require_once __DIR__.'/sdk/aws-autoloader.php';

use Aws\S3\S3Client;
use Aws\Credentials\CredentialProvider;
use Aws\Exception\AwsException;
use Aws\Result;

// Set response headers for JSON output
header('Content-Type: application/json');

$client = $_GET["c"]

$bucketName = 'skillcheck.dropbox'; // Replace with your S3 bucket name
$keyPrefix = 'smartpost/sampledata/'.$client.'/'; // Optional key prefix (folder path)

try {

    $provider = CredentialProvider::instanceProfile();

    $s3Client = new S3Client([
      'version' => 'latest',
      'region' => 'us-west-2',
      'credentials' => $provider
    ]);

    // List objects from the bucket with a prefix
    $result = $s3Client->listObjectsV2([
        'Bucket' => $bucketName,
        'Prefix' => $keyPrefix,
    ]);

    // Check if objects exist
    if (!isset($result['Contents']) || empty($result['Contents'])) {
        echo json_encode(["message" => "No XML files found."]);
        exit;
    }

    // Filter XML files
    $xmlFiles = array_filter($result['Contents'], function ($file) {
        return strtolower(pathinfo($file['Key'], PATHINFO_EXTENSION)) === 'xml';
    });

    // Sort XML files by LastModified (newest first)
    usort($xmlFiles, function ($a, $b) {
        return strtotime($b['LastModified']) - strtotime($a['LastModified']);
    });

    // Get the most recent 5 XML files
    $recentXmlFiles = array_slice($xmlFiles, 0, 5);

    // Prepare JSON response
    $response = [];

    foreach ($recentXmlFiles as $file) {
        $fileKey = $file['Key'];
        $lastModified = $file['LastModified'];

        // Retrieve file content from S3
        $fileContent = $s3Client->getObject([
            'Bucket' => $bucketName,
            'Key'    => $fileKey
        ]);

        // Read the XML content
        $xmlData = (string) $fileContent['Body'];

        // Append to response array
        $response[] = [
            "file" => $fileKey,
            "last_modified" => $lastModified,
            "content" => $xmlData
        ];
    }

    // Return JSON response
    echo json_encode(["files" => $response], JSON_PRETTY_PRINT);

} catch (AwsException $e) {
    echo json_encode(["error" => $e->getMessage()]);
}
?>

