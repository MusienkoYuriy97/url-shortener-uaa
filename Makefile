build:
	docker build -t 97musienko/uaa .
	docker login -u 97musienko -p 04612e6b-d0b4-44ca-83aa-e3a9fd19e096
	docker push 97musienko/uaa
up:
	docker-compose -f ./docker/docker-compose.yml up
down:
	docker-compose -f ./docker/docker-compose.yml down