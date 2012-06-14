/*global $, require */

// When the document is ready:
$().ready(function() {

	require([ "IndexContext" ], function(IndexContext) {

		// Create a new IndexContext and initialize it which will create and
		// start a HelloWorldController
		var indexContext = new IndexContext();
		indexContext.initialize();
	});

});
