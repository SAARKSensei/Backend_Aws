.PHONY: help build compile test verify run clean docker-build docker-up docker-down docker-logs

help: ## Show this help message
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  %-15s %s\n", $$1, $$2}'

build: ## Build the project (clean package)
	mvn clean package -DskipTests

compile: ## Compile the source code
	mvn clean compile

test: ## Run unit tests
	mvn clean test

verify: ## Run integration tests and verify package
	mvn clean verify

run: ## Run the Spring Boot application locally
	mvn spring-boot:run

clean: ## Clean the target directory
	mvn clean

docker-build: ## Build the Docker image
	docker build -t sensei-backend .

docker-up: ## Start the Docker containers (Dozzle logging, etc.)
	docker compose up -d

docker-down: ## Stop the Docker containers
	docker compose down

docker-logs: ## Tail the logs from Docker containers
	docker compose logs -f
