package com.jhkim.lolapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jhkim.lolapi.service.LolApiService;
import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping(path="/lolapi")
public class LolApiController {
	
	@Autowired
	LolApiService lolApiService;
	
	/**
	 * 
	* <PRE>
	* 간략 : 사용자 전적 검색
	* 상세 : 사용자 전적 검색
	* <PRE>
	* @param paramMap
	* @return 데이터 값
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(path="/getUserList.do", produces="text/plain; charset=UTF-8")
	public ModelAndView getUserList(@RequestParam Map<String, Object> paramMap) throws Exception
	{
		ModelAndView modelAndView = new ModelAndView("/userlist");
		
		if(MapUtils.getString(paramMap, "userId", "").equals("")){
			return modelAndView;
		}
		
		List<Map<String, Object>> retList = lolApiService.getLolRank(paramMap);
		
		modelAndView.addObject("userList", retList);
		
		return modelAndView;
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 포니 랭크 정보 조회
	* 상세 : 포니 랭크 정보 조회
	* <PRE>
	* @param paramMap
	* @return 데이터 값
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(path="/getPonyList.do", produces="text/plain; charset=UTF-8")
	public ModelAndView getPonyList(@RequestParam Map<String, Object> paramMap) throws Exception
	{
		ModelAndView modelAndView = new ModelAndView("/ponylist");
		
		paramMap.put("userType", "PONY");
		List<Map<String, Object>> retList = lolApiService.getLolRank(paramMap);
		
		modelAndView.addObject("ponyRankList", retList);
		
		return modelAndView;
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 상대 랭크 정보 조회
	* 상세 : 상대 랭크 정보 조회
	* <PRE>
	* @param paramMap
	* @return 데이터 값
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(path="/getEnemyList.do", produces="text/plain; charset=UTF-8")
	public ModelAndView getEnemyList(@RequestParam Map<String, Object> paramMap) throws Exception
	{
		ModelAndView modelAndView = new ModelAndView("/enemylist");
		
		paramMap.put("userType", "ENERMY");
		List<Map<String, Object>> retList = lolApiService.getLolRank(paramMap);
		
		modelAndView.addObject("enemyRankList", retList);
		
		return modelAndView;
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 챔피언 숙련도 정보 조회
	* 상세 : 챔피언 숙련도 정보 조회
	* <PRE>
	* @param paramMap
	* @return 데이터 값
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(path="/getChampList.do", produces="text/plain; charset=UTF-8")
	public ModelAndView getChampList(@RequestParam Map<String, Object> paramMap) throws Exception
	{
		ModelAndView modelAndView = new ModelAndView("/champlist");
		
		List<Map<String, Object>> retList = lolApiService.getChampList(paramMap);
		
		modelAndView.addObject("champList", retList);
		
		return modelAndView;
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 전적 검색
	* 상세 : 전적 검색
	* <PRE>
	* @param paramMap
	* @return 데이터 값
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserHs.do", method = RequestMethod.GET)
	public ModelAndView getUserHs(@RequestParam("userId") String userId, @RequestParam("userType") String userType) throws Exception{
	
		String retUrl = "/ponylist";
		String retListNm = "ponyRankList";
		
		lolApiService.createUserHs(userId);
		
		if(userType.equals("ENERMY")){
			retUrl = "/enemylist";
			retListNm = "enemyRankList";
			
		}
		
		ModelAndView modelAndView = new ModelAndView(retUrl);

		Map<String, Object> paramMap = new HashMap();
		paramMap.put("userType", userType);
		List<Map<String, Object>> retList = lolApiService.getLolRank(paramMap);
		
		modelAndView.addObject(retListNm, retList);
		
		return modelAndView;
	}
	
	/**
	 * API KEY 갱신
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 * @throws NotSupportedDataType 
	 */
	@RequestMapping(value = "/updApiKey", method = RequestMethod.GET)
	public ResponseEntity<String> updApiKey(@RequestParam("apiKey") String apiKey) throws Exception{

		HttpHeaders responseHeaders = new HttpHeaders();

		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		
		lolApiService.modifyApiKey(apiKey);
		
		return new ResponseEntity<>("", responseHeaders, HttpStatus.OK);
	}
}
