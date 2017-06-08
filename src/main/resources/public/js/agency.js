// Agency Theme JavaScript

(function($) {
	"use strict"; // Start of use strict

	// jQuery for page scrolling feature - requires jQuery Easing plugin
	$('a.page-scroll').bind('click', function(event) {
		var $anchor = $(this);
		$('html, body').stop().animate({
			scrollTop : ($($anchor.attr('href')).offset().top - 50)
		}, 1250, 'easeInOutExpo');
		event.preventDefault();
	});

	// Highlight the top nav as scrolling occurs
	$('body').scrollspy({
		target : '.navbar-fixed-top',
		offset : 51
	});

	// Closes the Responsive Menu on Menu Item Click
	$('.navbar-collapse ul li a').click(function() {
		$('.navbar-toggle:visible').click();
	});

	// Offset for Main Navigation
	$('#mainNav').affix({
		offset : {
			top : 100
		}
	})

})(jQuery); // End of use strict
// var DEFINITION_SERVICE_URL = 'http://localhost:8080/definitions';
var DEFINITION_SERVICE_URL = 'https://fathomless-tundra-22713.herokuapp.com/definitions';
var test_content = "{\"invalidWords\":[\"xpto\",\"xxxx\"],\"definitions\":[{\"word\":\"car\",\"definitions\":{\"a293b27a-9c4c-4814-a225-4419570b9e86\":\"car Isy0cANwPEAYy7pQKBV2\",\"089267b7-2b46-4cb0-a443-f41c2892d657\":\"car DiMA385PZZkpw5c5yfqO\"}},{\"word\":\"car\",\"definitions\":{\"a293b27a-9c4c-4814-a225-4419570b9e86\":\"car Isy0cANwPEAYy7pQKBV2\",\"089267b7-2b46-4cb0-a443-f41c2892d657\":\"car DiMA385PZZkpw5c5yfqO\"}},{\"word\":\"car\",\"definitions\":{\"a293b27a-9c4c-4814-a225-4419570b9e86\":\"car Isy0cANwPEAYy7pQKBV2\",\"089267b7-2b46-4cb0-a443-f41c2892d657\":\"car DiMA385PZZkpw5c5yfqO\"}},{\"word\":\"car\",\"definitions\":{\"a293b27a-9c4c-4814-a225-4419570b9e86\":\"car Isy0cANwPEAYy7pQKBV2\",\"089267b7-2b46-4cb0-a443-f41c2892d657\":\"car DiMA385PZZkpw5c5yfqO\"}},{\"word\":\"car\",\"definitions\":{\"a293b27a-9c4c-4814-a225-4419570b9e86\":\"car Isy0cANwPEAYy7pQKBV2\",\"089267b7-2b46-4cb0-a443-f41c2892d657\":\"car DiMA385PZZkpw5c5yfqO\"}},{\"word\":\"car\",\"definitions\":{\"a293b27a-9c4c-4814-a225-4419570b9e86\":\"car Isy0cANwPEAYy7pQKBV2\",\"089267b7-2b46-4cb0-a443-f41c2892d657\":\"car DiMA385PZZkpw5c5yfqO\"}},{\"word\": \"home\",\"definitions\": {\"b1af647c-0269-4d7a-972d-f67f8c3736c1\":\"home KC94oc8gi00vG9L77vby\",\"28e71e93-310b-4195-a727-9361b55493b4\": \"home HVuhKsviekex5LR9fsZE\"}}]}";

function treatError(response, msg, funcs) {
	console.log(msg + ' ' + response.status);
	if (-1 == response.status) {
	} else {
	}
	if (funcs != undefined) {
		for (i = 0; i < funcs.length; i++) {
			funcs[i]();
		}
	}
}

function showSearchLoad(show) {
	if (show) {
		$('.search-load').show('slow');
	} else {
		$('.search-load').hide('slow');
	}
}

function parseWords(words) {
	var arr = words.split(',');
	for (var i = 0; i < arr.length; i++) {
		arr[i] = arr[i].trim();
	}
	return arr;
}

function showMessage(title, errorMessage, type, show) {
	if (show) {
		$('#msg-row').find('.msg-title').html(title);
		$('#msg-row').find('.msg-content').html(' ' + errorMessage);
		var component = $('#msg-row').find('.msg-type');
		$('#msg-row').find('.msg-type').removeClass(
				$('#msg-row').find('.msg-type').attr('class'));
		component.addClass('alert msg-type ' + type);
		$('#msg-row').show('slow');
	} else {
		$('#msg-row').hide('slow');
	}
}
function clearMessage() {
	showMessage('', '', '', false);
}
function showErrorMessage(title, errorMessage, btn, load, hide) {
	showMessage(title, errorMessage, 'alert-danger', true);
}

function splitWords(content) {
	var result = '';
	for (var i = 0; i < content.length; i++) {
		if (i == 0) {
			result = content[i];
		} else {
			result = result + ', ' + content[i];
		}
	}
	return result;
}
