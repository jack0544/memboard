package com.icia.memboard.service;

import com.icia.memboard.dto.BOARD;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public interface BoardService {
    ModelAndView bWrite(BOARD board) throws IOException;

    ModelAndView bList(int page, int limit);

    ModelAndView bView(int boNum);

    ModelAndView bModifyForm(int boNum);

    ModelAndView bModify(BOARD board) throws IOException;

    ModelAndView bDelete(int boNum);
}
