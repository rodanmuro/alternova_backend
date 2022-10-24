# Prueba Alternova Backend

Back-end creado en Spring Boot, que consulta la api https://api.chucknorris.io/jokes/random, para devólverla al Front-End

Este Back-end usa como Database la Tier Free de Supabase, en dónde se tienen dos modelos:

Usuario con atributos id, usuario, password
Joke con atributos id, texto, usuario

Esta API, asociada con el repositorio alternova_frontend, está protegida con un Login de usuario y contraseña, que devuelve un JWT
