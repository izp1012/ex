package com.study.ex.repository;

import com.study.ex.entity.Memo;
import jakarta.transaction.Transactional;
import net.bytebuddy.description.NamedElement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.sql.SQLOutput;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        //DB에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        //findByID 한 순간 이미 SQL 처리 되었고 sout 작동

        System.out.println("=================================");

        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    //    select
    //    m1_0.mno,
    //    m1_0.memo_text
    //            from
    //    memo_inhyo m1_0
    //    where
    //    m1_0.mno=?
    //            =================================
    //    Memo(mno=100, memoText=Sample...100)
    @Transactional
    @Test
    public void testSelect2() {
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno); //getOne 의 메소드의 경우 transaction 어노테이션이 필요하며
        //리턴값은 해당 객체이지만, 실제 객체가 필요한 순간까지는 SQL 실행을 하지않는
        //기존 1번은 먼저 실행해서 값을 들고있고
        //2번의 경우에는 실제 객체를 사용하는 순간에 SQL 이 동작하는 모습.. (LAZY 로딩)

        System.out.println("=================================");

        System.out.println(memo);
//        =================================
//        Hibernate:
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        memo_inhyo m1_0
//        where
//        m1_0.mno=?
//        Memo(mno=100, memoText=Sample...100)
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("UPDATE TEXT").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 99L;
        memoRepository.deleteById(mno);
    }
}
