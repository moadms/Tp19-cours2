package ma.projet.servicevoiture.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marque;
    private String matricule;
    private String model;
    private Long clientId;

    @Transient
    private Client client;

    public Voiture(String marque, String matricule, String model, Long clientId, Client client) {
        this.marque = marque;
        this.matricule = matricule;
        this.model = model;
        this.clientId = clientId;
        this.client = client;
    }
}
