# **User Manager Spring**

### Sistema de Gestão de Usuários - Backend

O **User Manager Spring** é um sistema de backend para gerenciamento de usuários, desenvolvido com **Spring-boot**. Este projeto faz parte do meu portfólio como desenvolvedor e demonstra habilidades em desenvolvimento de sistemas, integração entre tecnologias e boas práticas de programação.

---

### **Funcionalidades**
- Autenticação de usuário via token JWT
- Cadastro de novos usuários  
- Listagem de usuários  
- Atualização de informações de usuários  
- Exclusão de usuários  
- Busca e filtros personalizados  
- Exportação de dados para **PDF** e **Excel** *(em desenvolvimento)*  

---

### **Tecnologias Utilizadas**
#### **Backend**
- Java 21
- Spring Boot 3.4.0
- Spring Data JPA
- PostgreSQL
- Maven

#### **Outras Ferramentas**
- Git & GitHub  
- IntelliJ IDEA / VSCode  
- Postman (para testes de API)  

---

### **Hospedagem**
O projeto está hospedado na **Render** e pode ser acessado pela interface swagger-ui :  
🔗 [Swagger - Servidor Hospedado](https://user-manager-spring.onrender.com/api/swagger-ui)  
Para testar a API em funcionamento juntamente com o Frontend pode acessado pelo link:
🔗 [User Manager Angular](https://user-manager-angular.vercel.app/auth/login) 
(Para testar, use os seguintes emails: admin@domain.com e a senha Admin#2025 para o perfil Admin ou user@domain.com e senha User#2025 para testar o User. O usuário de perfil Admin pode realizar todas ações no sistema inclusive excluir e editar, enquanto o usuário de perfil User só tem permisão para visualizar as informações.)

---

### **Como Executar o Projeto Localmente**

#### **Pré-requisitos**
- **Java 21+**  
- **PostgreSQL 16** (instalado e configurado)  
- **Maven** (configurado no PATH)  

#### **Passos para execução**

##### **1. Clonar o repositório**
```bash
git clone https://github.com/ricardosantanadev4/user-manager-spring.git
cd user-manager-spring
```
##### **3. Executar o projeto**
```bash
mvn spring-boot:run
```

---
### **Documentação da API (Swagger)**
A API possui uma documentação interativa via Swagger, onde você pode visualizar e testar os endpoints disponíveis.

📌 **Acesso à documentação:**  
🔗 [Swagger - Ambiente Local](http://localhost:8080/api/swagger-ui)  
🔗 [Swagger - Servidor Hospedado](https://user-manager-spring.onrender.com/api/swagger-ui)  

### **Contato**
Caso tenha alguma dúvida ou sugestão, fique à vontade para entrar em contato!
