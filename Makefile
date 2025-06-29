ROOT_DIR	=	$$HOME/services/pms_server/volumes/pms_server

prepareDirs:
	-mkdir -p $(ROOT_DIR)/cert/server
	-mkdir -p $(ROOT_DIR)/cert/postgres_client
	-mkdir -p $(ROOT_DIR)/data
	-mkdir -p $(ROOT_DIR)/spring

build: prepareDirs
	@mvn package -P PROD -U
	@docker compose build

foreground:
	@docker compose up pms_server

background:
	@docker compose up -d pms_server

.PHONY: prepareDirs build foreground background
