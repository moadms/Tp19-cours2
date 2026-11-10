package ma.projet.servicevoiture;

import ma.projet.servicevoiture.entities.Client;
import ma.projet.servicevoiture.entities.Voiture;
import ma.projet.servicevoiture.repositories.VoitureRepository;
import ma.projet.servicevoiture.services.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class VoitureApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoitureApplication.class, args);
    }

    @Bean
    CommandLineRunner initialiserBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
        return args -> {
            try {
                Client c1 = clientService.clientById(1L);
                Client c2 = clientService.clientById(2L);

                System.out.println("=== INITIALISATION VOITURES DANS H2 ===");
                if (c1 != null) {
                    System.out.println("Client 1 récupéré via OpenFeign : " + c1.getNom() + ", " + c1.getAge() + " ans");
                }
                if (c2 != null) {
                    System.out.println("Client 2 récupéré via OpenFeign : " + c2.getNom() + ", " + c2.getAge() + " ans");
                }
            } catch (Exception e) {
                System.out.println("Note: SERVICE-CLIENT n'était pas joignable au démarrage pour peupler le cache: " + e.getMessage());
            }

            voitureRepository.save(new Voiture(null, "Toyota", "A 25 333", "Corolla", 1L, null));
            voitureRepository.save(new Voiture(null, "Renault", "B 6 3456", "Megane", 1L, null));
            voitureRepository.save(new Voiture(null, "Peugeot", "A 55 4444", "301", 2L, null));

            System.out.println("=== 3 VOITURES ENREGISTREES AVEC SUCCES ===");
            voitureRepository.findAll().forEach(v -> {
                System.out.println("Voiture: " + v.getMarque() + " " + v.getModel() + " | Matricule: " + v.getMatricule() + " | ClientId: " + v.getClientId());
            });
        };
    }
}
