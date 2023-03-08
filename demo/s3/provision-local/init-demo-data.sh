#!/bin/bash

bin=$(dirname "$0")
bin=$(cd "$bin"; pwd)

BUCKET=movies-online

AWS='/usr/local/bin/aws --endpoint-url=http://localhost:9000'

echo "Creating bucket s3://${BUCKET}"
$AWS s3 mb s3://$BUCKET
$AWS s3api put-bucket-policy --bucket $BUCKET --policy "file://$bin/movies-online.policy.json"


echo "Uploading posters to s3://${BUCKET}/posters"
#Upload posters
POSTER_FILES=$(ls "$bin/posters")
for file in $POSTER_FILES ; do
    $AWS s3 cp "$bin/posters/$file" "s3://$BUCKET/posters/$file"
done