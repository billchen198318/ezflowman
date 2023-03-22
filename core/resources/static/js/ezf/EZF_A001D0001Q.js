var _queryParam = {
	cnfIdLike	:	'',
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
		setQueryDataList	:	_setQueryDataList,
		loadCnfItem			:	_loadCnfItem,
		deleteCnf			:	_deleteCnf
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
	this.queryParam.cnfIdLike = '';
	this.queryParam.cnfNameLike = '';	
	this.cnfList.splice(0);
}

function _queryDataList() {
	this.cnfList.splice(0);
	xhrSendParameter2(
		'./ezfMapQueryJson', 
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

function _loadCnfItem(oid) {
	alert('load:' + oid);
}

function _deleteCnf(oid) {
	alert('del:' + oid);
}


function appUnmount() {
	app.unmount();
	console.log('EZF_A001D0001Q appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
