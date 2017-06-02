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
					clearMessage();
					$('.search-link').hide('slow');
					var content = new Object();
					var arr = parseWords($('#words').val());
					content.words = arr;
					$scope.results = sendDefinitionService(JSON
							.stringify(content));
					$scope.results.$promise.then(function(result) {
						showSearchLoad(false);
						$('.search-link').show('slow');
						if (result.invalidWords.length > 0) {
							showMessage('Warning!',
									'Some given words are invalid: '
											+ splitWords(result.invalidWords),
									'alert-warning', true);
						}
					}, function(error) {
						showSearchLoad(false);
						$('.search-link').show('slow');
						treatError(error, 'send definition', showErrorMessage('Error!', error.data.message));
					});
				} else {
					showErrorMessage('Error!', 'At least one word must be informed.');
				}
			}
		} ]);
