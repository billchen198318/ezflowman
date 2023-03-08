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

var _dsList = [];
<@qifu.if test=" null != dsMap && dsMap.size > 0 ">
	<#list dsMap?keys as k>
_dsList.push({'id' : '${k}' , 'name' : '${dsMap[k]?js_string}'});
	</#list>
</@qifu.if>


var msgFields = new Object();
msgFields['efgpPkgId'] 		= 'efgpPkgId';
msgFields['cnfId'] 			= 'cnfId';
msgFields['cnfName'] 		= 'cnfName';
msgFields['dsId'] 			= 'dsId';
msgFields['mainTbl'] 		= 'mainTbl';

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
					<label for="efgpPkgId">EFGP流程編號</label>
					<input type="text" name="efgpPkgId" id="efgpPkgId" class="form-control" placeholder="請輸入EFGP流程編號" v-model="inpForm.efgpPkgId" >					
				</div>
			</div>
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">					
					<label for="cnfId">配置編號</label>
					<input type="text" name="cnfId" id="cnfId" class="form-control" placeholder="請輸入配置編號" v-model="inpForm.cnfId" >						
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<label for="cnfName">配置名稱</label>
					<input type="text" name="cnfName" id="cnfName" class="form-control" placeholder="請輸入配置名稱" v-model="inpForm.cnfName" >					
				</div>				
			</div>			
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<label for="dsId">資料來源</label>
					<select class="form-control" id="dsId" v-model="inpForm.dsId">
						<option v-for="(p, index) in dsList" v-bind:value="p.id">{{ p.name }}</option>
					</select>							
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<label for="mainTbl">映射資料表</label>
					<input type="text" name="mainTbl" id="mainTbl" class="form-control" placeholder="請輸入映射資料表" v-model="inpForm.mainTbl" >							
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
		
		
<!-- ###################################################################################### -->
			<div class="card border-dark" v-if="inpForm != null && inpForm.fields != null && inpForm.fields.length > 0">
				<div class="card-body" >
					<h5 class="card-title">表單與資料表欄位配置</h5>
					
<table class="table">
  <thead class="thead-dark">
    <tr>
      <th scope="col">表單欄位</th>
      <th scope="col">資料表欄位</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="(d, index) in inpForm.fields" >
      <td>{{d.formField}}</td>
      <td><input type="text" v-bind:id="'tblField_'+d.formField" class="form-control" placeholder="請輸入table欄位" v-model="d.tblField"></td>
    </tr>
  </tbody>
</table>
					
					
					<p style="margin-bottom: 10px"></p>
					
					
					<h5 class="card-title">流程序號&簽核狀態欄位配置</h5>
					
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6 ">					
					<label for="efgpProcessStatusField">簽核狀態欄位名稱</label>
					<input type="text" name="efgpProcessStatusField" id="efgpProcessStatusField" class="form-control" placeholder="請輸入簽核狀態欄位名稱" v-model="inpForm.efgpProcessStatusField" >						
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 ">
					<label for="efgpProcessNoField">流程序號欄位名稱</label>
					<input type="text" name="efgpProcessNoField" id="efgpProcessNoField" class="form-control" placeholder="請輸入流程序號欄位名稱" v-model="inpForm.efgpProcessNoField" >					
				</div>				
			</div>							
			
					
					
				</div>
			</div>	
			
			
			
			
<!-- ###################################################################################### -->		
<template v-for='(g, index) in inpForm.grids' >

			<p style="margin-bottom: 10px"></p>
		
			<div class="card border-dark">
				<div class="card-body" >
					<h5 class="card-title">表單Grid資料表欄位配置({{g.gridId}})</h5>		

<table class="table">
  <thead class="thead-dark">
    <tr>
      <th scope="col">表單欄位</th>
      <th scope="col">資料表欄位</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="(i, index) in g.items" >
      <td>{{i.formField}}</td>
      <td><input type="text" v-bind:id="'grid_' + g.gridId + '_'+i.formField" class="form-control" placeholder="請輸入table欄位" v-model="i.tblField"></td>
    </tr>
  </tbody>
</table>

				</div>
			</div>
			
</template>	
<!-- ###################################################################################### -->		
		
		</div>
	</div>
</div>		


<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/ezf/EZF_A001D0001A.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>		
