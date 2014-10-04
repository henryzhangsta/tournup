
var myApp = angular.module('myApp', []);

myApp.rankingsController('rankingsController', function($scope) {
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
});

// 'use strict';

// /* Controllers */

// var phonecatApp = angular.module('phonecatApp', []);

// phonecatApp.controller('PhoneListCtrl', function($scope) {
//   $scope.phones = [
//     {'name': 'Nexus S',
//      'snippet': 'Fast just got faster with Nexus S.',
//      'age': 1},
//     {'name': 'Motorola XOOM™ with Wi-Fi',
//      'snippet': 'The Next, Next Generation tablet.',
//      'age': 2},
//     {'name': 'MOTOROLA XOOM™',
//      'snippet': 'The Next, Next Generation tablet.',
//      'age': 3}
//   ];

//   $scope.orderProp = 'age';
// });
