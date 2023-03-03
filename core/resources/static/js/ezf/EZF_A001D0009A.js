var _queryParam = {
	dsIdLike	:	'',
	dsNameLike	:	''
}

var _inpParam = {
	oid			:	'',
	dsId		:	'',
	dsName		:	'',
	driverType	:	'1',
	dbAddr		:	'',
	dbUser		:	'',
	dbPasswd	:	''
}

var _dsList = [];

const PageEventHandling = {
	data() {
		return {
			queryParam	:	_queryParam,
			inpParam	:	_inpParam,
			dsList		:	_dsList,
			editMode	:	false
		}
	},
	methods: {
		initData			:	_initData,
		clearPage			:	_clearPage,
		queryDataList		:	_queryDataList,
		setQueryDataList	:	_setQueryDataList,
		addDsItem			:	_addDsItem,
		clearInputParam		:	_clearInputParam,
		setSaveDsInfo		:	_setSaveDsInfo,
		hideDsModal			:	_hideDsModal,
		loadDsItem			:	_loadDsItem,
		deleteDs			:	_deleteDs
	},
	mounted() {
		this.initData();
	},
	computed: {
		
	}
}

function _initData() {
	this.queryDataList();
	this.editMode = false;
}

function _clearInputParam() {
	this.inpParam.oid = '';
	this.inpParam.dsId = '';
	this.inpParam.dsName = '';
	this.inpParam.driverType = '1';
	this.inpParam.dbAddr = '';
	this.inpParam.dbUser = '';
	this.inpParam.dbPasswd = '';	
}

function _clearPage() {
	this.editMode = false;
	this.queryParam.dsIdLike = '';
	this.queryParam.dsNameLike = '';
	
	this.clearInputParam();
	this.dsList.splice(0);
}

function _queryDataList() {
	this.clearInputParam();
	this.dsList.splice(0);
	xhrSendParameter2(
		'./ezfDsConfigQueryJson', 
		JSON.stringify(this.queryParam), 
		this.setQueryDataList, 
		this.clearPage,
		_qifu_defaultSelfPleaseWaitShow
	);
}

function _setQueryDataList(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.notifyInfo( data.message );
	}
	if ( _qifu_success_flag == data.success ) {
		for (var n in data.value) {
			this.dsList.push(data.value[n]);
		}
	}
}

function _addDsItem() {
	var url = './ezfDsConfigSaveJson';
	if ('' != this.inpParam.oid) {
		url = './ezfDsConfigUpdateJson';
	}
	clearWarningMessageField(msgFields);
	xhrSendParameter(
		url, 
		this.inpParam, 
		this.setSaveDsInfo, 
		this.clearPage,
		_qifu_defaultSelfPleaseWaitShow
	);
}

function _setSaveDsInfo(data) {
	if ( _qifu_success_flag != data.success ) {
		setWarningMessageField(msgFields, data.checkFields);		
		parent.notifyWarning( data.message );
		return;
	}
	parent.notifyInfo( data.message );
	this.clearInputParam();
	this.hideDsModal();
	this.queryDataList();
}

function _loadDsItem(oid) {
	this.editMode = false;
	if (null == oid || '' == oid) {
		alert('error');
		return;
	}
	var that = this;
	xhrSendParameter(
		'./ezfDsConfigLoadJson', 
		{ 'oid' : oid }, 
		function(data) {
			if ( _qifu_success_flag != data.success ) {
				parent.notifyWarning( data.message );
				return;
			}
			that.inpParam.oid = data.value.oid;
			that.inpParam.dsId = data.value.dsId;
			that.inpParam.dsName = data.value.dsName;
			that.inpParam.driverType = data.value.driverType;
			that.inpParam.dbAddr = data.value.dbAddr;
			that.inpParam.dbUser = data.value.dbUser;
			that.inpParam.dbPasswd = data.value.dbPasswd;	
			$('#dsConfigModal').modal('show');
			that.editMode = true;
		}, 
		this.clearPage,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

function _deleteDs(oid) {
	var that = this;	
	parent.bootbox.confirm(
		"確定刪除?", 
		function(result) { 
			if (!result) {
				return;
			}
			xhrSendParameter(
				'./ezfDsConfigDeleteJson', 
				{ 'oid' : oid }, 
				function(data){
					parent.notifyInfo( data.message );
					that.queryDataList();		
				}, 
				function(){
					parent.notifyError( '錯誤' );
				},
				_qifu_defaultSelfPleaseWaitShow
			);	
		}
	);
}

function _hideDsModal() {
	this.editMode = false;
	$('#dsConfigModal').modal('hide');
	this.clearInputParam();
	clearWarningMessageField(msgFields);
}

function appUnmount() {
	app.unmount();
	console.log('EZF_A001D0009A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
