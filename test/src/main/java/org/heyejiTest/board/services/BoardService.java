package org.heyejiTest.board.services;

import lombok.RequiredArgsConstructor;
import org.heyejiTest.board.controllers.RequestBoard;
import org.heyejiTest.board.entities.BoardData;
import org.heyejiTest.board.repositories.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardData> items(){

        return boardRepository.findAll();
    }

    public void save(RequestBoard form){

        Long seq = form.getSeq();
        BoardData boardData = null;


        if (seq != null) {
            boardData = boardRepository.findById(seq).orElse(null); // 수정
            if (boardData != null) {
                boardData.setWriter(form.getWriter());
                boardData.setSubject(form.getSubject());
                boardData.setContent(form.getContent());
            }
        }

        if (boardData == null) boardData = new ModelMapper().map(form, BoardData.class); // 추가
        //BoardData boardData = new ModelMapper().map(form, BoardData.class);

        boardRepository.saveAndFlush(boardData);
    }

    public BoardData find(Long seq){
        return boardRepository.findById(seq).orElse(null);
    }

    public void delete(BoardData boardData){
        boardRepository.delete(boardData);
    }

    public RequestBoard getForm(Long seq){
        BoardData data = find(seq);
        RequestBoard form = new ModelMapper().map(data, RequestBoard.class);

        return form;
    }

}
