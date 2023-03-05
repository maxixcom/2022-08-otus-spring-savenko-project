.PHONY: help all build
.DEFAULT_GOAL := help

-include .env

export DOCKER?=docker

MODULES:=eureka gateway platform
GRADLE:=$(CURDIR)/gradlew

build: ## Build modules
	$(GRADLE) build

clean: ## Clean modules build files
	$(GRADLE) clean

docker-build: docker-build-back docker-build-front ## Build docker images
	$(GRADLE) jibDockerBuild

docker-build-back:
	$(GRADLE) jibDockerBuild

docker-build-front:
	$(MAKE) -C $(CURDIR)/frontend docker-build

-include $(CURDIR)/Makefile.DockerCompose.mk

help:
	@awk 'BEGIN {FS = ":.*##"; printf "Usage: make \033[36m<target>\033[0m\n"} /^[a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-10s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)
