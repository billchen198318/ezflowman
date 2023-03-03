var _inpList = [];

const PageEventHandling = {
	data() {
		return {
			inpList					:	_inpList,
			efgpProcessPackageId	:	''
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
	xhrSendParameter(
		'./ezfMapEfgpPackageIdLoadJson', 
		{ 'efgpProcessPackageId' : this.efgpProcessPackageId }, 
		function(data) {
			if ( _qifu_success_flag != data.success ) {
				parent.notifyWarning( data.message );
				return;
			}
			
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

