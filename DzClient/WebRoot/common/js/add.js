
var money=0;
var shop_name='';
var number=0;
var num=1;
var biaoqian='';
$(function () {
	    $(".subFly").hide();
	    $(".close").click(function(){
	        $(".subFly").hide();
	        $("#subChose").html('');
	    });
	    $(".up").click(function(){
	        $(".subFly").hide();
	    });

    $(".ad").click(function () {
        var n = parseFloat($(this).prev().text())+1;
           if (n == 0) { return; }
        $(this).prev().text(n);
        var danjia = $(this).next().text();//获取单价
         money=(money*1+danjia * 1).toFixed(2);//计算当前所选总价
         num=num+1;
          
    });
	    
	    //减的效果
	    $(".ms").click(function () {
	        var n = $(this).next().text();
			console.log(n);
	        if(n>1){
	            var num = parseFloat(n) - 1;
	            $(this).next().text(num);//减1
	            num=num-1;
	        }
	    });  

	    $(".up1").click(function(){
	        $(".up1").hide();
	        $(".shopcart-list.fold-transition").hide();
	    })
	    $(".empty").click(function(){
	        $(".list-content>ul").html("");
	        $("#totalcountshow").text("0");
	        $("#totalpriceshow").text("0");
	        $(".minus").next().text("0");
	        $(".minus").hide();
	        $(".minus").next().hide();
	        $(".shopcart-list").hide();
	        $(".up1").hide();
	        $(".disable").css("background-color","#818181");
	    });
	    
	      $(document).on('click','.ms2',function(){
	            var a = parseInt($(".ad2").next().text());
	            console.log(a);
	            var n = parseInt($(this).next().text())-1;
	           console.log(n);
	           
	            if (n == 0) {
	                $(this).parent().parent().remove();
	                $(".up1").hide()
	                
	            }
	            parseFloat(money)-
	            $(this).next().text(n);
	            if(money==0){
	            	$(".shopcart-list").hide();
	            }

	        });
	      	
	     	$("#jiesuan").click(function(){
	     		valLink('takeOutSub',$.cookie('cid'));
	     	});
		
})

function wab(x,i){
	$('#bna'+x+i).addClass("m-active").siblings().removeClass("m-active")
    $(".choseValue").text($(".subChose .m-active").text());
}
function wbf(x,i){
	$('#nba'+x+i).addClass("m-active").siblings().removeClass("m-active")
    $(".choseValue").text($(".subChose .m-active").text());
}

 //弹购物车出来   
 function addCart(name,price,src,shu,natureContent,goods_id) {
	 $.cookie('goods_id', goods_id);
	var arr = natureContent.split("@");
	for(var i=0;i<arr.length;i++){
		  if(arr[i]!=""){
			  var pan=arr[i].split("#");
			   var html='<dl class="subChose" ><dt>'+pan[0]+'</dt>';
		      var hang=pan[1].split(',');
		      for(var x=0;x<hang.length;x++){
		    	if(hang[x]!=""){
		    		
			    		if(x==0){
			    			html+='<dd class="m-active" onclick="wbf('+x+','+i+');" id="nba'+x+i+'">'+hang[x]+'</dd>';
			    		}else{
			    			html+='<dd onclick="wab('+x+','+i+');" id="bna'+x+i+'">'+hang[x]+'</dd>';
			    		}
			    		if(x==hang.length-1){
			    			html+='</dl>';
			    		}
		    	}
		    	
		    }
		      
		    $("#subChose").append(html);
		  }
	}
    	 $(".subFly").show();
    	 		
    	        var n = $(this).prev().text();
    	        var num;
    	        if(n==0){
    	            num =1
    	        }else{
    	            num = parseFloat(n);
    	        }
    	        number=shu;
    	        $(".subName dd p:nth-child(1)").html(name);
    	        $(".pce").text(price);
    	        $(".imgPhoto").attr('src',src);
    	        $("#price").text(price);
    	         money= parseFloat(money) + parseFloat(price);
    	         shop_name=name;
    	        $(".choseValue").text($(".subChose .m-active").text());
    	        biaoqian=$(".subChose .m-active").text();
    	        var dataIcon=$(this).parent().parent().children("h4").attr("data-icon");
    	        $(".subName dd p:first-child").attr("data-icon",dataIcon)
    	      

}

//商品飞入购物车
function fei_add(event){
	tijiao();
	$("#subChose").html('');
	$('#jian'+number).show();
	$('#shu'+number).html(num);
	$('#shu'+number).show();
	var offset = $('#end').offset(), flyer = $('<img class="u-flyer" src="../common/img/lanrenzhijia.jpg"/>');
	flyer.fly({
    start: {
        left: event.pageX,
        top: event.pageY
    },
    end: {
        left: offset.left,
        top: offset.top,
        width: 20,
        height: 20,
    }
  });
	var distributionPrice=$.cookie('distributionPrice');
     $(".disable").css("background-color","#f03c03");
     $('#end').attr("src","../common/img/shop-car.png");
     $('#pay').html(money+"元,另需配送费"+distributionPrice+"元");
     $(".subFly").hide();
     var dataIconN = $(this).parent().children(".subName").children("dd").children("p:first-child").attr("data-icon");
     var data=[shop_name,biaoqian,money,num,money,dataIconN];
     add(data);
     if(num>1){
    	 num--;
     }
     $("#su").html(num);
}

function apa(){
    $(".shopcart-list.fold-transition").show();
	
	
}

function tijiao(goods_id){
	var goods_id=$.cookie('goods_id');
	var cumId=$.cookie('cumId');
	$.ajax({
		type:'POST',
		url: BASE_URL + LOGIN_ACTION.SAVE,
	 	data: { 
			comId: cumId,
			token:'cOZ6cjmF9NF',
			goodId:goods_id,
			num:num
		},
		success:function(data) {
			
		},
		error:function(error) {
			console.log(error);
		}
	});
}


function add(data) {

    $(".list-content>ul").append( '<li class="food"><div><span class="accountName" data-icon="'+data[5]+'">'+data[0]+'</span><span>'+data[1]+'</span></div><div><span>￥</span><span class="accountPrice">'+data[2]+'</span></div><div class="btn"><button class="ms2" style="display: inline-block;"><strong></strong></button> <i style="display: inline-block;">'+data[3]+'</i><button class="ad2"> <strong></strong></button><i style="display: none;">'+data[4]+'</i></div></li>');

    var display = $(".shopcart-list.fold-transition").css('display');
    if(display=="block"){
        $("document").click(function(){
            $(".shopcart-list.fold-transition").hide();
        })
    }

	
}
