(function () {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.test:badgesTest
	 * @description
	 * # badgesTest
	 * Test of the app
	 */

	describe('badges test', function () {
		var controller = null, $scope = null;

		beforeEach(function () {
			module('g4mify-client-app');
		});

		beforeEach(inject(function ($controller, $rootScope) {
			$scope = $rootScope.$new();
			controller = $controller('BadgesCtrl', {
				$scope: $scope
			});
		}));

		it('Should controller must be defined', function () {
			expect(controller).toBeDefined();
		});

	});
})();
