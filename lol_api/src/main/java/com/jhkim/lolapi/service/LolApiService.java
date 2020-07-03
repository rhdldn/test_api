package com.jhkim.lolapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.jhkim.lolapi.dao.LolApiMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * LOL API
 * 
 * @author 김종현
 *
 */
@Service
@Slf4j
public class LolApiService {
	
	@Autowired
	LolApiMapper lolApiMapper;

	private static final String serviceUrl = "https://kr.api.riotgames.com/";

	private static final String champJsonUrl = "http://ddragon.leagueoflegends.com/cdn/10.12.1/data/en_US/champion.json";
			
	private static final String[] ponyIdAry = {"김지둔", "고이우", "삶은인과응보", "여무라", "헤이컴온요", "수쥐니아"};
	private static final String[] enemyIdAry = {"말롱꿀", "애승희", "부딪혀맞설뿐", "똘똘똘뜰똘뜰똘", "Hide on 6ush"};
	
	/**
	 * 유저 전적 등록/갱신
	 * @param parameters2 
	 * @throws Exception 
	 */
	public void createUserHs(String userId) throws Exception {
		
		Gson gson = new Gson();
			
		String apiKey = lolApiMapper.selectApiKey();
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("apikey", apiKey);
			
		ResponseEntity<String> userRsp = getLolUserInfo(userId, paramMap);
		Map<String, Object> apiUserMap = gson.fromJson(userRsp.getBody(), Map.class);
			
		apiUserMap.put("userId", userId);
		apiUserMap.put("userType", "");
		if(lolApiMapper.updateUserInfo(apiUserMap) == 0){
			lolApiMapper.insertUserInfo(apiUserMap);
		}
		List<Map<String, Object>> userList = lolApiMapper.selectUserList(apiUserMap);
		
		//전적 갱신
		getRankInfo(userList);
	}
	
	/**
	 * 포니 랭크 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getPonyLolRank() throws Exception {
		
		List<Map<String, Object>> userRankList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("userType", "PONY");
		List<Map<String, Object>> ponyList = lolApiMapper.selectUserList(paramMap);
		
		for (Map<String, Object> map : ponyList) {
			
			Map<String, Object> userRankMap = new HashMap();
			
			map.put("userGameId", MapUtils.getString(map, "USER_GAME_ID", ""));
			map.put("queueType", "RANKED_SOLO_5x5");
			Map<String, Object> soloRankMap = lolApiMapper.selectRankInfo(map);
			userRankMap.put("soloRankMap", soloRankMap);
			
			map.put("queueType", "RANKED_FLEX_SR");
			Map<String, Object> freeRankMap = lolApiMapper.selectRankInfo(map);
			userRankMap.put("freeRankMap", freeRankMap);
			
			List<Map<String, Object>> lastGameList = lolApiMapper.selectMatchList(map);
			userRankMap.put("lastGameList", lastGameList);
			
			userRankList.add(userRankMap);
		}
		return userRankList;
	}
	
	/**
	 * 상대 랭크 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getEnemyLolRank() throws Exception {
		
		List<Map<String, Object>> userRankList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("userType", "ENERMY");
		List<Map<String, Object>> ponyList = lolApiMapper.selectUserList(paramMap);
		
		for (Map<String, Object> map : ponyList) {
			
			Map<String, Object> userRankMap = new HashMap();
			
			map.put("userGameId", MapUtils.getString(map, "USER_GAME_ID", ""));
			map.put("queueType", "RANKED_SOLO_5x5");
			Map<String, Object> soloRankMap = lolApiMapper.selectRankInfo(map);
			userRankMap.put("soloRankMap", soloRankMap);
			
			map.put("queueType", "RANKED_FLEX_SR");
			Map<String, Object> freeRankMap = lolApiMapper.selectRankInfo(map);
			userRankMap.put("freeRankMap", freeRankMap);
			
			List<Map<String, Object>> lastGameList = lolApiMapper.selectMatchList(map);
			userRankMap.put("lastGameList", lastGameList);
			
			userRankList.add(userRankMap);
		}
		return userRankList;
	}
	
	/**
	 * 숙련도 챔피언 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getChampList(Map<String, Object> param) throws Exception {
		
		String apiKey = lolApiMapper.selectApiKey();
		param.put("apikey", apiKey);
		
		ResponseEntity<String> lvlChampRsp = getLvlChampList(param);
		return parsingLvlChampInfo(lvlChampRsp.getBody());
	}
	
	/**
	 * API KEY 갱신
	 * @param parameters2 
	 * @throws Exception 
	 */
	public void modifyApiKey(String apiKey){
		
		if(apiKey != null && !apiKey.equals("")){
			int updCnt = lolApiMapper.updateApiKey(apiKey);
			log.info("갱신 횟수 : {}", updCnt);
		}
	}
	
	
	/**
	 * 랭크 정보 조회/갱신
	 * @param parameters2 
	 * @throws Exception 
	 */
	public void getRankInfo(List<Map<String, Object>> userList) throws Exception {
		
		Gson gson = new Gson();
		Map<String, Object> parameters = new HashMap();
		
		try {
			
			for(int i=0; i<userList.size(); i++){
				
				String userId = MapUtils.getString(userList.get(i), "USER_GAME_ID", "");
				log.debug("gameId : {}", userId);
				
				String apiKey = lolApiMapper.selectApiKey();
				parameters.put("apikey", apiKey);
				ResponseEntity<String> userRsp = getLolUserInfo(userId, parameters);
			
				if(userRsp != null){
				
					Map<String, Object> userMap = gson.fromJson(userRsp.getBody(), Map.class);
					String summId = MapUtils.getString(userMap, "id", "");
					String accountId = MapUtils.getString(userMap, "accountId", "");
					log.info("summId : {}", summId);
					log.info("accountId : {}", accountId);
				
					//랭크 정보
					ResponseEntity<String> rankRsp = getLolUserRank(summId, parameters);
				
					if(rankRsp != null){
						
						//솔로랭크, 자유랭크 재파싱
						parsingRankInfo(rankRsp.getBody());
						
						//매치정보 파싱 적재
						getLstMatchInfo(accountId, userId);
						log.info("소환사아이디 : {} 정보 적재 성공", userId);
					}
				}
			}
		} catch (Exception e) {
			log.error("랭크 정보 조회/갱신 ERROR : {}", e.getMessage());
		}
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 랭크 정보 파싱.
	* 상세 : .
	* <PRE>
	* @param body
	* @return
	 */
	private Map<String, Object> parsingRankInfo(String body) {
		
		Map<String, Object> rankMap = new HashMap();
		Gson gson = new Gson();
		
		List<Map<String, Object>> retList = gson.fromJson(body, List.class);
		
		if(retList != null){
			for (Map<String, Object> map : retList) {
				if(lolApiMapper.updateUserRank(map) == 0){
					lolApiMapper.insertUserRank(map);
				}
			}
		}
		return rankMap;
	}
	
	/**
	 * 최근 매치 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	private void getLstMatchInfo(String accountId, String userId) throws Exception {
		
		Map<String, Object> parameters = new HashMap();
		
		try {
			
			String apiKey = lolApiMapper.selectApiKey();
			parameters.put("apikey", apiKey);
			ResponseEntity<String> matchRsp = getMatchInfo(accountId, parameters);
				
			if(matchRsp != null){
						
				//매치정보 재파싱
				parsingMatchInfo(matchRsp.getBody(), accountId, parameters, userId);
			}
		} catch (Exception e) {
			log.error("랭크 정보 조회 ERROR : {}", e.getMessage());
		}
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 매치 정보 파싱.
	* 상세 : .
	* <PRE>
	* @param body
	* @return
	 * @throws Exception 
	 */
	private void parsingMatchInfo(String body, String accountId, Map<String, Object> parameters, String userId) throws Exception {
		
		Gson gson = new Gson();
		
		Map<String, Object> retMap = gson.fromJson(body, Map.class);
		
		if(retMap != null){
			
			List<Map<String, Object>> matchList = (List<Map<String, Object>>) retMap.get("matches");
			
			for (Map<String, Object> map : matchList) {

				map.put("userId", userId);
				map.put("gameId", Long.toString(MapUtils.getLongValue(map, "gameId", 0)));
				map.put("timestamp", Long.toString(MapUtils.getLongValue(map, "timestamp", 0)));
				if(lolApiMapper.updateUserMatch(map) == 0){
					lolApiMapper.insertUserMatch(map);
				}
				
				//매치에 대한 KDA, 결과 조회
				ResponseEntity<String> matchDtlRsp = getMatchDetailInfo(Long.toString(MapUtils.getLongValue(map, "gameId", 0)), parameters);
				parsingMatchDetail(matchDtlRsp.getBody(), accountId, userId);
			}
		}
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 매치 상세 정보 파싱.
	* 상세 : .
	* <PRE>
	* @param body
	* @return
	 */
	private Map<String, Object> parsingMatchDetail(String body, String paramAccountId, String userId) {
		
		Map<String, Object> matchDtlMap = new HashMap();
		Gson gson = new Gson();
		
		Map<String, Object> retMap = gson.fromJson(body, Map.class);
		List<Map<String, Object>> matchUserList = new ArrayList<>();
		
		if(retMap.get("participantIdentities") != null){
			matchUserList = (List<Map<String, Object>>) retMap.get("participantIdentities");
		}
		String participantId = "";
		for (Map<String, Object> map : matchUserList) {
				
			Map<String, Object> userMap = (Map<String, Object>) map.get("player");
			
			String userAccountId = MapUtils.getString(userMap, "accountId", "");
			if(paramAccountId.equals(userAccountId)){
				participantId = MapUtils.getString(map, "participantId", "");
			}
		}
		
		List<Map<String, Object>> matchUserDtlList = new ArrayList<>();
		if(retMap.get("participants") != null){
			matchUserDtlList = (List<Map<String, Object>>) retMap.get("participants");
		}
		
		for (Map<String, Object> map : matchUserDtlList) {
			
			String igParticipantId = MapUtils.getString(map, "participantId", "");
			if(participantId.equals(igParticipantId)){
				
				Map<String, Object> matchUserSts = (Map<String, Object>) map.get("stats");
				
				//KDA, 승패여부
				matchDtlMap.put("userId", userId);
				matchDtlMap.put("gameId", Long.toString(MapUtils.getLongValue(retMap, "gameId", 0)));
				matchDtlMap.put("kills", MapUtils.getInteger(matchUserSts, "kills", 0));
				matchDtlMap.put("deaths", MapUtils.getInteger(matchUserSts, "deaths", 0));
				matchDtlMap.put("assists", MapUtils.getInteger(matchUserSts, "assists", 0));
				matchDtlMap.put("winflag", MapUtils.getString(matchUserSts, "win", ""));
				lolApiMapper.updateUserMatchDtl(matchDtlMap);
			}
		}
		return matchDtlMap;
	}
	
	/**
	 * 
	* <PRE>
	* 간략 : 숙련도 챔피언 정보 파싱.
	* 상세 : .
	* <PRE>
	* @param body
	* @return
	 */
	private List<Map<String, Object>> parsingLvlChampInfo(String body) {
		
		List<Map<String, Object>> retList = new ArrayList<>();
		Gson gson = new Gson();
		
		List<Map<String, Object>> champList = gson.fromJson(body, List.class);
		
		if(champList != null){
			
			for (Map<String, Object> map : champList) {
				
				Map<String, Object> champMap = new HashMap();
				String champion = Integer.toString(MapUtils.getInteger(map, "championId", 0));
				int championLevel = MapUtils.getInteger(map, "championLevel", 0);
				int championPoints = MapUtils.getInteger(map, "championPoints", 0);
				
				champMap.put("championNm", getChampMap(champion));
				champMap.put("championLevel", championLevel);
				champMap.put("championPoints", championPoints);
				retList.add(champMap);
			}
		}
		return retList;
	}
	
	private int getRankImg(String tierNm){
		
		int retVal = 0;
		switch (tierNm) {
		
		case "Iron":
			retVal = 1;
			break;
		case "Bronze":
			retVal = 2;
			break;
		case "Silver":
			retVal = 3;
			break;
		case "Gold":
			retVal = 4;
			break;
		case "Platinum":
			retVal = 5;
			break;
		case "Diamond":
			retVal = 6;
			break;
		case "Master":
			retVal = 7;
			break;
		case "Grandmaster":
			retVal = 8;
			break;
		case "Challenger":
			retVal = 9;
			break;
		default:
			break;
		}
		return retVal;
	}

	/**
	 * 숙련도 챔피언 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> getLvlChampList(Map<String, Object> parameters) throws Exception {
		String apiUrl = "lol/champion-mastery/v4/champion-masteries/by-summoner/" + MapUtils.getString(parameters, "summId", "");
		return sendRest(serviceUrl, apiUrl+"?api_key="+MapUtils.getString(parameters, "apikey", ""), parameters);
	}
	
	/**
	 * 챔피언 정보 파싱
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> parsingChampInfo(Map<String, Object> parameters) throws Exception {
		return sendRestChamp(champJsonUrl, parameters);
	}
	
	/**
	 * 매치 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> getMatchInfo(String accountId, Map<String, Object> parameters) throws Exception {
		String apiUrl = "lol/match/v4/matchlists/by-account/" + accountId;
		return sendRestMatch(serviceUrl, apiUrl+"?api_key="+MapUtils.getString(parameters, "apikey", ""), parameters);
	}
	
	/**
	 * 매치 상세 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> getMatchDetailInfo(String matchId, Map<String, Object> parameters) throws Exception {
		String apiUrl = "lol/match/v4/matches/" + matchId;
		return sendRest(serviceUrl, apiUrl+"?api_key="+MapUtils.getString(parameters, "apikey", ""), parameters);
	}
	
	/**
	 * 챔피언 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> getChampInfo(Map<String, Object> parameters) throws Exception {
		return sendRestChamp(champJsonUrl, parameters);
	}
	
	/**
	 * 사용자 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> getLolUserInfo(String summNm, Map<String, Object> parameters) throws Exception {
		String apiUrl = "/lol/summoner/v4/summoners/by-name/" + summNm;
		return sendRest(serviceUrl, apiUrl+"?api_key="+MapUtils.getString(parameters, "apikey", ""), parameters);
	}
	
	/**
	 * 사용자 랭크 정보 조회
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> getLolUserRank(String encSummId, Map<String, Object> parameters) throws Exception {
		String apiUrl = "lol/league/v4/entries/by-summoner/" + encSummId;
		return sendRest(serviceUrl, apiUrl+"?api_key="+MapUtils.getString(parameters, "apikey", ""), parameters);
	}
	
	/**
	 * REST BASE
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> sendRest(String url, Map<String, Object> parameters) throws Exception {
		return sendRest(serviceUrl, url, parameters);
	}
	
	/**
	 * REST BASE
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> sendRestParam(String url, Map<String, Object> parameters, Map<String, Object> queryParam) throws Exception {
		return sendRest(serviceUrl, url, parameters);
	}
	
	/**
	 * REST BASE
	 * @param parameters2 
	 * @throws Exception 
	 */
	public ResponseEntity<String> sendRestChamp(String url, Map<String, Object> parameters) throws Exception {
		return sendRest("", url, parameters);
	}
	
	/**
	 * REST
	 * @param parameters2 
	 * @throws Exception 
	 */
	private ResponseEntity<String> sendRest(String restUrl, String url, Map<String, Object> parameters) throws Exception {
		
		try {

			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(parameters, headers);
			ResponseEntity<String> response = restTemplate.exchange(restUrl + url, HttpMethod.GET, request, String.class);
			
			return response;
			
		} catch (Exception e) {
			log.error("error : {}", e.getMessage());
		}

		
		return null;
	}
	
	/**
	 * REST
	 * @param parameters2 
	 * @throws Exception 
	 */
	private ResponseEntity<String> sendRestMatch(String restUrl, String url, Map<String, Object> parameters) throws Exception {
		
		try {

			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + url)
			        .queryParam("beginIndex", 0)
			        .queryParam("endIndex", 8);
			
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(parameters, headers);
			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, String.class);
			
			return response;
			
		} catch (Exception e) {
			log.error("error : {}", e.getMessage());
		}

		
		return null;
	}
	
	
	private String getChampMap(String champId){
		
		Map<String, Object> champMap = new HashMap<>();
		champMap.put("266","Aatrox");
		champMap.put("103","Ahri");
		champMap.put("84","Akali");
		champMap.put("12","Alistar");
		champMap.put("32","Amumu");
		champMap.put("34","Anivia");
		champMap.put("1","Annie");
		champMap.put("523","Aphelios");
		champMap.put("22","Ashe");
		champMap.put("136","AurelionSol");
		champMap.put("268","Azir");
		champMap.put("432","Bard");
		champMap.put("53","Blitzcrank");
		champMap.put("63","Brand");
		champMap.put("201","Braum");
		champMap.put("51","Caitlyn");
		champMap.put("164","Camille");
		champMap.put("69","Cassiopeia");
		champMap.put("31","Chogath");
		champMap.put("42","Corki");
		champMap.put("122","Darius");
		champMap.put("131","Diana");
		champMap.put("119","Draven");
		champMap.put("36","DrMundo");
		champMap.put("245","Ekko");
		champMap.put("60","Elise");
		champMap.put("28","Evelynn");
		champMap.put("81","Ezreal");
		champMap.put("9","Fiddlesticks");
		champMap.put("114","Fiora");
		champMap.put("105","Fizz");
		champMap.put("3","Galio");
		champMap.put("41","Gangplank");
		champMap.put("86","Garen");
		champMap.put("150","Gnar");
		champMap.put("79","Gragas");
		champMap.put("104","Graves");
		champMap.put("120","Hecarim");
		champMap.put("74","Heimerdinger");
		champMap.put("420","Illaoi");
		champMap.put("39","Irelia");
		champMap.put("427","Ivern");
		champMap.put("40","Janna");
		champMap.put("59","JarvanIV");
		champMap.put("24","Jax");
		champMap.put("126","Jayce");
		champMap.put("202","Jhin");
		champMap.put("222","Jinx");
		champMap.put("145","Kaisa");
		champMap.put("429","Kalista");
		champMap.put("43","Karma");
		champMap.put("30","Karthus");
		champMap.put("38","Kassadin");
		champMap.put("55","Katarina");
		champMap.put("10","Kayle");
		champMap.put("141","Kayn");
		champMap.put("85","Kennen");
		champMap.put("121","Khazix");
		champMap.put("203","Kindred");
		champMap.put("240","Kled");
		champMap.put("96","KogMaw");
		champMap.put("7","Leblanc");
		champMap.put("64","LeeSin");
		champMap.put("89","Leona");
		champMap.put("127","Lissandra");
		champMap.put("236","Lucian");
		champMap.put("117","Lulu");
		champMap.put("99","Lux");
		champMap.put("54","Malphite");
		champMap.put("90","Malzahar");
		champMap.put("57","Maokai");
		champMap.put("11","MasterYi");
		champMap.put("21","MissFortune");
		champMap.put("62","MonkeyKing");
		champMap.put("82","Mordekaiser");
		champMap.put("25","Morgana");
		champMap.put("267","Nami");
		champMap.put("75","Nasus");
		champMap.put("111","Nautilus");
		champMap.put("518","Neeko");
		champMap.put("76","Nidalee");
		champMap.put("56","Nocturne");
		champMap.put("20","Nunu");
		champMap.put("2","Olaf");
		champMap.put("61","Orianna");
		champMap.put("516","Ornn");
		champMap.put("80","Pantheon");
		champMap.put("78","Poppy");
		champMap.put("555","Pyke");
		champMap.put("246","Qiyana");
		champMap.put("133","Quinn");
		champMap.put("497","Rakan");
		champMap.put("33","Rammus");
		champMap.put("421","RekSai");
		champMap.put("58","Renekton");
		champMap.put("107","Rengar");
		champMap.put("92","Riven");
		champMap.put("68","Rumble");
		champMap.put("13","Ryze");
		champMap.put("113","Sejuani");
		champMap.put("235","Senna");
		champMap.put("875","Sett");
		champMap.put("35","Shaco");
		champMap.put("98","Shen");
		champMap.put("102","Shyvana");
		champMap.put("27","Singed");
		champMap.put("14","Sion");
		champMap.put("15","Sivir");
		champMap.put("72","Skarner");
		champMap.put("37","Sona");
		champMap.put("16","Soraka");
		champMap.put("50","Swain");
		champMap.put("517","Sylas");
		champMap.put("134","Syndra");
		champMap.put("223","TahmKench");
		champMap.put("163","Taliyah");
		champMap.put("91","Talon");
		champMap.put("44","Taric");
		champMap.put("17","Teemo");
		champMap.put("412","Thresh");
		champMap.put("18","Tristana");
		champMap.put("48","Trundle");
		champMap.put("23","Tryndamere");
		champMap.put("4","TwistedFate");
		champMap.put("29","Twitch");
		champMap.put("77","Udyr");
		champMap.put("6","Urgot");
		champMap.put("110","Varus");
		champMap.put("67","Vayne");
		champMap.put("45","Veigar");
		champMap.put("161","Velkoz");
		champMap.put("254","Vi");
		champMap.put("112","Viktor");
		champMap.put("8","Vladimir");
		champMap.put("106","Volibear");
		champMap.put("19","Warwick");
		champMap.put("498","Xayah");
		champMap.put("101","Xerath");
		champMap.put("5","XinZhao");
		champMap.put("157","Yasuo");
		champMap.put("83","Yorick");
		champMap.put("350","Yuumi");
		champMap.put("154","Zac");
		champMap.put("238","Zed");
		champMap.put("115","Ziggs");
		champMap.put("26","Zilean");
		champMap.put("142","Zoe");
		champMap.put("143","Zyra");
		
		return MapUtils.getString(champMap, champId, "");
	}
}
