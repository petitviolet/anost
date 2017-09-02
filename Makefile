TOKEN=

run:
	sbt -mem 2048 'project adapter' '~run'

compile:
	sbt -mem 2048 compile

create-user:
	curl 'localhost:8888/user' -XPOST -H "Content-Type: application/json" -d $$(jo name=hoge email=aa@aa.aa password=password) -v

create-post:
	curl localhost:8888/post -XPOST -H "Content-Type: application/json" -H "Authorization: Bearer $(TOKEN)" -d $$(jo title=test fileType=scala contents="println(\"HelloWorld!\")") -v

login:
	@curl -sS 'localhost:8888/user/login?email=aa@aa.aa&password=password' | jq -r .token.value

reset-db:
	sbt -mem 2048 'project adapter' 'flywayClean' 'flywayMigrate'
