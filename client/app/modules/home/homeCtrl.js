(function () {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:HomeCtrl
	* @description
	* # HomeCtrl
	* Controller of the app
	*/

	angular
		.module('g4mify-client-app')
		.controller('HomeCtrl', Home);

	Home.$inject = ['homeService', '$rootScope'];

	/*
	* recommend
	* Using function declarations
	* and bindable members up top.
	*/

	function Home(homeService, $rootScope) {
		/*jshint validthis: true */
		var vm = this;

		homeService.init(vm);

		vm.showme = true;
		vm.form = '';
		vm.greetings = '';
		vm.username = '';

		vm.data = [0, 0];
		vm.labels = ['Beep', 'Boop'];


		vm.onBeep = function () {
			vm.data[0] = vm.data[0] + 1;

			var event = {
				"user": vm.username,
				"type": "beep",
				"data": {}
			};

			homeService.postBeep(vm, event);

		};

		vm.onBoop = function () {
			vm.data[1] = vm.data[1] + 1;

			var event = {
				"user": vm.username,
				"type": "boop",
				"data": {}
			};
			homeService.postBoop(vm, event);
		};


		vm.submit = function () {
			homeService.checkIfUserExists(vm);
		}
	}
})();
