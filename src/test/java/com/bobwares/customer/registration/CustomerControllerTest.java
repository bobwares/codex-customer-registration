package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureTestDatabase
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void healthEndpointReturnsStatusUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void customerLifecycle() throws Exception {
        String payload = objectMapper.writeValueAsString(new java.util.HashMap<>() {{
            put("firstName", "Jane");
            put("middleName", "Q");
            put("lastName", "Doe");
            put("emails", List.of("jane.doe@example.com"));
            put("phoneNumbers", List.of(new java.util.HashMap<>() {{
                put("type", "mobile");
                put("number", "+1234567890");
            }}));
            put("address", new java.util.HashMap<>() {{
                put("line1", "123 Main St");
                put("line2", "Apt 4");
                put("city", "Springfield");
                put("state", "IL");
                put("postalCode", "62704");
                put("country", "US");
            }});
            put("privacySettings", new java.util.HashMap<>() {{
                put("marketingEmailsEnabled", true);
                put("twoFactorEnabled", true);
            }});
        }});

        MvcResult createResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        JsonNode createdNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
        String id = createdNode.get("id").asText();

        mockMvc.perform(get("/api/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));

        String updatePayload = payload.replace("Jane", "Janet");

        mockMvc.perform(put("/api/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Janet"));

        MvcResult listResult = mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode listNode = objectMapper.readTree(listResult.getResponse().getContentAsString());
        assertThat(listNode.isArray()).isTrue();
        assertThat(listNode).hasSize(1);

        mockMvc.perform(delete("/api/customers/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/" + id))
                .andExpect(status().isNotFound());
    }
}
