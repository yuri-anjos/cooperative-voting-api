# cooperative-voting-api
Desafio técnico para vaga Java PLENO



## REQUISITOS

- Java 21+
- Maven 3.9+
- Spring Boot 3.5+
- Docker Desktop (necessário para banco, e testes de integração)



## DOCKER

O projeto possui um `docker-compose.yml` configurado para subir as dependências necessárias da aplicação:

- PostgreSQL 16 (persistência dos dados)
- Redis (cache)

Para iniciar as dependências: `docker compose up -d`

Para parar as dependências: `docker compose down`



## EXECUÇÃO DA APLICAÇÃO

Após configurar o banco de dados, execute: `mvn spring-boot:run`

A aplicação estará disponível em: http://localhost:8080



## REDIS CACHE

Foi utilizado Redis Cache para reduzir o número de consultas ao banco de dados em operações frequentes.

A sessão de votação é armazenada em cache e reutilizada durante o registro dos votos, evitando consultas repetidas para buscar a sessão. 

Como o endpoint de registro de votos tende a ser o mais utilizado em cenários com grande volume de acessos, essa estratégia reduz a carga sobre o banco de dados e melhora o tempo de resposta da aplicação.



## POR QUE NÃO CRIEI UMA ENTIDADE ASSOCIADO
A entidade Associado não foi criada porque está fora do escopo do desafio. Como não existe nenhuma funcionalidade de cadastro ou manutenção de associados, foram armazenados apenas os dados necessários (associadoId e associadoCpf) para validar o voto e atender às regras de negócio.



## EXPLICANDO AS CHAVES ÚNICAS E INDICES
### UNIQUE `sessao(pauta_id)`
Garante que cada pauta possua apenas uma sessão de votação, além de otimizar as consultas por `pauta_id`.

### UNIQUE `voto(sessao_id, associado_id)`
Garante que um associado possa votar apenas uma vez em uma sessão de votação, protegendo essa regra diretamente no banco de dados e também melhorando a consulta de verificação de voto duplicado.

### INDEX `voto(sessao_id, voto)`
Índice criado para otimizar a contabilização dos votos (`SIM` e `NAO`) durante a consulta do resultado da votação.



## VERSIONAMENTO DA API
A API utiliza versionamento através da URL.

Novas versões serão criadas apenas quando houver mudanças incompatíveis com consumidores existentes.

Exemplo:
- versão 1: `/api/v1/pautas`
- versão 2: `/api/v2/pautas`



## TESTES

Os testes de integração utilizam Testcontainers para criar uma instância temporária do PostgreSQL.

Não é necessário iniciar o banco manualmente para executar os testes.

Para executar:

`mvn test`

É necessário que o Docker Desktop esteja em execução.



## DOCUMENTAÇÃO DA API (SWAGGER)

Após iniciar a aplicação, acesse a documentação da API:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)



# ⚠️ Observação sobre a Bônus 1 integração externa

O serviço disponibilizado pelo desafio (`https://user-info.herokuapp.com/users/{cpf}`) encontra-se indisponível no momento da entrega.

A implementação foi iniciada e encontra-se disponível na branch `feat/integracao-sistema-cpf`, porém não foi incorporada à branch principal por não ser possível validar seu funcionamento com o serviço externo fora do ar.

```
GET https://user-info.herokuapp.com/users/{cpf}
```