package com.icia.memboard.service;

import com.icia.memboard.dto.MEMBER;
import com.icia.memboard.dto.SEARCH;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface MemberService {

    ModelAndView mJoin(MEMBER member) throws IOException;

    ModelAndView mLogin(MEMBER member);

    ModelAndView mList(int page, int limit);

    ModelAndView mSearch(SEARCH search);

    ModelAndView mView(String memId);

    ModelAndView mModiForm(String memId);

    ModelAndView mModify(MEMBER member) throws IOException;

    ModelAndView mDelete(String memId);

    String idoverlap(String memId);

    String mCheckEmail(String memEmail);

    String sendmail(String mailTitle, String mailContent);
}
