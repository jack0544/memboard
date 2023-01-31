package com.icia.memboard.controller;


import com.icia.memboard.dto.BOARD;
import com.icia.memboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class BController {


    // ModelAndView 객체 생성
    private ModelAndView mav = new ModelAndView();

    // service에 연결

    private final BoardService bsvc;


    private final HttpSession session;

    // bWriteForm : 게시글 작성 페이지로 이동
    @GetMapping("/bWriteForm")
    public String bWriteForm() {
        return "B_Write";
    }

    // bWrite : 게시글 작성
    @PostMapping("/bWrite")
    public ModelAndView bWrite(@ModelAttribute BOARD board) throws IOException {

        System.out.println("[1] jsp -> controller \n  board " + board);

        mav = bsvc.bWrite(board);

        System.out.println("[5] service -> controller \n  board " + board);

        return mav;
    }

    // bList : 게시글 목록
    @GetMapping("/bList")
    public ModelAndView bList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "limit", required = false, defaultValue = "5") int limit) {
        // page를 필수로 가져와야 하나 -> false
        /*
         * String page1 = request.getParameter("page"); if(page1 == null) { int page =
         * 1; } else { int page = Integer.parseInt(page1); }
         */

        System.out.println("[1] jsp → controller \n page : " + page);



        mav = bsvc.bList(page, limit);



        return mav;

    }

    // bView : 게시글 상세보기
    @GetMapping("/bView")
    public ModelAndView bView(@RequestParam ("boNum") int boNum) {

        System.out.println("[1] jsp -> controller \n  boNum " + boNum);

        mav = bsvc.bView(boNum);

        System.out.println("[5] service -> controller \n  boNum " + boNum);

        return mav;
    }

    // bModifyForm : 게시글 수정 페이지 이동
    @GetMapping("/bModifyForm")
    public ModelAndView bModifyForm(@RequestParam("boNum") int boNum) {


        mav = bsvc.bModifyForm(boNum);

        return mav;
    }

    // bModify : 게시글 수정
    @PostMapping("/bModify")
    public ModelAndView bModify(@ModelAttribute BOARD board) throws IOException {


        mav = bsvc.bModify(board);

        return mav;
    }

    // bDelete : 게시글 삭제
    @GetMapping("/bDelete")
    public ModelAndView bDelete(@RequestParam("boNum") int boNum) {

        mav = bsvc.bDelete(boNum);


        return mav;
    }



}
