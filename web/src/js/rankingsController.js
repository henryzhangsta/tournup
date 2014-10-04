function rankingsController($scope) {
    $scope.ranks = [
	 	{name: 'Ryan "420 Blaze It" Hon H$U', 
		score: 53} ,
		{name: 'Henry Zhang', 
		score: 51} , 
		{name: 'Alex Kang', 
		score: 12} ,
		{name: 'Yiwen Zhang', 
		score: 10} ,
		{name: 'Ryan Zhang', 
		score: 8},
		{name: 'Zhang Huadian', 
		score: 6}
	];
    $scope.orderProp = '-score';
} 