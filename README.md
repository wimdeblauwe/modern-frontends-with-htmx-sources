# Modern frontends with htmx Sources

These are the sources for the book 'Modern frontends with htmx' which is available at https://www.wimdeblauwe.com/books/modern-frontends-with-htmx

Each directory matches with what the code is supposed to look like by the end of each chapter in the book.

The sources are for the Spring Boot 4 edition of the book. They use Java 25, Spring Boot 4 and htmx 4.

## Technologies

All examples use:

* [Spring Boot](https://spring.io/projects/spring-boot) 4 with Spring MVC
* [Thymeleaf](https://www.thymeleaf.org/) together with the [Thymeleaf Layout Dialect](https://ultraq.github.io/thymeleaf-layout-dialect/)
* [htmx](https://four.htmx.org/) 4
* [htmx-spring-boot](https://github.com/wimdeblauwe/htmx-spring-boot) for integrating htmx with Spring Boot and Thymeleaf
* [Tailwind CSS](https://tailwindcss.com/) 4
* [Vite](https://vite.dev/) with [vite-spring-boot](https://github.com/wimdeblauwe/vite-spring-boot) for the frontend build and live reload
* [WebJars](https://www.webjars.org/) for client-side libraries

Some chapters use additional libraries:

| Directory                   | Additional libraries                                                                                                                                             |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| chapter02 - getting started | [Alpine.js](https://alpinejs.dev/)                                                                                                                               |
| chapter05 - todomvc         | [Spring Data JPA](https://spring.io/projects/spring-data-jpa), [TodoMVC](https://todomvc.com/)                                                                    |
| chapter07 - error handling  | [Bootstrap](https://getbootstrap.com/)                                                                                                                           |
| chapter07b - inline editing | [Alpine.js](https://alpinejs.dev/), [SortableJS](https://sortablejs.github.io/Sortable/)                                                                         |
| chapter08 - security        | [Spring Security](https://spring.io/projects/spring-security)                                                                                                    |
| chapter09 - contacts        | [Shadleaf](https://shadleaf.dev/), [Datafaker](https://www.datafaker.net/)                                                                                       |
| chapter10 - web components  | [Web Awesome](https://webawesome.com/), [GitHub API for Java](https://github-api.kohsuke.org/), [commonmark-java](https://github.com/commonmark/commonmark-java) |
| chapter11 - sse             | [daisyUI](https://daisyui.com/)                                                                                                                                  |
| chapter11b - ws             | [daisyUI](https://daisyui.com/), [Spring WebSocket](https://docs.spring.io/spring-framework/reference/web/websocket.html)                                         |
