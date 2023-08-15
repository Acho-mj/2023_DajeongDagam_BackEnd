package com.dagagam.dagagamweb.controller;


import com.dagagam.dagagamweb.dto.DictRequestDto;
import com.dagagam.dagagamweb.dto.DictionaryDto;
import com.dagagam.dagagamweb.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 단어 검색으로 사전 조회
    @GetMapping("/search")
    public ResponseEntity<List<DictionaryDto>> searchDictionaries(
            @RequestParam String keyword
    ) {
        List<DictionaryDto> dictionaries = dictionaryService.searchDictionaries(keyword);
        return new ResponseEntity<>(dictionaries, HttpStatus.OK);
    }

    // 사용자가 참여한 사전 조회
    @GetMapping("/{userId}/part")
    public ResponseEntity<List<DictionaryDto>> getParticipatedDictionaries(
            @PathVariable Long userId
    ) {
        List<DictionaryDto> dictionaries = dictionaryService.getUserDictionaries(userId);
        return new ResponseEntity<>(dictionaries, HttpStatus.OK);
    }
    
    // 사전 수정
    @PutMapping("/edit/{dictionaryId}")
    public ResponseEntity<DictionaryDto> updateDictionary(@PathVariable Long dictionaryId,
                                                          @RequestBody DictRequestDto requestDto) {
        try {
            DictionaryDto updatedDictionary = dictionaryService.updateDictionary(dictionaryId, requestDto);
            return ResponseEntity.ok(updatedDictionary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 사전 삭제
    @DeleteMapping("/{userId}/{dictionaryId}")
    public ResponseEntity<String> deleteDictionary(
            @PathVariable Long userId,
            @PathVariable Long dictionaryId
    ) {
        try {
            dictionaryService.deleteDictionary(userId, dictionaryId);
            return new ResponseEntity<>("사전 삭제 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
