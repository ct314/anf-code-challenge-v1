/*
 User Form Exercise 1
 */
 /***Begin Code - Robert Krulich***/
(function(document, $) {
	"use strict";

	$(document).on("click", ".cmp-form-button", function(e) {
        var formID = $(this).attr('id')

        var lastName ="";
        var firstName = "";
        var age = "";
        var country = "";

        $('input, select').each(
    		function(index){
        		var input = $(this);

				var inputName = input.attr('name');
                if (input.attr('name').search(/lastname/i) >= 0) {
                	lastName = input.val();
            	}
            	if (input.attr('name').search(/firstname/i) >= 0) {
                	firstName = input.val();
            	}
        		if (input.attr('name').search(/age/i) >= 0) {
                	age = input.val();
            	}
    		}
		);
        country = document.querySelector('.country__cmp').textContent;
        var pos = 0;
        pos = country.search(/country: /i);
        if(pos >= 0) {
            pos = pos+"country: ".length;
            country = country.substr(pos).trim();
    	}
        submitUserDetails(lastName,firstName,age,country);
	});

    function submitUserDetails(lastName,firstName,age,country) {
		// Ajax call to post the file data
		$.ajax({
			type: 'GET',
			url: '/bin/saveUserDetails',
			processData: false,
			contentType: false,
			data: "firstName=" + firstName + "&lastName=" + lastName + "&age=" + age + "&country=" + country,
			success: function(msg) {
				console.log(msg); //display the data returned by the servlet
			},
			error: function() {
				console.log("Error");
			}
		});
	}
})(document, Granite.$);
/***End Code***/