# Courses_Api_Front

Resumo detalhado da aplicação (análise feita assistindo videoaulas da RocketSeat).

## Visão geral
Aplicação web front-end desenvolvida em Spring Boot + Thymeleaf que consome uma API externa (configurada via `api.url`) para listar cursos e criar usuários. Objetivo: interface server-side renderizada que mostra cursos, permite busca e interage com endpoints REST do backend.

## Tecnologias
- Java 21
- Spring Boot (Web, Thymeleaf, Security)
- RestTemplate (consumo de API externa)
- Lombok (DTOs)
- spring-dotenv (variáveis via .env)
- Maven

## Funcionalidades principais
- Página inicial com resumo de cursos (total, ativos, categorias).
- Busca de cursos (endpoint `/courses/v2` — pesquisa por nome).
- Criação de usuários via POST para o endpoint remoto `/users`.
- Templates Thymeleaf para renderização no servidor.

## Estrutura de pastas (principal)
- src/main/java/br/com/jhonecmd/courses_api_front
  - CoursesApiFrontApplication.java         -> Classe principal Spring Boot
  - modules/
    - controllers/                         -> Controllers (ex.: HomeController)
    - courses/
      - dto/                                -> DTOs (CourseResponseDTO)
      - services/                           -> Serviços que consomem API (FetchAllCoursesService)
    - users/
      - dto/                                -> DTOs para criação de usuário
      - services/                           -> Serviço CreateUserService para postar /users
    - categories/                           -> Estrutura para categorias (controllers/dto/services)
  - utils/                                  -> Utilitários (se houver)
- src/main/resources/
  - templates/                              -> Views Thymeleaf (módulos/home/...)
  - application.properties                  -> Configurações (usa `api.url`)

## Rotas observadas e permissões sugeridas
Rotas observadas diretamente no código:
- GET `/` — HomeController.home — página pública que lista cursos
- GET `/courses/v2` — HomeController.courses — página de listagem/ busca

Chamadas a API externa (via serviços):
- GET `{api.url}/courses/v2` — usado por FetchAllCoursesService
- POST `{api.url}/users` — usado por CreateUserService para criar usuários

Permissões / Segurança:
- A dependência `spring-boot-starter-security` está presente no projeto, mas não foi encontrada configuração de segurança customizada no código analisado. Com isso:
  - Recomenda-se criar uma classe de configuração de segurança (SecurityConfig) para explicitar as rotas públicas e protegidas.
  - Sugestão mínima: permitir acesso público a `/`, `/courses/**`, `/css/**`, `/js/**` e proteger rotas administrativas (ex.: `/admin/**`).

Exemplo de política recomendada (resumo):
- Público: `/`, `/courses/v2`, recursos estáticos
- Autenticado: páginas de administração/edição
- Papel `ROLE_ADMIN`: acesso a rotas de gerenciamento

## Como executar
1. Criar arquivo `.env` com a variável: `API_URL=http://seu-backend:porta` (ou configurar `application.properties` com `api.url`).
2. Construir: `./mvnw clean package` ou `mvnw.cmd` no Windows
3. Executar: `./mvnw spring-boot:run`

## Observações técnicas
- FetchAllCoursesService e CreateUserService utilizam RestTemplate para comunicação com a API externa.
- CourseResponseDTO contém: id, name, description, categoryName, teacherName, active.
- Se Spring Security não for configurado, a aplicação pode cair no comportamento padrão (autenticação básica exigida). Ajustar SecurityConfig conforme necessário.

## Créditos
Análise e documentação geradas enquanto assistia às videoaulas da plataforma RocketSeat.

---
