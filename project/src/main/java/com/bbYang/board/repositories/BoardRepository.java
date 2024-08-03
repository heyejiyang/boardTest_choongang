package com.bbYang.board.repositories;

import com.bbYang.board.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardRepository extends JpaRepository<Board,String>, QuerydslPredicateExecutor<Board> {

}
