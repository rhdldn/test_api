<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns:th="http://www.typeleaf.org">
<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>사용자 전적 검색</title>

  <!-- Bootstrap core CSS -->
  <link href="/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="/resources/css/shop-homepage.css" rel="stylesheet">

</head>

<body>

  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
      <a class="navbar-brand" href="#">사용자 전적 검색</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item active">
            <a class="nav-link" id="userLink" href="#">사용자 전적 검색</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" id="ponyLink" href="#">포니 랭크 리스트</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" id="enemyLink" href="#">상대 랭크 리스트</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <!-- Page Content -->
  <div class="container" style="margin-left:inherit;">

    <div class="row">

      <!-- <div class="col-lg-3">

        <h1 class="my-4">Shop Name</h1>
        <div class="list-group">
          <a href="#" class="list-group-item">Category 1</a>
          <a href="#" class="list-group-item">Category 2</a>
          <a href="#" class="list-group-item">Category 3</a>
        </div>

      </div> -->
      <!-- /.col-lg-3 -->
		
	  <div>
	  	<input type="text" id="userId"><button onclick="javascript:searchUserSts()">조회</button>
	  </div>
      <div class="col-lg-9" style="flex:0 0 90%">

        <div class="row" style="flex-wrap:inherit;">
		
		  <c:forEach var="list" varStatus="status" items="${userList}">
          <div id="userArea" class="col-lg-4 col-md-6 mb-4" style="margin-top: 1.5rem!important;">
            <div class="card h-100">
            	<h4 class="card-title">
                  <a href="javascript:champClick('${list.userMap.EUC_ID}');" style="font-size: 1.75rem;">${list.soloRankMap.USER_GAME_ID}</a>
                </h4>
                <button onclick="javascript:stsUpdClick('${list.soloRankMap.USER_GAME_ID}')">전적 갱신
                <c:if test="${list.soloRankMap.DIFF_DAY > 0}">
                ${list.soloRankMap.DIFF_DAY}일전
                </c:if> 
                <c:if test="${list.soloRankMap.DIFF_DAY == 0}">
					<c:if test="${list.soloRankMap.DIFF_HOUR == 0}">
						${list.soloRankMap.DIFF_MIN}분전
					</c:if>
					<c:if test="${list.soloRankMap.DIFF_HOUR > 0}">
					${list.soloRankMap.DIFF_HOUR}시간전
					</c:if>
                </c:if>
                </button>
              <div class="card-body">
              	<div style="display: flex; text-align: center;"> 
                <div style="font-size: 18px; font-weight: 900; width: 100%">솔로랭크</div>
                <div style="font-size: 18px; font-weight: 900; width: 100%">자유랭크</div>
                </div>
                <div style="display: flex;">
                <c:choose>
                <c:when test="${list.soloRankMap.TIER_IMG ne null}">
                	<a href="#"><img class="card-img-top" style="height:100%; width: 100%" src="/resources/img/${list.soloRankMap.TIER_IMG}.png" alt=""></a>
                </c:when>
                <c:otherwise>
                	<a href="#"><img class="card-img-top" style="height:100%; width: 100%" src="/resources/img/none.png" alt=""></a>
                </c:otherwise>
                </c:choose>
                <c:choose>
                <c:when test="${list.freeRankMap.TIER_IMG ne null}">
                	<a href="#"><img class="card-img-top" style="height:100%; width: 100%" src="/resources/img/${list.freeRankMap.TIER_IMG}.png" alt=""></a>
                </c:when>
                <c:otherwise>
                <a href="#"><img class="card-img-top" style="height:100%; width: 100%" src="/resources/img/none.png" alt=""></a>
                </c:otherwise>
                </c:choose>
                </div>
                <div style="display: flex; font-size: 19px; font-weight:800; text-align: center;">
                <div style="width: 100%">${list.soloRankMap.TIER } ${list.soloRankMap.RANK_LVL }</div>
                <div style="width: 100%">${list.freeRankMap.TIER } ${list.freeRankMap.RANK_LVL }</div>
                </div>
                <div style="display: flex; font-size: 30px; font-weight:800; text-align: center;">
                <div style="width: 100%">${list.soloRankMap.LEAGUE_PT }P</div>
                <div style="width: 100%">${list.freeRankMap.LEAGUE_PT }P</div>
                </div>
                <br/>
                <div style="display: flex; font-size: 17px; font-weight:700; text-align: center;">
                	<div style="width: 100%">${list.soloRankMap.WINS_CNT }승 ${list.soloRankMap.LOSSES_CNT }패</div>
                	<div style="width: 100%">${list.freeRankMap.WINS_CNT }승 ${list.freeRankMap.LOSSES_CNT }패</div>
                </div>
                <br/>
                <h3 style="font-size: 17px; font-weight: 600;">최근 10게임 선호 라인</h3>
                <div style="width:100%;">
                <c:forEach var="matchList" varStatus="status" items="${list.lastGameList}">
                	<div style="width:25%; float:left;">
                		<img class="card-img-top" src="https://ddragon.leagueoflegends.com/cdn/10.6.1/img/champion/${matchList.CHAMPION_ENG_NAME}.png" alt="">
		                <c:if test="${matchList.WIN_FLAG eq 'true'}">
		                	<img class="card-img-top" style="background-color: #007bff26;" src="/resources/img/${matchList.POSITION_IMG}.png" alt="">
		                	<div style="font-weight: 800; font-size:12px; background-color: #007bff26; text-align: center;">${matchList.KILLS}/${matchList.DEATHS}/${matchList.ASSISTS}</div>
		                </c:if>
		                <c:if test="${matchList.WIN_FLAG eq 'false'}">
		                	<img class="card-img-top" style="background-color: #ff000012;" src="/resources/img/${matchList.POSITION_IMG}.png" alt="">
		                	<div style="font-weight: 800; font-size:12px; background-color: #ff000012; text-align: center;">${matchList.KILLS}/${matchList.DEATHS}/${matchList.ASSISTS}</div>
		                </c:if>
        	        </div>
                </c:forEach>
                </div>
              </div>        
            </div>
          </div>
		  </c:forEach>
			
        </div>
        <!-- /.row -->

      </div>
      <!-- /.col-lg-9 -->

    </div>
    <!-- /.row -->

  </div>
  <!-- /.container -->

  <!-- Footer -->
  <footer class="py-5 bg-dark">
    <div class="container">
      <p class="m-0 text-center text-white">Copyright &copy; 고이우 2020</p>
    </div>
    <!-- /.container -->
  </footer>

  <!-- Bootstrap core JavaScript -->
  <script src="/resources/vendor/jquery/jquery.min.js"></script>
  <script src="/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

</body>
<script type="text/javascript">
	
	$(document).ready(function(){
		var userList = '${userList}';
		console.log("여기탐0");
		if(userList == null || userList.length == 0){  
			console.log("여기탐1");
			$("#userArea").hide();
		}
		console.log("여기탐2");
	});
	
	$("#userLink").click(function(){
		
		location.href = "/lolapi/getUserList.do";
	});
	
	$("#ponyLink").click(function(){
		
		location.href = "/lolapi/getPonyList.do";
	});
	
	$("#enemyLink").click(function(){
		
		location.href = "/lolapi/getEnemyList.do";
	});
	
	function champClick(summId){
		
		location.href = "/lolapi/getChampList.do?summId="+ summId;
	}
	
	function stsUpdClick(userId){
		location.href = "/lolapi/getUserHs.do?userId="+ userId;
	}
	
	function searchUserSts(){ 
		var userId = $("#userId").val();
		location.href = "/lolapi/getUserList.do?userId="+ encodeURIComponent(userId);
	}
	
</script>
</html>
