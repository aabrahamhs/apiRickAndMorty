package com.rickandmorty.service;

import com.rickandmorty.config.ResourceNotFoundException;
import com.rickandmorty.dto.CharacterDTO;
import com.rickandmorty.dto.PageResponseDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonajesRickAndMortyService {

    private final RestTemplate restTemplate;

    @Value("${rickandmorty.api.url}")
    private String baseUrl;

    @Cacheable(value = "characters", key = "#page")
    public PageResponseDTO getCharacters(int page) {

        String url = baseUrl + "?page=" + page;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> response = responseEntity.getBody();

        if (response == null) {
            throw new ResourceNotFoundException("No se obtuvo respuesta de la API externa");
        }

        Map<String, Object> info = (Map<String, Object>) response.get("info");
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        if (results == null || results.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron personajes en la página: " + page);
        }

        List<CharacterDTO> characters = results.stream()
                .map(c -> new CharacterDTO(
                        (String) c.get("name"),
                        (String) c.get("species"),
                        (String) c.get("image")
                ))
                .collect(Collectors.toList());

        int totalPages = (int) info.get("pages");
        int totalElements = (int) info.get("count");

        return new PageResponseDTO(page, totalPages, totalElements, characters);
    }
}