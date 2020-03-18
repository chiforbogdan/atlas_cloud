'use strict'

var atlas_app = angular.module('atlasApp' , ['ngRoute', 'ui.bootstrap']);

atlas_app.config(function($routeProvider){

    $routeProvider
                .when('/' , {
                    templateUrl : "view/about_us.html"
                })
                .when('/gateways' , {
                    templateUrl : "view/list_gateways.html",
                    controller  : 'GatewayController'
                })
                .when('/management' , {
                    templateUrl : "view/management_gateway.html",
                    controller  : 'GatewayController'
                })
                .when('/gateway/:id1/:id2' , {
                    templateUrl : "view/list_clients.html",
                    controller  : 'ClientsController'
                })
                .when('/client/:id1/:id2' , {
                    templateUrl : "view/client_item.html",
                    controller  : 'ClientDetailsController'
                })
                .otherwise({
                        redirectTo: '/'
                });
});

atlas_app.controller('MainController',[ '$scope', '$location', function($scope, $location) {

    $scope.selected = false;

    init();

    $scope.showNav = function(){
        $scope.selected = true;
    };

    $scope.hideNav = function(){
        $scope.selected = false;
    };

    function init() {
      $scope.path = $location.url();
      $scope.selected = $scope.path.includes('gateway') || $scope.path.includes('client');
    }
}]);