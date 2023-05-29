package com.study.ex.repository;

import com.study.ex.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo,Long> {

    //Query 방식 사용전 - 메소드 이름이 상당히 길어짐
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
    //ㅡmno 가 10보다 작은 데이터를 삭제
    void deleteMemoByMnoLessThan(Long num);
}
