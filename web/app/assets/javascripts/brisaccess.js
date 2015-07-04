angular.module('brisAccess', ['uiGmapgoogle-maps', 'ng-mfb'])
.controller('mainController', ['$scope', '$timeout', function($scope, $timeout){
	
	$scope.map = { 
			center: { 
				latitude: -27.4667, 
				longitude: 153.0333 
			}, 
			zoom: 13 
		};

	$timeout(function(){
		$("ui-gmap-google-map").height($(window).innerHeight());
		$(".angular-google-map-container").height($(window).innerHeight());
	}, 100);

}]);

$(window).resize(function(){
	$("ui-gmap-google-map").height($(window).innerHeight());
	$(".angular-google-map-container").height($(window).innerHeight());
})