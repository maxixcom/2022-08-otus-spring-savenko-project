
DOCKER_COMPOSE?=docker-compose

start: ## dev - Run docker compose for dev env
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	up -d

stop: ## dev - Stop docker for compose dev env
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	down

stop-clean: ## dev - Stop docker for compose dev env andremove volumes
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	down -v


status: ## dev - Status running docker services dev env
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	ps

start-demo: ## demo - Run docker compose for demo env
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-demo.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	up -d

stop-demo: ## demo - Stop docker for compose demo env
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-demo.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	down

stop-demo-clean: ## demo - Stop docker for compose demo env and remove volumes
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-demo.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	down -v

status-demo: ## demo - Status running docker services for demo dev
	@$(DOCKER_COMPOSE) \
	-f $(CURDIR)/docker-compose.yml \
	-f $(CURDIR)/docker-compose-demo.yml \
	-f $(CURDIR)/docker-compose-metrics.yml \
	ps