package com.dagagam.dagagamweb.controller;


import com.dagagam.dagagamweb.dto.DictRequestDto;
import com.dagagam.dagagamweb.dto.DictionaryDto;
import com.dagagam.dagagamweb.entity.Member;
import com.dagagam.dagagamweb.service.CustomUserDetail;
import com.dagagam.dagagamweb.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PostMapping("/new")
    public ResponseEntity<?> addDictionary(
            @RequestBody DictRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetail userDetails
    ) {
        Member member = userDetails.getMember();
        requestDto.setUserId(member.getId());
        try {
            dictionaryService.addDictionary(requestDto, member);
            return new ResponseEntity<>("사전 등록 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 사전 전체 조회
    @GetMapping("/all")
    public List<DictionaryDto> getAllDictionaries() {
        return dictionaryService.getAllDictionaries();
    }

    // 사전 상세 조회
    @GetMapping("/{dictionaryId}")
    public DictionaryDto getDictionaryDetail(@PathVariable Long dictionaryId) {
        return dictionaryService.getDictionaryDetail(dictionaryId);
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
    @GetMapping("/part")
    public ResponseEntity<List<DictionaryDto>> getParticipatedDictionaries(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<DictionaryDto> dictionaries = dictionaryService.getUserDictionaries(userDetails);
        return new ResponseEntity<>(dictionaries, HttpStatus.OK);
    }
    
    // 사전 수정
    @PutMapping("/edit/{dictionaryId}")
    public ResponseEntity<String> updateDictionary(
            @PathVariable Long dictionaryId,
            @RequestBody DictRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            dictionaryService.updateDictionary(dictionaryId, requestDto, userDetails);
            return ResponseEntity.ok("사전 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사전 수정 실패: " + e.getMessage());
        }
    }

    // 사전 삭제
    @DeleteMapping("/{dictionaryId}")
    public ResponseEntity<String> deleteDictionary(
            @PathVariable Long dictionaryId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            dictionaryService.deleteDictionary(dictionaryId, userDetails);
            return new ResponseEntity<>("사전 삭제 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
