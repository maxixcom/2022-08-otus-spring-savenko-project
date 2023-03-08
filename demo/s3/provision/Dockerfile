FROM amazon/aws-cli

COPY .aws/ /root/.aws/
COPY minio-init/ /minio-init/

WORKDIR /minio-init

ENV ENDPOINT_URL "http://minio:9000"

ENTRYPOINT ["/bin/bash","-c","./entrypoint.sh"]