package com.icia.memboard.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Data
@Alias("member")
public class MEMBER {

	private String memId;
	private String memPw;
	private String memName;
	private String memBirth;
	private String memGender;
	private String memEmail;
	private String memPhone;
	private String memAddr;
	private MultipartFile memProfile;
	private String memProfileName;

	private String addr1;
	private String addr2;
	private String addr3;

	private String mailTitle;

	private String mailContent;

}
