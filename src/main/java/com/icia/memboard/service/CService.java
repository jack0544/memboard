package com.icia.memboard.service;

import java.util.List;

import com.icia.memboard.dao.CDAO;
import com.icia.memboard.dto.COMMENT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@RequiredArgsConstructor
public class CService implements CommentService{
	

	private final CDAO cdao;
	
	private ModelAndView mav = new ModelAndView();

	// 댓글 리스트
	public List<COMMENT> cList(int cbNum) {

		mav = new ModelAndView();
		
		List<COMMENT> commentList = cdao.cList(cbNum);
		
		return commentList;
	}

	// 댓글 작성
	public List<COMMENT> cmtWrite(COMMENT comment) {

		mav = new ModelAndView();
		
		List<COMMENT> commentList;
		
		int result = cdao.cmtWrite(comment);
		if(result>0) {
			commentList = cdao.cList(comment.getCbNum());
			
		} else {
			commentList = null;
		}
		
		return commentList;
	}

	// 댓글 삭제
	public List<COMMENT> cmtDelete(COMMENT comment) {

		mav = new ModelAndView();

		List<COMMENT> commentList;
		
		int result = cdao.cmtDelete(comment);
		
		if(result>0) {
			commentList = cdao.cList(comment.getCbNum());
			
		} else {
			commentList = null;
		}
		
		return commentList;
	}

	// 댓글 수정
	public List<COMMENT> cmtModify(COMMENT comment) {
		List<COMMENT> commentList;
		
		int result = cdao.cmtModify(comment);
		
		if(result>0) {
			commentList = cdao.cList(comment.getCbNum());
			
		} else {
			commentList = null;
		}
		
		return commentList;
	}

	}


