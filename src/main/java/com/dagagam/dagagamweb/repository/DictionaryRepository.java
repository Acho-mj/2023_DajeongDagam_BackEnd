package com.dagagam.dagagamweb.repository;

import com.dagagam.dagagamweb.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    // 사전 등록 시 단어 중복 확인
    boolean existsByWord(String word);

}
