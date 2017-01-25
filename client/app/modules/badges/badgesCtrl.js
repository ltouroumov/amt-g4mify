(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:badgesCtrl
	* @description
	* # badgesCtrl
	* Controller of the app
	*/

  	angular
		.module('badges')
		.controller('BadgesCtrl', Badges);

		Badges.$inject = ['BadgesService'];

		/*
		* recommend
		* Using function declarations
		* and bindable members up top.
		*/

		function Badges(BadgesService) {
			/*jshint validthis: true */
			var vm = this;

			BadgesService.getBadges(vm);

		}

})();
