
# 1. Vamos criar o DiscoryServer (Eureka).
> Verificar as versões de acordo com o curso se tiver problemas.

> O eureka server é quem vai unir as instâncias do microserviços, é como se elas tivessem dentro dele.

> Para subir esse querido é necessário somente definir a porta e os parametros abaixo:

```bash

eureka:
  client:
    register-with-eureka: false # Está associado ao microserviço ser registrar nele mesmo.
    fetch-registry: false # Está associado a criar uma lista de microserviços disponíveis.

```

# 2. Implementar o microservice de clientes.

definir no application.yml

```bash

spring:
  application:
    name: msclientes
  cloud:
    config:
      enabled: false
    
server:
  port: 0 # Isso faz com que o spring suba em uma porta aleatória disponível.
    
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/ # Define que é esse o endereço do Server Eureka que esse microservico deve se registrar.
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance-id:${random.value}} # Cria para essa instância do microservico um id específico e aleatório. A fim de permitir que sejam "subidas" várias instâncias do mesmo microservico.

    
```

Após isso forma implementadas as camadas do microservico como Resource, Service, Repository, Entity e DTO. Com spring JPA.

# 3. Implementar o microservice de Gateway.

Esse microservice permite que existam várias instâncias de um microservico, ele consome a lista de microservice que estão registrados no Eureka Server. Sendo assim ele cria um gateway e loadbalancer baseado no nome dos microservicos.


definir no application.yml

```bash

spring:
  application:
    name: mscloudgateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true # Ativa alguma funcionalidade relacionada a descobrir os microservicos
          lower-case-service-id: true

server:
  port: 8080

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/  # Define o endereco do servidor Eureka.
    
```

Após isso é necessário implementar na classe application principal do Spring. 

```java

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route(r -> r.path("/clientes/**").uri("lb://msclientes"))
					.route(r -> r.path("/cartoes/**").uri("lb://mscartoes"))
				.build();
	}
	

```
 
O trecho de código acima define que todas as chamadas para o path "/clientes/**" será redirecionadas para as intância do microservico "msclientes". Sendo assim caso existam várias instâncias ele irá distribuir caso não tenha não irá, apenas mandará para a única existente. Mesma coisa para o Microserviço de cartões.