angular.module('brisAccess', ['uiGmapgoogle-maps', 'ng-mfb', 'angucomplete'])
.controller('mainController', ['$scope', '$timeout', 'uiGmapGoogleMapApi', '$http', function($scope, $timeout, uiGmapGoogleMapApi, $http){
	
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

    $scope.allSearchData = [];

	$scope.transportMarkers = [];
	$scope.restaurantMarkers = [];
	$scope.suburbsMarkers = [];
	$scope.allQrData = [];
	$scope.allLocationsData = [];
	$scope.allSuburbsData = [];
	$scope.showingTransport = false;
	$scope.showingRestaurant = false;
	$scope.showingSuburb = false;

	$scope.showDetails = false;
	$scope.details = {};

	$scope.clickMarkerTransport = function(a) {
		// console.log(a);
    	$scope.showDetails = true;
		$scope.showingTransport = true;
		$scope.showingSuburb = false;
		$scope.showingRestaurant = false;

    	var pinLookup = $scope.allQrData.filter(function(c){
    		return c.stopIds ? (c.stopIds[0] == a.key) : (c.TerminalName ? (c.TerminalName == a.key) : false);
    	});

    	console.log(pinLookup);

    	var p = a.getPosition();
    	// $scope.map.center = {
    	// 	latitude: p.A,
    	// 	longitude: p.F
    	// }

    	$scope.details = pinLookup[0]
    	$scope.$apply();
	}

	$scope.clickMarkerRestaurant = function(a) {
		// console.log(a);
    	$scope.showDetails = false;
		$scope.showingRestaurant = true;
		$scope.showingSuburb = false;

    	var pinLookup = $scope.allLocationsData.filter(function(c){
    		return c.location == a.key;
    	});

    	var p = a.getPosition();
    	// console.log(p);
    	// $scope.map.center = {
    	// 	latitude: p.A,
    	// 	longitude: p.F
    	// }

    	$scope.details = pinLookup[0]
    	$scope.$apply();
	}

	$scope.clickMarkerArea = function(a) {
		$scope.showDetails = false;
		$scope.showingRestaurant = false;
		$scope.showingSuburb = true;
		$scope.showingRestaurant = false;

    	var pinLookup = $scope.allSuburbsData.filter(function(c){
    		return c.sa1no == a.key;
    	});

    	var p = a.getPosition();
    	// console.log(p);
    	// $scope.map.center = {
    	// 	latitude: p.A,
    	// 	longitude: p.F
    	// }

    	$scope.details = pinLookup[0]
    	$scope.$apply();
	}

	$scope.getRating = function(r) {
		
		if (!r.rating) return [];

		var k = [];
		for (var l=0; l<r.rating; l++)
			k.push(l);

		return k;
	}

	$scope.filterData = function(typed){
        var newdata = $scope.transportMarkers.filter(function(c){
        	return c.stationid ? c.stationid.match(typed) : false;
        });
        return newdata;
    }

    $scope.selectedData = {};
    $scope.selectedPin = [];
    $scope.$watch('selectedData', function() {

    	$scope.transportMarkers = [];
		$scope.restaurantMarkers = [];
		$scope.suburbsMarkers = [];
		$scope.selectedPin = [];

    	var pinLookup = $scope.allQrData.filter(function(c){
    		if (c.Station)
    			return c.Station == $scope.selectedData.originalObject.original.Station;
    		else if (c.TerminalName)
    			return c.TerminalName == $scope.selectedData.originalObject.original.TerminalName;
    		else
    			return false;
    	});
    	if (pinLookup[0]) {
    		$scope.showingTransport = true;
    		$scope.showingRestaurant = false;
    		$scope.showingSuburb = false;
	    	$scope.details = pinLookup[0];
	    	$scope.map.center = {
	    		latitude: $scope.details.position ? $scope.details.position.Lat : Number($scope.details.Latitude),
	    		longitude: $scope.details.position ? $scope.details.position.Lng : Number($scope.details.Longitude)
	    	}
	    	$scope.map.zoom = 15;
	    	if ($scope.details.Latitude)
		    	$scope.selectedPin = [{
					latitude: Number($scope.details.Latitude),
			        longitude: Number($scope.details.Longitude),
			        title: $scope.details.TerminalName,
			        id: $scope.details.TerminalName,
			        icon: '/images/icons/icons-web-04.png'
				}]
			else
				$scope.selectedPin = [{
					latitude: $scope.details.position.Lat,
			        longitude: $scope.details.position.Lng,
			        title: $scope.details.Station,
			        id: $scope.details.stopIds[0],
		        	icon: '/images/icons/icons-web-03.png'
				}]
	    	return ;
	    }

	    var pinLookup = $scope.allLocationsData.filter(function(c){
    		if (c.location)
    			return c.location == $scope.selectedData.originalObject.original.location;
    		else
    			return false;
    	});
    	if (pinLookup[0]) {
    		$scope.showingTransport = false;
    		$scope.showingRestaurant = true;
    		$scope.showingSuburb = false;
	    	$scope.details = pinLookup[0];
	    	$scope.map.center = {
	    		latitude: $scope.details.position.Lat,
	    		longitude: $scope.details.position.Lng
	    	}

	    	var icon = '/images/icons/icons-web-01.png';
			if ( $scope.details.category.match(/hospital/i) || $scope.details.category.match(/care/i) )
				icon = '/images/icons/icons-web-05.png'
			else if ( $scope.details.category.match(/sport/i) )
				icon = '/images/icons/icons-web-06.png'
			else if ( $scope.details.category.match(/shop/i) )
				icon = '/images/icons/icons-web-47.png'

	    	$scope.map.zoom = 15;
	    	$scope.selectedPin = [{
				latitude: Number($scope.details.position.Lat),
		        longitude: Number($scope.details.position.Lat),
		        title: $scope.details.location,
		        id: $scope.details.location,
		        icon: icon
			}]

	    	return ;
	    }

	    var pinLookup = $scope.allSuburbsData.filter(function(c){
    		if (c.Suburb_Name)
    			return c.Suburb_Name == $scope.selectedData.originalObject.original.Suburb_Name;
    		else
    			return false;
    	});
    	if (pinLookup[0]) {
    		$scope.showingTransport = false;
    		$scope.showingRestaurant = false;
    		$scope.showingSuburb = true;
	    	$scope.details = pinLookup[0];
	    	$scope.map.center = {
	    		latitude: $scope.details.position_lat,
	    		longitude: $scope.details.position_lng
	    	}

	    	$scope.map.zoom = 15;
	    	$scope.selectedPin = [{
				latitude: $scope.details.position_lat,
		        longitude: $scope.details.position_lat,
		        title: $scope.details.Suburb_Name,
		        id: $scope.details.Suburb_Name,
		        icon: '/images/icons/icons-web-02.png'
			}]

	    	return ;
	    }

   	});

	$http.get('/data/qr').success(function(data){
		$scope.allQrData = angular.copy(data);
		for(var q in data) {
			$scope.allSearchData.push({
				title: data[q].Station,
				original: angular.copy(data[q])
			});
		}
	});
	$http.get('/data/ferries').success(function(data){
		console.log(data);
		for(var q in data) {
			$scope.allQrData.push(angular.copy(data[q]));
			$scope.allSearchData.push({
				title: data[q].TerminalName,
				original: angular.copy(data[q])
			});
		}
		console.log($scope.allSearchData);
	});
	$http.get('/data/locations').success(function(data){
		$scope.allLocationsData = angular.copy(data);
		for(var q in data) {
			$scope.allSearchData.push({
				title: data[q].location,
				original: angular.copy(data[q])
			});
		}
	});
	$http.get('/data/areas').success(function(data){
		$scope.allSuburbsData = angular.copy(data);
		for(var q in data) {
			$scope.allSearchData.push({
				title: data[q].Suburb_Name,
				original: angular.copy(data[q])
			});
		}
	});
    $scope.toggleTransportData = function() {
    	$scope.allLocationsData = [];
    	$scope.suburbsMarkers = [];
    	$scope.restaurantMarkers = [];
    	$scope.selectedPin = [];

    	$scope.map.zoom = 13;

		for(var q in $scope.allQrData) {
			if ($scope.allQrData[q].Station)
				$scope.transportMarkers.push({
					latitude: $scope.allQrData[q].position.Lat,
			        longitude: $scope.allQrData[q].position.Lng,
			        title: $scope.allQrData[q].Station,
			        id: $scope.allQrData[q].stopIds[0],
		        	icon: '/images/icons/icons-web-03.png'
				})
			else
				$scope.transportMarkers.push({
					latitude: Number($scope.allQrData[q].Latitude),
			        longitude: Number($scope.allQrData[q].Longitude),
			        title: $scope.allQrData[q].TerminalName,
			        id: $scope.allQrData[q].TerminalName,
			        icon: '/images/icons/icons-web-04.png'
				})
		}
    }

    $scope.toggleSuburbData = function() {
    	$scope.allLocationsData = [];
    	$scope.restaurantMarkers = [];
    	// $scope.suburbsMarkers = [];
    	$scope.selectedPin = [];

    	$scope.map.zoom = 13;

		for(var q in $scope.allSuburbsData) {

			$scope.suburbsMarkers.push({
				latitude: $scope.allSuburbsData[q].position_lat,
		        longitude: $scope.allSuburbsData[q].position_lng,
		        title: $scope.allSuburbsData[q].Suburb_Name,
		        id: $scope.allSuburbsData[q].sa1no,
		        icon: '/images/icons/icons-web-02.png'
			})
		}
    }

    $scope.toggleRestaurantData = function() {
		$scope.allQrData = [];
		$scope.transportMarkers = [];
		$scope.showDetails = false;
		$scope.showingTransport = false;
		$scope.suburbsMarkers = [];
		$scope.selectedPin = [];

		$scope.map.zoom = 13;

		var icon = '/images/icons/icons-web-01.png';

		for(var q in $scope.allLocationsData) {

			// console.log($scope.allLocationsData[q]);
			icon = '/images/icons/icons-web-01.png';
			if ( $scope.allLocationsData[q].category.match(/hospital/i) || $scope.allLocationsData[q].category.match(/care/i) )
				icon = '/images/icons/icons-web-05.png'
			else if ( $scope.allLocationsData[q].category.match(/sport/i) )
				icon = '/images/icons/icons-web-06.png'
			else if ( $scope.allLocationsData[q].category.match(/shop/i) )
				icon = '/images/icons/icons-web-47.png'

			$scope.restaurantMarkers.push({
				latitude: $scope.allLocationsData[q].position.Lat,
		        longitude: $scope.allLocationsData[q].position.Lng,
		        title: $scope.allLocationsData[q].location,
		        id: $scope.allLocationsData[q].location,
		        icon: icon
			})
		}
    }

}]);

$(window).resize(function(){
	$("ui-gmap-google-map").height($(window).innerHeight());
	$(".angular-google-map-container").height($(window).innerHeight());
})