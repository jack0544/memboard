package com.icia.memboard.service;

import com.icia.memboard.dao.MDAO;
import com.icia.memboard.dto.MEMBER;
import com.icia.memboard.dto.PAGE;
import com.icia.memboard.dto.SEARCH;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MService implements MemberService {

		// ModelAndView 
		private ModelAndView mav;
		
		// DAO
		private final MDAO mdao;
		
		// session
		private final HttpSession session;
		
		//

		private final HttpServletRequest request;
		

		private final PasswordEncoder pwEnc;
		

		private final JavaMailSender mailSender;



		// 회원가입
	public ModelAndView mJoin(MEMBER member) throws IOException {
		mav = new ModelAndView();

		// 비밀번호 암호화
		// [1] 입력한 비밀번호 가져오기 : member.getMemPw()
		// [2] 입력한 비밀번호 암호화 : pwEnc.encode()
		// [3] 암호화 된 비밀번호 저장 : member.setMemPw()

		member.setMemPw(pwEnc.encode(member.getMemPw()));

		System.out.println("암호화된 비밀번호 : " + member.getMemPw());

		// 파일 업로드
		// 1. 파일 불러오기(업로드한 사진을 가지고 오는 단계)
		MultipartFile profile = member.getMemProfile();

		// 2. 파일 선택 여부
		// profile.isEmpty() : 파일이 선택되지 않았다
		// !profile.isEmpty() : 파일이 선택됐다.
		if(!profile.isEmpty()) {

			// 4. 파일 저장 위치 설정(상대경로)
			Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/profile");


			// 5. 난수(UUID) 생성하기(숫자, 문자 섞어서 랜덤숫자 8글자 생성)
			String uuid = UUID.randomUUID().toString().substring(0,8);

			// 6. 파일 이름 불러오기(사진의 이름을 가지고 오는 단계)
			String originalFileName = profile.getOriginalFilename();

			// 7. 업로드할 파일 이름 생성하기(3번 + 언더바(_) + 2번)
			String profileName = uuid + "_" + originalFileName;

			// 8. 파일 선택시 member객체의 profileName에 업로드파일 이름 저장
			member.setMemProfileName(profileName);

			// 9.저장될 폴더와 파일 이름
			String savePath = path + "/" + profileName;

			// 10. 파일 업로드
			profile.transferTo(new File(savePath));

			// throw 추가 -> service, serviceImpl, controller
		} else {
			member.setMemProfileName("사진없음");
		}

		// 주소 api 결합
		String addr1 = member.getAddr1();
		String addr2 = member.getAddr2();
		String addr3 = member.getAddr3();

		String addr = "(" + addr1 + ") " + addr2 + ", " + addr3;

		member.setMemAddr(addr);

		try{
			// 회원가입 성공시 ( 에러나 예외처리가 없을 경우)
			mdao.mJoin(member);

			// 가입성공 메일 보내기
			String str = "<h2>안녕하세요. 인천일보 아카데미입니다.</h2>" +
					"<p>회원가입을 진심으로 축하한다네.</p>" +
					"<p>로그인 후에 이용이 가능하니 이점 참고하게!</p>" +
					"<a href='http://192.168.0.21:9090'>로그인하시게</a>";

			MimeMessage mail = mailSender.createMimeMessage();

			mail.setSubject("인천일보 아카데미 회원가입");
			mail.setText(str, "UTF-8", "html");
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(member.getMemEmail()));

			mailSender.send(mail);

			mav.setViewName("index");
		} catch (Exception e){
			// 예외처리 발생시
			mav.setViewName("M_Join");

			// 파일(사진) 삭제
			Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/profile");
			String deletePath = path + "/" + member.getMemProfileName();

			File deleteFile = new File(deletePath);

			if(deleteFile.exists()){
				deleteFile.delete();
			}

		}


		return mav;
		}

		// 로그인
		public ModelAndView mLogin(MEMBER member) {

			mav = new ModelAndView();

			String encPw = mdao.mLogin(member);
			
			if(pwEnc.matches(member.getMemPw(), encPw)) {
				session.setAttribute("loginId", member.getMemId());
				mav.setViewName("index");
			} else {
				mav.setViewName("M_Login");
			}

			return mav;
		}

		// 회원 리스트
		public ModelAndView mList(int page, int limit) {

			mav = new ModelAndView();

			System.out.println("[2] controller -> service \n page : " + page);

			int block = 5;


			int mCount = mdao.mCount();

			int startRow = (page - 1) * limit + 1;
			int endRow = page * limit;


			int maxPage = (int) (Math.ceil((double) mCount / limit));
			int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
			int endPage = startPage + block - 1;

			if (endPage > maxPage) {
				endPage = maxPage;
			}

			if(startPage <= 0){
				startPage = 1;
			}

			PAGE paging = new PAGE();

			paging.setPage(page);
			paging.setStartRow(startRow);
			paging.setEndRow(endRow);
			paging.setMaxpage(maxPage);
			paging.setStartpage(startPage);
			paging.setEndpage(endPage);
			paging.setLimit(limit);

			List<MEMBER> MemberList = mdao.mList(paging);

			// model
			mav.addObject("MemberList", MemberList);
			mav.addObject("paging", paging);
			// view
			mav.setViewName("M_List");

			return mav;
		}

		// 회원 검색
		public ModelAndView mSearch(SEARCH search) {

			mav = new ModelAndView();

			List<MEMBER> memberList = mdao.mSearch(search);
			
			System.out.println("dao -> service \n search : " + search);
			
			System.out.println("memberList : " + memberList);
			
			mav.addObject("memberList", memberList);
			mav.setViewName("S_List");
			
			
			
			return mav;
		}

		// 회원 상세보기
		public ModelAndView mView(String memId) {

			mav = new ModelAndView();

			MEMBER member = mdao.mView(memId);
			
			mav.addObject("member", member);
			mav.setViewName("M_View");
			
			return mav;
		}

		// 회원 수정페이지 이동
		public ModelAndView mModiForm(String memId) {

			mav = new ModelAndView();

			MEMBER member = mdao.mView(memId);
			
			mav.addObject("member", member);
			mav.setViewName("M_Modify");
			
			return mav;
		}

		// 회원 수정
		public ModelAndView mModify(MEMBER member) throws IOException {

			mav = new ModelAndView();

			// 비밀번호 암호화
			// [1] 입력한 비밀번호 가져오기 : member.getMemPw()
			// [2] 입력한 비밀번호 암호화 : pwEnc.encode()
			// [3] 암호화 된 비밀번호 저장 : member.setMemPw()

			member.setMemPw(pwEnc.encode(member.getMemPw()));

			System.out.println("암호화된 비밀번호 : " + member.getMemPw());

			// 파일 업로드
			// 1. 파일 불러오기(업로드한 사진을 가지고 오는 단계)
			MultipartFile profile = member.getMemProfile();

			// 2. 파일 선택 여부
			// profile.isEmpty() : 파일이 선택되지 않았다
			// !profile.isEmpty() : 파일이 선택됐다.
			if(!profile.isEmpty()) {

				// 4. 파일 저장 위치 설정(상대경로)
				Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/profile");


				// 5. 난수(UUID) 생성하기(숫자, 문자 섞어서 랜덤숫자 8글자 생성)
				String uuid = UUID.randomUUID().toString().substring(0,8);

				// 6. 파일 이름 불러오기(사진의 이름을 가지고 오는 단계)
				String originalFileName = profile.getOriginalFilename();

				// 7. 업로드할 파일 이름 생성하기(3번 + 언더바(_) + 2번)
				String profileName = uuid + "_" + originalFileName;

				// 8. 파일 선택시 member객체의 profileName에 업로드파일 이름 저장
				member.setMemProfileName(profileName);

				// 9.저장될 폴더와 파일 이름
				String savePath = path + "/" + profileName;

				// 10. 파일 업로드
				profile.transferTo(new File(savePath));

				// throw 추가 -> service, serviceImpl, controller
			} else {
				member.setMemProfileName("사진없음");
			}

			// 주소 api 결합
			String addr1 = member.getAddr1();
			String addr2 = member.getAddr2();
			String addr3 = member.getAddr3();

			String addr = "(" + addr1 + ") " + addr2 + ", " + addr3;

			member.setMemAddr(addr);

			try{
				// 회원가입 성공시 ( 에러나 예외처리가 없을 경우)
				mdao.mModify(member);

				mav.setViewName("redirect:/mView?memId=\" + member.getMemId()");
			} catch (Exception e){
				// 예외처리 발생시
				mav.setViewName("index");

				// 파일(사진) 삭제
				Path path = Paths.get(System.getProperty("user.dir"), "src/main//static/profile");
				String deletePath = path + "/" + member.getMemProfileName();

				File deleteFile = new File(deletePath);

				if(deleteFile.exists()){
					deleteFile.delete();
				}

			}




			return mav;
		}

		// 회원 삭제
		public ModelAndView mDelete(String memId) {
			
			int result = mdao.mDelete(memId);
			
			if(result > 0) {
				mav.setViewName("redirect:/mList");
			} else {
				mav.setViewName("index");
			}
			
			return mav;
		}

		// 아이디 중복체크
		public String idoverlap(String memId) {
			String checkId  = mdao.mCheckId(memId);
			String result;
			
			if(checkId == null) {
				result = "OK";
			} else {
				result = "NO";
			}
			
			return result;
		
		}

		// 메일 보내기
		public String mCheckEmail(String memEmail) {
			System.out.println("이메일 주소 : " + memEmail);
			String uuid = UUID.randomUUID().toString().substring(0, 6);
			
			MimeMessage mail = mailSender.createMimeMessage();
			
			String mailContent = "<h2>안녕하세요. 인천일보 아카데미입니다.</h2><br/>"
					+ "<h3>인증번호는 " + uuid + " 입니다.</h3>";
			
			try {
				mail.setSubject("[이메일 인증] 인천일보 아카데미 이메일 인증", "UTF-8");
				mail.setText(mailContent, "UTF-8", "html");
				mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(memEmail));
				
				mailSender.send(mail);
			} catch (MessagingException e) {
				e.printStackTrace();
			}

			return uuid;
		}

		// 메일 보내기
	@Override
	public String sendmail(String mailTitle, String mailContent) {
		List <MEMBER> memberList = mdao.mList1();

		String result = "";

		String str = "<pre>" + mailContent + "</pre>";

		MimeMessage mail = mailSender.createMimeMessage();

		try {
			mail.setSubject(mailTitle);
			mail.setText(str, "UTF-8", "html");
			for(MEMBER member : memberList) {
				System.out.println(member);
				mail.addRecipient(Message.RecipientType.TO, new InternetAddress(member.getMemEmail()));
			}
			mailSender.send(mail);
			return result = "ok";
		} catch (MessagingException e) {
			return result = "no";
		}

	}

}
