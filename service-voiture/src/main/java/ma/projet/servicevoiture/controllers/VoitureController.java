package ma.projet.servicevoiture.controllers;

import ma.projet.servicevoiture.entities.Client;
import ma.projet.servicevoiture.entities.Voiture;
import ma.projet.servicevoiture.repositories.VoitureRepository;
import ma.projet.servicevoiture.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired(required = false)
    private ClientService clientService;

    @GetMapping(value = "/voitures", produces = {"application/json"})
    public ResponseEntity<List<Voiture>> findAll() {
        List<Voiture> voitures = voitureRepository.findAll();
        if (clientService != null) {
            for (Voiture v : voitures) {
                if (v.getClientId() != null) {
                    try {
                        v.setClient(clientService.clientById(v.getClientId()));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return ResponseEntity.ok(voitures);
    }

    @GetMapping("/voitures/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return voitureRepository.findById(id)
                .map(voiture -> {
                    if (clientService != null && voiture.getClientId() != null) {
                        try {
                            voiture.setClient(clientService.clientById(voiture.getClientId()));
                        } catch (Exception ignored) {
                        }
                    }
                    return ResponseEntity.ok((Object) voiture);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Voiture not found with ID: " + id));
    }

    @GetMapping("/voitures/client/{id}")
    public ResponseEntity<List<Voiture>> findByClient(@PathVariable Long id) {
        List<Voiture> voitures = voitureRepository.findByClientId(id);
        if (clientService != null) {
            Client client = null;
            try {
                client = clientService.clientById(id);
            } catch (Exception ignored) {
            }
            if (client != null) {
                for (Voiture v : voitures) {
                    v.setClient(client);
                }
            }
        }
        return ResponseEntity.ok(voitures);
    }

    @PostMapping("/voitures/{clientId}")
    public ResponseEntity<Object> save(@PathVariable Long clientId, @RequestBody Voiture voiture) {
        voiture.setClientId(clientId);
        if (clientService != null) {
            try {
                Client client = clientService.clientById(clientId);
                if (client != null) {
                    voiture.setClient(client);
                }
            } catch (Exception ignored) {
            }
        }
        Voiture saved = voitureRepository.save(voiture);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/voitures/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Voiture updatedVoiture) {
        return voitureRepository.findById(id)
                .map(existing -> {
                    if (updatedVoiture.getMatricule() != null && !updatedVoiture.getMatricule().isEmpty()) {
                        existing.setMatricule(updatedVoiture.getMatricule());
                    }
                    if (updatedVoiture.getMarque() != null && !updatedVoiture.getMarque().isEmpty()) {
                        existing.setMarque(updatedVoiture.getMarque());
                    }
                    if (updatedVoiture.getModel() != null && !updatedVoiture.getModel().isEmpty()) {
                        existing.setModel(updatedVoiture.getModel());
                    }
                    if (updatedVoiture.getClientId() != null) {
                        existing.setClientId(updatedVoiture.getClientId());
                    }
                    Voiture saved = voitureRepository.save(existing);
                    return ResponseEntity.ok((Object) saved);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Voiture not found with ID: " + id));
    }

    @DeleteMapping("/voitures/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (voitureRepository.existsById(id)) {
            voitureRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
