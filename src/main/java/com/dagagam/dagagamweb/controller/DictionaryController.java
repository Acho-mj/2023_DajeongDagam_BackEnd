package com.dagagam.dagagamweb.controller;


import com.dagagam.dagagamweb.dto.DictRequestDto;
import com.dagagam.dagagamweb.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dict")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    // 사전 등록
    @PostMapping("/{userId}/new")
    public ResponseEntity<String> addDictionary(
            @PathVariable Long userId,
            @RequestBody DictRequestDto requestDto
    ) {
        try {
            dictionaryService.addDictionary(userId, requestDto);
            return new ResponseEntity<>("사전 등록 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
