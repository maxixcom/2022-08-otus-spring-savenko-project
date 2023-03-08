#!/bin/bash

bin=$(dirname "$0")
bin=$(cd "$bin"; pwd)

BUCKET=movies-online

AWS='/usr/local/bin/aws --endpoint-url=http://localhost:9000'


FILES=$($AWS s3 ls s3://movies-online/posters/ | cut -d ' ' -f 9)

for file in $FILES; do
    $AWS s3 cp "s3://$BUCKET/posters/$file" "$bin/posters/$file"
done
