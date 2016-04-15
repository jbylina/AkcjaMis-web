(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamMemberController', TeamMemberController);

    TeamMemberController.$inject = ['$scope', '$state', 'TeamMember', 'TeamMemberSearch'];

    function TeamMemberController ($scope, $state, TeamMember, TeamMemberSearch) {
        var vm = this;
        vm.teamMembers = [];
        vm.loadAll = function() {
            TeamMember.query(function(result) {
                vm.teamMembers = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TeamMemberSearch.query({query: vm.searchQuery}, function(result) {
                vm.teamMembers = result;
            });
        };
        vm.loadAll();
        
    }
})();
