/*global define */

define([ "jquery", "HelloWorld/HelloWorldController", "HelloWorld/desktop/HelloWorldView", "model/HelloWorldCommand" ],
        function($, HelloWorldController, HelloWorldView, HelloWorldCommand) {
    
    /**
     * Context for our index.html entry point.
     * 
     * Sets up required objects, handles dependency injection, and starts
     * the HelloWorldController.
     * 
     * @class Context for our index.html entry point.
     * @name IndexContext
     */
    function IndexContext(){}
    
    /**
     * Initialize this context.
     * 
     * Instantiates HelloWorld components, injects dependencies, and starts
     * the HelloWorldController.
     */
    IndexContext.prototype.initialize = function() {
        var controller, view, command;
        
        // Instantiate objects
        controller = new HelloWorldController();
        view = new HelloWorldView();
        command = new HelloWorldCommand();
        
        // Perform dependency injection by extending objects
        $.extend(view, {
            div: $("#helloWorld")
        });
        
        $.extend(controller, {
           view: view,
           command: command
        });
        
        // Start the HelloWorldController
        controller.start();
    };
    
    // Return the function
    return IndexContext;
    
});
