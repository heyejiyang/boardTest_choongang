package org.heyejiTest.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.heyejiTest.board.entities.BoardData;
import org.heyejiTest.board.repositories.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EX01 {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void init(){
//        BoardData boardData = BoardData.builder()
//                .subject("제목1")
//                .content("내용내요용")
//                .writer("나작성")
//                .build();
//        boardDataRepository.saveAndFlush(boardData);

        List<BoardData> items = IntStream.rangeClosed(1,5)
                .mapToObj(i -> BoardData.builder()
                        .subject("제목"+i)
                        .content("내용"+i)
                        .writer("작성자"+i)
                        .build()).toList();

        boardRepository.saveAllAndFlush(items);

        em.clear();
    }
    @Test
    void test1(){ //전체 조회
        List<BoardData> items = boardRepository.findAll();
        items.forEach(System.out::println);

        BoardData bd = boardRepository.findById(1L).orElse(null);
        System.out.println(bd);

        System.out.println(bd.getCreatedAt());
    }

    @Test
    void test2() throws Exception{ //수정
        List<BoardData> boards = boardRepository.findAll();

        BoardData bd = boardRepository.findById(2L).orElse(null);

        boards.forEach(System.out::println);
        System.out.println(bd.getCreatedAt());

        Thread.sleep(5000);
        bd.setSubject("두번째 작성자");
        bd.setModifiedAt(LocalDateTime.now());

        boardRepository.saveAndFlush(bd);

        boards.forEach(System.out::println);
        System.out.println(bd.getModifiedAt());

    }

    @Test
    void test3(){ //삭제
        BoardData bd = boardRepository.findById(3L).orElse(null);

        boardRepository.delete(bd);
        boardRepository.flush();

        List<BoardData> boards = boardRepository.findAll();
        boards.forEach(System.out::println);
    }

}
