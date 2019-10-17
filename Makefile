###

ls:
	@docker-compose run scraper /bin/bash -c "pwd && ls -la /var/lib/"

appname="throwawaycensusscraper"
run:
	@docker-compose run scraper /bin/bash -c "./target/universal/stage/bin/${appname}"


 .PHONY: run