/*global define, window */

define( ["jquery-ui"], function($) {
    
    /**
     * User interface part of HelloWorld*.
     * Shows messages to the user and reacts to user input.
     * This view is part of the desktop view implementation.
     * 
     * This is a "view" as defined by "supervising controller".
     * @see http://martinfowler.com/eaaDev/SupervisingPresenter.html
     * 
     * @class User interface part of HelloWorld*.
     * @name HelloWorldView
     */
    function HelloWorldView() {
    }
    
    /**
     * The div used for the hello world message.
     */
    HelloWorldView.prototype.div = undefined;
    
    /**
     * The event handler for a click on #helloButton.
     */
    HelloWorldView.prototype.helloButtonClick = undefined;
    
    /**
     * Initialize the view.
     * - Setup buttons
     */
    HelloWorldView.prototype.initialize = function() {
        var self = this;
        
        // Setup #helloButton as a button and bind click to helloButtonClick event
        $("#helloButton", self.div).button()
            .click( function(event){
                self.helloButtonClick(event);
            });
    };
    
    /**
     * Sets the hello world message in the #helloWorld div.
     * 
     * @param message The message to set.
     */
    HelloWorldView.prototype.setHelloWorldDivText = function(message) {
        var self = this;
        
        // Set message on the hello world div
        $("#helloMessage", self.div).text(message);
    };
    
    /**
     * Sets the hello world message in the #helloButton button.
     * 
     * @param message The message to set.
     */
    HelloWorldView.prototype.setHelloButtonText = function(message) {
        var self = this;
        
        // Set message on the hello world div
        $("#helloButton span", self.div).text(message);
    };
    
    /**
     * Show an error message.
     * 
     * @param message The message to show.
     */
    HelloWorldView.prototype.showError = function(message) {
        var self = this;
        
        // Show error message
        window.alert("Error: " + message);
    };
    
    // Return the function
    return HelloWorldView;
  
});