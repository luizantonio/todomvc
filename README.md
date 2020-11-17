# todomvc

Aplicação para ESIG
------------------------------------------------------------

1) ITENS IMPLEMENTADOS:
------------------------------------------------------------

A - Cria uma aplicação java web utilizando JavaServer Faces (JSF)

B. Utilizar persistência em um banco de dados (qualquer banco dese$
 B.1. MySQL
C. Utilizar Hibernate e JPA

F. Utilizar Bootstrap 4

G. Utilizar PrimeFaces

2) RODAR A APLICAÇÃO LOCALMENTE
------------------------------------------------------------
1. Clone o projeto. Observe que existem arquivos de configurações.
2. Crie o banco de dados (código abaixo)
3. Crie o usuário (código abaixo)
4. Abra a IDE Eclipse
5. Atualize os pacotes MAVEN (Orientações abaixo)

3) ORIENTAÇÕES:
------------------------------------------------------------

------------------------------------------------------------
3.1) AMBIENTE DE DESENVOLVIMENTO:
------------------------------------------------------------


3.1.1) AMBIENTE:(Ubuntu)18.04.1
------------------------------------------------------------

Linux coral 5.4.0-53-generic #59~18.04.1-Ubuntu
SMP Wed Oct 21 12:14:56 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux

IDE: Eclipse IDE for Enterprise Java Developers.
------------------------------------------------------------

Version: 2019-06 (4.12.0)
Build id: 20190614-1200

SERVER: Apache TomCat v9.0 Server

JAVA: java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)

JDK: jdk version "1.8.0_191"


4) SQL:
------------------------------------------------------------

MySQL Workbench:
versão 6.3

Executar:
------------------------------------------------------------
-- Criar  o Banco

create database todo;

-- Em seguida executar
-- Criar usuário

CREATE USER 'todouser'@'localhost' identified by '1234';
grant
create, select, insert, execute, update, references
on todo.*
to 'todouser'@'localhost';


5)PASSOS PARA CRIAÇÃO DO PROJETO (UTILIZADOS)
------------------------------------------------------------

Crear Projeto em IDE Eclipse
New > Dinamic Web Project
Configure > Convert to Maven project
Configure > Convert to JPA Prject > Select JPA 2.1
Configure builpath > Select Project Facets > JavaServer Faces 2.0
Configure builpath > Selecione o JDK

Add Dependences no pom.xml

Maven> update project
------------------------------------------------------------

a) Criar class Modelo:
Todo.java
b) Criar class Persistence:
JPAUtil.java
c) Criar class Repository:
TodoRepository.java
d) Criar class Controller:
TodoController.java
criar views


6)RUN PROJETO (Executar o projeto com o IDE Eclipse)
------------------------------------------------------------

Clique com o "botão direito do mouse" sobre o projeto >
Run As > Run on Server > Selecione o Server > Finish


6.1)UTILIZAÇÃO:
------------------------------------------------------------

CADASTRO:
------------------------------------------------------------

Preencher o campo e clique em no botão "Salvar";

EDITAR:
------------------------------------------------------------

Clique no botão "Editar";
Preencher o campo e clique em no botão "Atualizar";

OU

Clique no botão "Voltar", para não atualizar;


EXCLUIR:
------------------------------------------------------------

Clique no botão "Excluir";


