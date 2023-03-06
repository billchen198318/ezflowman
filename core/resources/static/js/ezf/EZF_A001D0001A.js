var _inpForm = null;

const PageEventHandling = {
	data() {
		return {
			inpForm			:	_inpForm,
			efgpPkgId		:	''
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
	this.inpForm = null;
	var that = this;
	xhrSendParameter(
		'./ezfMapEfgpPackageIdLoadJson', 
		{ 'efgpPkgId' : this.efgpPkgId }, 
		function(data) {
			if ( _qifu_success_flag != data.success ) {
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

