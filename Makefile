###

ls:
	@docker-compose run scraper /bin/bash -c "pwd && ls -la /var/lib/"

appname="throwawaycensusscraper"
run:
	@docker-compose run scraper /bin/bash -c "./target/universal/stage/bin/${appname}"

dc-clean:
	docker-compose down && docker-compose rm -v

pg-test:
	docker exec -it my_postgres  psql -U john -d mydb -c "CREATE TABLE IF NOT EXISTS test (id serial PRIMARY KEY, num integer, data varchar);"

pg-loc:
	PGPASSWORD=pwd0123456789  psql -h localhost -p 54320 -U john -d mydb

 .PHONY: run