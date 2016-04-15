(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Team', 'TeamSearch'];

    function TeamController ($scope, $state, Team, TeamSearch) {
        var vm = this;
        vm.teams = [];
        vm.loadAll = function() {
            Team.query(function(result) {
                vm.teams = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TeamSearch.query({query: vm.searchQuery}, function(result) {
                vm.teams = result;
            });
        };
        vm.loadAll();
        
    }
})();
