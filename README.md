# this is a sample application to control your used memory in intention to analyze your environment how it behaves.
we tested
  * **our OOM notification setup** (for instance send a mail on OOM with [-XX:OnOutOfMemoryError](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/clopts001.html))
  * **has your system enough power** (for instance is your docker container configured well)

# how to use that
build and run this app with maven
```
mvn clean package -DskipDocker
java -jar target/oom.generator-0.0.1-SNAPSHOT.jar
```

## add to current used memory
  * 500 MB http://localhost:8080/memory/?add=524288000
  * 1G http://localhost:8080/memory/?add=1073741824
  * 2G http://localhost:8080/memory/?add=2147483648

## clear the added memory
  * http://localhost:8080/clear-memory

## generate out of memory
  * OOM http://localhost:8080/generate-oom

## current used memory
  * http://localhost:8080/actuator/metrics/jvm.memory.used

## max jvm memory
  * http://localhost:8080/actuator/metrics/jvm.memory.max

## max system memory
  * is there a spring boot endpoint?

# Deployment in an AWS ECS Cluster

## without build & deployment Codepipeline

**Make sure the ECS Cluster can pull from the ECR repo**

1. get credentials for your ECR
  ```
  aws ecr get-login --region <aws_region>
  ```
1. build and push the oom.generator in your ECR
  ```
  mvn clean deploy -DawsAccountId=<aws_account_id> -DawsRegion=<aws_region>
  ```
1. deploy the generated Image in your ECS Cluster
  ```
  aws cloudformation create-stack --stack-name oom-generator --template-body file://ecs.yaml --parameters ParameterKey=ClusterName,ParameterValue="<ecs_cluster_name>" ParameterKey=ClusterSecurityGroup,ParameterValue="<ecs_cluster_security_group>" ParameterKey=DesiredCount,ParameterValue="<ecs_service_desired_count>"  ParameterKey=FirstAlbSubnet,ParameterValue="<first_application_loadbalancer_subnet>" ParameterKey=SecondAlbSubnet,ParameterValue="<second_application_loadbalancer_subnet>" ParameterKey=Image,ParameterValue="<ecr_image>" ParameterKey=ServiceName,ParameterValue="<ecs_service_name>" ParameterKey=Vpc,ParameterValue="<ecs_cluster_vpc>"
  ```
  for example:
  ```
  aws cloudformation create-stack --stack-name oom-generator --template-body file://ecs.yaml --parameters ParameterKey=ClusterName,ParameterValue="ecs-cluster" ParameterKey=ClusterSecurityGroup,ParameterValue="sg-a1496acc" ParameterKey=DesiredCount,ParameterValue="1" ParameterKey=FirstAlbSubnet,ParameterValue="subnet-42799629" ParameterKey=SecondAlbSubnet,ParameterValue="subnet-42799630" ParameterKey=Image,ParameterValue="217216133150.dkr.ecr.eu-central-1.amazonaws.com/oom.generator:0.0.1-SNAPSHOT" ParameterKey=ServiceName,ParameterValue="oom-generator" ParameterKey=Vpc,ParameterValue="vpc-7148111c"
  ```

  ## with build & deployment Codepipeline

  1. create your personal Github access token as described  [here](https://docs.aws.amazon.com/codepipeline/latest/userguide/GitHub-rotate-personal-token-CLI.html)
  1. set/update the desired values in the *ecs.config* file e.g. *DesiredCount* or *LogzioToken*
  1. create the build & deployment Codepipeline, which will automatically deploy the oom.generator with a separate Cloudformation stack your the ECS cluster
    ```
    aws cloudformation create-stack --stack-name oom-build-deployment --template-body file://codepipeline.yaml --parameters ParameterKey=ClusterName,ParameterValue="<ecs_cluster_name>" ParameterKey=ClusterSecurityGroup,ParameterValue="<ecs_cluster_security_group>" ParameterKey=CreateCodePipelineS3Bucket,ParameterValue="{true|false}" ParameterKey=S3BucketName,ParameterValue="<codepipeline_s3_bucket_name>" ParameterKey=CreateEcrRepository,ParameterValue="{true|false}" ParameterKey=ECRRepositoryName,ParameterValue="<ecr_name>" ParameterKey=FirstAlbSubnet,ParameterValue="<first_application_loadbalancer_subnet>" ParameterKey=SecondAlbSubnet,ParameterValue="<second_application_loadbalancer_subnet>" ParameterKey=OAuthToken,ParameterValue="<github_personal_tolen>" ParameterKey=ProjectOwner,ParameterValue="<github_project_owner>" ParameterKey=ProjectName,ParameterValue="<github_project_name>" ParameterKey=StackName,ParameterValue="<ecs_cloudformation_stack_name>" ParameterKey=Vpc,ParameterValue="<ecs_cluster_vpc>"
    ```
    for example:
    ```
    aws cloudformation create-stack --stack-name oom-build-deployment --template-body file://codepipeline.yaml --parameters ParameterKey=ClusterName,ParameterValue="ecs-cluster" ParameterKey=ClusterSecurityGroup,ParameterValue="sg-a1496acc>" ParameterKey=CreateCodePipelineS3Bucket,ParameterValue="true" ParameterKey=S3BucketName,ParameterValue="oom-generator" ParameterKey=CreateEcrRepository,ParameterValue="true" ParameterKey=ECRRepositoryName,ParameterValue="oom-generator" ParameterKey=FirstAlbSubnet,ParameterValue="subnet-42799629" ParameterKey=SecondAlbSubnet,ParameterValue="subnet-42799630" ParameterKey=OAuthToken,ParameterValue="92d923ada93757********" ParameterKey=ProjectOwner,ParameterValue="StephanPraetsch" ParameterKey=ProjectName,ParameterValue="oom.generator" ParameterKey=StackName,ParameterValue="oom-generator" ParameterKey=Vpc,ParameterValue="vpc-7148111c"
    ```
  1.
