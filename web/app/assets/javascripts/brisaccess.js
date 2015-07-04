angular.module('brisAccess', ['uiGmapgoogle-maps', 'ng-mfb'])
.controller('mainController', ['$scope', '$timeout', 'uiGmapGoogleMapApi', function($scope, $timeout, uiGmapGoogleMapApi){
	
	$scope.map = { 
			center: { 
				latitude: -27.4667, 
				longitude: 153.0333 
			}, 
			zoom: 13,
			options: {
				disableDefaultUI: true
			}
		};

	$timeout(function(){
		$("ui-gmap-google-map").height($(window).innerHeight());
		$(".angular-google-map-container").height($(window).innerHeight());
	}, 100);

	uiGmapGoogleMapApi.then(function(maps) {
		console.log(maps);
    });

}]);

$(window).resize(function(){
	$("ui-gmap-google-map").height($(window).innerHeight());
	$(".angular-google-map-container").height($(window).innerHeight());
})