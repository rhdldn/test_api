package com.jhkim.lolapi.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LolApiMapper {

	public String selectApiKey();
}
