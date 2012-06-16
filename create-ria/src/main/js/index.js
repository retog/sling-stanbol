/*global $, require, Backbone, window */

// When the document is ready:
$().ready(function() {

	var console = window.console;
	/*require([ "IndexContext" ], function(IndexContext) {

		// Create a new IndexContext and initialize it which will create and
		// start a HelloWorldController
		var indexContext = new IndexContext();
		indexContext.initialize();
	});*/
	
	require([ "createjs" ], function(Create) {
		//window.alert('f');
		//console.log('ss');
	});


	$('body').midgardCreate({
		url: function () {
		return false;
		},
		stanbolUrl: 'http://dev.iks-project.eu:8081',
		tags: true
	});
	
	 // Fake Backbone.sync since there is no server to communicate with
	Backbone.sync = function(method, model, options) {
		if (console && console.log) {
			console.log('Model contents', model.toJSONLD());
		}
		options.success(model);
	};
   
});
