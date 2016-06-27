(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .directive('activeLink', activeLink);

    function activeLink() {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var clazz = attrs.activeLink;
            var path = attrs.href;
            path = path.substring(1); //hack because path does bot return including hashbang
            scope.location = location;
            scope.$watch('location.path()', function(newPath) {
                if (path === newPath) {
                    element.addClass(clazz);
                } else {
                    element.removeClass(clazz);
                }
            });
        }

        var typeAhead = angular.module('app', []);

        typeAhead.factory('dataFactory', function($http) {
            return {
                get: function(url) {
                    return $http.get(url).then(function(resp) {
                        return resp.data; // success callback returns this
                    });
                }
            };
        });

        typeAhead.controller('TypeAheadController', function($scope, dataFactory) { // DI in action
            dataFactory.get('/hibernate-search/families').then(function(data) {
                $scope.items = data;
            });
            $scope.name = ''; // This will hold the selected item
            $scope.onItemSelected = function() { // this gets executed when an item is selected
                console.log('selected=' + $scope.name);
            };
        });

        typeAhead.directive('typeahead', function($timeout) {
            return {
                restrict: 'AEC',
                scope: {
                    items: '=',
                    prompt: '@',
                    title: '@',
                    subtitle: '@',
                    model: '=',
                    onSelect: '&amp;'
                },
                link: function(scope, elem, attrs) {
                        scope.handleSelection = function(selectedItem) {
                            scope.model = selectedItem;
                            scope.current = 0;
                            scope.selected = true;
                            $timeout(function() {
                                scope.onSelect();
                            }, 200);
                        };
                        scope.current = 0;
                        scope.selected = true; // hides the list initially
                        scope.isCurrent = function(index) {
                            return scope.current == index;
                        };
                        scope.setCurrent = function(index) {
                            scope.current = index;
                        };
                },
                templateUrl: 'templates/templateurl.html'
            };
        });


    }
})();
