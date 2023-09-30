
# Music API (V2)

API para cadastro e consulta de musicas usando AWS SDK V2 e API do Spotify.

## Documentação da API

### `POST /musics/v2/save`

Busca registros de uma musica e salva os dados em uma tabela do DynamoDB.

#### Body

- `name`: Nome da música
- `artists` : Lista com os nome dos artistas envolvidos na música

#### Response

Será retornado um objeto com as seguintes informações:

- `name`: Nome da música.
- `uri`: Link para ouvir a música no Spotify.
- `album`: Informações do album:
  - `album_name`: Nome do album.
  - `release_date`: Data de lançamento do album.
  - `cover_url`: URL com a imagem de capa do album.
  - `href`: Link para ouvir o album no Spotify.
- `artists`: Lista com informações do(s) artista(s):
  - `name`: Nome do artista.
  - `href`: Link para ouvir as músicas do artista no Spotify.
- `disk_number`: Numero da faixa no album.
- `duration`: Duração da música em minutos.
- `explicit`: Indicador de que a música possuí conteúdo explicito.
- `popularity`: Popularidade da música.

#### Exemplo

Request:

```
POST /musics/v2/save
```

Body:

```json
{
	"name": "Linha do Equador",
	"artists": [
		"Djavan"
	]
}
```

Response:

```json
{
	"name": "Linha do Equador",
	"uri": "https://open.spotify.com/track/51K301CKuavVEP7A9vsUaz",
	"album": {
		"album_name": "Coisa de Acender",
		"release_date": "1992-03-25",
		"cover_url": "https://i.scdn.co/image/ab67616d00001e02cca4a9128ae58440c667876f",
		"href": "https://open.spotify.com/album/6RnT2W5jK3g7ETuQHv1U5F"
	},
	"artists": [
		{
			"name": "Djavan",
			"href": "https://open.spotify.com/artist/5rrmaoBXZ7Jcs4Qb77j0YA"
		}
	],
	"disk_number": 4,
	"duration": "04:33",
	"explicit": false,
	"popularity": 55
}
```

Headers:

```
Location: http://localhost:8080/musics/v2/save/load/e3b56bda-503b-4204-ba1c-34a5f1086d59?music_name=Linha+do+Equador
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 30 Sep 2023 18:40:53 GMT
```

```json
{
  "title": "string",
  "detail": "string",
  "instance": "string",
  "status": 0,
  "timestamp": "2023-09-30T18:45:06.586Z"
}
```

### `GET musics/v2/scan`

Faz uma consulta na tabela usando qualquer atributo da tabela.

#### Query Parameters

- `music_id`: Id da música.
- `music_name`: Nome da música.
- `music_uri`: URL no Spotify da música.
- `music_album`: Nome do album.
- `music_artists`: Nome do artista principal.
- `music_number`: Numero da musica no album.
- `music_duration`: Duração da música (em minutos).
- `music_is_explicit`: Indicativo de conteúdo explicito (”yes” ou “no”)
- `music_popularity`: Popularidade da musica

Obs:  todos os query parameters são opcionais, mas é preciso que tenha pelo menos um deles na requisição

#### Response

Será retornado uma lista de objetos com a seguinte estrutura:

- `name`: Nome da música.
- `uri`: Link para ouvir a música no Spotify.
- `disk_number`: Numero da faixa no album.
- `duration`: Duração da música em minutos.
- `explicit`: Indicador de que a música possuí conteúdo explicito.
- `popularity`: Popularidade da música.

#### Exemplo

Request:

```
GET /musics/v2/scan?music_id=e3b56bda-503b-4204-ba1c-34a5f1086d59&music_name=Linha%20do%20Equador&music_uri=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F51K301CKuavVEP7A9vsUaz&music_album=Coisa%20de%20Acender&music_artists=Djavan&music_number=4&music_duration=04%3A33&music_is_explicit=No&music_popularity=55
```

Response:

```json
[
	{
		"name": "Linha do Equador",
		"uri": "https://open.spotify.com/track/51K301CKuavVEP7A9vsUaz",
		"disk_number": 4,
		"duration": "04:33",
		"explicit": false,
		"popularity": 55
	}
]
```

Headers:

```
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 30 Sep 2023 19:06:31 GMT
```

### `GET musics/v2/query`

Faz uma consulta na tabela usando as chaves da tabela.

#### Query Parameters

- `music_id`: Id da música.
- `music_name`: Nome da música.

#### Response

Será retornado uma lista de objetos com a seguinte estrutura:

- `name`: Nome da música.
- `uri`: Link para ouvir a música no Spotify.
- `disk_number`: Numero da faixa no album.
- `duration`: Duração da música em minutos.
- `explicit`: Indicador de que a música possuí conteúdo explicito.
- `popularity`: Popularidade da música.

#### Exemplo

Request:

```
GET /musics/v2/query?music_id=e3b56bda-503b-4204-ba1c-34a5f1086d59&music_name=Linha%20do%20Equador
```

Response:

```json
[
	{
		"name": "Linha do Equador",
		"uri": "https://open.spotify.com/track/51K301CKuavVEP7A9vsUaz",
		"disk_number": 4,
		"duration": "04:33",
		"explicit": false,
		"popularity": 55
	}
]
```

Headers:

```
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 30 Sep 2023 19:10:08 GMT
```

### `GET musics/v2/load/{music_id}`

Faz uma consulta na tabela usando as chaves da tabela, retornando mais informações da música.

#### Path Parameters

- `music_id`: Id da música.

#### Query Parameters

- `music_name`: Nome da música.

#### Response

Será retornado um objeto com as seguintes informações:

- `name`: Nome da música.
- `uri`: Link para ouvir a música no Spotify.
- `album`: Informações do album:
  - `album_name`: Nome do album.
  - `release_date`: Data de lançamento do album.
  - `cover_url`: URL com a imagem de capa do album.
  - `href`: Link para ouvir o album no Spotify.
- `artists`: Lista com informações do(s) artista(s):
  - `name`: Nome do artista.
  - `href`: Link para ouvir as músicas do artista no Spotify.
- `disk_number`: Numero da faixa no album.
- `duration`: Duração da música em minutos.
- `explicit`: Indicador de que a música possuí conteúdo explicito.
- `popularity`: Popularidade da música.

#### Exemplo

Request:

```
GET /musics/v2/load/e3b56bda-503b-4204-ba1c-34a5f1086d59?music_name=Linha%20do%20Equador
```

Response:

```json
{
	"name": "Linha do Equador",
	"uri": "https://open.spotify.com/track/51K301CKuavVEP7A9vsUaz",
	"album": {
		"album_name": "Coisa de Acender",
		"release_date": "1992-03-25",
		"cover_url": "https://i.scdn.co/image/ab67616d00001e02cca4a9128ae58440c667876f",
		"href": "https://open.spotify.com/album/6RnT2W5jK3g7ETuQHv1U5F"
	},
	"artists": [
		{
			"name": "Djavan",
			"href": "https://open.spotify.com/artist/5rrmaoBXZ7Jcs4Qb77j0YA"
		}
	],
	"disk_number": 4,
	"duration": "04:33",
	"explicit": false,
	"popularity": 55
}
```

Headers:

```
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 30 Sep 2023 19:15:57 GMT
```

### Erros

Em casos de erro, a API retornará a seguinte resposta:

- `title`: Tipo do erro
- `detail`: Descrição do erro ocorrido
- `instance`: Endpoint que ocorreu o erro
- `status`: Status code do erro
- `timestamp`: Data e hora em que ocorreu o erro

#### Exemplo

Response:

```json
{
  "title": "string",
  "detail": "string",
  "instance": "string",
  "status": 0,
  "timestamp": "2023-09-30T19:06:48.121Z"
}
```

### Exemplo

Response:

```json
{
	"title": "Bad Request",
	"detail": "This song has already been saved",
	"instance": "/musics/v2/save",
	"status": 400,
	"timestamp": "2023-09-30T16:20:15.588018931-03:00"
}
```

Headers:

```
Content-Type: application/problem+json
Transfer-Encoding: chunked
Date: Sat, 30 Sep 2023 19:20:15 GMT
Connection: close
```

## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente.

- `SERVER_PORT`: (Opcional) Porta em que a aplicação irá rodar, por padrão está como **8080**.
- `ACTIVE_PROFILE`: (Opcional) Ambiente em que aplicação está rodando,  por padrão está como **ec2**, mas aceita também o valor **local**.
- `AWS_SIGNINGREGION`:  (Obrigatório) Região da AWS em que a aplicação estará.
- `AWS_ACCESSKEY`: (Opcional) Access Key do usuário AWS, só é necessário adionar caso esteja rodando local.
- `AWS_SECRETKEY`: (Opcional) Secret Key do usuário AWS, só é necessário adionar caso esteja rodando local.

## Rodando localmente

Gerar um ClientId e ClientSecret para poder acessar a API do Spotify, veja como criar [aqui](https://developer.spotify.com/documentation/web-api/tutorials/getting-started). Depois de obter as credenciais, crie no diretório `infra` um arquivo chamado `spotify.tfvars` e salve-as conforme o exemplo abaixo:

```terraform
spotify_client = "seu client id"
spotify_secret = "seu client secret"
```

Comentar todos os arquivos `.tf`, exceto os arquivos `dynamo.tf`, `provider.tf` e `secrets-manager.tf`.

Alterar os valores no `application.yml, ou declarar os valores das variáveis de ambiente para: 
- `ACTIVE_PROFILE` = local
- `AWS_SIGNINGREGION` = sua região aws
- `AWS_ACCESSKEY` = sua access key
- `AWS_SECRETKEY` = sua secret key

Depois disso a applicação estará pronta para uso local. Use a collection para testar as requisições.

## Rodando na AWS

Gerar uma chave ssh chamada `terraform-key-ec2.pub` e salva em `~/.ssh`, será necessária para acessar a instância do EC2.

Gerar um ClientId e ClientSecret para poder acessar a API do Spotify, veja como criar [aqui](https://developer.spotify.com/documentation/web-api/tutorials/getting-started). Depois de obter as credenciais, crie no diretório `infra` um arquivo chamado `spotify.tfvars` e salve-as conforme o exemplo abaixo:

```terraform
spotify_client = "seu client id"
spotify_secret = "seu client secret"
```

Rodar o comando `terraform apply` dentro do diretório `infra` do projeto para subir os recursos na AWS. Obs, tenha os arquivos `config` e `credentials` no diretório `~/.aws`. Após concluido o deploy, pegue o endereço público da instância e altere a collection para fazer as requisições.

## Rodando os testes

Para rodar os testes, rode o seguinte comando

```shell
  mvn tests
```
## Collection para teste

[![Run in Insomnia}](https://insomnia.rest/images/run.svg)](https://insomnia.rest/run/?label=Music%20API%20V2&uri=https%3A%2F%2Fraw.githubusercontent.com%2Fgasfgrv%2Fmusics-api-v2%2Fmaster%2Fcollection.yaml)

## Tecnologias utilizadas
 
- Terraform para IaC
- AWS como provedor de cloud, além disso foram usandos os seguintes serviços:
  - DynamoDB
  - Secrets Manager
  - VPC
  - EC2
  - IAM
- Maven como ferramenta de build
- Kotlin como linguagem de programação
- Spring Boot como framework

**Dependências do projeto**

- json-path
- springdoc-openapi-starter-webmvc-ui
- dynamodb
- dynamodb-enhanced
- secretsmanager
- spring-boot-starter-validation
- spring-boot-starter-web
- spring-cloud-starter-openfeign
- jackson-module-kotlin
- kotlin-reflect
- kotlin-stdlib
- slf4j-api
- logback-classic
- logback-core
- logstash-logback-encoder
- spring-boot-starter-test
- spring-boot-testcontainers
- junit-jupiter
- localstack
- rest-assured
- json-schema-validator

## Autores

- [@gasfgrv](https://www.github.com/gasfgrv)
