package com.gihub.pakisan.pubsub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TestHelpers {

    @Autowired
    private ObjectMapper objectMapper;

    public <Type> Type load(String resourcePath, TypeReference<Type> typeReference) {
        try (InputStream resource = TestHelpers.class.getResourceAsStream(resourcePath)) {
            return objectMapper.readValue(resource, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String resourcePath) {
        try (InputStream resource = TestHelpers.class.getResourceAsStream(resourcePath)) {
            return new String(resource.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
