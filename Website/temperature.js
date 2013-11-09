function RoomCtrl($scope){

//This is an array of objects. Each object has three attributes: name, temperature, and occupied
 $scope.rooms = [
 	{name:"Bedroom", temperature:72, occupied:false},
 	{name:"Bathroom", temperature:69, occupied:false},
 	{name:"Kitchen", temperature:74, occupied:true},
 	{name:"Basement", temperature:61, occupied:false}
 	];

 	//This function just returns the total number of rooms
 	$scope.getTotalRooms = function(){
 		return $scope.rooms.length;
 	}

 	$scope.getAverageTemperature = function(){
 		var total = 0;
 		//This is an underscore function from the underscore library
 		//This function iterates through the rooms array that was created above
 		_.each($scope.rooms, function(room){
 			//The 'room' object is the single room pulled from the array at that moment. The line below adds the new temperature to the existing temperature
 			total = total + room.temperature;
 		});

 		//Return the average
 		return total/$scope.getTotalRooms();
 	}

 	$scope.getRoom = function(room){
 		console.log(room);
 	}

}