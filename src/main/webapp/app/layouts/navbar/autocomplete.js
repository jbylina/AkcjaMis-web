(
    function() {
        'use strict';

    angular
        .module('akcjamisApp')
        .controller('autoCompleteCTRL',  ['$scope', '$rootScope', 'FamilyHibernateSearch', 'AlertService', "$window", function ($scope, $rootScope, FamilyHibernateSearch, AlertService, $window){
        $rootScope.searchItems = [];

    //Sort Array
    $rootScope.searchItems.sort();
    //Define Suggestions List
    $rootScope.suggestions = [];
    //Define Selected Suggestion Item
    $rootScope.selectedIndex = -1;

    //Function To Call On ng-change
    $rootScope.search = function(){
        $rootScope.suggestions = [];
        var myMaxSuggestionListLength = 0;

        FamilyHibernateSearch.query({
            query: $scope.searchText,
        }, onSuccess, onError);

        /*for(var i=0; i<$rootScope.searchItems.length; i++){
            var searchItemsSmallLetters = angular.lowercase($rootScope.searchItems[i]);
            var searchTextSmallLetters = angular.lowercase($scope.searchText);
            if( searchItemsSmallLetters.indexOf(searchTextSmallLetters) !== -1){
                $rootScope.suggestions.Spush(searchItemsSmallLetters);
                myMaxSuggestionListLength += 1;
                if(myMaxSuggestionListLength == 5){
                    break;
                }
            }
        }*/
        function onSuccess(data, headers) {
            $rootScope.searchItems = angular.fromJson(data);
           // $rootScope.suggestions = angular.fromJson(data);
            //$rootScope.suggestions = angular.fromJson(data);
           // AlertService.error(angular.fromJson(data));
            var i = 0;
            console.log('data size ' + data.length);
            for(i = 0; i < data.length; i++) {
                console.log('data is ' + angular.fromJson(data)[i].id);
                $rootScope.suggestions.push(angular.fromJson(data)[i].city + " " + angular.fromJson(data)[i].street + angular.fromJson(data)[i].houseNo);
            }


        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
    }

    //Keep Track Of Search Text Value During The Selection From The Suggestions List
    $rootScope.$watch('selectedIndex',function(val){
        if(val !== -1) {
            $scope.searchText = $rootScope.suggestions[$rootScope.selectedIndex];
        }
    });


    //Text Field Events
    //Function To Call on ng-keydown
    $rootScope.checkKeyDown = function(event){
        if(event.keyCode === 40){//down key, increment selectedIndex
            event.preventDefault();
            if($rootScope.selectedIndex+1 !== $rootScope.suggestions.length){
                $rootScope.selectedIndex++;
            }
        }else if(event.keyCode === 38){ //up key, decrement selectedIndex
            event.preventDefault();
            if($rootScope.selectedIndex-1 !== -1){
                $rootScope.selectedIndex--;
            }
        }else if(event.keyCode === 13){ //enter key, empty suggestions array
            event.preventDefault();
            $rootScope.suggestions = [];
        }
    }
    //Function To Call on ng-keyup
    $rootScope.checkKeyUp = function(event){
        if(event.keyCode !== 8 || event.keyCode !== 46){//delete or backspace
            if($scope.searchText == ""){
                $rootScope.suggestions = [];
            }
        }
    }
    //======================================

    //List Item Events
    //Function To Call on ng-click
    $rootScope.AssignValueAndHide = function(index){
        console.log('choosen url ' + '/#/family/' + $scope.searchItems[index].id);
        $window.location.href = '/#/family/' + $scope.searchItems[index].id;
        $scope.searchText = $rootScope.suggestions[index];
        $rootScope.suggestions=[];
    }
    //======================================

}]);

        angular
            .module('akcjamisApp')
            .factory('FamilyHibernateSearch', FamilyHibernateSearch);

        FamilyHibernateSearch.$inject = ['$resource'];

        function FamilyHibernateSearch($resource) {
            var resourceUrl =  '/api/hibernate-search/families/:id';

            return $resource(resourceUrl, {}, {
                'query': { method: 'GET', isArray: true}
            });
        }
    })();

