package com.icia.memboard.controller;

import com.icia.memboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
// 페이지 이동 없이 값만 넘겨줄 경우 (ajax)
// @ResponseBody를 작성하지 않아도 ajax 함수를 처리할 수 있다
// Controller - ModelAndView / RestController - data 처리
public class restController {

    private final MemberService msvc;

    // idoverlap : 아이디 중복체크 ajax
    @PostMapping("/idoverlap")
    public @ResponseBody String idoverlap(@RequestParam("memId") String memId) {
        System.out.println("ajax로 넘어온 아이디 : " + memId);

        String result = msvc.idoverlap(memId);

        System.out.println("db에서 확인한 result메세지 : " + result);

        return result;
    }

    // 이메일 보내기
    @PostMapping("/sendmail")
    public String sendmail(String mailTitle, String mailContent)  {
        System.out.println(mailTitle + "," + mailContent);
        return msvc.sendmail(mailTitle, mailContent);
    }



}
