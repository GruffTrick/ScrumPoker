package io.github.grufftrick.ScrumPoker.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class GameControllerTest {

    private static String sessionId;
    private static String playerId;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Setup required before each test, if any
    }

    @Test
    @Order(1)
    void createSession_ShouldReturnSession() throws Exception {
        int maxPlayers = 5;

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/session/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(maxPlayers)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.maxPlayers").value(maxPlayers))
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        sessionId = jsonNode.get("id").asText();
    }

    @Test
    @Order(2)
    void addPlayer_ShouldReturnPlayer() throws Exception {
        String sessionId = GameControllerTest.sessionId;
        String playerName = "John Doe";

        MvcResult mockResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/session/" + sessionId + "/player/add")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(playerName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andReturn();
        Player player = objectMapper.readValue(mockResult.getResponse().getContentAsString(), Player.class);
        playerId = player.getId();
        assertThat(player.getName()).isEqualTo(playerName);
    }

    @Test
    @Order(3)
    void getSession_ShouldReturnSession() throws Exception {
        String sessionId = GameControllerTest.sessionId;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionId));
    }

    @Test
    @Order(4)
    void getAllSessions_ShouldReturnList_WhenSessionsExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/all"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void getPlayers_ShouldReturnPlayerList() throws Exception {
        String sessionId = GameControllerTest.sessionId;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + sessionId + "/player/all"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void updateSelection_ShouldReturnUpdatedPlayer() throws Exception {
        String sessionId = GameControllerTest.sessionId;
        String playerId = GameControllerTest.playerId;
        int selection = 3;

        MvcResult mockResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/session/" + sessionId + "/player/" + playerId +
                                        "/selection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selection)))
                .andExpect(status().isOk())
                .andReturn();
        String json = mockResult.getResponse().getContentAsString();
        Player player = objectMapper.readValue(json, new TypeReference<>() {});
        assertThat(player.getSelection()).isEqualTo(selection);
    }

    @Test
    @Order(7)
    void deleteSession_ShouldReturnDeletedSession() throws Exception {
        String sessionId = GameControllerTest.sessionId;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/delete/" + sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionId));
    }
}
