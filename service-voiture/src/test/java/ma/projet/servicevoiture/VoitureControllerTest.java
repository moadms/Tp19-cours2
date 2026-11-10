package ma.projet.servicevoiture;

import ma.projet.servicevoiture.entities.Client;
import ma.projet.servicevoiture.entities.Voiture;
import ma.projet.servicevoiture.repositories.VoitureRepository;
import ma.projet.servicevoiture.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoitureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VoitureRepository voitureRepository;

    @MockBean
    private ClientService clientService;

    @Test
    void testGetVoitures() throws Exception {
        given(clientService.clientById(1L)).willReturn(new Client(1L, "Amine Alami", 24f));

        mockMvc.perform(get("/voitures")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetVoitureById() throws Exception {
        Voiture saved = voitureRepository.save(new Voiture(null, "Audi", "C 77 999", "A4", 1L, null));
        given(clientService.clientById(1L)).willReturn(new Client(1L, "Amine Alami", 24f));

        mockMvc.perform(get("/voitures/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marque").value("Audi"))
                .andExpect(jsonPath("$.model").value("A4"));
    }

    @Test
    void testSaveVoiture() throws Exception {
        String json = "{\"marque\":\"BMW\",\"matricule\":\"D 88 1234\",\"model\":\"Serie 3\"}";
        given(clientService.clientById(2L)).willReturn(new Client(2L, "Salma Bennani", 28f));

        mockMvc.perform(post("/voitures/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marque").value("BMW"));
    }
}
