(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChildDetailController', ChildDetailController);

    ChildDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Child', 'Family'];

    function ChildDetailController($scope, $rootScope, $stateParams, entity, Child, Family) {
        var vm = this;
        vm.child = entity;
        vm.load = function (id) {
            Child.get({id: id}, function(result) {
                vm.child = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:childUpdate', function(event, result) {
            vm.child = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
