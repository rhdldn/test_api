<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns:th="http://www.typeleaf.org">
<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>포니 랭크 조회 리스트</title>

  <!-- Bootstrap core CSS -->
  <link href="/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="/resources/css/shop-homepage.css" rel="stylesheet">

</head>

<body>

  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
      <a class="navbar-brand" href="#">포니 RANK LIST</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item active">
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

      <div class="col-lg-9" style="flex:0 0 90%">

        <div class="row" style="flex-wrap:inherit;">
		
		  <c:forEach var="list" varStatus="status" items="${ponyRankList}">
          <div class="col-lg-4 col-md-6 mb-4" style="margin-top: 1.5rem!important;">
            <div class="card h-100">
            	<h4 class="card-title">
                  <a href="javascript:champClick('${list.summId}');" style="font-size: 1.75rem;">${ list.nm }</a>
                </h4>
              <div class="card-body">
                <h5>솔로랭크</h5>
                <a href="#"><img class="card-img-top" style="height:17%;" src="/resources/img/${list.soloRkImg}.png" alt=""></a>
                <h5>${ list.soloRkTier } ${ list.soloRkRank } ${ list.soloRkPt }P</h5>
                <h3>${ list.soloRkWins }승 ${ list.soloRkLosses }패</h3>
                <br/>
                <h5>자유랭크</h5>
                <a href="#"><img class="card-img-top" style="height:17%;" src="/resources/img/${list.freeRkImg}.png" alt=""></a>
                <h5>${ list.freeRkTier } ${ list.freeRkRank } ${ list.freeRkPt }P</h5>
                <h3>${ list.freeRkWins }승 ${ list.freeRkLosses }패</h3>
                
                <br/>
                <h3 style="font-size: 1.00rem;">최근 10게임 선호 라인</h3>
                <div style="width:100%;">
                <c:forEach var="matchList" varStatus="status" items="${list.lastGameList}">
                	<div style="width:25%; float:left;">
                		<img class="card-img-top" src="https://ddragon.leagueoflegends.com/cdn/10.6.1/img/champion/${matchList.champNm}.png" alt="">
		                <c:if test="${matchList.matchDtlMap.winflag eq 'true'}">
		                	<img class="card-img-top" style="background-color: #007bff26;" src="/resources/img/${matchList.positionImg}.png" alt="">
		                	<div style="font-weight: 800; background-color: #007bff26; text-align: center;">${matchList.matchDtlMap.kills}/${matchList.matchDtlMap.deaths}/${matchList.matchDtlMap.assists}</div>
		                </c:if>
		                <c:if test="${matchList.matchDtlMap.winflag eq 'false'}">
		                	<img class="card-img-top" style="background-color: #ff000012;" src="/resources/img/${matchList.positionImg}.png" alt="">
		                	<div style="font-weight: 800; background-color: #ff000012; text-align: center;">${matchList.matchDtlMap.kills}/${matchList.matchDtlMap.deaths}/${matchList.matchDtlMap.assists}</div>
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
		
		console.log('${json}');
		console.log("탐");
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
	
</script>
</html>
