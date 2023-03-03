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
			dsList		:	_dsList
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
		hideDsModal			:	_hideDsModal
	},
	mounted() {
		this.initData();
	},
	computed: {
		
	}
}

function _initData() {
	this.queryDataList();
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

function _hideDsModal() {
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
