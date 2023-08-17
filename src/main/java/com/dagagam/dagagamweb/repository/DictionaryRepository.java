package com.dagagam.dagagamweb.repository;

import com.dagagam.dagagamweb.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    // 사전 등록 시 단어 중복 확인
    boolean existsByWord(String word);

    // 단어 검색 조회
    List<Dictionary> findByWordContainingIgnoreCase(String keyword);

    // 사용자가 참여한 사전 조회
    @Query("SELECT d FROM Dictionary d " +
            "LEFT JOIN FETCH d.creator c " +
            "LEFT JOIN FETCH d.participants p " +
            "WHERE c.id = :userId OR p.id = :userId")
    List<Dictionary> findUserDictionaries(@Param("userId") Long userId);
}
