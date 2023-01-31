package com.icia.memboard.dao;

import com.icia.memboard.dto.MEMBER;
import com.icia.memboard.dto.PAGE;
import com.icia.memboard.dto.SEARCH;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MDAO {
    int mJoin(MEMBER member);

    String mLogin(MEMBER member);

    int mCount();

    List<MEMBER> mList(PAGE paging);
    List<MEMBER> mList();

    List<MEMBER> mSearch(SEARCH search);

    MEMBER mView(String memId);

    int mModify(MEMBER member);

    int mDelete(String memId);

    String mCheckId(String memId);

    List<MEMBER> mList1();
}
