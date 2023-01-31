package com.icia.memboard.controller;


import com.icia.memboard.dto.COMMENT;
import com.icia.memboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CController {

	private final CommentService csvc;
	
	private ModelAndView mav = new ModelAndView();

	// cList : 댓글 리스트
	@GetMapping("/cList")
	public List<COMMENT> cList(@RequestParam("cbNum") int cbNum){

		List<COMMENT> commentList = csvc.cList(cbNum);

		System.out.println(commentList);

		return commentList;
	}
	
	// cmtWrite: 댓글 작성
	@PostMapping("/cmtWrite")
	public List<COMMENT> cmtWrite(@ModelAttribute COMMENT comment){
		
		List<COMMENT> clist = csvc.cmtWrite(comment);
		
		return clist;
	}
	
	// cmtDelete : 댓글 삭제
	@PostMapping("/cmtDelete")
	public @ResponseBody List<COMMENT> cmtDelete(@ModelAttribute COMMENT comment){
		
		List<COMMENT> commentList = csvc.cmtDelete(comment);
		
		System.out.println(commentList);
		
		return commentList;
	}
	
	// cModifyForm : 댓글 수정
	@PostMapping("/cmtModify")
	public @ResponseBody List<COMMENT> cmtModify(@ModelAttribute COMMENT comment){
		
		List<COMMENT> commentList = csvc.cmtModify(comment);
		
		System.out.println(commentList);
		
		return commentList;
	}
	
	
}
