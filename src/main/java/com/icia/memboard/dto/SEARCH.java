package com.icia.memboard.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("search")
public class SEARCH {
	private String category;
	private String keyword;
	
}
