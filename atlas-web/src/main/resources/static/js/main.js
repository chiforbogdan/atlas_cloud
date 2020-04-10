'use strict'

var atlas_app = angular.module('atlasApp' , ['ngRoute', 'ui.bootstrap', 'zingchart-angularjs']);

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

atlas_app.controller('MainController',[ '$scope', '$location', '$rootScope', function($scope, $location, $rootScope) {

    $scope.selected = false;
    $scope.trace = [];

    $scope.showNav = function(){
        $scope.selected = true;
    };

    $scope.hideNav = function(){
        $scope.selected = false;
    };

    $rootScope.$on('$routeChangeStart', function () {
       $scope.path = $location.url();
       $scope.selected = !angular.equals($scope.path,"/");

       /* Trace the path from gateways for backpropagation */
       var regExp =  /\/([^/]+)?(?=\/|$)/ //matches /page/
       var page = $scope.path.match(regExp)[1];

       if (page != null) {
          $scope.index = $scope.trace.indexOf($scope.path);

          if ($scope.index != -1)
             $scope.trace.splice($scope.index, $scope.trace.length - $scope.index);

          $scope.trace.push($scope.path);
       }

    });
}]);