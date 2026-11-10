package ma.projet.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * Configuration des routes statiques et logiques via Eureka (lb://SERVICE-NAME)
     */
    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("clients-service", r -> r.path("/clients/**")
                        .uri("lb://SERVICE-CLIENT"))
                .route("client-single", r -> r.path("/client/**")
                        .uri("lb://SERVICE-CLIENT"))
                .route("voitures-service", r -> r.path("/voitures/**")
                        .uri("lb://SERVICE-VOITURE"))
                .build();
    }

    /**
     * Découverte dynamique automatique par nom de service:
     * Exemple: http://localhost:8888/SERVICE-CLIENT/clients
     */
    @Bean
    DiscoveryClientRouteDefinitionLocator dynamicRoutes(
            ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }
}
