(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'Team', 'ChristmasPackage'];

    function EventDetailController($scope, $rootScope, $stateParams, entity, Event, Team, ChristmasPackage) {
        var vm = this;
        vm.event = entity;
        vm.load = function (id) {
            Event.get({id: id}, function(result) {
                vm.event = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
