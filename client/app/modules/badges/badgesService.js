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

		Badges.$inject = ['$http', '$rootScope'];

		function Badges ($http, $rootScope) {


			return {
				getBadges:getBadges
			};


			function getBadges(vm) {

				var badges = [];
				var url = "https://raw.githubusercontent.com/ltouroumov/amt-g4mify/master/client/app/assets/images/";

				var req = {
					method: 'GET',
					url: 'http://localhost:8080/api/users/' + $rootScope.username +'/badges',
					headers: {
						'Content-Type': 'application/json',
						'Identity': '1:secret'
					}
				};

				$http(req).then(function(res){
					console.log("Badges: OK");

					for(var i = 0; i < res.data.length; i++){
						var badge = {
							level: res.data[i].level,
							name: res.data[i].type.name,
							image: url + res.data[i].type.image
						};
						console.log(badges);
						badges.push(badge);
					}

					vm.badges = badges;

				}, function(err){
					console.log("Badges: ERROR");
					vm.msg = "- An error occurred posting the event to the gamification platform";
					vm.success = false;
				});
			}
		}
})();
