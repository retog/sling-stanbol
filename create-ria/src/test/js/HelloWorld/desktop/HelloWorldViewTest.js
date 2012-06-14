/*global require */

require([ "jquery", "./HelloWorldView", "qunit" ], function($,
		HelloWorldView, QUnit) {

	var equal = QUnit.equal, expect = QUnit.expect, test = QUnit.test;

	/**
	 * Test that the setText method sets the text in the #helloWorld div
	 */
	test("setText sets text in #helloWorld #helloMessage", function() {

		var magicWord, view;

		// Setup test data
		magicWord = "abracadabra";

		// Setup view and call method under test
		view = new HelloWorldView();
		view.div = $("#helloWorld");
		view.setHelloWorldDivText(magicWord);

		// Expect that the text was set on the expected element
		equal($("#helloWorld #helloMessage").text(), magicWord,
				"Expected text not set in '#helloWorld #helloMessage' div");
	});

});