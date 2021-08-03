build:
	docker build -t 97musienko/uaa .
	docker login
	docker push 97musienko/uaa
up:
	docker-compose -f ./docker/docker-compose.yml up
down:
	docker-compose -f ./docker/docker-compose.yml down