#!/bin/bash

bin=$(dirname "$0")
bin=$(cd "$bin" && pwd)

BUCKET=movies-online

AWS="aws --endpoint-url=${ENDPOINT_URL}"

echo "Sleeping for 10 seconds for minio start..."
sleep 10

echo "Creating bucket s3://${BUCKET}"
$AWS s3 mb s3://$BUCKET
$AWS s3api put-bucket-policy --bucket $BUCKET --policy "file://$bin/movies-online.policy.json"

echo "Uploading posters to s3://${BUCKET}/posters"
POSTER_FILES=$(ls "/posters")
for file in $POSTER_FILES ; do
    $AWS s3 cp "/posters/$file" "s3://$BUCKET/posters/$file"
done
