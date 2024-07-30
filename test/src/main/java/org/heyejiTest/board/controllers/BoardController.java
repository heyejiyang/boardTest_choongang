package org.heyejiTest.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.heyejiTest.board.entities.BoardData;
import org.heyejiTest.board.services.BoardService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(@ModelAttribute RequestBoard form, Model model){
        List<BoardData> boardData = boardService.items();
        model.addAttribute("boardData", boardData);
        return "board/list";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute RequestBoard form, Model model){
        model.addAttribute("boardData",new BoardData());
        return "board/register";
    }

    @PostMapping("/register")
    public String registerPs(@Valid RequestBoard form, Errors errors){
        if (errors.hasErrors()) {
            return "board/register";
        }
        boardService.save(form);
        return "redirect:/board/list";
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model){
        BoardData boardData = boardService.find(seq);
        model.addAttribute("boardData", boardData);
        model.addAttribute("mode", "view");
        return "/board/update_view";
    }

    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq,Model model){

        model.addAttribute("boardData",boardService.find(seq));
        model.addAttribute("mode", "update");
        return "board/update_view";
    }

    @PostMapping("/update")
    public String updatePs(@Valid RequestBoard form,Errors errors){
        if (errors.hasErrors()) {
            return "board/update_view";
        }
        BoardData boardData = new ModelMapper().map(form, BoardData.class);
        boardService.save(form);

        return "redirect:/board/view/" + boardData.getSeq();
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq){
        BoardData boardData = boardService.find(seq);
        if (boardData != null) {
            boardService.delete(boardData);
        }
        return "redirect:/board/list";
    }


}
