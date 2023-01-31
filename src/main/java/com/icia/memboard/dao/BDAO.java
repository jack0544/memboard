package com.icia.memboard.dao;

import com.icia.memboard.dto.BOARD;
import com.icia.memboard.dto.PAGE;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BDAO {
    int bWrite(BOARD board);

    int bCount();

    List<BOARD> bList(PAGE paging);

    BOARD bView(int boNum);

    int bModify(BOARD board);

    int bDelete(int boNum);
}
