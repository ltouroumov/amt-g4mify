(function () {
	'use strict';

	/**
	* @ngdoc function
	* @name app.service:homeService
	* @description
	* # homeService
	* Service of the app
	*/

	angular.module('g4mify-client-app')
		.factory('homeService', homeService);

	homeService.$inject = ['$http'];

	function homeService($http) {

		var list = [
			{"feature": "Implemented Best Practices, following: John Papa's Guide"},
			{"feature": "Using Controller AS syntax"},
			{"feature": "Wrap Angular components in an Immediately Invoked Function Expression (IIFE)"},
			{"feature": "Declare modules without a variable using the setter syntax"},
			{"feature": "Using named functions"},
			{"feature": "Including Unit test with Karma"},
			{"feature": "Including UI options for Bootstrap or Angular-Material"},
			{"feature": "Including Angular-Material-Icons for Angular-Material UI"},
			{"feature": "Dynamic Menu generator for both themes"},
			{"feature": "Grunt task for Production and Development"}
		];

		return {
			init:init,
			postBeep:postBeep,
			postBoop:postBoop,
			getFeaturesList: getFeaturesList
		};

		function getFeaturesList() {
			return list;
		}

		function init(vm) {

			var req = {
				method: 'GET',
				url: 'http://localhost:8080/api/domain',
				headers: {
					'Content-Type': 'application/json',
					'Identity': '1:secret'
				}
			};

			$http(req).then(function(res){
				console.log(res.status);
				console.log(res.data);
				vm.msg = "- The domain has been successfully connected to the gamification platform";
				vm.success = true;

			}, function(err){
				vm.msg = "- An error occurred while connecting to he gamification platform ";
				vm.success = false;
			});
		}

		function postBeep(data) {

			var req = {
				method: 'POST',
				url: 'http://localhost:8080/api/events',
				data: JSON.stringify(data),
				headers: {
					'Content-Type': 'application/json',
					'Identity': '1:secret'
				}
			};

			$http(req).then(function(res){
				console.log("Beep: OK");
			}, function(err){
				console.log("Beep: ERROR");
				vm.msg = "- An error occurred posting the event to the gamification platform";
				vm.success = false;
			});
		}

		function postBoop(data) {

			var req = {
				method: 'POST',
				url: 'http://localhost:8080/api/events',
				data: JSON.stringify(data),
				headers: {
					'Content-Type': 'application/json',
					'Identity': '1:secret'
				}
			};

			$http(req).then(function(err){
				console.log("Boop: OK");

			}, function(data){
				console.log("Boop: ERROR");
				vm.msg = "- An error occurred posting the event to the gamification platform";
				vm.success = false;

			});
		}

	}

})();
