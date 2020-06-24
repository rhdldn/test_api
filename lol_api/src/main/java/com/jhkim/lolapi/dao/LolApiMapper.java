package com.jhkim.lolapi.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LolApiMapper {

	public String selectCnncInfr();
	
	/**
	 * 
	* <PRE>
	* 간략 : LOCK 취득
	* 상세 : LOCK 취득
	* <PRE>
	* @param paramMap
	* @return 데이터 값
	 */
	public boolean selectLock(@Param("lockId") long lockId);

	/**
	 * 
	* <PRE>
	* 간략 : UNLOCK.
	* 상세 : UNLOCK.
	* <PRE>
	* @param lockId1
	 */
	public boolean excuteUnLock(long lockId1);
}
