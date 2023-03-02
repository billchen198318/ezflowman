var _queryParam = {
	dsId	:	'',
	dsName	:	''
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

const PageEventHandling = {
	data() {
		return {
			queryParam	:	_queryParam,
			inpParam	:	_inpParam
		}
	},
	methods: {
		initData		:	_initData,
		clearPage		:	_clearPage,
		queryDataList	:	_queryDataList,
		addDsItem		:	_addDsItem
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

function _addDsItem() {
	alert('456...');
}

function appUnmount() {
	app.unmount();
	console.log('EZF_A001D0009A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
