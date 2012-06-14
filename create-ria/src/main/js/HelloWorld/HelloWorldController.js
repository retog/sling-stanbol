/*global define */

define( ["jquery"], function($) {
    
    /**
     * Controls the showing of a 'Hello World' message.
     * 
     * This is a "supervising controller" otherwise known as a "presenter".
     * @see http://martinfowler.com/eaaDev/SupervisingPresenter.html
     * 
     * @class Controls the showing of a 'Hello World' message.
     * @name HelloWorldController
     */
    function HelloWorldController() {
    }
    
    /**
     * The HelloWorldView instance (injected)
     */
    HelloWorldController.prototype.view = undefined;
    
    /**
     * The HelloWorldCommand instance (injected)
     */
    HelloWorldController.prototype.command = undefined;
    
    /**
     * Success callback for the HelloWorldCommand.
     * Show the 'Hello World' message using the view.
     * 
     * @param message The message to show.
     */
    HelloWorldController.prototype.getTextSuccess = function(message) {
        var self = this;
        
        self.view.setHelloWorldDivText(message);
    };
    
    /**
     * Failure callback for the HelloWorldCommand.
     * Show the error message using the view.
     * 
     * @param errorMessage A description of the error
     */
    HelloWorldController.prototype.getTextFailure = function(errorMessage) {
        var self = this;
        
        self.view.showError(errorMessage);
    };
    
    /**
     * Initialization method for this controller.
     */
    HelloWorldController.prototype.start = function() {
        var self = this, text;
        
        // Inject our helloButtonClick handler into the view
        self.view.helloButtonClick = $.proxy(self.helloButtonClick, self);
        // Initialize the view
        self.view.initialize();
        
        // Fetch the 'hello world' text using a command object
        text = self.command.execute( $.proxy(self.getTextSuccess, self), $.proxy(self.getTextFailure, self) );
    };
    
    /**
     * Handle a click on the #helloButton element.
     * 
     * @param event The event object which triggered this handler.
     */
    HelloWorldController.prototype.helloButtonClick = function(event) {
        var self = this;
        
        self.view.setHelloButtonText("Clicked");
    };
    
    // Return the function
    return HelloWorldController;
  
});