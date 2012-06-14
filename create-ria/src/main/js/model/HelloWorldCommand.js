/*global define*/

define( [] ,function() {
    
    /**
     * Command object to retrieve the 'Hello World' text.
     * 
     * @class Command object to retrieve the 'Hello World' text.
     * @name HelloWorldCommand
     */
    function HelloWorldCommand() {
    }

    /**
     * Execute this command and then call the supplied success or
     * failure callback as appropriate.
     * 
     * @param success Callback function for a successful execution.
     * @param failure Callback function for a failed execution.
     */
    HelloWorldCommand.prototype.execute = function(success, failure) {
        success("Hello World");
    };
    
    // Return the function
    return HelloWorldCommand;
  
});