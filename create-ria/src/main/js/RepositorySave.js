/*global define, window */

define([], function() {
	

	
	function RepositorySave() {}
	
	RepositorySave.prototype.saveToRepository = function() {
			
			window.alert("saving");
			
			function getContent() {
				return "some content";
			}
			
            // Repository storage URL
            var base_url  = "http://localhost:7402/content/",
            // get the content we want to post
            params = "content=" + getContent(),
            // prompt the user to give it a name
            name = "test",
            // appending "/*" to the full URL
            // tells Sling to create a new node:
            url = "http://localhost:8080/" + name + "/*",
			// prepare for AJAX POST
            http = new XMLHttpRequest();
	
            if (!name || name.length === 0) {
				throw "No name provided.";
			}
            
            http.open("POST", url, true);

            // Send the proper header information along with the request
            http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            http.setRequestHeader("Content-length", params.length);
            http.setRequestHeader("Connection", "close");

            // Show whether we succeeded...
            http.onreadystatechange = function() {
                    if(http.readyState === 4) {
						window.alert("http.status = " + http.status);
					}
            };
            // do the AJAX POST
            http.send(params);

		};
		
		return RepositorySave;
});
