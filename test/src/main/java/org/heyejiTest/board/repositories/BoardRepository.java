package org.heyejiTest.board.repositories;

import org.heyejiTest.board.entities.BoardData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardData,Long> {
}
