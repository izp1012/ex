package com.study.ex.repository;

import com.study.ex.entity.Memo;
import jakarta.transaction.Transactional;
import net.bytebuddy.description.NamedElement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.swing.text.html.Option;
import java.sql.SQLOutput;
import java.util.List;
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

    ////////////////////////////////////////////////////페이징 처리////////////////////////////////////////////////////
    @Test
    public void testPagingDefault(){
        //1페이지 10개
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
    }

    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("==========================================================");

        System.out.println("Total page : "+result.getTotalPages()); //총 몇 페이지

        System.out.println("Total Count : "+result.getTotalElements()); //전체 개수

        System.out.println("Total Number : "+result.getNumber());//현재 페이지 번호 0부터 시작

        System.out.println("Total Size : "+result.getSize());//페이지당 데이터 개수

        System.out.println("has next Page ? : "+result.hasNext());//다음 페이지 존재 여부

        System.out.println("first Page ? : "+result.isFirst());//시작 페이지(0) 여부

        System.out.println("==========================================================");
        for (Memo memo : result.getContent()){
            System.out.println(memo);
        }
    }
    //페이징에 정렬 조건 추가하기
    //Sort 라는 파라미터를 통해 정렬 처리 다중 필드 및 오름차순, 내림차순이 가능하다
    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sort3 = sort1.and(sort2);

        Pageable pageable1 = PageRequest.of(0,10, sort1);
        Pageable pageable2 = PageRequest.of(0,30, sort3);

        Page<Memo> result1 = memoRepository.findAll(pageable1);
        Page<Memo> result2 = memoRepository.findAll(pageable2);

            System.out.println("Result1 = ");
        result1.get().forEach(memo -> {
            System.out.println(memo);
        });

            System.out.println("Result2 = ");
        result2.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    //////////////////////////////////////////JPQL Query//////////////////////////////////////////
    //Spring Data JPA 에서 제공하는 방법들
    //쿼리 메서드  : 메서드의이름 자체가 쿼리의 구문으로 처리되는 기능  <<이거랑
    //@Query : SQL 과 유사하게 엔티티 클래스의 정보를 이용하여 쿼리를 작성하는 기능  <<이거 테스트
    //Querydsl 의 동작기능



    // 예제 1 : mno 가 70부터 80인 객체를 구하고 mno 의 역순으로 정렬하여라
    //(Query 메서드 적용 전)
    @Test
    public void testQueryMethod(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for(Memo memo : list){
            System.out.println(memo);
        }
    }


    @Test
    public void testQueryMethodWithPageble(){
        //Paging 처리 적용 10개를 출력할 거다 (PageRequest) mno 의 역순으로
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        //Mno 를 기준(10~50 객체) 으로 가져올건데 Paging 을 적용해서.
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        //-> 50번 ~40번까지 출력됨
        result.get().forEach(memo -> System.out.println(memo));

    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethod(){
        /*
        @Transactional
        Delete 의 경우애는 Commit, Transactional 어노테이션이 사용되어야 하는데
        deleteBy 인 경우에는 우선은 'select 문으로 해당 엔티티 객체들을 가져오는 작업과 각 에티티를 삭제하는 작업이 같이 이루어지기 떄문'

        Transactional 이 없는 경우 발생하는 오류
        No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call

        @Commit
        최종결과를 커밋하기 위해서 사용하는데, 이를 적용하지 않은면 기본적으로 deleteBy 는 롤백 처리되어 받영되지 않기떄문에
        Delete 메소드의 경우에는 Commit 을 적용해야한다   -> Delete 는 실제 개발에선 잘 사용 X (Why ? 한번에 삭제가아니라 각 엔티티 객체를 하나씩 삭제하기 때문에)

         */
        memoRepository.deleteMemoByMnoLessThan(10L);
    }




}
