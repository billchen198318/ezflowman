var _inpForm = {
	'oid'		:	'',
    'cnfId'		:	'',
	'cnfName'	:	'',
	'dsId'		:	_qifu_please_select_id,
    'efgpPkgId'	:	'',
    'mainTbl'	:	'',
    'fields'	:	[],
    'grids'		:	[]
}

const PageEventHandling = {
	data() {
		return {
			inpForm			:	_inpForm,
			dsList			:	_dsList // create-page.ftl
		}
	},
	methods: {
		initData	:	_initData,
		clearPage	:	_clearPage,
		loadEfgpPkg	:	_loadEfgpPkg
	},
	mounted() {
		this.initData();
	},
	computed: {
		
	}
}

function _initData() {
	
}

function _loadEfgpPkg() {
	var that = this;
	
	console.log( JSON.stringify(this.inpForm) );
	
	clearWarningMessageField(msgFields);
	xhrSendParameter(
		'./ezfMapEfgpPackageIdLoadJson', 
		that.inpForm, 
		function(data) {
			if ( _qifu_success_flag != data.success ) {
				setWarningMessageField(msgFields, data.checkFields);
				parent.notifyWarning( data.message );
				return;
			}
			that.inpForm = data.value;
		}, 
		this.clearPage,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

function _clearPage() {
	
}

function appUnmount() {
	app.unmount();
	console.log('EZF_A001D0001A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');

