(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageNoteDetailController', ChristmasPackageNoteDetailController);

    ChristmasPackageNoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ChristmasPackageNote', 'ChristmasPackage'];

    function ChristmasPackageNoteDetailController($scope, $rootScope, $stateParams, entity, ChristmasPackageNote, ChristmasPackage) {
        var vm = this;
        vm.christmasPackageNote = entity;
        vm.load = function (id) {
            ChristmasPackageNote.get({id: id}, function(result) {
                vm.christmasPackageNote = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:christmasPackageNoteUpdate', function(event, result) {
            vm.christmasPackageNote = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
