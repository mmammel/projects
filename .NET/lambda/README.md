https://docs.aws.amazon.com/lambda/latest/dg/csharp-package-cli.html

dotnet new list  # list templates

dotnet new lambda.EmptyFunction --help  # help with creating an empty function

dotnet new lambda.EmptyFunction --name myDotnetFunction # create a function

aws-lambda-tools-defaults.json:

  "profile" : "default",
  "region" : "us-east-2",
  "configuration" : "Release",
  "function-architecture": "x86_64",
  "function-runtime":"dotnet8",
  "function-memory-size" : 256,
  "function-timeout" : 30,
  "function-handler" : "myDotnetFunction::myDotnetFunction.Function::FunctionHandler"

# Deployment

cd myDotnetFunction/src/myDotnetFunction

dotnet lambda deploy-function myDotnetFunction

dotnet lambda invoke-function myDotnetFunction --payload "Just checking if everything is OK"

# Layers

dotnet lambda publish-layer <layer_name> --layer-type runtime-package-store --s3-bucket <s3_bucket_name>

dotnet lambda deploy-function <function_name> --function-layers arn:aws:lambda:us-east-1:123456789012:layer:layer-name:1


