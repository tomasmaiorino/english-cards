var app = angular.module("EnglishCards", [ 'ngResource' ]);

app.factory('sendDefinitionService', [ '$resource', function($resource) {
	console.log("DEFINITION_SERVICE_URL: " + DEFINITION_SERVICE_URL);
	var Definition = $resource(DEFINITION_SERVICE_URL, {}, {
		definition : {
			method : "POST",
			params : {},
			headers : {
				'Content-Type' : 'application/json'
			}
		}
	});
	return function(entry) {
		return Definition.definition(entry);
	};
} ]);

app.controller('SendDefinitionCtrl', [
		'$scope',
		'sendDefinitionService',
		function($scope, sendDefinitionService) {
			$scope.searchDefinitions = function() {
				if ($('#words').val() != '') {
					// pre configuration
					showSearchLoad(true);
					$('.search-link').hide('slow');
					var content = new Object();
					var arr = parseWords($('#words').val());
					content.words = arr;
					$scope.results = sendDefinitionService(JSON
							.stringify(content));
					$scope.results.$promise.then(function(result) {
						showSearchLoad(false);
						$('.search-link').show('slow');
						if(result.invalidWords.length > 0) {
							var invalidContent = '';
							for (var i = 0; i < result.invalidWords.length; i++) {
								if (i == 0) {
									invalidContent = result.invalidWords[i];
								} else {
									invalidContent = invalidContent + ', ' + result.invalidWords[i];	
								}
							}
							showMessage('Warning!', 'Some given words are invalid: ' + invalidContent, 'alert-warning', true);
						}
					}, function(error) {
						showSearchLoad(false);
						$('.search-link').show('slow');
						treatError(error, 'send definition', {});
					});
				} else {
					showErrorMessage();
				}
			}
		} ]);
