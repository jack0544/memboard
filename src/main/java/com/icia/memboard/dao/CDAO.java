package com.icia.memboard.dao;

import com.icia.memboard.dto.COMMENT;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CDAO {
    int cmtDelete1(int cbNum);

    List<COMMENT> cList(int cbNum);

    int cmtWrite(COMMENT comment);

    int cmtDelete(COMMENT comment);

    int cmtModify(COMMENT comment);
}
