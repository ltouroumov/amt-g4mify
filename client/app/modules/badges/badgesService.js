(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:badgesService
	 * @description
	 * # badgesService
	 * Service of the app
	 */

  	angular
		.module('badges')
		.factory('BadgesService', Badges);
		// Inject your dependencies as .$inject = ['$http', 'someSevide'];
		// function Name ($http, someSevide) {...}

		Badges.$inject = ['$http'];

		function Badges ($http) {

		}

})();
