var _queryParam = {
	cnfId		:	'',
	cnfNameLike	:	''
}

var _cnfList = [];

const PageEventHandling = {
	data() {
		return {
			queryParam	:	_queryParam,
			cnfList		:	_cnfList
		}
	},
	methods: {
		initData			:	_initData,
		clearPage			:	_clearPage,
		queryDataList		:	_queryDataList,
		setQueryDataList	:	_setQueryDataList
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

function _clearPage() {
	this.queryParam.cnfId = '';
	this.queryParam.cnfNameLike = '';	
	this.cnfList.splice(0);
}

function _queryDataList() {
	this.clearInputParam();
	this.cnfList.splice(0);
	xhrSendParameter2(
		'./testQueryJson', 
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
			this.cnfList.push(data.value[n]);
		}
	}
}
