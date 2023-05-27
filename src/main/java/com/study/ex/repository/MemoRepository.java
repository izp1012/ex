package com.study.ex.repository;

import com.study.ex.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo,Long> {
}
