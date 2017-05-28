var app = angular.module("EnglishCards", ['ngResource']);

      app.factory('sendDefinitionService', ['$resource', function($resource) {
        console.log("RATING_URL: " + RATING_URL);
        var Definition = $resource(RATING_URL,{}, {
        send: { method: "POST", params: {}, headers: {'Content-Type':'application/json'}}
        }
      );
         return function(entry) {
           debug(entry);
           return Definition.send(entry);
         };
       }]);

       app.controller('SendRatingCtrl', ['$scope', 'sendDefinitionService', 'ratingService', function($scope, sendDefinitionService, ratingService) {
         $scope.sendRating = function() {
           //only submit the rating if the user choose at least one star
           if ($('#ratingCount').val() >= 1) {
             showRatingErrorMessage(undefined, undefined, undefined, true);
             var content = new Object();
             content.words = $('#words').val();

             $("#btnRating").attr("disabled", true);
             $("#imgRatingLoad").show();

             $scope.sentDefinition = sendDefinitionService(JSON.stringify(content));
             //$scope.sentRating = sendRatingService(content);
             $scope.sentRating.$promise.then(function (result) {
               showRatingSuccessMessage('', '#btnRating', '#imgRatingLoad');
               //update rating -- start
               $scope.rating = ratingService();
               $scope.rating.$promise.then(function (result) {
                 finalizeRating($scope.rating.average);
               }, function(error) {
                 treatError(error, 'rating');
               });
               //update rating -- end
             }, function(error) {
               showRatingErrorMessage('', '#btnRating', '#imgRatingLoad');
               treatError(error, 'send rating');
             });
           } else {
             showRatingErrorMessage('You need to choose at least one start', undefined, undefined)
           }
        }
       }]);
