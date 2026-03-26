package com.rickandmorty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rickandmorty.dto.PageResponseDTO;
import com.rickandmorty.service.PersonajesRickAndMortyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class PersonajesRickAndMortyServiceController {
	private final PersonajesRickAndMortyService service;
    
	@GetMapping
    public PageResponseDTO getCharacters(@RequestParam(defaultValue = "1") int page) {
        return service.getCharacters(page);
    }
}
