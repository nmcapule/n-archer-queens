# Run gradle via docker.
# Examples:
#   make docker-gradle ARGS="init --project-name=n-archers --type=java-application"
#   make docker-gradle ARGS="run"
docker-gradle:
	docker run --rm -u gradle \
		-v "$(PWD)":/home/gradle/project \
		-w /home/gradle/project \
		gradle gradle $(ARGS)
