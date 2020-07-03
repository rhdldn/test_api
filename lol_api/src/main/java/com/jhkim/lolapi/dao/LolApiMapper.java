package com.jhkim.lolapi.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LolApiMapper {

	public String selectApiKey();
	
	/**
	 * 유저 목록 조회
	 */
	public List<Map<String, Object>> selectUserList(Map<String, Object> paramMap);
	
	/**
	 * 유저 정보 조회
	 */
	public Map<String, Object> selectUserInfo(@Param("userId") String userId);

	/**
	 * 랭크 정보 조회
	 */
	public Map<String, Object> selectRankInfo(Map<String, Object> paramMap);
	
	/**
	 * 매치 목록 조회
	 */
	public List<Map<String, Object>> selectMatchList(Map<String, Object> paramMap);
	
	/**
	 * 유저 정보 등록
	 */
	public int insertUserInfo(Map<String, Object> paramMap);
	
	/**
	 * 유저 정보 갱신
	 */
	public int updateUserInfo(Map<String, Object> paramMap);
	
	/**
	 * 랭크 정보 갱신
	 */
	public int updateUserRank(Map<String, Object> paramMap);
	
	/**
	 * 랭크 정보 등록
	 */
	public int insertUserRank(Map<String, Object> paramMap);
	
	/**
	 * 매치 정보 갱신
	 */
	public int updateUserMatch(Map<String, Object> paramMap);
	
	/**
	 * 매치 정보 등록
	 */
	public int insertUserMatch(Map<String, Object> paramMap);
	
	/*
	 * 매치 상세 정보 갱신
	 */
	public int updateUserMatchDtl(Map<String, Object> paramMap);
	
	public int updateApiKey(@Param("apiKey") String apiKey);
}
