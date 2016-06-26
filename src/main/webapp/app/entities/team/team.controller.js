(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Team', 'TeamSearch'];

    function TeamController ($scope, $state, Team, TeamSearch) {
        var vm = this;
        vm.year = $state.params.year;
        vm.teams = [];

        vm.editable = vm.year < (new Date().getFullYear());

        vm.loadAll = function() {
            Team.query({year: vm.year}, function(result) {
                vm.teams = result;
            });
        };

        vm.loadAll();
    }
})();
