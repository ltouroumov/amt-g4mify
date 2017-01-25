'use strict';

/**
 * @ngdoc function
 * @name app.route:badgesRoute
 * @description
 * # badgesRoute
 * Route of the app
 */

angular.module('badges')
	.config(['$stateProvider', function ($stateProvider) {

		$stateProvider
			.state('badges', {
				url:'/badges',
				templateUrl: 'app/modules/badges/badges.html',
				controller: 'BadgesCtrl',
				controllerAs: 'vm'
			});
	}]);
