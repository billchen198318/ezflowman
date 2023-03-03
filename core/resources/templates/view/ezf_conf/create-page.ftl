<html>
<head>
<title>EFGP整合配置</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script type="text/javascript" src="${qifu_basePath}js/vue.global.js"></script>


<style type="text/css">



</style>


<script type="text/javascript">

$( document ).ready(function() {
	
	
});

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">	
	<div class="row">	
		<div id="main-content" class="col-xs-12 col-md-12 col-lg-12">
		
		
	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<label for="efgpProcessPackageId">EFGP流程編號</label>
					<input type="text" name="efgpProcessPackageId" id="efgpProcessPackageId" class="form-control" placeholder="請輸入EFGP流程編號" v-model="efgpProcessPackageId" >					
				</div>
			</div>
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<button type="button" class="btn btn-primary" v-on:click="loadEfgpPkg"><i class="icon fa fa-search"></i>&nbsp;載入流程</button>	
					&nbsp;
					<button type="button" class="btn btn-primary" ><i class="icon fa fa-eraser"></i>&nbsp;清除</button>	
				</div>
			</div>			
		</div>
	</div>
	
	<br>			
		
		
		
		
		
		
		</div>
	</div>
</div>		


<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/ezf/EZF_A001D0001A.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>		
