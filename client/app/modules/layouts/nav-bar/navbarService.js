(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:menuService
	 * @description
	 * # menuService
	 * Service of the app
	 */

  	angular
		.module('g4mify-client-app')
		.factory('MenuService', Menu);
		// Inject your dependencies as .$inject = ['$http', 'someSevide'];
		// function Name ($http, someSevide) {...}

		Menu.$inject = ['$http'];

		function Menu ($http) {

			var menu = [

					{
						link: 'home',
						name: 'Game'
					},

					{
						link: 'badges',
						name: 'Badges'
					}

		  	];

			return {
				listMenu: function () {
					return menu;
				}
		  	}

		}

})();
