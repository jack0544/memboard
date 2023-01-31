package com.icia.memboard.controller;

import com.icia.memboard.dto.MEMBER;
import com.icia.memboard.dto.SEARCH;
import com.icia.memboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MController {
	// ModelAndView 객체 생성
	private ModelAndView mav = new ModelAndView();

	// MemberService 연결

	private final MemberService msvc;


	private final HttpSession session;

	// 프로젝트 시작페이지
	@GetMapping("/")
	public String index() {
		return "index";
	}

	// 프로젝트 홈페이지
	@GetMapping("/index")
	public String index1() {
		return "index";
	}

	// 회원가입 홈페이지
	@GetMapping("/mJoinForm")
	public String mJoinForm() {
		return "M_Join";
	}

	// mJoin : 회원가입
	@PostMapping( "/mJoin")
	public ModelAndView mJoin(@ModelAttribute MEMBER member) throws IOException {

		System.out.println("[1] jsp → controller \n member : " + member);

		mav.setViewName("redirect:/mJoinForm");

		mav = msvc.mJoin(member);

		return mav;
	}

	// mLoginForm
	@GetMapping("/mLoginForm")
	public String mLoginForm() {
		return "M_Login";
	}

	// mLogin
	@PostMapping("/mLogin")
	public ModelAndView mLogin(@ModelAttribute MEMBER member) throws IllegalStateException, IOException {

		System.out.println("[1] jsp → controller \n member : " + member);

		mav = msvc.mLogin(member);

		return mav;
	}

	// mLogout : 로그아웃
	@GetMapping("/mLogout")
	public String mLogout() {

		session.invalidate();

		return "index";
	}

	// mList : 페이징 처리 & 리스트
	@GetMapping("/mList")
	public ModelAndView mList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit) {
		// page를 필수로 가져와야 하나 -> false
		/*
		 * String page1 = request.getParameter("page"); if(page1 == null) { int page =
		 * 1; } else { int page = Integer.parseInt(page1); }
		 */

		System.out.println("[1] jsp → controller \n page : " + page);

		mav = msvc.mList(page, limit);

//			System.out.println("[5] service → controller \n mav : " + mav);

		return mav;
	}

	// mSearch : 회원검색
	@GetMapping("/mSearch")
	public ModelAndView mSearch(@ModelAttribute SEARCH search) throws IllegalStateException, IOException {

		System.out.println("[1] jsp → controller \n search : " + search);

		mav = msvc.mSearch(search);

		return mav;
	}

	// mView : 회원 상세보기
	@GetMapping("/mView")
	public ModelAndView mView(@RequestParam("memId") String memId) {

		mav = msvc.mView(memId);

		return mav;
	}

	// mModiForm : 수정
	@GetMapping("/mModiForm")
	public ModelAndView mModiForm(@RequestParam("memId") String memId) {

		mav = msvc.mModiForm(memId);

		return mav;
	}

	// mModify : 수정

	@PostMapping("/mModify")
	public ModelAndView mModify(@ModelAttribute MEMBER member) throws IllegalStateException, IOException {

		System.out.println("[1] jsp → controller \n member : " + member);



		mav = msvc.mModify(member);

		return mav;
	}

	// mDelete : 회원 삭제
	@GetMapping(value = "/mDelete")
	public ModelAndView mDelete(@RequestParam("memId") String memId) {

		mav = msvc.mDelete(memId);

		return mav;
	}



	// mCheckEmail : 이메일 인증
	@PostMapping("/mCheckEmail")
	public @ResponseBody String mCheckEmail(@RequestParam("memEmail") String memEmail) {

		String uuid = msvc.mCheckEmail(memEmail);

		// return msvc.mCheckEmail(memEmail);
		return uuid;
	}

	// dashboard : 내 정보 보기 창 이동
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	// tables : 내 정보 보기 창 이동
	@GetMapping("/tables")
	public String tables() {
		return "tables";
	}

	// sendmail : 공지메일 창 이동
	@GetMapping("/sendmail")
	public String sendmail(){
		return "sendmail";
	}




}
