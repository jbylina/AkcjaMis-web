(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('EventController', EventController);

    EventController.$inject = ['$scope', '$state', 'Event', 'EventSearch'];

    function EventController ($scope, $state, Event, EventSearch) {
        var vm = this;
        vm.events = [];
        vm.loadAll = function() {
            Event.query(function(result) {
                vm.events = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EventSearch.query({query: vm.searchQuery}, function(result) {
                vm.events = result;
            });
        };
        vm.loadAll();
        
    }
})();
