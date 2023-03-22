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
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<label for="cnfIdLike">編號</label>
					<input type="text" name="cnfIdLike" id="cnfIdLike" class="form-control" placeholder="請輸入編號" v-model="queryParam.cnfIdLike" @input="queryParam.cnfIdLike=$event.target.value.toUpperCase()" >					
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<label for="cnfNameLike">名稱</label>
					<input type="text" name="cnfNameLike" id="cnfNameLike" class="form-control" placeholder="請輸入名稱" v-model="queryParam.cnfNameLike" >					
				</div>				
			</div>
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<button type="button" class="btn btn-primary" v-on:click="queryDataList"><i class="icon fa fa-search"></i>&nbsp;查詢</button>	
					&nbsp;
					<button type="button" class="btn btn-primary" v-on:click="clearPage"><i class="icon fa fa-eraser"></i>&nbsp;清除</button>	
					
				</div>
			</div>			
		</div>
	</div>
	
	<br>		
		
		
	<div class="row">
			
			<div class="card-columns col-xs-12 col-md-12 col-lg-12" v-if=" cnfList.length == 0 ">
				<div class="alert alert-primary" role="alert"><h5>無配置資料!</h5></div>
			</div>	
			
			
			<div class="card-columns col-xs-12 col-md-12 col-lg-12" v-if=" cnfList.length > 0 ">
			
			  <div class="card border-dark" v-for="d in cnfList">
			    <div class="card-body">
			      <h5 class="card-title">編號:&nbsp;{{ d.cnfId }}      
			      </h5>
			      
			      <h5>名稱:&nbsp;<span class="badge badge-info">{{ d.cnfName }}</span></h5>
			      
			      <h5><span class="badge badge-success">{{ d.efgpPkgId }}</span></h5>
			      
			      <br>
			      
			      <button type="button" class="btn btn-info" title="編輯表單" v-on:click="loadCnfItem(d.oid)" ><i class="icon fa fa-edit"></i>編輯配置</button>
			    	&nbsp;
			      <button type="button" class="btn btn-danger" title="刪除" v-on:click="deleteCnf(d.oid)" ><i class="icon fa fa-remove"></i>刪除</button>
			      
			    </div>
			  </div>
			  
			</div>
			
	</div>		
	
	
	
	
		
		
		</div>
	</div>
</div>		


<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/ezf/EZF_A001D0001Q.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>		
