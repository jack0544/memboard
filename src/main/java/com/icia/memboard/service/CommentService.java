package com.icia.memboard.service;

import com.icia.memboard.dto.COMMENT;

import java.util.List;

public interface CommentService {
    List<COMMENT> cList(int cbNum);

    List<COMMENT> cmtWrite(COMMENT comment);

    List<COMMENT> cmtDelete(COMMENT comment);

    List<COMMENT> cmtModify(COMMENT comment);
}
