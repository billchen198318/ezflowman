var _queryParam = {
	dsId	:	'',
	dsName	:	''
}

const PageEventHandling = {
	data() {
		return {
			queryParam	:	_queryParam
		}
	},
	methods: {
		initData		:	_initData,
		clearPage		:	_clearPage,
		queryDataList	:	_queryDataList
	},
	mounted() {
		this.initData();
	},
	computed: {
		
	}
}

function _initData() {
	
}

function _clearPage() {
	
}

function _queryDataList() {
	alert('test...');
}

function appUnmount() {
	app.unmount();
	console.log('EZF_A001D0009A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
