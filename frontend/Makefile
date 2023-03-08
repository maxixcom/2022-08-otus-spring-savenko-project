.PHONY: help
.DEFAULT_GOAL := help

NPM=npm

## Install
install: node_modules ## Install procedure

node_modules:
	$(NPM) install

build: node_modules
	$(NPM) run build

clean: ## Clean everything
	rm -rf $(CURDIR)/node_modules
	rm -rf $(CURDIR)/dist

uninstall: clean ## Uninstall procedure


DOCKER=docker
IMAGE=movies-online/frontend

docker-build: ## Build docker image (Fix: registry is localhost)
	$(DOCKER) build -t $(IMAGE) -f Dockerfile $(CURDIR)


help:
		@awk 'BEGIN {FS = ":.*##"; printf "Usage: make \033[36m<target>\033[0m\n"} /^[a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-10s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)
