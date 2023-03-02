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
					<label for="employeeId">編號</label>
					<input type="text" name="dsId" id="dsId" class="form-control" placeholder="請輸入編號" v-model="queryParam.dsId" @input="queryParam.dsId=$event.target.value.toUpperCase()" >					
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<label for="employeeId">名稱</label>
					<input type="text" name="dsName" id="dsName" class="form-control" placeholder="請輸入名稱" v-model="queryParam.dsName" >					
				</div>				
			</div>
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<button type="button" class="btn btn-primary" v-on:click="queryDataList"><i class="icon fa fa-search"></i>&nbsp;查詢</button>	
					&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#dsConfigModal" ><i class="icon fa fa-plus"></i>&nbsp;新增</button>
					
				</div>
			</div>			
		</div>
	</div>
	
	<br>		
	
<!-- modal -->
<div class="modal fade" id="dsConfigModal" tabindex="-1" aria-labelledby="dsConfigModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="dsConfigModalLabel">新增資料來源</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form>
          <div class="form-group">
            <label for="inp_dsId" class="col-form-label">編號:</label>
            <input type="text" class="form-control" id="inp_dsId">
          </div>
          <div class="form-group">
            <label for="inp_dsName" class="col-form-label">名稱:</label>
            <input type="text" class="form-control" id="inp_dsName">
          </div>                 
          <div class="form-group">
		    <label for="inp_dsDriveType">Driver</label>
		    <select class="form-control" id="inp_dsDriveType">
		      <option>1 - mariadb</option>
		      <option>2 - mssql</option>
		      <option>3 - oracle</option>
		    </select>
          </div>          
          <div class="form-group">
            <label for="inp_dsUrl" class="col-form-label">JDBC Url:</label>
            <input type="text" class="form-control" id="inp_dsUrl">
          </div> 
          <div class="form-group">
            <label for="inp_user" class="col-form-label">帳戶:</label>
            <input type="text" class="form-control" id="inp_user">
          </div>
          <div class="form-group">
            <label for="inp_passwd" class="col-form-label">密碼:</label>
            <input type="text" class="form-control" id="inp_passwd">
          </div>                                                                                                  
        </form>        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
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
