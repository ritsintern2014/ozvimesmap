var WW; //window width
var WH; //window height
var OldW= 826;	//倍率の基準となるwindow横幅
var Meter = 80;	//1mを座標変換したとき
var OMW;	//リサイズ前のスケール(メートルリサイズの基準)
var defM = 36.8;	//リサイズ前のm

var scale;	//リサイズ用
var mscale;

var inmap;
var layer;
var ctx;
var mapURI = "inmap.png";

var mode=-1; // 1:ベース位置変更
var isData = false;
var firstOpen = true;

var BPx = 125; var BPy = 375;
var firstMake = true;

window.onresize = function() {
	if(!firstOpen)	Initialize();
}

function changeEvent() {

	console.log("not found input[file]");
	if( $("input[type=file]").length >= 1 ){
		$('input[type=file]').change(function() {
			var file = $(this).prop('files')[0];
			// 画像表示
			var reader = new FileReader();
			reader.onload = function() {
				var img_src = $("#inmap").attr('src', reader.result);
				//$('#mapCanvas').css('zIndex', 3);
				$("#onChangeMap").val("true");
				Initialize();
				
				mode=1;
				
				//alert("②基準点の場所をクリックしてください。");
				showMessageBox("②基準点の場所をクリックしてください。");
			}
			reader.readAsDataURL(file);
		});	
	} else{
		setTimeout('changeEvent()',100);
	}
}

function CalledJava(){
	console.log("cant get map");
	if($('#JavaRead').val() == "true"){
		console.log("_js_");
		
		BPx = $("#BPx").val();
		BPy = $("#BPy").val();
		Meter = $("#unitM").val();
		OWS = $("#OWS").val();
		

		$("#onChangeMap").val("false");
/*
	var image = new Image();
	image.src = $("#inmap").prop("src");
	
//	image.onload = function() {


		image.onload = function() {
			console.log("loaded map");*/
			mode = 1;
			firstMake = false;
			showMessageBox("①地図を変更する場合は、<br>　画面左上のオレンジのボタンを押して地図を選択。<br>　地図を変更しない場合は<br>　基準点の場所をクリックして下さい。");
			Initialize();
//		}

	} else {
		setTimeout('CalledJava()',100);
	}
}


var fx,fy,lx,ly;
var cf = false;	//クリックフラグ
var isFirstBase = true;
var onCanDown = false;

$(document).ready(function(){
	/*
	$('#JavaRead').change(function() {
		CalledJava();
		alert("b");
	})*/;
	
	changeEvent();
	//マップデータがあれば表示する -> webプログラミングのルールで，formに勝手に指定できない
	CalledJava();
	
	//keyareaでkey値を受け渡す
//	document.getElementById("areaKey").setAttribute("value", window.location.search.substring(1));
	$("#areaKey").val( window.location.search.substring(1) );
	
	OMW = $(window).width();

	/* 後で消す！！！ */
	defM *= $(window).width()/(OldW*2);

	//Initialize();

	$("#modeBtn").click(function () {
		if(mode==0){
			$("#modeBtn").html("縮尺を設定する");
			showMessageBox("②基準点の場所をクリックしてください。");
			mode = 1;
		}
		else if(mode==1){
			$("#modeBtn").html("基準点を移動する");
			showMessageBox("③縮尺を決めます。地図上をドラッグしてください。<br>　基準点を修正する場合は、画面左上の<br>　「基準点を移動する」をクリックしてください。");
			mode=0;
		}
	});

	$("#meterBtn").click(function(){
		Meter = Math.abs(fx-lx) / $("#nummeter").val();
		Drawing();
		
		showMessageBox("⑤これでよければ画面左上の送信ボタンを押してください。");
	})


	$("#can").mousemove(function (event){
		if(mode==1){
			var rect = event.target.getBoundingClientRect();

			BPx = (event.clientX - rect.left)/scale;///2/0.96;
			BPy = (event.clientY - rect.top)/scale;///2/0.96;

			Drawing();
		}
	});

	$("#can").mousedown(function (event){
		if(mode==0){
			var rect = event.target.getBoundingClientRect() ;
			fx = event.clientX - rect.left;
			fy = event.clientY - rect.top;

			cf = true;
		}
		if(mode==1)	onCanDown=true;
	});

	$("#can").mousemove(function (event){
		if(cf){
			lx = event.clientX-$("#can").offset().left;	//Clickしたcanvasの座標
			ly = event.clientY-$("#can").offset().top;
			ctx.strokeStyle = "red";
			ctx.lineWidth = 10;
			ctx.clearRect(0,0,mapW,mapH);
			ctx.beginPath();
			ctx.moveTo(fx,fy)
			ctx.lineTo(lx,fy);
			ctx.closePath();
			ctx.stroke();
		}
		//alert(Math.abs(fx-lx) + "\n" + Math.abs(fy-ly));
	});

	$("#can").mouseup(function (event){
		if(cf){
			cf=false;
			Meter = Math.abs(fx-lx) / $("#nummeter").val();//*scale;
			if(Meter<2){	//重描画回避
				Meter=2;
				showMessageBox("細かすぎるため、<br>　グリッドを正しく表示できません。");
			} else{
				defM=Meter;
				//Drawing();
				
				popMeter();
			}
		}
		
		else if(mode==1 && onCanDown){
			$("#modeBtn").html("基準点を移動する");

			$("#BPx").val(BPx);
			$("#BPy").val(BPy);
			$("#unitM").val(Meter);
			$("#OWS").val($("#can").width());
			/* マップを選んだ直後にこいつが出てくる。マップ選択をダブルクリックで行うと出てくるっぽい？ */
			showMessageBox("③縮尺を決めます。地図上をドラッグしてください。<br>　基準点を修正する場合は、画面左上の<br>　「基準点を移動する」をクリックしてください。");
			onCanDown=false;
			mode=0;
			Drawing();
		}
	});


	//スマホ用
	$('#can').bind({
		/* フリック開始時 */
		'touchstart': function(event) {
			if(mode==1){
				
				var rect = event.target.getBoundingClientRect();

				BPx = (event.originalEvent.touches[0].pageX - $("#can").offset().left)/scale;///2/0.96;
				BPy = (event.originalEvent.touches[0].pageY - $("#can").offset().top)/scale;///2/0.96;

//				Drawing();
				
/*

*/				
//				Drawing();
			}

			else{
				var rect = event.target.getBoundingClientRect() ;
				fx =  event.originalEvent.touches[0].pageX - $("#can").offset().left;
				fy =  event.originalEvent.touches[0].pageY - $("#can").offset().top;

				cf = true;
			}
		},
		/* フリック中 */
		'touchmove': function(event) {
			event.preventDefault();

			if(cf && mode == 0){
				lx = event.originalEvent.touches[0].pageX - $("#can").offset().left;	//Clickしたcanvasの座標
				ly = event.originalEvent.touches[0].pageY - $("#can").offset().top;
				ctx.strokeStyle = "red";
				ctx.lineWidth = 10;
				ctx.clearRect(0,0,mapW,mapH);
				ctx.beginPath();
				ctx.moveTo(fx,fy)
				ctx.lineTo(lx,fy);
				ctx.closePath();
				ctx.stroke();
			}
			
			else if(mode == 1){
				var rect = event.target.getBoundingClientRect();

				BPx = (event.originalEvent.touches[0].pageX - $("#can").offset().left)/scale;///2/0.96;
				BPy = (event.originalEvent.touches[0].pageY - $("#can").offset().top)/scale;///2/0.96;

				Drawing();
			}
		},
		'touchend': function(event) {
			if(cf){
				cf=false;
				Meter = Math.abs(fx-lx) / $("#nummeter").val();//*scale;
				if(Meter<2){	//重描画回避
					Meter = 2;
					showMessageBox("細かすぎるため、<br>グリッドを正しく表示できません。");
				}	else {
					defM=Meter;
					//Drawing();
					
					popMeter();
				}
			}
			
			else if(mode==1){
				$("#modeBtn").html("基準点を移動する");
				
				$("#BPx").val(BPx);
				$("#BPy").val(BPy);
				$("#unitM").val(Meter);
				$("#OWS").val($("#can").width());
				
				mode=0;
				
				showMessageBox("③縮尺を決めます。地図上をドラッグしてください。<br>　基準点を修正する場合は、画面左上の<br>　「基準点を移動する」をクリックしてください。");
				
				$("#BPx").val(BPx);
				$("#BPy").val(BPy);
				$("#unitM").val(Meter);
				$("#OWS").val($("#can").width());
				
				Drawing();
			}
		}
	});
	
	$('#nummeter').change(function (){
		Meter = Math.abs(fx-lx) / $("#nummeter").val();
		Drawing();
	})
	
	if(firstMake)	showMessageBox("①画面左上のオレンジのボタンを押して、<br>　地図を選択してください。");
	else {
		showMessageBox("①地図を変更する場合は、<br>画面左上のオレンジのボタンを押して地図を選択<br>地図を変更しない場合は基準点の場所を<br>　入力して下さい。");
	}

});

function popMeter(){
	//$("#inputMeter").offset({top: lx-50, left:ly-50});
	
	var rect = $("#can").offset();
	
	var inArea = document.getElementById("inputMeter");
	inArea.style.left = rect.left + (fx+lx)/2 - $("#inputMeter").width()/2 + "px";
	inArea.style.top = rect.top + fy - $("#inputMeter").height() - 5 + "px";
	
	showMessageBox("④ドラッグした線の長さを記入してください。<br>　線は何度でも引き直せます。");
	
	//alert(rect.left +":"+ rect.top + "\n" + fx +":"+ fy + "\n" + lx +":"+ ly + "\n")
}

function Initialize(){
	WW = $(window).width();
	WH = $(window).height();
	Meter = defM * WW/OMW;

	Drawing();
	Drawing();	//１回だと何故かCanvasサイズが小さい…
}

var sca = 0.96;
sca = 0.8;

function Drawing(){
	inmap = $("#inmap");
	layer = document.getElementById("can");

	scale = $(window).width()/OldW*sca;
	mapW = inmap.width();
	mapH = inmap.height();
	inmap.width($(window).width()*sca);

	layer.width = inmap.width();
	layer.height = inmap.height();

	//$("#can").offset({top:$("#inmap").offset().top, left:0});
	//$("#can").width(inmap.width());
	$("#can").height(inmap.height());

	//物品ﾚｲﾔ整形
	//$("#inmap").offset({top:200, left: $("#inmap").offset().left });
	//$("#can").offset({top:200, left: $("#inmap").offset().left });


	if ( ! layer || ! layer.getContext ) { return false; }
	ctx = layer.getContext("2d");

	//BP
	var col = 'rgba(90, 90, 255, 0.9)';
	DrawPoint(BPx*scale, BPy*scale, col);

//	draw grid
	if(mode==1){	//縮尺決定中は一本だけ
		ctx.strokeStyle = "rgba(255,0,255,0.6)";
		ctx.lineWidth = 1;
		ctx.beginPath();
		ctx.moveTo(BPx*scale,0);
		ctx.lineTo(BPx*scale,$("#can").height());
		ctx.closePath();
		ctx.stroke();
		ctx.beginPath();
		ctx.moveTo(0,BPy*scale);
		ctx.lineTo($("#can").width(),BPy*scale);
		ctx.closePath();
		ctx.stroke();
	}
	
	else{
		var i=0;
		for(i=-1*$(window).width();Meter*i<$(window).width();i++){
			ctx.strokeStyle = "rgba(255,0,255,0.6)";
			ctx.lineWidth = 1;
			ctx.beginPath();
			ctx.moveTo(BPx*scale+i*Meter,0);
			ctx.lineTo(BPx*scale+i*Meter,$("#can").height());
			ctx.closePath();
			ctx.stroke();
			ctx.beginPath();
			ctx.moveTo(0,BPy*scale-i*Meter);
			ctx.lineTo($("#can").width(),BPy*scale-i*Meter);
			ctx.closePath();
			ctx.stroke();
			//alert(i*Meter);
		}
	}
	
	$("#BPx").val(BPx*scale);
	$("#BPy").val(BPy*scale);
	$("#unitM").val(Meter);
	$("#OWS").val($("#can").width());
}

function DrawPoint(x,y,color){
	ctx.beginPath();
	ctx.fillStyle = color;
	var r = 8;
	if(mode==1){
		r = 50;
	}
	
	ctx.arc(x, y, r*scale, 0, Math.PI*2, false);
	ctx.fill();
}

function TrasnGrid(pos){	//メートルを座標に
	var coodinater = [0,0];
	coodinater[0] = BPx*scale+(pos[0]*Meter);
	coodinater[1] = BPy*scale-(pos[1]*Meter);

	return coodinater;
}

function showMessageBox(mes){	
	var htmlStr = "";
	htmlStr += "<div id='mesbox'>";
	htmlStr += mes;
	htmlStr += "<button id='closebtn' onClick='closeMessageBox()' >×</button>"
	htmlStr += "</div>";

	if($('#mesbox').length > 0){
		htmlStr = mes;
		htmlStr += "<button id='closebtn' onClick='closeMessageBox()' >×</button>"
		$('#mesbox').html(htmlStr);
	}

	else{
		$(htmlStr).appendTo("body").trigger('create');

		$("#mesbox").css("position", "absolute");
		$("#mesbox").css("left", $(window).width/2 - $("#mesbox").width()/2);
		$("#mesbox").css("top", $(window).height/2 - $("#mesbox").height()/2);
	}
}

function closeMessageBox(){
	$('#mesbox').remove();
}