'use strict'

var atlas_app = angular.module('atlasApp' , ["ngRoute"]);

atlas_app.config(function($routeProvider){

            $routeProvider
            .when('/' , {
                templateUrl : "view/list_gateways.html",
                controller  : 'GatewayController'
            })
            .when('/gateway/:id' , {
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