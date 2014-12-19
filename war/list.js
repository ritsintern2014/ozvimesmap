var tabFlag = false;
var ang = 0; 


var dragX = 0;
var dragY = 0;
var set = "";

$(document).ready(function(){

	$("#tabBtn").click(function(){
		var tabLeft ="0%";

		if(tabFlag){	//今からひっこむ
			tabLeft = "85%";

			//tabLeft = -1 * ( $("#header").width() - $("#tabBtn").width()*1.5 );
			tabLeft = set;
			
			//ang = 180;
			
			ang = 0;
		} else {	//今から飛び出す
			tabLeft = "20%";

			tabLeft = "5rem";
			//ang = 0;
			ang = 180;
			
			$("#add_area").css("zIndex",1);
		}

		$("#header").animate({
			"left": tabLeft
		}, 1000);

		$('#tabBtn').animate({ 
			zIndex : 4
		},
		{
			//1秒かけてアニメーション
			duration:1000,
			//stepは、アニメーションが進むたびに呼ばれる
			step:function(now){
				//nowに現在のz-indexの値（0から1に変化しているところ）が渡してもらえる
				//0から1に向かって変化していくnowを利用して回転
				$("#tabBtn").css({transform:'rotate( -' + ((now-4) * 180 + ang) + 'deg )'});
			},
			//終わったら
			complete:function(){
				//次のために、元に戻しておく
				$('#tabBtn').css('zIndex', 5);
				
				if(tabFlag)	$("#add_area").css("zIndex",11);
				
				tabFlag = !tabFlag;
			}
		});
	});

	/*
	var dragX = 0;
	var dragY = 0;
	var propo = 0.49;

	var pTouchPitch = 0;
	var tableArea = document.getElementById('htmltable');

	$('body').bind({
		// フリック開始時
		'touchstart': function(e) {
			dragX = e.originalEvent.touches[0].pageX;
			dragY = e.originalEvent.touches[0].pageY;

			if(e.originalEvent.touches.length > 1){
				pTouchPitch = getPitch(e);
			} else{
				return;
			}
		},
		// フリック中 
		'touchmove': function(e) {
			e.preventDefault();

			var areaTop = $("#htmltable").offset().top;
			var areaLeft = $("#htmltable").offset().left;

			var paX = e.originalEvent.touches[0].pageX;
			var paY = e.originalEvent.touches[0].pageY;

//			tableArea.style.top = String( areaTop + ( paY  -  dragY ) ) + "px";
//			tableArea.style.left = String( areaLeft + ( paX - dragX ) ) + "px";

//			console.log(tableArea.style.left);

			dragX = paX
			dragY = paY;

			if(e.originalEvent.touches.length > 1){
				e.preventDefault();

				var gScale = (getPitch(e) / pTouchPitch)*0.05 + 0.95;				

				if( ( propo < 2 && gScale > 1 ) || ( propo > 0.3 && gScale < 1 ) ){

					propo *= gScale;
					var tmpScale = propo * 100;
					var strScale = "" + tmpScale.toString() + "%";

					var dLeft = ( $("#htmltable").width() * gScale - $("#htmltable").width() ) / 2;
					var dUp = ( $("#htmltable").height() * gScale - $("#htmltable").height() ) / 2;

					tableArea.style.top = String( areaTop - dUp  )  + "px";
					tableArea.style.left = String( areaLeft - dLeft ) + "px";


					$("#main").css('zoom',strScale);
					console.log(strScale);
				}
			}
		}
	});*/
});

window.onload = function(){
	//var set = -1 * parseInt( $("#header").width() - $("#tabBtn").width()*1.5 );
	set = "-31.5rem";
	$('#tabBtn').css('zIndex', 5);

	$("#header").offset({top:"3%", left: set });
	//$("#main").css('zoom','49%');
	//$("#main").css('marginLeft',  $("#tabBtn").width()*2);
	$("#main").css('marginLeft',  "8rem");
	
	//全部読み込んだら、表示ないものを消す
	var timer1 = setInterval(function(){
		if($("#htmltable button").length > 0) {
			disp();
			clearInterval(timer1);
		}
	}, 100);
}

function getPitch(e){
	return Math.sqrt(Math.pow((e.originalEvent.touches[1].pageX - e.originalEvent.touches[0].pageX),2)+Math.pow((e.originalEvent.touches[1].pageY - e.originalEvent.touches[0].pageY),2));
}

var showF = false;
function disp(){

	
	var isChecked = new Array();
	isChecked = GetCookies();
	//if(isChecked.length != 0){
	/*		document.getElementById("picDisp").checked = Boolean(isChecked["picDisp"]);
		document.getElementById("upDisp").checked = Boolean(isChecked["upDisp"]);
		document.getElementById("delDisp").checked = Boolean(isChecked["delDisp"]);
		document.getElementById("qrDisp").checked = false;
	 */
	//alert( Boolean(isChecked["delDisp"]));


	var isD1s = "";
	var isD2s = "";
	var isD3s = "";
	var isD4s = "";
	var isD5s = "";
	var isD6s = "";
	var isD7s = "";
	var isD8s = "";
	if(isChecked["picDisp"] == "true")	isD1s = "checked";
	else{	isD1s = "";}
	if(isChecked["upDisp"] == "true")	isD2s = "checked";
	else{	isD2s = "";}
	if(isChecked["delDisp"] == "true")	isD3s = "checked";
	else{	isD3s = "";}
	if(isChecked["qrDisp"] == "true")	isD4s = "checked";
	else{	isD4s = "";}
	if(isChecked["desDisp"] == "true")	isD5s = "checked";
	else{	isD5s = "";}
	if(isChecked["batDisp"] == "true")	isD6s = "checked";
	else{	isD6s = "";}
	if(isChecked["debugDisp"] == "true")	isD7s = "checked";
	else{	isD7s = "";}
	if(isChecked["minDisp"] == "true")	isD8s = "checked";
	else{	isD8s = "";}

	$("#picDisp").prop('checked', isD1s);
	$("#upDisp").prop('checked', isD2s);
	$("#delDisp").prop('checked', isD3s);
	$("#qrDisp").prop('checked', isD4s);
	$("#desDisp").prop('checked', isD5s);
	$("#batDisp").prop('checked', isD6s);
	$("#debugDisp").prop('checked', isD7s);
	$("#minDisp").prop('checked', isD8s);
	/*
		console.log(isChecked["picDisp"]);
		console.log(isChecked["upDisp"]);
		console.log(isChecked["delDisp"]);
		console.log(isChecked["qrDisp"]);*/

	checkDisp("picDisp",".tbicon");
	checkDisp("upDisp",".regi");
	checkDisp("delDisp",".del");
	checkDisp("qrDisp",".qr");
	checkDisp("desDisp",".destination");
	checkDisp("batDisp",".battery");
	checkDisp("debugDisp",".debug");
	checkDisp("minDisp",".min");
}

function checkDisp(boxid,classname){
	var isCheck = $("#"+boxid).prop('checked');
	
	if(classname==".min"){
		if(isCheck){
			$('#htmltable img').css("max-height","5rem");
			$('#htmltable button').css("max-height","5rem");
			$('#htmltable button').css("max-width","5rem");
			$('.qrcode').css("max-height","5rem");
			$('.qrcode').css("max-width","5rem");
			$('.upbtn').css("font-size","0");
			$('.tableindex td').css("font-size","2rem");
			
			
			$('#htmltable img').css("min-height","5rem");
			$('#htmltable button').css("min-height","5rem");
			$('#htmltable button').css("min-width","5rem");
			
			$('#htmltable button').css("height","5rem");
			$('#htmltable button').css("width","5rem");
			
			$('.qrcode').css("min-width","5rem");
			$('.qrcode').css("min-height","5rem");

//			$('#htmltable .btn1').css("font-size","1.5rem");
			
			$("#" + boxid).prop('checked','checked');
		} else{
			$('#htmltable img').css("max-height","");
			$('#htmltable button').css("max-height","12rem");
			$('#htmltable button').css("max-width","12rem");
			$('.qrcode').css("max-height","12rem");
			$('.qrcode').css("max-width","12rem");
			$('.upbtn').css("font-size","2.5rem");
			$('.tableindex td').css("font-size","3rem");

//			$('#htmltable button').css("font-size","3rem");
			
			$('#htmltable img').css("max-height","12rem");
			$('#htmltable button').css("max-height","12rem");
			$('#htmltable button').css("max-width","12rem");

			$('#htmltable button').css("height","12rem");
			$('#htmltable button').css("width","12rem");
			
			$('.qrcode').css("min-width","12rem");
			$('.qrcode').css("min-height","12rem");
			
			$("#" + boxid).prop('checked', false);
		}
		var str = boxid + '=' + isCheck;
		document.cookie = str;
	//	console.log(document.cookie);
		return;
	}
	if(isCheck){
		$(classname).css("display","");
		$("#" + boxid).prop('checked','checked');
//		console.log("true:"+classname);
	} else{
		$(classname).css("display","none");
		$("#" + boxid).prop('checked', false);
//		console.log("false:"+classname);
	}
	var str = boxid + '=' + isCheck;
	document.cookie = str;
	//console.log(document.cookie);
}

function GetCookies(){
	var result = new Array();

	var allcookies = document.cookie;
	//console.log(document.cookie);
	if( allcookies != '' ){
		var cookies = allcookies.split( '; ' );

		for( var i = 0; i < cookies.length; i++ ) {
			var cookie = cookies[ i ].split( '=' );

			// クッキーの名前をキーとして 配列に追加する
			result[ cookie[ 0 ] ] = decodeURIComponent( cookie[ 1 ] );
		}
	} else{
		result["picDisp"] = "true";
		result["upDisp"] = "true";
		result["delDisp"] = "true";
		result["qrDisp"] = "false";
		result["desDisp"] = "true";
		result["batDisp"] = "false";
		result["debugDisp"] = "false";
		result["minDisp"] = "false";
	}

	return result;
}

var isFirstHide = true;
function hideLoader(){
	$("#loading").css("display","none");

	if(isFirstHide){
		$(".tbicon img").click(function (event){
			if(showF == false){
				var picL = $(event.target).prop("src");	//画像のURL
				var htmlStr = "<div class='pic-l'><img src='"+ picL +"' /></div>";
				$(htmlStr).appendTo("body");

				$(".pic-l img").click(function (event){
					$(".pic-l").remove();
					showF = false;
				});

				showF = true;
			}	else{
				$(".pic-l").remove();
				showF = false;
			}
		});

		isFirstHide = false;
	}
}

function showLoader(){
	$("#loading").css("display","block");
	$("#loading").css("left", ( $(window).width()-$("#loading").width() )/2);
}