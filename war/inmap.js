var WW; //window width
var WH; //window height
var OldW= 826;	//倍率の基準となるwindow横幅
var Meter = 80;	//1mを座標変換したとき
var OMW;	//リサイズ前のスケール(メートルリサイズの基準)
var defM = 36.8;	//リサイズ前のm
var OWS = 1440;

var scale;	//リサイズ用
var mscale;

var inmap;
var layer;
var ctx;
var mapURI;

var xm=0,ym=0;	//物品の座標[m]

var mode=0; // 1:ベース位置変更
var isData = false;
var firstOpen = true;

var propo = 0.96;

//x=67で0m, +80で1m
//y=505で0m, +80
var IMESx=300;	var IMESy=300;
var BPx = 10; var BPy = 460;
//var BPx = 125; var BPy = 375;
var AP1x = 147;	var AP1y=425;
var AP2x = 400; var AP2y = 140;	//80で1m
var inAcc = 2;

var isAll = false;

window.onresize = function() {
	if(!firstOpen)	Initialize();
}

function checkValue(){
	//alert($("#can").height()+":val_1");
	//alert("called : 1");
	if( $("#g_name").val() != "no_name"){
		if( $("#g_x").val() != "no_x" ){
			if( $("#g_y").val() != "no_y"){
				if( $("#g_bx").val() != "no_bx"){
					if( $("#g_by").val() != "no_by"){
						if( $("#g_unitM").val() != "no_unitM"){
							if( $("#g_OWS").val() != "no_OWS"){
								if( $("#inmap").attr('src') != "inmap.png" ){
									if( $("#g_inAcc").val() != "no_inAcc"){
										//alert("called : 2");
										//name = $("#g_name").val();
										
										if( $("#g_x").val() != "showAll"){
											xm = parseFloat( $("#g_x").val() );
											ym = parseFloat( $("#g_y").val() );
											isAll = false;
										} else{
											isAll = true;
										}
										
										inAcc = parseFloat( $("#g_inAcc").val() );
										defM = parseFloat( $("#g_unitM").val() );
										OWS = parseFloat( $("#g_OWS").val() );
										BPx = parseFloat( $("#g_bx").val() )/propo;
										BPy = parseFloat( $("#g_by").val() )/propo;
										isData = true;

//										OMW = $(window).width();
										OMW = OWS;


										//Initialize();

										firstOpen = false;

									}
								}

							}
						}
					}
				}
			}
		}

	}
	if(!isData)
		setTimeout('checkValue()',100);
}

$(document).ready(function() {

	showLoader();

	//keyareaでkey値を受け渡す
	var valueStr = "";
	while(valueStr == ""){
		valueStr = window.location.search.substring(1);
		console.log(window.location.search.substring(1));
	}
	
	document.getElementById("keyarea").setAttribute("key", window.location.search.substring(1));
	
	/*
	var timer1 = setInterval( function() {	//googleMap読み込んだら、背面に移動
		if($(".gm-win-img").length >= 1 && $(".gm-win-img").prop("src")!=""){
			setTimeout(function(){
				$("#liMap").addClass("hide");
				$("#liMap").css("position","relative");
				$("#liMap").css("left","0");

//				console.log("Gmap Loaded");
				if(! isGmapLoad) setTimeout('drawFirst()',200);
				
				isGmapLoad = true;
				
				clearInterval(timer1);
			}, 500);
		}
	} ,100);*/
	/*
	var timer2 = setInterval( function() {	//画像を読み込んだら Initialize() -> draw()


	} ,100);*/


	if(!isData)
		checkValue();
	
	$("#inmap").load(function(){
		var timer = setInterval( function(){
			if(isData){
				Initialize();

				clearInterval(timer);
			}
		}, 50);
	});

	var dragX = 0;
	var dragY = 0;
	var dragFlag = false;

	var pTouchPitch = 0;

	var inmapFlag = true;

	var view = document.getElementById("viewArea");


	//クリックしたときのファンクションをまとめて指定
	$('.tab li').click(function() {

		//.index()を使いクリックされたタブが何番目かを調べ、
		//indexという変数に代入します。
		var index = $('.tab li').index(this);

		//コンテンツを一度すべて非表示にし、
		//$('.content li').css('display','none');

		$('.content li').css('visibility','hidden');

		//クリックされたタブと同じ順番のコンテンツを表示します。
		//$('.content li').eq(index).css('display','block');
		$('.content li').eq(index).css('visibility', "visible");
		//$('.content li').eq(index).css('position', "absolute");
		//$('.content li').eq(index).css('top', "0");

		$('.content li').eq(index).css('position', "absolute");
		$('.content li').eq(index).css('top', "0px");
		
//		$('.content li').css('height', $("#viewArea").height() + "px");
		
		//一度タブについているクラスselectを消し、
		$('.tab li').removeClass('select');

		//クリックされたタブのみにクラスselectをつけます。
		$(this).addClass('select');


			console.log(index);

		if(index == 0){
			$('.infowindow').css('visibility','visible');
		} else{
			$('.infowindow').css('visibility','hidden');
			console.log("b");
		}

		///inmapFlag = ! inmapFlag;

/*		if(inmapFlag)	Initialize();*/

	});
});

function getPitch(e){
	return Math.sqrt(Math.pow((e.originalEvent.touches[1].pageX - e.originalEvent.touches[0].pageX),2)+Math.pow((e.originalEvent.touches[1].pageY - e.originalEvent.touches[0].pageY),2));
}

function Initialize(){
	showLoader();
			console.log("Initialize");

	WW = $(window).width();
	WH = $(window).height();
	propo = 0.96;
//	propo = 1;
	Meter = defM * WW/OMW;

	Drawing();
	//Drawing();	//１回だと何故かCanvasサイズが小さい…

	var view = document.getElementById("viewArea");
	//view.style.left =String( $(window).width()/2 - $("#can").width()/2 ) + "px";
	//view.style.top =String( $(window).height()*0.3) + "px";	
}

function Drawing(){

	inmap = $("#inmap");
	layer = document.getElementById("can");

	//scale = $(window).width()/OldW*propo * $(window).width()/OWS;

	var a = $(window).width()*0.96;
	//var a =  $("#content").width();

	inmap.width(a*propo);

	layer.style.left = -1 * ($("#inmap").width() + 3 )+"px";

	layer.width = inmap.width();
	layer.height = inmap.height();

//console.log($("#inmap").width());

	//$("#can").height(inmap.height());


	a = $("#can").width();
	Meter = defM * a/OWS;
	scale = propo * a/OWS;


	//物品ﾚｲﾔ整形
	//$("#inmap").offset({top:200, left: $("#inmap").offset().left});
	//$("#can").offset({top:200, left: $("#inmap").offset().left});
	

//$("#can").offset( $("#inmap").offset().top, left: $("#inmap").offset().left});

	if ( ! layer || ! layer.getContext ) { return false; }
	ctx = layer.getContext("2d");
	
	ctx.clearRect(0, 0, $("#can").width(), $("#can").height());

	//BP
	//alert(BPx);
	var col = 'rgba(90, 90, 255, 0.9)';
	DrawPoint(BPx*scale, BPy*scale, col);
	//


	//draw grid
	var i=0;
	for(i=-1*layer.width*4; (Meter*i < layer.height*4 ) && (Meter*i < layer.width*4 ) ;i++){
		ctx.strokeStyle = "rgba(255,100,255,0.6)";
		ctx.lineWidth = 1;
		ctx.beginPath();
		ctx.moveTo(BPx*scale+i*Meter,0);
		ctx.lineTo(BPx*scale+i*Meter,WH + BPy + $("#can").height()*1.1 );
		ctx.closePath();
		ctx.stroke();
		ctx.beginPath();
			ctx.moveTo(0,BPy*scale-i*Meter);
		ctx.lineTo($("#can").width()*1.1 + WW, BPy*scale-i*Meter);
		ctx.closePath();
		ctx.stroke();
		//alert(i*Meter);
	}
	
	while( $("#urls").length == 0){
		console.log("cant get urls");
	}
	
	if(isAll){
		showAllItems();
	}
	else{
		var iconUrl = $("#urls").val();
		
		console.log("url:"+iconUrl);
		
		var posMeter = [xm, ym];
		ObjectDisplay(TrasnGrid(posMeter)[0], TrasnGrid(posMeter)[1], iconUrl, inAcc, $("#names").val());
	}
	
	$("#viewArea").width( $("#inmap").width() );

	if($("#can").height() >= $("#inmap").height() + 1){
		Initialize();
	}

	hideLoader();
}


function showAllItems(){
	var names = $("#names").val().split("@@@");
	var xs = $("#xs").val().split("@@@");
	var ys = $("#ys").val().split("@@@");
	var oAccs = $("#oAccs").val().split("@@@");
	var urls = $("#urls").val().split("@@@");
	
	console.log(urls);
	
	var i = 0;
	for(i=0; i<names.length-1; i++){
		var posMeter = [xs[i], ys[i]];
		
		console.log(urls[i]);
		
		ObjectDisplay(TrasnGrid(posMeter)[0], TrasnGrid(posMeter)[1], urls[i], oAccs[i], names[i]);
	}
}


function ObjectDisplay(x, y, url, oAcc, name){	//物品の描画
	//var ctx = layer.getContext('2d');
	ctx.beginPath();
	ctx.fillStyle = 'rgba(255, 20, 20, 1)';
	ctx.arc(x, y, 0.3*Meter, 0, Math.PI*2, false);
	ctx.fill();

	ctx.beginPath();
	ctx.fillStyle = 'rgba(192, 20, 20, 0.4)';
	ctx.arc(x, y, oAcc*Meter, 0, Math.PI*2, false);
	ctx.fill();
	
	/*
	ctx.font = "25px 'メイリオ'";
	ctx.fillStyle = "#eee";
	ctx.textAlign = "center";
	ctx.fillText(name, x, y+Meter);*/
	
	var image = new Image();
	image.src = url;
	
	image.onload = function() {
		/*
		ctx.fillStyle = 'rgba(240, 240, 240, 0.8)';
		ctx.fillRect(x-0.5*Meter,y-2*Meter,Meter,Meter);
		ctx.strokeStyle = 'rgba(70,70,70,0.8)';
		//ctx.rect(x-40,y-80-Meter-1,80,80+1);
		ctx.rect(x-0.5*Meter-1,y-2*Meter-1,Meter+1,Meter+2);
		ctx.stroke();
		//ctx.drawImage(image, x-40, y-80-Meter, 80, 80);
		ctx.drawImage(image, x-0.5*Meter, y-2*Meter, 1*Meter, 1*Meter);*/
		/*
		var htmlStr = "";
		htmlStr += "<div id='itemwindow'><h2>"+ name +"<h2><img src='"+url+"' />";
		htmlStr += "</div>";

		$(htmlStr).appendTo("body").trigger('create');
		
		$("#itemwindow").css("position", "absolute");
		$("#itemwindow").css("left",x + $("#viewArea").offset().left - $("#itemwindow").width()/2);
		$("#itemwindow").css("top", y + $("#viewArea").offset().top - $("#itemwindow").height()*1.1);*/

		ItemWindow(name, url, x, y);
	}
}

function DrawPoint(x,y,color){
	ctx.beginPath();
	ctx.fillStyle = color;
	ctx.arc(x, y, 0.3*Meter, 0, Math.PI*2, false);
	ctx.fill();
}

function TrasnGrid(pos){	//メートルを座標に
	var coodinater = [0,0];
	coodinater[0] = BPx*scale+(pos[0]*Meter);
	coodinater[1] = BPy*scale-(pos[1]*Meter);

	return coodinater;
}

function SetOneMeter(e){
	alert(e.clientX);
}

function ItemWindow(name, url, x, y) {

	var htmlStr = "";
	var iwid = "iw-" + name;

	if( $("#"+iwid).length>0 )	$("#"+iwid).remove();

	htmlStr += "<div id="+ iwid +" class='infowindow'><h2>"+ name +"</h2><img src='"+url+"' />";
	htmlStr += "</div>";

	$(htmlStr).appendTo("body").trigger('create');

	$("#"+iwid).css("position", "absolute");
	$("#"+iwid).css("left",x + $("#viewArea").offset().left - $("#"+iwid).width()/2);
	$("#"+iwid).css("top", y + $("#viewArea").offset().top - $("#"+iwid).height()*1.1);
};

function hideLoader(){
	$("#loading").css("display","none");
}

function showLoader(){
	$("#loading").css("display","block");
	$("#loading").css("left", ( $(window).width()-$("#loading").width() )/2);
}