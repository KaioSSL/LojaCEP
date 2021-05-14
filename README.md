# LojaCEP
Projeto Iniciante - API REST com Spring - JPA/Hibernate

Projeto visa implementar um CRUD inicial de uma API REST, aplicando regras de integridade entre os tratamentos de inserção e manipulação de dados.

API conta com o seguinte endereço para receber as requisições
/api/loja

Para cada tipo de requisição é realizado um tratamento do CRUD.
C(Create) - POST,
R(Read) - GET,
U(Update) - PUT,
D(Delete) - DELETE

Além do endereço padrão para tratamento do CRUD de faixas de CEP entre lojas, o endereço 
/api/loja/lojaMaisProxima/{cep} 

Recebe uma requisição do tipo Get, e retorna a loja responsável pela faixa de cep em que o cep inserido se encontra.

Para configurar as informações de banco e conexões
bin\src\main\resources\application.properties