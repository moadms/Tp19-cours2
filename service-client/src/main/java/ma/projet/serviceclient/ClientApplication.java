package ma.projet.serviceclient;

import ma.projet.serviceclient.entities.Client;
import ma.projet.serviceclient.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    CommandLineRunner initialiserBaseH2(ClientRepository clientRepository) {
        return args -> {
            clientRepository.save(new Client(null, "Amine Alami", 24f));
            clientRepository.save(new Client(null, "Salma Bennani", 28f));
            clientRepository.save(new Client(null, "Karim Tazi", 35f));

            System.out.println("=== CLIENTS INITIALISES DANS H2 ===");
            clientRepository.findAll().forEach(c -> {
                System.out.println("Client ID: " + c.getId() + " | Nom: " + c.getNom() + " | Age: " + c.getAge());
            });
        };
    }
}
