package com.icia.memboard.dto;

import java.sql.Date;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Alias("board")
public class BOARD {
	
	// 게시판 필드
		private int boNum;			// 게시글 번호
		private String boWriter;		// 작성자
		private String boTitle;		// 글제목
		private String boContent;	// 글내용
		private String boDate;			// 작성일 (Date -> String으로 바꾸면 시,분,초도 나옴)
		private int boHit;			// 조회수
		
		private MultipartFile boFile;	// 업로드 파일
		private String boFileName;		// 파일이름
}
