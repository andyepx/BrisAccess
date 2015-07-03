angular.module('brisAccess', ['uiGmapgoogle-maps'])
.controller('mainController', ['$scope', function($scope){
	
	$scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };
	


}]);