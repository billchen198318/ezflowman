var _inpForm = {
	'oid'		:	'',
    'cnfId'		:	'',
	'cnfName'	:	'',
	'dsId'		:	_qifu_please_select_id,
    'efgpPkgId'	:	'',
    'mainTbl'	:	'',
    'efgpProcessStatusField'	:	'',
    'efgpProcessNoField'		:	'',
    'efgpRequesterIdField'		:	'',
    'efgpOrgUnitIdField'		:	'',
    'efgpSubjectScript'			:	'',
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
		loadEfgpPkg	:	_loadEfgpPkg,
		saveEzfMap	:	_saveEzfMap
	},
	mounted() {
		this.initData();
	},
	computed: {
		loadEfgpProcessBtnDisabled() {
			return ( null != this.inpForm && null != this.inpForm.oid && '' != this.inpForm.oid );
		}		
	}
}

function _initData() {
	
}

function _loadEfgpPkg() {
	var that = this;
	clearWarningMessageField(msgFields);
	xhrSendParameter2(
		'./ezfMapEfgpPackageIdLoadJson', 
		JSON.stringify(that.inpForm), 
		function(data) {
			if ( _qifu_success_flag != data.success ) {
				setWarningMessageField(msgFields, data.checkFields);
				parent.notifyWarning( data.message );
				that.clearPage();
				return;
			}
			that.inpForm = data.value;
		}, 
		this.clearPage,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

function _saveEzfMap() {
	var that = this;
	var eventUrl = './ezfMapSaveJson';
	if (null != this.inpForm && null != this.inpForm.oid && '' != this.inpForm.oid) {
		eventUrl = './ezfMapUpdateJson';
	} 
	clearWarningMessageField(msgFields);	
	xhrSendParameter2(
		eventUrl, 
		JSON.stringify(that.inpForm), 
		function(data) {
			if ( _qifu_success_flag != data.success ) {
				setWarningMessageField(msgFields, data.checkFields);
				parent.notifyWarning( data.message );
				//that.clearPage();
				return;
			}
			that.inpForm = data.value;
		}, 
		this.clearPage,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

function _clearPage() {
	this.inpForm.oid = '';
	this.inpForm.efgpPkgId = '';
	if (this.inpForm.fields != null) {
		this.inpForm.fields.splice(0);
	}
	if (this.inpForm.grids != null) {
		this.inpForm.grids.splice(0);
	}
}

function appUnmount() {
	app.unmount();
	console.log('EZF_A001D0001A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');

