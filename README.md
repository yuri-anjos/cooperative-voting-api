# cooperative-voting-api
Desafio técnico para vaga Java PLENO da SOFTDESIGN



## REQUISITOS

- Java 21+
- Maven 3.9+
- Spring Boot 3.5+
- PostgreSQL 16+
- Docker Desktop (necessário para execução dos testes de integração com Testcontainers)


## BANCO DE DADOS

A aplicação utiliza PostgreSQL como banco de dados.

Existem duas opções para executar o banco:

### Opção 1 - PostgreSQL via Docker Compose (recomendado)

O projeto possui um docker-compose.yml configurado para subir uma instância do PostgreSQL.

Para iniciar o banco: `docker compose up -d`

Configuração utilizada:
URL: `jdbc:postgresql://localhost:5432/cooperative_voting_db`  
Usuário: `admin`  
Senha: `admin`

Para parar o banco: `docker compose down`



### Opção 2 - PostgreSQL local

Também é possível utilizar uma instalação local do PostgreSQL.

Configure a aplicação com:

URL: `jdbc:postgresql://localhost:5432/cooperative_voting_db`  
Usuário: `admin`  
Senha: `admin`



## EXECUÇÃO DA APLICAÇÃO

Após configurar o banco de dados, execute: `mvn spring-boot:run`

A aplicação estará disponível em: http://localhost:8080



### Testes de integração

Os testes de integração utilizam Testcontainers para criar uma instância temporária do PostgreSQL.

Não é necessário iniciar o banco manualmente para executar os testes.



## DECISÃO NA CONSULTA DO RESULTADO DA VOTAÇÃO

Durante o desenvolvimento, considerei duas abordagens para a consulta do resultado enquanto a sessão de votação ainda está em andamento:

- Retornar a quantidade parcial de votos (SIM e NÃO), sem informar o resultado final da votação.
- Bloquear a consulta do resultado até o encerramento da sessão, retornando um erro de regra de negócio.

Optei pela segunda abordagem. Entendi que ocultar a contagem em tempo real dos votos evita que o andamento da votação influencie a decisão dos associados que ainda não votaram.



## POR QUE TODAS AS ROTAS ESTÃO EM `/pautas`
Os endpoints foram centralizados em Pauta, já que todas as funcionalidades do sistema partem de uma pauta cadastrada. Para o escopo deste desafio, essa organização deixa a API mais simples e intuitiva, evitando uma divisão desnecessária em vários controllers.



## POR QUE NÃO CRIEI UMA ENTIDADE ASSOCIADO
A entidade Associado não foi criada porque está fora do escopo do desafio. Como não existe nenhuma funcionalidade de cadastro ou manutenção de associados, foram armazenados apenas os dados necessários (associadoId e associadoCpf) para validar o voto e atender às regras de negócio.



## EXPLICANDO AS CHAVES ÚNICAS E INDICES
### UNIQUE `sessao(pauta_id)`
Garante que cada pauta possua apenas uma sessão de votação, além de otimizar as consultas por `pauta_id`.

### UNIQUE `voto(sessao_id, associado_id)`
Garante que um associado possa votar apenas uma vez por pauta/sessão, protegendo essa regra diretamente no banco de dados e também melhorando a consulta de verificação de voto duplicado.

### INDEX `voto(sessao_id, voto)`
Índice criado para otimizar a contabilização dos votos (`SIM` e `NAO`) durante a consulta do resultado da votação.



## VERSIONAMENTO DA API
A API utiliza versionamento através da URL.

Novas versões serão criadas apenas quando houver mudanças incompatíveis com consumidores existentes.

Exemplo:
- versão 1: `/api/v1/pautas`
- versão 2: `/api/v2/pautas`


## SWAGGER - DOCUMENTAÇÃO

Após iniciar a aplicação, acesse a documentação da API:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)



