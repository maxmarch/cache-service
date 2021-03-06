package com.mpoznyak.cache.controller;

import com.mpoznyak.cache.dto.ItemDto;
import com.mpoznyak.cache.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by mpoznyak on 04.04.2019
 */

@RestController
public class CacheController extends BaseController {

    private GenericService cacheService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

    @GetMapping("/item/{id}")
    public ResponseEntity<ItemDto> findItemById(@PathVariable("id") Long id) {
        LOGGER.info("CACHE TOUCHED!");
        ItemDto item = cacheService.findById(id);
        if (item.getId() == null) {
            return new ResponseEntity<>(item, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/item/")
    public ResponseEntity<List<ItemDto>> findAllItems() {
        List<ItemDto> itemsList = cacheService.findAll();
        return new ResponseEntity<>(itemsList, HttpStatus.OK);
    }

    @PostMapping("/item")
    public ResponseEntity<Void> addItem(@RequestBody ItemDto itemDto, UriComponentsBuilder builder) {
        ItemDto savedItemDto = cacheService.add(itemDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/item/{id}").buildAndExpand(savedItemDto.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/item")
    public ResponseEntity<ItemDto> updateItem(@RequestBody ItemDto itemDto) {
        cacheService.update(itemDto);
        return new ResponseEntity<>(itemDto, HttpStatus.OK);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        cacheService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @Autowired
    public void setCacheService(GenericService cacheService) {
        this.cacheService = cacheService;
    }

}
