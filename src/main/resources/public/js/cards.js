var test_content = "[{'word': 'car','definitions': {'a293b27a-9c4c-4814-a225-4419570b9e86': 'car Isy0cANwPEAYy7pQKBV2','089267b7-2b46-4cb0-a443-f41c2892d657': 'car DiMA385PZZkpw5c5yfqO'},{'word': 'home','definitions': {'b1af647c-0269-4d7a-972d-f67f8c3736c1': 'home KC94oc8gi00vG9L77vby','28e71e93-310b-4195-a727-9361b55493b4': 'home HVuhKsviekex5LR9fsZE'}}]";
var app = angular.module("EnglishCards", ['ngResource']);

      app.factory('sendDefinitionService', ['$resource', function($resource) {
        console.log("RATING_URL: " + RATING_URL);
        var Definition = $resource(RATING_URL,{}, {
        send: { method: "POST", params: {}, headers: {'Content-Type':'application/json'}}
        }
      );
         return function(entry) {
          // debug(entry);
           return Definition.send(entry);
         };
       }]);

       app.controller('SendDefinitionCtrl', ['$scope', 'sendDefinitionService', function($scope, sendDefinitionService) {
         $scope.sendDefinition = function() {
             //showRatingErrorMessage(undefined, undefined, undefined, true);
             var content = new Object();
             content.words = $('#words').val();
             $scope.definitions = JSON.parse(test_content);
  //           $("#btnDefinition").attr("disabled", true);
//             $("#imgDefinitionLoad").show();

            /*
             $scope.sentDefinition = sendDefinitionService(JSON.stringify(content));
             $scope.sentDefinition.$promise.then(function (result) {
               console.info(result);
             }, function(error) {
               showRatingErrorMessage('', '#btnDefinition', '#imgDefinitionLoad');
               treatError(error, 'send rating');
             });
             */
        }


       }]);
