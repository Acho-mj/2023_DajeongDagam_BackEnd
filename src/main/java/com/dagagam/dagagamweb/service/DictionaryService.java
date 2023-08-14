package com.dagagam.dagagamweb.service;

import com.dagagam.dagagamweb.constant.DictionaryState;
import com.dagagam.dagagamweb.dto.DictRequestDto;
import com.dagagam.dagagamweb.dto.DictionaryDto;
import com.dagagam.dagagamweb.entity.Dictionary;
import com.dagagam.dagagamweb.entity.Member;
import com.dagagam.dagagamweb.repository.DictionaryRepository;
import com.dagagam.dagagamweb.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public DictionaryService(DictionaryRepository dictionaryRepository,MemberRepository memberRepository) {
        this.dictionaryRepository = dictionaryRepository;
        this.memberRepository = memberRepository;
    }

    // 사전 등록
    public void addDictionary(Long userId, DictRequestDto requestDto) throws Exception {
        // 사용자 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new Exception("사용자가 존재하지 않습니다.");
        }
        Member participant = optionalMember.get();

        // 중복된 단어가 있는지 확인
        if (dictionaryRepository.existsByWord(requestDto.getWord())) {
            throw new Exception("이미 사전에 등록된 단어입니다.");
        }

        // Dictionary 객체 생성 및 저장
        Dictionary dictionary = new Dictionary();
        dictionary.setWord(requestDto.getWord());
        dictionary.setDescription(requestDto.getDescription());
        dictionary.setParticipant(participant);
        dictionary.setLikes(0); // 초기 좋아요 수
        dictionary.setDictionaryState(DictionaryState.ACCESSIBLE); // 초기 상태

        dictionaryRepository.save(dictionary);
    }

    // 단어 검색 사전 조회
    public List<DictionaryDto> searchDictionaries(String keyword) {
        List<Dictionary> dictionaries = dictionaryRepository.findByWordContainingIgnoreCase(keyword);

        List<DictionaryDto> dictionaryDtos = new ArrayList<>();
        for (Dictionary dictionary : dictionaries) {
            DictionaryDto dictionaryDto = new DictionaryDto();
            dictionaryDto.setId(dictionary.getId());
            dictionaryDto.setWord(dictionary.getWord());
            dictionaryDto.setDescription(dictionary.getDescription());
            dictionaryDto.setLikes(dictionary.getLikes());
            dictionaryDto.setDate(dictionary.getDate());
            dictionaryDto.setParticipantName(dictionary.getParticipant().getName());

            dictionaryDtos.add(dictionaryDto);
        }

        return dictionaryDtos;
    }


    // 사전 삭제
    @Transactional
    public void deleteDictionary(Long userId, Long dictionaryId) throws Exception {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }

        Member member = optionalMember.get();
        Dictionary dictionary = dictionaryRepository.findById(dictionaryId)
                .orElseThrow(() -> new Exception("사전을 찾을 수 없습니다."));

        if (!dictionary.getParticipant().equals(member)) {
            throw new Exception("사전을 삭제할 권한이 없습니다.");
        }

        dictionaryRepository.delete(dictionary);
    }
}
