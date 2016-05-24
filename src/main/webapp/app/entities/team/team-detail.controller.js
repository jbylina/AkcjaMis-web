(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Team', 'User', 'Event', 'ChristmasPackage'];

    function TeamDetailController($scope, $rootScope, $stateParams, entity, Team, User, Event, ChristmasPackage) {
        var vm = this;
        vm.team = entity;
        vm.date = new Date().getFullYear();
        vm.load = function (id) {
            Team.get({id: id}, function(result) {
                vm.team = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
