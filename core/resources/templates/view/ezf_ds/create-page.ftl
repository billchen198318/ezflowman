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

var msgFields = new Object();
msgFields['inp_dsId'] 		= 'inp_dsId';
msgFields['inp_dsName'] 	= 'inp_dsName';
msgFields['inp_driveType'] 	= 'inp_driveType';
msgFields['inp_dbAddr'] 	= 'inp_dbAddr';
msgFields['inp_dbUser'] 	= 'inp_dbUser';
msgFields['inp_dbPasswd'] 	= 'inp_dbPasswd';

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
					<label for="employeeId">編號</label>
					<input type="text" name="dsId" id="dsId" class="form-control" placeholder="請輸入編號" v-model="queryParam.dsIdLike" @input="queryParam.dsIdLike=$event.target.value.toUpperCase()" >					
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<label for="employeeId">名稱</label>
					<input type="text" name="dsName" id="dsName" class="form-control" placeholder="請輸入名稱" v-model="queryParam.dsNameLike" >					
				</div>				
			</div>
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<button type="button" class="btn btn-primary" v-on:click="queryDataList"><i class="icon fa fa-search"></i>&nbsp;查詢</button>	
					&nbsp;
					<button type="button" class="btn btn-primary" v-on:click="clearPage"><i class="icon fa fa-eraser"></i>&nbsp;清除</button>	
					&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#dsConfigModal" ><i class="icon fa fa-plus"></i>&nbsp;新增</button>
					
				</div>
			</div>			
		</div>
	</div>
	
	<br>		
	
	
	
	
	
	<div class="row">
			
			<div class="card-columns col-xs-12 col-md-12 col-lg-12" v-if=" dsList.length == 0 ">
				<div class="alert alert-primary" role="alert"><h5>無資料來源配置資料!</h5></div>
			</div>	
			
			
			<div class="card-columns col-xs-12 col-md-12 col-lg-12" v-if=" dsList.length > 0 ">
			
			  <div class="card border-dark" v-for="d in dsList">
			    <div class="card-body">
			      <h5 class="card-title">編號:&nbsp;{{ d.dsId }}
			      	<span class="badge badge-warning" v-if=" '1' == d.driverType " >MS Sql-Server</span>
			      	<span class="badge badge-warning" v-if=" '2' == d.driverType " >Oracle</span>
			      	<span class="badge badge-warning" v-if=" '3' == d.driverType " >Mariadb</span>	      
			      </h5>
			      
			      <h5>名稱:&nbsp;<span class="badge badge-info">{{ d.dsName }}</span></h5>
			      
			      <p class="card-text"><pre>{{ d.dbAddr }}</pre></p>
			      
			      <br>
			      
			      <button type="button" class="btn btn-info" title="編輯表單" ><i class="icon fa fa-edit"></i>編輯配置</button>
			    	&nbsp;
			      <button type="button" class="btn btn-danger" title="刪除" ><i class="icon fa fa-remove"></i>刪除</button>
			      
			    </div>
			  </div>
			  
			</div>
			
	</div>	
	
	
	
	
	
	
	
	
<!-- modal -->
<div class="modal fade" id="dsConfigModal" tabindex="-1" aria-labelledby="dsConfigModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="dsConfigModalLabel">新增資料來源</h5>
        <button type="button" class="close" aria-label="Close" v-on:click="hideDsModal">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form>
          <div class="form-group">
            <label for="inp_dsId" class="col-form-label">編號:</label>
            <input type="text" class="form-control" id="inp_dsId" v-model="inpParam.dsId" @input="inpParam.dsId=$event.target.value.toUpperCase()" >
          </div>
          <div class="form-group">
            <label for="inp_dsName" class="col-form-label">名稱:</label>
            <input type="text" class="form-control" id="inp_dsName" v-model="inpParam.dsName" >
          </div>                 
          <div class="form-group">
		    <label for="inp_driveType">Driver</label>
		    <select class="form-control" id="inp_driveType" v-model="inpParam.driverType" >
		      <option value="1">1 - MS SqlServer</option>
		      <option value="2">2 - oracle</option>
		      <option value="3">3 - mariadb</option>
		    </select>
          </div>          
          <div class="form-group">
            <label for="inp_dbAddr" class="col-form-label">JDBC Url:</label>
            <input type="text" class="form-control" id="inp_dbAddr" v-model="inpParam.dbAddr" >
          </div> 
          <div class="form-group">
            <label for="inp_dbUser" class="col-form-label">帳戶:</label>
            <input type="text" class="form-control" id="inp_dbUser" v-model="inpParam.dbUser" >
          </div>
          <div class="form-group">
            <label for="inp_dbPasswd" class="col-form-label">密碼:</label>
            <input type="text" class="form-control" id="inp_dbPasswd" v-model="inpParam.dbPasswd" >
          </div>                                                                                                  
        </form>        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="hideDsModal">取消</button>
        <button type="button" class="btn btn-primary" v-on:click="addDsItem">儲存</button>
      </div>
    </div>
  </div>
</div>		
<!-- modal -->	
		
		
		
		
		
		
		
		
		
		
		
		</div>
	</div>
</div>		


<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/ezf/EZF_A001D0009A.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>		
