package com.jhkim.lolapi.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

import com.google.gson.Gson;
import com.jhkim.lolapi.service.LolApiService;

@RestController
@RequestMapping(path="/lolapi")
public class LolApiController {
	
	@Autowired
	LolApiService lolApiService;
	
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
		
		List<Object> retList = lolApiService.getPonyLolRank();
		
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
		
		List<Object> retList = lolApiService.getEnemyLolRank();
		
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
	 * 대기오염 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 * @throws NotSupportedDataType 
	 */
	@RequestMapping(value = "/updWeather", method = RequestMethod.GET)
	public ResponseEntity<String> weatherInfo() throws Exception{

		HttpHeaders responseHeaders = new HttpHeaders();

		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		
		//ResponseEntity<String> retStr = lolApiService.sendRest("", null);
		String retStr = "";
		
		//String retStr = weatherInfoMngService.createWeatherInfo();
		
		return new ResponseEntity<>(retStr, responseHeaders, HttpStatus.OK);
	}
}
