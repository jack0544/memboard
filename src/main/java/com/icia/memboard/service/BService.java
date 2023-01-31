package com.icia.memboard.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.icia.memboard.dao.BDAO;
import com.icia.memboard.dao.CDAO;
import com.icia.memboard.dto.BOARD;
import com.icia.memboard.dto.PAGE;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Service
@RequiredArgsConstructor
public class BService implements BoardService {

	private ModelAndView mav;

	// session 추가

	private final HttpSession session;

	//

	private final HttpServletRequest request;


	private final BDAO bdao;


	private final CDAO cdao;

	public ModelAndView bWrite(BOARD board) throws IOException {

		mav = new ModelAndView();

		System.out.println("[2] controller → service \n board : " + board);

// 파일 업로드
		// 1. 파일 불러오기(업로드한 사진을 가지고 오는 단계)
		MultipartFile boFile = board.getBoFile();

		// 2. 파일 선택 여부
		// profile.isEmpty() : 파일이 선택되지 않았다
		// !profile.isEmpty() : 파일이 선택됐다.
		if (!boFile.isEmpty()) {

			// 4. 파일 저장 위치 설정(상대경로)
			Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/FileUpload");


			// 5. 난수(UUID) 생성하기(숫자, 문자 섞어서 랜덤숫자 8글자 생성)
			String uuid = UUID.randomUUID().toString().substring(0, 8);

			// 6. 파일 이름 불러오기(사진의 이름을 가지고 오는 단계)
			String originalFileName = boFile.getOriginalFilename();

			// 7. 업로드할 파일 이름 생성하기(3번 + 언더바(_) + 2번)
			String boFileName = uuid + "_" + originalFileName;

			// 8. 파일 선택시 member객체의 profileName에 업로드파일 이름 저장
			board.setBoFileName(boFileName);

			// 9.저장될 폴더와 파일 이름
			String savePath = path + "/" + boFileName;

			// 10. 파일 업로드
			boFile.transferTo(new File(savePath));

			// throw 추가 -> service, serviceImpl, controller
		} else {
			board.setBoFileName("사진없음");
		}


		int result = bdao.bWrite(board);

		try {


			if (result > 0) {
				mav.setViewName("index");
			} else {
				mav.setViewName("B_Write");
			}

			System.out.println("[4] dao -> service \n  board " + board);

		} catch (Exception e) {
			// 파일(사진) 삭제
			Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/FileUpload");
			String deletePath = path + "/" + board.getBoFileName();

			File deleteFile = new File(deletePath);

			if (deleteFile.exists()) {
				deleteFile.delete();
			}
		}
		return mav;
	}

	// 게시글 리스트
	public ModelAndView bList(int page, int limit) {

		mav = new ModelAndView();

		System.out.println("[2] controller → service \n page : " + page);

		// 한 화면에 보여줄 페이지 번호 개수
		int block = 5;

		// 전체 게시글 개수 : 0
		int bCount = bdao.bCount();

/*		if(bCount == 0){
			bCount = 1;
		}*/

		int startRow = (page - 1) * limit + 1;		// 1
		int endRow = page * limit;					// 5

		// Math.ceil() -> 소숫점 올림
		int maxPage = (int) (Math.ceil((double) bCount / limit));	// 0
		int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
		int endPage = startPage + block - 1;

		if (endPage > maxPage) {
			endPage = maxPage;
		}

		if(startPage <= 0){
			startPage = 1;
		}

		// 페이징 객체 생성
		PAGE paging = new PAGE();

		paging.setPage(page);
		paging.setStartRow(startRow);
		paging.setEndRow(endRow);
		paging.setMaxpage(maxPage);
		paging.setStartpage(startPage);
		paging.setEndpage(endPage);
		paging.setLimit(limit);

		List<BOARD> BoardList = bdao.bList(paging);

		System.out.println("[2] controller → service \n page : " + page);

		// model
		mav.addObject("BoardList", BoardList);
		mav.addObject("paging", paging);
		
		// view
		mav.setViewName("B_List");
		
		return mav;
	}

	// 게시글 상세보기
	public ModelAndView bView(int boNum) {

		mav = new ModelAndView();
		
		System.out.println("[2] controller → service \n boNum : " + boNum);
		
		BOARD board = bdao.bView(boNum);
		
		
		
		mav.setViewName("B_View");
		mav.addObject("board", board);
		
		System.out.println("[4] dao → service \n board : " + board);
		
		return mav;
	}

	// 게시글 수정페이지 이동
	public ModelAndView bModifyForm(int boNum) {

		mav = new ModelAndView();

		System.out.println("[2] controller �� service");

		BOARD board = bdao.bView(boNum);

		// Model
		mav.addObject("modify", board);

		// View
		mav.setViewName("B_Modify");

		return mav;
	}
	// 게시글 수정
	public ModelAndView bModify(BOARD board) throws IOException {

		mav = new ModelAndView();

		System.out.println("[2] controller → service \n board : " + board);

		// [1] 파일업로드
		// - 파일 이름 생성
		// - (식별자 uuid + 파일원래 이름)
		// - profile 폴더 생성 후 업로드

		MultipartFile boFile = board.getBoFile();

		UUID uuid = UUID.randomUUID();

		String boFileName = uuid.toString().substring(0, 8) + "_" + boFile.getOriginalFilename();

//					String savePath = "C:\\Users\\user\\Documents\\workspace-sts-3.9.18.RELEASE\\MEMBOARD\\src\\main\\webapp\\resources\\profile\\";
		String savePath = request.getServletContext().getRealPath("/FileUpload/");

		if (!boFile.isEmpty()) {
			boFile.transferTo(new File(savePath + boFileName));
			board.setBoFileName(boFileName);

		} else {
			board.setBoFileName("사진없음");
		}

		System.out.println("uuid 확인 : " + uuid.toString().substring(0, 8));

		int result = bdao.bModify(board);

		if (result > 0) {
			mav.setViewName("redirect:/bView?boNum=" + board.getBoNum());
		} else {
			mav.setViewName("redirect:/bList");
		}

		System.out.println("[4] dao -> service \n  board " + board);
		
		return mav;
	}

	// 게시글 삭제
	public ModelAndView bDelete(int boNum) {

		mav = new ModelAndView();

		int cbNum = boNum;
		
/*		int result1 = cdao.cmtDelete1(cbNum);

		int result = bdao.bDelete(boNum);*/

		try{
			cdao.cmtDelete1(cbNum);

			bdao.bDelete(boNum);

			mav.setViewName("redirect:/bList");

		} catch(Exception e){

			mav.setViewName("redirect:/bView?boNum=" + boNum);

		}
		

/*
		if (result > 0 && result1 > 0) {
			mav.setViewName("redirect:/bList");
		} else {
			mav.setViewName("redirect:/bView?boNum=" + boNum);
		}
*/

		return mav;
	}

	}


