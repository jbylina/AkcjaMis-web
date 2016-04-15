(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Team', 'Event'];

    function TeamDetailController($scope, $rootScope, $stateParams, entity, Team, Event) {
        var vm = this;
        vm.team = entity;
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
