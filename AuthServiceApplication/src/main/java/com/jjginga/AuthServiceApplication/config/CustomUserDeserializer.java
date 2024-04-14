package com.jjginga.AuthServiceApplication.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Collections;

public class CustomUserDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String username = node.get("username").asText();
        String password = node.get("password").asText();

        if (node.has("email")) {
            String email = node.get("email").asText();
            String firstName = node.get("firstName").asText();
            String lastName = node.get("lastName").asText();
        }

        return new User(username, password, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
