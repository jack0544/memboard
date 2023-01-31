package com.icia.memboard.dto;

import java.sql.Date;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("comment")
public class COMMENT {
	private int cmtNum;			// 댓글 번호
	private int cbNum;			// 게시글 번호
	private String cmtWriter;	// 댓글 작성자
	private String cmtContent; 	// 댓글 내용
	private Date cmtDate;		// 댓글 작성일
	
}
